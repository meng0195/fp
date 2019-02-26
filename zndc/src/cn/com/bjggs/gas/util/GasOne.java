package cn.com.bjggs.gas.util;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.lang.Lang;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.basis.enums.TypeTestTag;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.msg.enums.TypeMsg;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmTT;

/**
 * 气体检测工具类,手动检测
 * @author	wc
 * @date	2017-07-18
 */
public class GasOne extends Thread{
	
	public static void initDao(Dao d){
		dao = d;
	};
	private static Dao dao;
	
	/**
	 * 仓房编号
	 */
	private String houseNo;
	private GasInfo gasInfo;
	private int[] ways = null;
	private AlarmTT att;
	
	private boolean first = true;
	
	private AlarmNotes an;//检测异常
	private AlarmNotes an0 = null;//co2超标
	private AlarmNotes an1 = null;//o2超标
	private AlarmNotes an2 = null;//ph3超标
	
	private TestGas datas;
	
	private GasResults results;
	
	/**
	 * 各层温度集合
	 */
	private int[] alls = new int[3];
	private int[] mins = new int[3];
	private int[] maxs = new int[3];
	
	private byte[] gasSet;
	
	private byte[] warnSet;
	
	public GasOne(){}
	
	public GasOne(String houseNo, Date testDate, int[] ways){
		this.houseNo = houseNo;
		this.datas = new TestGas(TypeCheck.ONE.val(), houseNo, null, testDate);
		this.an = new AlarmNotes(Constant.W_GAN, Constant.W_DO, houseNo, datas.getStartTime());
		this.gasInfo = HouseUtil.get(houseNo, TypeHouseConf.GAS.code(), GasInfo.class);
		this.att = HouseUtil.get(houseNo, TypeHouseConf.WARNS.code(), AlarmTT.class);
		if(att == null){
			addError("该仓房预警信息未配置！");
		}
		if(gasInfo == null || gasInfo.getWayNumb() == 0){
			addError("该仓房未配置检测配置信息！");
		} else {
			if(Lang.isEmpty(ways)){
				this.ways = new int[gasInfo.getWayNumb()];
				for(int i = 0; i < gasInfo.getWayNumb(); i++){
					this.ways[i] = i + 1;
				}
			} else {
				this.ways = ways;
			}
			this.gasSet = new byte[gasInfo.getWayNumb() * 6];
			this.warnSet = new byte[gasInfo.getWayNumb()];
		}
		datas.setGasSet(gasSet);
		datas.setWarnSet(warnSet);
		results = new GasResults(houseNo, datas, an);
		GasUtil.lastChecks.put(houseNo, results);
	}
	
	private void check(){
		if(ways != null && ways.length > 0){
			//验证该仓房是否正在检测
			if(GasUtil.checking.contains(houseNo)){
				addError("仓房正在检测!");
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
			} else {
				//填入正在检测仓房
				GasUtil.checking.add(houseNo);
				//修改状态正在检测
				results.setTag(TypeCT.ING.val());
				DwrUtil.sendGas(GasUtil.getGasJson());
				
				GasChecks check = GasMbsFactory.createCheck(gasInfo);
				//TODO 同步时间
				byte[] req;
				
				for(int way : ways){
					DwrUtil.sendWay(String.valueOf(way));
					results.setCheckSum(way);
					//处理强制结束的情况
					if(results.getTag() == TypeCT.TOEND.val()){
						addError("强制结束！");
						break;
					}
					//尝试获取某一风路的数值
					try{
						req = check.getGas(way);
						doPoints(req, way);
					} catch(Exception e){
						addError(e.getMessage());
					}
				}
				
				//执行排空操作
				try {
					check.clear();
				} catch (Exception e) {
					addError(e.getMessage());
				}
				DwrUtil.sendWay("stop");
			}
		}
		datas.setEndTime(new Date());
		insertAll();
		//移除正在检测仓房
		GasUtil.checking.remove(houseNo);
		DwrUtil.sendGas(GasUtil.getGasJson());
	}
	
	private void doPoints(byte[] req, int way){
		//同步检测数据
		System.arraycopy(req, 0, gasSet, (way - 1) * 6, 6);
		//氧气0， 磷化氢1， 二氧化碳2
		int CO2 = MathUtil.byte2Int(req[0], req[1]);
		int O2 = MathUtil.byte2Int(req[2], req[3]);
		int PH3 = MathUtil.byte2Int(req[4], req[5]);
		alls[0] += O2;
		alls[1] += PH3;
		alls[2] += CO2;
		if(first){
			mins[0] = O2;
			mins[1] = PH3;
			mins[2] = CO2;
			maxs[0] = O2;
			maxs[1] = PH3;
			maxs[2] = CO2;
		} else {
			mins[0] = Math.min(mins[0], O2);
			mins[1] = Math.min(mins[1], PH3);
			mins[2] = Math.min(mins[2], CO2);
			maxs[0] = Math.max(maxs[0], O2);
			maxs[1] = Math.max(maxs[1], PH3);
			maxs[2] = Math.max(maxs[2], CO2);
		}
		//磷化氢报警
		if(att.isWarns(Constant.IDX_G_0) && PH3 > att.getThd(Constant.IDX_G_0)){
			if(an2 == null) an2 = new AlarmNotes(Constant.W_GAN, Constant.W_GAN_1, houseNo, datas.getStartTime(), att.getThd(Constant.IDX_G_0));
			an2.setNums(an2.getNums() + 1);
			warnSet[way - 1] += WarnType.WARN_1;
		}
		//二氧化碳报警
		if(att.isWarns(Constant.IDX_G_1) && CO2 > att.getThd(Constant.IDX_G_1)){
			if(an0 == null) an0 = new AlarmNotes(Constant.W_GAN, Constant.W_GAN_2, houseNo, datas.getStartTime(), att.getThd(Constant.IDX_G_1));
			warnSet[way - 1] += WarnType.WARN_2;
		}
		//氧气报警
		if(att.isWarns(Constant.IDX_G_2) && O2 < att.getThd(Constant.IDX_G_2)){
			if(an1 == null) an1 = new AlarmNotes(Constant.W_GAN, Constant.W_GAN_3, houseNo, datas.getStartTime(), att.getThd(Constant.IDX_G_2));
			warnSet[way - 1] += WarnType.WARN_4;
		}
	}
	
	private void insertAll(){
		//计算平均值
		datas.setAvgO2(alls[0]/(ways.length * 10.0D));
		datas.setAvgPH3(alls[1]/ways.length);
		datas.setAvgCO2(alls[2]/ways.length);
		//最小值
		datas.setMinO2(mins[0]/10.0D);
		datas.setMinPH3(mins[1]);
		datas.setMinCO2(mins[2]);
		//最大值
		datas.setMaxO2(maxs[0]/10.0D);
		datas.setMaxPH3(maxs[1]);
		datas.setMaxCO2(maxs[2]);
		List<AlarmNotes> as = new LinkedList<AlarmNotes>();
		if(an != null && an.getNums() > 0){
			as.add(an);
			an.setTestCode(datas.getId());
			MsgUtil.insertMsg(datas.getHouseNo(), "气体检测异常：异常数量" + an.getNums(), TypeMsg.WARN.val(), Constant.W_GAN, datas.getId());
		}
		if(an0 != null && an0.getNums() > 0){
			as.add(an0);
			an0.setTestCode(datas.getId());
			MsgUtil.insertMsg(datas.getHouseNo(), "二氧化碳超标!", TypeMsg.WARN.val(), Constant.W_GAN, datas.getId());
		}
		if(an1 != null && an1.getNums() > 0){
			as.add(an1);
			an1.setTestCode(datas.getId());
			MsgUtil.insertMsg(datas.getHouseNo(), "氧气低于警戒值!", TypeMsg.WARN.val(), Constant.W_GAN, datas.getId());
		}
		if(an2 != null && an2.getNums() > 0){
			as.add(an2);
			an2.setTestCode(datas.getId());
			MsgUtil.insertMsg(datas.getHouseNo(), "磷化氢超标!", TypeMsg.WARN.val(), Constant.W_GAN, datas.getId());
		}
		if(!Lang.isEmpty(as)){
			datas.setTestTag(TypeTestTag.ABNORMAL.val());
		}
		dao.insert(datas);
		dao.insert(as);
	}
	
	private void addError(String msg){
		an.setNums(an.getNums() + 1);
		datas.setWarnStr(datas.getWarnStr() + msg + "|");
	}
	
	@Override
	public void run() {
		check();
	}
	
}
