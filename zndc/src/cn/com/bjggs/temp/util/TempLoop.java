package cn.com.bjggs.temp.util;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.basis.enums.TypeHouse;
import cn.com.bjggs.basis.enums.TypeProtocol;
import cn.com.bjggs.basis.enums.TypeTestTag;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.msg.enums.TypeMsg;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.squery.util.QueryUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmTT;
import cn.com.bjggs.weather.util.WeatherUtil;

/**
 * 检测工具类,循环检测
 * @author	yucy
 * @date	2017-07-18
 */
public class TempLoop extends Thread{
	
	public static void initDao(Dao d){
		dao = d;
	};
	private static Dao dao;
	
	private static final Log log = Logs.getLog(TempLoop.class);
	
	/**
	 * 仓房编号
	 */
	private String houseNo;
	private TempInfo eqment;
	private List<TempBoaInfo> tbis;
	private Map<String, PointInfo> points;
	private AlarmTT att;
	
	private AlarmNotes an;
	private AlarmNotes an0;//温度高限
	private AlarmNotes an1;//温度升高率
	private AlarmNotes an2;//温度异常点
	private AlarmNotes an3;//层均温
	private AlarmNotes an4;//缺点率
	private AlarmNotes an5;//冷芯
	
	private TempResults res = new TempResults();
	
	private TestData datas = new TestData();
	
	/**
	 * 各层温度集合
	 */
	private int[] leveTemps;
	private int[] leveMax;
	private int[] leveMin;
	private int[] leveNum;
	/**
	 * 各区温度集合
	 */
	private int[] leveOne;
	private int[] leveTwo;
	private int[] leveThree;
	private int[] leveFour;
	private int[] numOne;
	private int[] numTwo;
	private int[] numThree;
	private int[] numFour;
	private boolean[] areaTags;
	private int areaLen;
	
	private int allPoints = 0;
	private int inValidPoint = 0;//无效点个数
	
	private int avgT;
	private int maxT = -1024;
	private int minT = -1024;
	
	//冷芯数量 冷芯温度
	private int ccN = 0;
	private int ccT = 0;
		
	/**
	 * 所有检测温度集合
	 */
	private byte[][] allTemps;
	
	/**
	 * 点报警集合
	 */
	private byte[] warnPoints;
	
	private byte[] lastTSet;
	
	private double times = 1;
	
	private boolean saveTag;
	
	private String[][] cableRanges = new String[4][];
	
	public TempLoop(){}
	
	@SuppressWarnings("unchecked")
	public TempLoop(String houseNo){
		this.houseNo = houseNo;
		this.eqment = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		this.tbis = (List<TempBoaInfo>) HouseUtil.get(houseNo, TypeHouseConf.BOARD.code(), TempBoaInfo.class);
		if(eqment == null && (tbis == null || tbis.size() == 0)) {
			addError("仓房线缆信息未配置!");
		}
		this.points = (Map<String, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS.code());
		//判断此次数据是否插入
		this.saveTag = ChecksUtil.isSaveLoop(houseNo);
		if(saveTag){
			ChecksUtil.SAVE_NUM.put(houseNo, ChecksUtil.SAVE_NUM.get(houseNo) + 1);
		}
		this.att = HouseUtil.get(houseNo, TypeHouseConf.WARNS.code(), AlarmTT.class);
		if(att != null){
			if(att.isWarns(Constant.IDX_T_0)){
				this.an0 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_1, houseNo, datas.getTestDate(), att.getThd(Constant.IDX_T_0));//温度高限
				res.setAn0(an0);
			}
			if(att.isWarns(Constant.IDX_T_1)){
				this.an1 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_2, houseNo, datas.getTestDate(), att.getThd(Constant.IDX_T_1));//温度升高率
				res.setAn1(an1);
			}
			if(att.isWarns(Constant.IDX_T_2)){
				this.an2 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_3, houseNo, datas.getTestDate(), att.getThd(Constant.IDX_T_2));//温度异常点
				res.setAn2(an2);
			}
			if(att.isWarns(Constant.IDX_T_3)){
				this.an3 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_4, houseNo, datas.getTestDate(), att.getThd(Constant.IDX_T_3));//层均温
				res.setAn3(an3);
			}
			if(att.isWarns(Constant.IDX_T_4)){
				this.an4 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_5, houseNo, datas.getTestDate(), att.getThd(Constant.IDX_T_4));//缺点率
				res.setAn4(an4);
			}
			if(att.isWarns(Constant.IDX_T_5)){
				this.an5 = new AlarmNotes(Constant.W_TEMP, Constant.W_TEMP_6, houseNo, datas.getTestDate(), att.getThd(Constant.IDX_T_5));//冷心
				res.setAn5(an5);
			}
		}
		
		//初始化各区
		StoreHouse h = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		if(h.getHouseType() == TypeHouse.WARE.val()){
			areaLen = 5;
		} else {
			areaLen = 3;
		}
		leveOne = new int[areaLen];
		leveTwo = new int[areaLen];
		leveThree = new int[areaLen];
		leveFour = new int[areaLen];
		numOne = new int[areaLen];
		numTwo = new int[areaLen];
		numThree = new int[areaLen];
		numFour = new int[areaLen];
		areaTags = new boolean[areaLen];
		//最大层数
		int maxZ = 0;
		if(eqment != null ) maxZ = eqment.getMaxLNum();
		leveTemps = new int[maxZ];
		leveMax = new int[maxZ];
		leveMin = new int[maxZ];
		leveNum = new int[maxZ];
		for(int i = 0; i < maxZ; i++){
			leveMin[i] = -1024;
			leveMax[i] = -1024;
		}
		warnPoints = new byte[eqment == null ? 0 : eqment.getPointNum()];
		//初始化温度升高率验证的前一次温度
		if(att != null && att.isWarns(1) && ChecksUtil.lastChecks.containsKey(houseNo)){
			TempResults lastRes = ChecksUtil.lastChecks.get(houseNo);
			if(lastRes != null && lastRes.getDatas() != null){
				lastTSet = lastRes.getDatas().getTset();
				times = ((new Date()).getTime() - lastRes.getDatas().getTestDate().getTime())/(1000 * 60 * 60 * 24);
			}
		}
		datas = new TestData(TypeCheck.LOOP.val(), Times.now(), houseNo, null);
		datas.setWarns(warnPoints);
		res.setHouseNo(houseNo);
		res.setDatas(datas);
		res.setTag(TypeCT.WAIT.val());
		//将测温板信息依次取出,放入cableRanges
		TempBoaInfo tb;
		for (int i = 0; i < 4; i++) {
			if(tbis == null || tbis.size() <= i) break;
			tb = tbis.get(i);
			if (Strings.isNotBlank(tb.getCableNo())) {
				cableRanges[i] = tb.getCableNo().split(",");
			}
		}
	}
	
	private void check(){
		res.setTag(TypeCT.ING.val());
		if(points == null || points.size() == 0){
			addError("电缆信息未配置或配置有误!");
			datas.setTestTag(TypeTestTag.ABNORMAL.val());
			//如果此次循环数据保存,插入异常数据,并返回
			if (saveTag) {
				insertAll();
			}
		} else {
			//验证该仓房是否正在检测
			if(ChecksUtil.LOOPINGS.contains(houseNo)){
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				addError("仓房正在检测!");
				//如果此次循环数据保存,插入异常数据,并返回
				if (saveTag) {
					insertAll();
				}
			} else {
				//正在检测的仓房
				try {
					ChecksUtil.LOOPINGS.add(houseNo);
					DwrUtil.sendLoop(TempUtil.getLoopJson());
					//获取外温外湿
					getOutTH();
					//获取内温内湿
					getInTH();
					//获取夹层温度
					getLayT();
					//获取通风口温度
					getVentT();
					//获取吊顶温度
					getTopT();
					//获取温度
					getTemps();
					//处理获取的温度
					initDatas();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		ChecksUtil.LOOPINGS.remove(houseNo);
		ChecksUtil.LOOPS.offer(houseNo);
		ChecksUtil.lastChecks.put(houseNo, res);
		QueryUtil.refreshTemp(houseNo);
		if(!ChecksUtil.loopTag){
			DwrUtil.sendLoop(TempUtil.getLoopJson());
		}
	}
	
	private void getOutTH(){
		try{
			TempBoaInfo tb = tbis.get(5);
			if (tb.getBoardType() != TypeProtocol.WEA.val() && Strings.isBlank(tb.getIp())) {
				addError("外温外湿信息未配置或配置有误!");
			} else {
				Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
				//TODO 从气象站获取的外温外湿需要重新编写计算规则类 wc
				if(tb.getBoardType() == TypeProtocol.WEA.val()){
					datas.setOutT(WeatherUtil.testWeather.getTemp());
					datas.setOutH(WeatherUtil.testWeather.getHumidity());
				} else {
					Temps tUtil = TempFactory.createCheck(tb.getBoardType());
					byte[] outTH = check.getTH(tb.getWayNo());
					datas.setOutT(tUtil.getT(outTH, 0, 1));
					datas.setOutH(tUtil.getH(outTH, 2, 3));
				}
			}
		} catch (Exception e){
			if(log.isErrorEnabled()){
				log.error("获取外温外湿失败：仓号 " + houseNo + e.getMessage());
				addError(e.getMessage());
			}
		}
	}
	
	private void getInTH(){
		try{
			TempBoaInfo tb = tbis.get(4);
			if (Strings.isBlank(tb.getIp())) {
				addError("内温内湿信息未配置或配置有误!");
			} else {
				Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
				Temps tUtil = TempFactory.createCheck(tb.getBoardType());
				byte[] inTH = check.getTH(tb.getWayNo());
				datas.setInT(tUtil.getT(inTH, 0, 1));
				datas.setInH(tUtil.getH(inTH, 2, 3));
			}
		} catch (Exception e){
			if(log.isErrorEnabled()){
				log.error("获取内温內湿失败：仓号 " + houseNo + e.getMessage());
				addError(e.getMessage());
			}
		}
	}
	
	//获取夹层温度
	private void getLayT(){
		try{
			TempBoaInfo tb = tbis.get(6);
			if (Strings.isNotBlank(tb.getIp())) {
				Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
				Temps tUtil = TempFactory.createCheck(tb.getBoardType());
				byte[] layT = check.getTH(tb.getWayNo());
				datas.setLayT(tUtil.getT(layT, 0, 1));
			}
		} catch (Exception e){
			if(log.isErrorEnabled()){
				log.error("获取夹层温度失败：仓号 " + houseNo + e.getMessage());
				addError(e.getMessage());
			}
		}
	}
	
	private void getVentT(){
		try{
			TempBoaInfo tb = tbis.get(7);
			if (Strings.isNotBlank(tb.getIp())) {
				Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
				Temps tUtil = TempFactory.createCheck(tb.getBoardType());
				byte[] ventT = check.getTH(tb.getWayNo());
				datas.setVentT(tUtil.getT(ventT, 0, 1));
			}
		} catch (Exception e){
			if(log.isErrorEnabled()){
				log.error("获取通风口温度失败：仓号 " + houseNo + e.getMessage());
				addError(e.getMessage());
			}
		}
	}
	
	private void getTopT(){
		try{
			TempBoaInfo tb = tbis.get(8);
			if (Strings.isNotBlank(tb.getIp())) {
				Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
				Temps tUtil = TempFactory.createCheck(tb.getBoardType());
				byte[] topT = check.getTH(tb.getWayNo());
				datas.setTopT(tUtil.getT(topT, 0, 1));
			}
		} catch (Exception e){
			if(log.isErrorEnabled()){
				log.error("获取吊顶温度失败：仓号 " + houseNo + e.getMessage());
				addError(e.getMessage());
			}
		}
	}
	
	private boolean isWarns(){
		if(!Lang.isEmpty(an) && an.getNums() > 0){
			return true;
		}
		if(!Lang.isEmpty(an0) && an0.getNums() > 0){
			return true;
		}
		if(!Lang.isEmpty(an1) && an1.getNums() > 0){
			return true;
		}
		if(!Lang.isEmpty(an2) && an2.getNums() > 0){
			return true;
		}
		if(!Lang.isEmpty(an3) && an3.getNums() > 0){
			return true;
		}
		if(!Lang.isEmpty(an4) && an4.getNums() > 0){
			return true;
		}
		if(!Lang.isEmpty(an5) && an5.getNums() > 0){
			return true;
		}
		return false;
	}
	
	private void getTemp(byte[][] bss,int i, int type, String ip, int port){
		try{
			Checks check =ModbusFactory.createCheck(type, ip, port);
			bss[i] = check.getTemps();
			//有温度获取成功,需要进行缺点率校验
		} catch(Exception e) {
			addError("读取第" + (i + 1) + "块控制板温度失败：" + e.getMessage());
		}
	}
	
	private void getTemps(){
		allTemps = new byte[4][];
		boolean tag = false;
		for (int i = 0; i < 4; i++) {
			TempBoaInfo tb = tbis.get(i);
			if (Strings.isNotBlank(tb.getIp())) {
				tag = true;
				getTemp(allTemps, 0, tb.getBoardType(), tb.getIp(), tb.getPort());
			}
		}
		if(!tag){
			addError("未配置测温板信息!");
		}
	}
	
	private boolean isComplie(byte cable, int index){
		String[] cs; 
		String[] ss;
		int c = 0;
		try{
			cs = cableRanges[index];
			if(!Lang.isEmpty(cs)){
				for(String s : cs){
					ss = s.split("-");
					c = cable & 0xFF;
					if(ParseUtil.toInt(ss[0], 0) <= c && ParseUtil.toInt(ss[1], 0) >= c){
						return true;
					}
				}
			} else {
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
	
	private void initDatas(){
		StringBuffer key = new StringBuffer("C");
		PointInfo p;
		int temp;
		byte[] ds = new byte[eqment.getPointNum() * 2];
		byte[] bs;
		Temps tUtil;
		int i = 0;
		int len = 0;
		TempBoaInfo tb;
		for(int j = 0; j < 4; j++){
			bs = allTemps[j];
			tb = tbis.get(j);
			tUtil = TempFactory.createCheck(tb.getTempType());
			i = 0;
			if(bs != null){
				len = bs.length - 3;
				while(len > i){
					key = new StringBuffer("C");
					key.append(MathUtil.byte2int(bs[i])).append("S").append(MathUtil.byte2int(bs[i+1]));
					if(points.containsKey(key.toString()) && (isComplie(bs[i], j))){
						p = points.get(key.toString());
						if (p.getValid() != 1) {//如果是有效点
							//获取10倍温度
							temp = tUtil.getT10(bs, i+2, i+3);
							if(temp == 0) temp = 1;
							//修改层温
							setLeveTemp(p, temp);
							setAreaTemp(p, temp);
							//设置对应点温度
							if((p.getPoinNo1() - 1) * 2 <= ds.length - 2){
								System.arraycopy(MathUtil.int2Bytes(temp), 0, ds, (p.getPoinNo1() - 1) * 2, 2);
							}
							//处理预警,单个点的
							doPointWarns(p, temp);
							avgT = avgT + temp;
							maxT = Math.max(temp, maxT);
							minT = -1024 == minT ? temp : Math.min(temp, minT);
						} else {//如果是无效点
							inValidPoint ++;
						}
					}
					i = i + 4;
				}
			}
		}
		//处理异常,整仓的
		doAllwarns();
		datas.setTset(ds);
		setAllDatas();
		if(isWarns()){
			datas.setTestTag(TypeTestTag.ABNORMAL.val());
		} else {
			datas.setTestTag(TypeTestTag.NORMAL.val());
		}
		//插入数据
		if (saveTag) {
			insertAll();
		}
	}
	
	private void setAllDatas(){
		datas.setWarns(warnPoints);
		datas.setAvgT((avgT/(allPoints == 0 ? 1 : allPoints))/10.0D);
		datas.setMaxT(maxT/10.0D);
		datas.setMinT((minT == -1024 ? 0 : minT)/10.0D);
		datas.setCcT(getCcT()/10.0D);
		Map<String, double[]> maps = new LinkedHashMap<String, double[]>();
		double[] arrs;
		for(int i = 0; i < leveTemps.length; i++){
			arrs = new double[3];
			arrs[0] = (leveTemps[i]/(leveNum[i] == 0 ? 1 : leveNum[i]))/10.0D;
			arrs[1] = leveMax[i]/10.0D;
			arrs[2] = ((leveMin[i] == -1024) ? 0 : leveMin[i])/10.0D;
			maps.put(String.valueOf(i), arrs);
		}
		datas.setLayerTs(JsonUtil.toJson(maps));
		
		//各区
		Map<String, double[]> maps1 = new LinkedHashMap<String, double[]>();
		double[] arrs1;
		int[] all = new int[4];
		int ar1, ar2, ar3, ar4;
		int als = 0;
		for(int i = 0; i < areaLen; i++){
			if(areaTags[i]){
				als += 1;
				arrs1 = new double[4];
				ar1 = leveOne[i]/(numOne[i] == 0 ? 1 : numOne[i]);
				ar2 = leveTwo[i]/(numTwo[i] == 0 ? 1 : numTwo[i]);
				ar3 = leveThree[i]/(numThree[i] == 0 ? 1 : numThree[i]);
				ar4 = leveFour[i]/(numFour[i] == 0 ? 1 : numFour[i]);
				arrs1[0] = ar1/10.0D;
				arrs1[1] = ar2/10.0D;
				arrs1[2] = ar3/10.0D;
				arrs1[3] = ar4/10.0D;
				all[0] += ar1;
				all[1] += ar2;
				all[2] += ar3;
				all[3] += ar4;
				maps1.put(String.valueOf(i + 1), arrs1);
			}
		}
		arrs1 = new double[4];
		if(als == 0) als = 1;
		arrs1[0] = (all[0]/als)/10.0D;
		arrs1[1] = (all[1]/als)/10.0D;
		arrs1[2] = (all[2]/als)/10.0D;
		arrs1[3] = (all[3]/als)/10.0D;
		maps1.put("0", arrs1);
		datas.setAreaTs(JsonUtil.toJson(maps1));		
	}
	
	private void insertAll(){
		try{
			//插入异常集合
			List<AlarmNotes> ls = new LinkedList<AlarmNotes>();
			if(an != null && an.getNums() > 0){
				ls.add(an);
				res.setAn(an);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			if(an0 != null && an0.getNums() > 0){
				ls.add(an0);
				res.setAn(an0);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an0.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an0.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			if(an1 != null && an1.getNums() > 0){
				ls.add(an1);
				res.setAn(an1);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an1.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an1.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			if(an2 != null && an2.getNums() > 0){
				ls.add(an2);
				res.setAn(an2);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an2.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an2.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			if(an3 != null && an3.getNums() > 0){
				ls.add(an3);
				res.setAn(an3);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an3.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an3.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			if(an4 != null && an4.getNums() > 0){
				ls.add(an4);
				res.setAn(an4);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an4.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an4.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			if(an5 != null && an5.getNums() > 0){
				ls.add(an5);
				res.setAn(an5);
				datas.setTestTag(TypeTestTag.ABNORMAL.val());
				an5.setTestCode(datas.getId());
				MsgUtil.insertMsg(houseNo, "温度检测异常！异常数量：" + an5.getNums(), TypeMsg.WARN.val(), Constant.W_TEMP, datas.getId());
			}
			//插入异常集合
			dao.insert(ls);
			//插入温度数据
			dao.insert(datas);
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
	private void setLeveTemp(PointInfo p, int temp){
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
	private void setAreaTemp(PointInfo p, int temp){
		if (p.getArea() > 0) {
			int index = p.getArea()-1;
			switch (p.getLeve()) {
			case 1: 
				leveOne[index] += temp;
				numOne[index] += 1;
				break;
			case 2: 
				leveTwo[index] += temp;
				numTwo[index] += 1;
				break;
			case 3: 
				leveThree[index] += temp;
				numThree[index] += 1;
				break;
			case 4: 
				leveFour[index] += temp;
				numFour[index] += 1;
				break;
			default:
				break;
			}
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
	private void doPointWarns(PointInfo p, int temp){
		int index = p.getPoinNo1() - 1;
		if(warnPoints.length > p.getPoinNo1()){
			//处理高温报警
			if(att.isWarns(Constant.IDX_T_0) && temp/10.0D > att.getThd(Constant.IDX_T_0)){
				an0.setNums(an0.getNums() + 1);
				warnPoints[index] += WarnType.WARN_1;
			}
	
			//温度升高率
			if(att.isWarns(Constant.IDX_T_1) && lastTSet != null && index * 2 < (lastTSet.length - 1)){
				if((temp - MathUtil.byte2IntNb(lastTSet[(index * 2)], lastTSet[index * 2 + 1]))/times > att.getThd(Constant.IDX_T_1)){
					an1.setNums(an1.getNums() + 1);
					warnPoints[index] += WarnType.WARN_2;
				}
			}
			
			//温度异常点
			if(att.isWarns(Constant.IDX_T_2) && temp/10.0D > att.getThd(Constant.IDX_T_2)){
				an2.setNums(an2.getNums() + 1);
				warnPoints[index] += WarnType.WARN_4;
			}
		}
	}
	
	private void doAllwarns(){
		//层均温
		if(att.isWarns(Constant.IDX_T_3)){
			for(int i = 0; i < leveTemps.length; i++){
				if((leveTemps[i]/(leveNum[i] == 0 ? 1 : leveNum[i]))/10.0D > att.getThd(3)){
					an3.setNums(an3.getNums() + 1);
					//TODO
				}
			}
		}
		//缺点率
		if(att.isWarns(Constant.IDX_T_4)){
			double miss = Math.round((eqment.getPointNum() - inValidPoint - allPoints) * 1000D/(eqment.getPointNum() == 0 ? 1 : eqment.getPointNum() - inValidPoint));
			if(miss/10.0 > att.getThd(Constant.IDX_T_4)){
				an4.setNums(ParseUtil.toInt(miss));
			}
		}
		//冷芯
		if(att.isWarns(Constant.IDX_T_5) && getCcT()/10.0D > att.getThd(Constant.IDX_T_5)){
			an5.setNums(getCcT());
			an5.setThreshold(att.getThd(Constant.IDX_T_5));
		}
	}
	private int getCcT(){
		return ccT/(ccN == 0 ? 1 : ccN);
	}
	@Override
	public void run() {
		check();
	}
	
}
