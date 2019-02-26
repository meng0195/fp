package cn.com.bjggs.temp.util;


import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.enums.TypeTestTag;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.domain.TestDataCopy;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmNotesCopy;
import cn.com.bjggs.warns.domain.AlarmTT;

/**
 * 检测工具类,手动检测
 * @author	wc
 * @date	2017-07-18
 */
public class TempChange {
	
	private static Dao dao;
	
	public static void initDao(Dao d){
		dao = d;
	}
	
	private static final Log log = Logs.getLog(TempChange.class);
	
	/**
	 * 仓房编号
	 */
	private String houseNo;
	private TempInfo eqment;
	private Map<Integer, PointInfo> points;
	private AlarmTT att;
	
	private AlarmNotes an;
	private AlarmNotes an0;//温度高限
	private AlarmNotes an2;//温度异常点
	private AlarmNotes an3;//层均温
	private AlarmNotes an5;//冷芯
	
	private TestData datas = new TestData();
	
	/**
	 * 各层温度集合
	 */
	private double[] leveTemps;
	private double[] leveMax;
	private double[] leveMin;
	private int[] leveNum;
	/**
	 * 各区温度集合
	 */
	private double[] areaTemps;
	private double[] areaMax;
	private double[] areaMin;
	private int[] areaNum;
	
	private int allPoints = 0;
	
	private double avgT;
	private double maxT;
	private double minT = -102.4;
	
	//冷芯数量 冷芯温度
	private int ccN = 0;
	private double ccT = 0;
	
	/**
	 * 点报警集合
	 */
	private byte[] warnPoints;
	
	private byte[] tset;
	
	private TestData old;
	
	private List<AlarmNotes> olds;
	
	private double[] temps;
	
	private int[] indexs;
	
	public TempChange(){}
	
	@SuppressWarnings("unchecked")
	public TempChange(String id, double[] _temps, int[] _indexs){
		this.old = dao.fetch(TestData.class, id);
		this.olds = dao.query(AlarmNotes.class, Cnd.where("testCode", "=", id));
		if(this.old == null || _temps == null || _indexs == null || _temps.length != _indexs.length){
			throw new RuntimeException("温度配置错误！");
		}
		this.houseNo = old.getHouseNo();
		this.eqment = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		if(eqment == null){
			addError("仓房线缆信息未配置!");
		}
		this.points = (Map<Integer, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS1.code());
		this.att = HouseUtil.get(houseNo, TypeHouseConf.WARNS.code(), AlarmTT.class);
		this.temps = _temps;
		this.indexs = _indexs;
		if(att != null){
			if(att.isWarns(Constant.IDX_T_2)){
				this.an2 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_3, houseNo, old.getTestDate(), att.getThd(Constant.IDX_T_2));//温度异常点
			}
			if(att.isWarns(Constant.IDX_T_3)){
				this.an3 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_4, houseNo, old.getTestDate(), att.getThd(Constant.IDX_T_3));//层均温
			}
			if(att.isWarns(Constant.IDX_T_5)){
				this.an5 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_6, houseNo, old.getTestDate(), att.getThd(Constant.IDX_T_5));//冷心
			}
		}
		
		//初始化各区
		areaTemps = new double[5];
		areaMax = new double[5];
		areaMin = new double[5];
		areaNum = new int[5];
		for(int i = 0; i < 5; i++){
			areaMin[i] = -1024;
		}
		//最大层数
		int maxZ = 0;
		if(eqment != null ) maxZ = eqment.getMaxLNum();
		leveTemps = new double[maxZ];
		leveMax = new double[maxZ];
		leveMin = new double[maxZ];
		leveNum = new int[maxZ];
		for(int i = 0; i < maxZ; i++){
			leveMin[i] = -1024;
		}
		warnPoints = new byte[eqment == null ? 0 : eqment.getPointNum()];
		tset = new byte[eqment == null ? 0 : eqment.getPointNum() * 2];
		datas.setWarns(warnPoints);
	}
	
	public void change(){
		if(points != null && points.size() > 0){
			initTestDate();
			initDatas();
		}
	}
	
	private void initDatas(){
		PointInfo p;
		double temp = 0;
		int temp10 = 0;
		for(int i = 0; i < indexs.length; i++){
			p = points.get(indexs[i]);
			temp = temps[i];
			if(p != null && temp != 0){
				setLeveTemp(p, temp);
				setAreaTemp(p, temp);
				temp10 = (int)(temp * 10);
				tset[(indexs[i] - 1) * 2] = MathUtil.int2HByte(temp10);
				tset[indexs[i] * 2 - 1] = MathUtil.int2LByte(temp10);
				doPointWarns(p, temp);
				avgT = avgT + temp;
				maxT = Math.max(temp, maxT);
				minT = -1024 == minT ? temp : Math.min(temp, minT);
			}
		}
		//处理异常,整仓的7
		doAllwarns();
		datas.setTset(tset);
		setAllDatas();
		//插入数据
		insertAll();
		//是否异常
	}
	
	private void setAllDatas(){
		datas.setWarns(warnPoints);
		datas.setAvgT(toFixed(avgT/(allPoints == 0 ? 1 : allPoints), 1));
		datas.setMaxT(toFixed(maxT, 1));
		datas.setMinT(minT == -102.4 ? 0 : minT);
		datas.setCcT(toFixed(getCcT(), 1));
		//各层
		Map<String, double[]> maps = new LinkedHashMap<String, double[]>();
		double[] arrs;
		for(int i = 0; i < leveTemps.length; i++){
			arrs = new double[3];
			arrs[0] = toFixed(leveTemps[i]/(leveNum[i] == 0 ? 1 : leveNum[i]), 1);
			arrs[1] = toFixed(leveMax[i], 1);
			arrs[2] = leveMin[i] == -102.4 ? 0 : leveMin[i];
			maps.put(String.valueOf(i), arrs);
		}
		datas.setLayerTs(JsonUtil.toJson(maps));
		//各区
		Map<String, double[]> maps1 = new LinkedHashMap<String, double[]>();
		double[] arrs1;
		for(int i = 0; i < areaTemps.length; i++){
			arrs1 = new double[3];
			arrs1[0] = toFixed(areaTemps[i]/(areaNum[i] == 0 ? 1 : areaNum[i]), 1);
			arrs1[1] = toFixed(areaMax[i], 1);
			arrs1[2] = areaMin[i] == -102.4 ? 0 : areaMin[i];
			maps1.put(String.valueOf(i), arrs1);
		}
		datas.setAreaTs(JsonUtil.toJson(maps1));
	}
	
	private void insertAll(){
		try{
			//插入异常集合
			List<AlarmNotes> ls = new LinkedList<AlarmNotes>();
			if(an0 != null && an0.getNums() > 0){
				ls.add(an0);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an0.setTestCode(datas.getId());
			}
			if(an2 != null && an2.getNums() > 0){
				ls.add(an2);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an2.setTestCode(datas.getId());
			}
			if(an3 != null && an3.getNums() > 0){
				ls.add(an3);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an3.setTestCode(datas.getId());
			}
			if(an5 != null && an5.getNums() > 0){
				ls.add(an5);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an5.setTestCode(datas.getId());
			}
			//备份表
			TestDataCopy tdc = new TestDataCopy(old);
			List<AlarmNotesCopy> list = new LinkedList<AlarmNotesCopy>();
			if(olds != null && olds.size() > 0){
				for(AlarmNotes aln : olds){
					list.add(new AlarmNotesCopy(aln));
				}
			}
			if(null == dao.fetch(TestDataCopy.class, old.getId())){
				dao.insert(tdc);
				if(!Lang.isEmpty(list)){
					dao.insert(list);
				}
			}
			//删除原有数据
			dao.delete(TestData.class, old.getId());
			//删除原有报警
			dao.delete(olds);
			//插入异常集合
			dao.insert(ls);
			//插入温度数据
			dao.insert(datas);
			//TODO 删除原有消息
		}catch(Exception e){
			e.printStackTrace();
			log.info("检测数据插入异常！");
		}
	};
	
	/**
	 * 计算层均温
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	void
	 */
	private void setLeveTemp(PointInfo p, double temp){
		int index = p.getZaxis();
		//实际所有点数+1
		allPoints = allPoints + 1;
		//层点数+1
		leveNum[index] = leveNum[index] + 1;
		leveTemps[index] = leveTemps[index] + temp;
		leveMax[index] = Math.max(leveMax[index], temp);
		leveMin[index] = -1024 == leveMin[index] ? temp : Math.min(leveMin[index], temp);
		if(p.getXaxis() >= eqment.getMinx() && p.getXaxis() <= eqment.getMaxx()
				&& p.getYaxis() >= eqment.getMiny() && p.getYaxis() <= eqment.getMaxy()
				&& p.getZaxis() >= eqment.getMinz() && p.getZaxis() <= eqment.getMaxz()){
			ccN += 1;
			ccT += temp;
		}
	}
	/**
	 * 计算区均温
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	void
	 */
	private void setAreaTemp(PointInfo p, double temp){
		if (p.getArea() > 0) {
			int index = p.getArea()-1;
			//区点数+1
			areaNum[index] = areaNum[index] + 1;
			areaTemps[index] = areaTemps[index] + temp;
			areaMax[index] = Math.max(areaMax[index], temp);
			areaMin[index] = -1024 == areaMin[index] ? temp : Math.min(areaMin[index], temp);
		}
	}
	
	
	private void addError(String msg){
		if(an == null) {
			an = new AlarmNotes(Constant.W_TEMP, Constant.W_DO, houseNo, datas.getTestDate());
		}
		an.setNums(an.getNums() + 1);
		datas.setTestWarns((datas.getTestWarns() == null ? "" : datas.getTestWarns()) + msg + "|");
	}
	
	//TODO 报警类型有问题
	private void doPointWarns(PointInfo p, double temp){
		int index = p.getPoinNo1() - 1;
		if(warnPoints.length > p.getPoinNo1()){
			//处理高温报警
			if(att.isWarns(Constant.IDX_T_0) && temp > att.getThd(Constant.IDX_T_0)){
				an0.setNums(an0.getNums() + 1);
				warnPoints[index] += WarnType.WARN_1;
			}
			//温度异常点
			if(att.isWarns(Constant.IDX_T_2) && temp > att.getThd(Constant.IDX_T_2)){
				an2.setNums(an2.getNums() + 1);
				warnPoints[index] += WarnType.WARN_4;
			}
		}
	}
	
	private void doAllwarns(){
		//层均温
		if(att.isWarns(Constant.IDX_T_3)){
			for(int i = 0; i < leveTemps.length; i++){
				if((leveTemps[i]/(leveNum[i] == 0 ? 1 : leveNum[i])) > att.getThd(3)){
					an3.setNums(an3.getNums() + 1);
					//TODO
				}
			}
		}
		//冷芯
		if(att.isWarns(Constant.IDX_T_5) && getCcT() > att.getThd(Constant.IDX_T_5)){
			an5.setNums((int)(getCcT() * 10));
			an5.setThreshold(att.getThd(Constant.IDX_T_5));
		}
	}
	
	private double getCcT(){
		return ccT/(ccN == 0 ? 1 : ccN);
	}
	
	private void initTestDate(){
		this.datas.setId(old.getId());
		this.datas.setHouseNo(houseNo);
		this.datas.setCurveFlag(old.getCurveFlag());
		this.datas.setPlanCode(old.getPlanCode());
		this.datas.setReportFlag(old.getReportFlag());
		this.datas.setTestDate(old.getTestDate());
		this.datas.setTestType(old.getTestType());
		this.datas.setInH(old.getInH());
		this.datas.setInT(old.getInT());
		this.datas.setOutH(old.getOutH());
		this.datas.setOutT(old.getOutT());
		this.datas.setTopT(old.getTopT());
		this.datas.setVentT(old.getVentT());
		this.datas.setLayT(old.getLayT());
	}
	
	public double toFixed(double target, int scale) {
		BigDecimal bg = new BigDecimal(target);
		return bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}
