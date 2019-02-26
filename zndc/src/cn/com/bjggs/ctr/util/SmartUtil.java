package cn.com.bjggs.ctr.util;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.ctr.domain.SmartCondition;
import cn.com.bjggs.ctr.domain.SmartConf;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.temp.util.TempSmart;
import cn.com.bjggs.weather.util.WeatherUtil;

public class SmartUtil {
	
	/**
	 * 降温通风开启条件温度差
	 */
	public static final int JWS = PropsUtil.getInteger("smart.jw.start", 8);
	
	/**
	 * 降温通风关闭条件温度差
	 */
	public static final int JWE = PropsUtil.getInteger("smart.jw.end", 4);
	
	/**
	 * 自然通风开启条件温度差
	 */
	public static final int ZRS = PropsUtil.getInteger("smart.zr.start", 6);
	
	/**
	 * 自然通风关闭条件温度差
	 */
	public static final int ZRE = PropsUtil.getInteger("smart.zr.end", 2);
	
	/**
	 * 保水通风开启条件温度差
	 */
	public static final int BSS = PropsUtil.getInteger("smart.bs.start", 8);
	
	/**
	 * 保水通风关闭条件温度差
	 */
	public static final int BSE = PropsUtil.getInteger("smart.bs.end", 4);

	/**
	 * 排积热通风开启条件温度差
	 */
	public static final int PJRS = PropsUtil.getInteger("smart.pjr.start", 8);
	
	/**
	 * 排积热通风关闭条件温度差
	 */
	public static final int PJRE = PropsUtil.getInteger("smart.pjr.end", 2);
	
	/**
	 * 内环流开启条件温度差
	 */
	public static final int NHLS = PropsUtil.getInteger("smart.nhl.start", 8);
	
	/**
	 * 内环流冷芯条件
	 */
	public static final int NHLC = PropsUtil.getInteger("smart.nhl.core", 15);
	
	/**
	 * 内环流关闭条件温度差
	 */
	public static final int NHLE = PropsUtil.getInteger("smart.nhl.end", 2);
	
	public static Dao dao;
	
	public static final Map<String, SmartCondition> cons = new LinkedHashMap<String, SmartCondition>();
	
	public static final void initCons(){
		for(String s : HouseUtil.houseMap.keySet()){
			cons.put(s, new SmartCondition());
			cons.get(s).setGrainCode(HouseUtil.get(s, TypeHouseConf.GRAIN.code(), GrainInfo.class).getGrainCode());
		}
	}
	
	public static final void refreshEv(){
		double outT = WeatherUtil.testWeather.getTemp();
		double outH = WeatherUtil.testWeather.getHumidity();
		SmartCondition sc;
		for(Map.Entry<String, SmartCondition> entry : cons.entrySet()){
			sc = entry.getValue();
			sc.setOutT(outT);
			sc.setOutH(outH);
		}
	}
	
	private static final ExecutorService esSao = Executors.newFixedThreadPool(5);
	
	public static final void refreshTemp(){
		for(Map.Entry<String, SmartCondition> entry : cons.entrySet()){
			try {
				//TODO 临时注掉其他仓
					TempSmart checks = new TempSmart(entry.getKey(), entry.getValue());
					esSao.execute(checks);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public static final String Timer = "0 0/" + Math.min(PropsUtil.getInteger("smart.ev.timer", 10), PropsUtil.getInteger("smart.in.timer", 30)) + " * * * ?";
	
	public static final void initTimer(){
		String timer1 = "0 0/" + PropsUtil.getInteger("smart.ev.timer", 10) + " * * * ?";
		String timer2 = "0 0/" + PropsUtil.getInteger("smart.in.timer", 30) + " * * * ?";
		SmartUtil.refreshEv();
		SmartUtil.refreshTemp();
		QuartzUtil.addJob("smartEv", SmartJob.class, timer1);
		QuartzUtil.addJob("smartIn", SmartJob1.class, timer2);
		QuartzUtil.addJob("smartJob", ScanSmartJob.class, timer1);
	}
	
	public static final Map<String, Integer> models = new LinkedHashMap<String, Integer>();
	
	public static final void initModels(Dao d){
		dao = d;
		for(String houseNo : HouseUtil.houseMap.keySet()){
			models.put(houseNo, TypeCtrTag.SD.val());
		}
	}
	
	public static final void changeModel(String houseNo, int model){
		models.put(houseNo, model);
	}
	
	public static final Map<String, Set<String>> modelOpens = new LinkedHashMap<String, Set<String>>();
	
	public static final void removeSmart(String houseNo, String modelCode){
		if(SmartUtil.modelOpens.containsKey(houseNo)){
			Set<String> opens = SmartUtil.modelOpens.get(houseNo);
			if(opens.contains(modelCode)){
				//TODO 停止当前模式所操作的设备；
				opens.remove(modelCode);
				if(opens.size() <= 0){
					SmartUtil.models.put(houseNo, TypeCtrTag.SD.val());
					SmartUtil.modelOpens.remove(houseNo);
				}
			}
		}
	}
	
	public static final void openSmart(String houseNo, String modelCode){
		Set<String> opens;
		if(modelOpens.containsKey(houseNo)){
			opens = modelOpens.get(houseNo);
		} else {
			opens = new HashSet<String>();
			modelOpens.put(houseNo, opens);
		}
		opens.add(modelCode);
		models.put(houseNo, TypeCtrTag.ZN.val());
	}
	
	public static final void initOpens(Dao d){
		dao = d;
		List<SmartConf> list = dao.query(SmartConf.class, Cnd.where("status", "=", 1));
		Set<String> opens;
		if(!Lang.isEmpty(list)){
			for(SmartConf sc : list){
				if(modelOpens.containsKey(sc.getHouseNo())){
					opens = modelOpens.get(sc.getHouseNo());
				} else {
					opens = new HashSet<String>();
					modelOpens.put(sc.getHouseNo(), opens);
				}
				opens.add(sc.getModelCode());
			}
			//添加智能模式标记
			for(String s : HouseUtil.houseMap.keySet()){
				if(modelOpens.containsKey(s)){
					models.put(s, TypeCtrTag.ZN.val());
				}
			}
		}
	}
	
	public static final Map<String, Integer> plans = new LinkedHashMap<String, Integer>();
	
	public static final void initPlans(Dao d){
		dao = d;
		for(String houseNo : HouseUtil.houseMap.keySet()){
			plans.put(houseNo, 0);
		}
	}
	
	public static synchronized void changePlans(String houseNo, int add){
		if(plans.containsKey(houseNo)){
			plans.put(houseNo, plans.get(houseNo) + add);
		}
	}
	
}
