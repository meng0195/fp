package cn.com.bjggs.squery.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypeHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.DateUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.core.util.StringUtil;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.power.enums.TypeEnergy;
import cn.com.bjggs.squery.domain.QueryPointTemp;
import cn.com.bjggs.squery.domain.QueryPower;
import cn.com.bjggs.squery.domain.QueryPowerRank;
import cn.com.bjggs.squery.domain.QueryPowerTrend;
import cn.com.bjggs.squery.domain.QueryTemp;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.util.PassUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.ChecksUtil;
import cn.com.bjggs.temp.util.PointUtil;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.weather.domain.TestWeather;
import cn.com.bjggs.weather.util.WeatherUtil;

@IocBean(name = "queryService", args = { "refer:dao" })
@SuppressWarnings("unchecked")
public class QueryServiceImpl extends BaseServiceImpl implements IQueryService{
	
	public QueryServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public String getGasCurve(Map<String, String[]> params){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		Criteria cri = this.getCriteria(TestGas.class, Cnd.cri(), params);
		cri.getOrderBy().asc("startTime");
		List<TestGas> list = dao.query(TestGas.class, cri);
		
		TestGas tg;
		int len = 0;
		if(list != null) len = list.size();
		double[] avgCO2 = new double[len];
		double[] avgPH3 = new double[len];
		double[] avgO2 = new double[len];
		String[] xAs = new String[len];
		reqs.put("avgCO2", avgCO2);
		reqs.put("avgPH3", avgPH3);
		reqs.put("avgO2", avgO2);
		reqs.put("xAs", xAs);
		//拼接条件
		for(int i = 0; i < len; i++){
			tg = list.get(i);
			avgCO2[i] = tg.getAvgCO2();
			avgPH3[i] = tg.getAvgPH3();
			avgO2[i] = tg.getAvgO2();
			xAs[i] = Times.format("yyyy-MM-dd", tg.getStartTime());
		}
		reqs.put("size", list.size());
		return JsonUtil.toJson(reqs);
	}
	
	public String getPestCurve(Map<String, String[]> params){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		Criteria cri = this.getCriteria(TestPest.class, Cnd.cri(), params);
		cri.getOrderBy().asc("startTime");
		List<TestPest> list = dao.query(TestPest.class, cri);
		
		TestPest tg;
		int len = 0;
		if(list != null) len = list.size();
		int[] avgNum = new int[len];
		int[] maxNum = new int[len];
		int[] minNum = new int[len];
		String[] xAs = new String[len];
		reqs.put("avgNum", avgNum);
		reqs.put("maxNum", maxNum);
		reqs.put("minNum", minNum);
		reqs.put("xAs", xAs);
		//拼接条件
		for(int i = 0; i < len; i++){
			tg = list.get(i);
			avgNum[i] = tg.getAvgNum();
			maxNum[i] = tg.getMaxNum();
			minNum[i] = tg.getMinNum();
			xAs[i] = Times.format("yyyy-MM-dd", tg.getStartTime());
		}
		reqs.put("size", list.size());
		return JsonUtil.toJson(reqs);
	}
	
	public String getPestCompare(Map<String, String[]> params){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		String houseNo = null;
		if(params.containsKey("ft_EQ_S_houseNo")){
			houseNo = params.get("ft_EQ_S_houseNo")[0];
		}
		Date time1 = null;
		if(params.containsKey("ft_EQ_DT_TIME1")){
			time1 = DateUtil.parse("yyyy-MM-dd HH:mm:ss", params.get("ft_EQ_DT_TIME1")[0]);
		}
		Date time2 = null;
		if(params.containsKey("ft_EQ_DT_TIME2")){
			time2 = DateUtil.parse("yyyy-MM-dd HH:mm:ss", params.get("ft_EQ_DT_TIME2")[0]);
		}
		if(null != houseNo && time1 != null && time2 != null){
			PestInfo pi = HouseUtil.get(houseNo, TypeHouseConf.PEST.code(), PestInfo.class);
			TestPest tp1 = dao.fetch(TestPest.class, Cnd.where("houseNo", "=", houseNo).and("startTime", "=", time1));
			TestPest tp2 = dao.fetch(TestPest.class, Cnd.where("houseNo", "=", houseNo).and("startTime", "=", time2));
			reqs.put("layers", pi.getLayers());
			reqs.put("datas1", tp1 == null ? null : tp1.getPset());
			reqs.put("datas2", tp2 == null ? null : tp2.getPset());
			reqs.put("datas", HouseUtil.get(houseNo, TypeHouseConf.PPS.code()));
		}
		return JsonUtil.toJson(reqs);
	}
	
	public String getPestTimes(String houseNo){
		Sql sql = Sqls.create("SELECT StartTime as TESTDATE FROM testPest WHERE houseNo = '" + houseNo + "' ORDER BY TestDate DESC");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> times = sql.getList(Record.class);
		return JsonUtil.toJson(times);
	}
	
	/** 
	 * 异常页面的查看,传过来的testDataId是异常表的ID
	 */
	public String getDetailWarn(String testDataId){
		AlarmNotes an = dao.fetch(AlarmNotes.class, testDataId);
		if (an.getType() == Constant.W_TEMP) {//测温
			TestData ts = dao.fetch(TestData.class, an.getTestCode());
			if(null == ts) throw new RuntimeException("无温度数据！");
			Map<Integer, PointInfo> points = (Map<Integer, PointInfo>) HouseUtil.get(an.getHouseNo(), HouseUtil.TypeHouseConf.TPS1.code());
			StringBuffer sb = new StringBuffer();
			initWarnDetail(points, an, ts, sb);
			return sb.toString();
		} else if (an.getType() == Constant.W_PEST){//测虫
			return getPestWarnDetail(null, an.getTestCode(), an.getType1());
		} else if (an.getType() == Constant.W_GAN){//测气
			return getGasWarnDetail(null, an.getTestCode(), an.getType1());
		}
		return null;
	}
	
	public Record findSheetByTsId(String id){
		Record m = new Record();
		TestData t = dao.fetch(TestData.class, id);
		TestWeather w = WeatherUtil.testWeather;
		if (t == null || Strings.isBlank(t.getHouseNo())) throw new RuntimeException("不存在温度信息！");
		TempInfo eqment = dao.fetch(TempInfo.class, Cnd.where("houseNo", "=", t.getHouseNo()));
		m.put("temps", t);
		m.put("grain", dao.fetch(GrainInfo.class, Cnd.where("houseNo", "=", t.getHouseNo())));
		StoreHouse house = dao.fetch(StoreHouse.class, Cnd.where("houseNo", "=", t.getHouseNo()));
		m.put("house", house);
		m.put("eqment", eqment);
		m.put("title", PassUtil._NAME);
		m.put("wd", w.getSleet()==0?"无雨雪":"有雨雪");
		//获取所有点
		if(house.getHouseType() == TypeHouse.WARE.val()){
			List<List<List<PointInfo>>> points = PointUtil.getPointLevel(t.getHouseNo());
			m.put("html", PointUtil.getSheet(points, eqment, t));
		} else {
			List<List<PointInfo>> points = getPointsYuan(t.getHouseNo());
			m.put("html", PointUtil.getSheetYuan(points, eqment, t));
		}
		m.put("now", Times.format("yyyy-MM-dd HH:mm", new Date()));
		return m;
	}
	
	private List<List<PointInfo>> getPointsYuan(String houseNo){
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo);
		cri.getOrderBy().asc("cableNo1");
		cri.getOrderBy().asc("zaxis");
		List<PointInfo> list = dao.query(PointInfo.class, cri);
		List<List<PointInfo>> pss = new LinkedList<List<PointInfo>>();
		List<PointInfo> ps;
		int[] x = {-1, -1};
		//拼装参数
		for(PointInfo p : list){
			if(x[0] == p.getCableNo1()){
				ps = pss.get(x[1]);
			} else {
				ps = new LinkedList<PointInfo>();
				pss.add(ps);
				x[0] = p.getCableNo1();
				x[1] = pss.size() - 1;
			}
			ps.add(p);
		}
		return pss;
	}
	
	public Record findSheetByHouseNo(String houseNo){
		Record m = new Record();
		TestWeather w = WeatherUtil.testWeather;
		TempResults res = ChecksUtil.lastChecks.get(houseNo);
		if(null == res) throw new RuntimeException("不存在检测记录！");
		TestData t = res.getDatas();
		if (t == null || Strings.isBlank(t.getHouseNo())) throw new RuntimeException("不存在温度信息！");
		TempInfo eqment = HouseUtil.get(t.getHouseNo(), TypeHouseConf.TEMP.code(), TempInfo.class);
		m.put("temps", t);
		m.put("grain", HouseUtil.get(t.getHouseNo(), TypeHouseConf.GRAIN.code(), GrainInfo.class));
		StoreHouse house = HouseUtil.get(t.getHouseNo(), TypeHouseConf.HOUSE.code(), StoreHouse.class);
		m.put("house", house);
		m.put("eqment", eqment);
		m.put("title", PassUtil._NAME);
		m.put("wd", w.getSleet()==0?"无雨雪":"有雨雪");
		//获取所有点
		if(house.getHouseType() == TypeHouse.WARE.val()){
			List<List<List<PointInfo>>> points = PointUtil.getPointLevel(t.getHouseNo());
			m.put("html", PointUtil.getSheet(points, eqment, t));
		} else {
			List<List<PointInfo>> points = getPointsYuan(t.getHouseNo());
			m.put("html", PointUtil.getSheetYuan(points, eqment, t));
		}
		m.put("now", Times.format("yyyy-MM-dd HH:mm", new Date()));
		return m;
	}
	
	public String[] findStereoByHouseNo(String houseNo){
		TempResults res = ChecksUtil.lastChecks.get(houseNo);
		if(res != null){
			return findStere(res.getDatas());
		}
		return new String[]{"", ""};
	}
	
	public String[] findStereoByTsId(String id){
		TestData ts = dao.fetch(TestData.class, id);
		String[] ss = findStere(ts);
		StoreHouse s = HouseUtil.get(ts.getHouseNo(), TypeHouseConf.HOUSE.code(), StoreHouse.class);
		return new String[]{ss[0], s == null ? "" : "type" + s.getHouseType(), ss[1]};
	}
	
	private String[] findStere(TestData ts){
		StringBuffer sb = new StringBuffer();
		StoreHouse house = HouseUtil.get(ts.getHouseNo(), TypeHouseConf.HOUSE.code(), StoreHouse.class);
		if(house != null && !(house.getHouseType() == TypeHouse.WARE.val())){
			TempInfo eqment = HouseUtil.get(ts.getHouseNo(), TypeHouseConf.TEMP.code(), TempInfo.class);
			int maxZ = 1;
			for(String l : eqment.getLnums()){
				maxZ = Math.max(ParseUtil.toInt(l, 0), maxZ);
			}
			double r1 = 72/eqment.getVnum();
			double r2 = 225/eqment.getVnum();
			List<PointInfo> ps = dao.query(PointInfo.class, Cnd.where("houseNo", "=", ts.getHouseNo()));
			int z = 360/maxZ;
			boolean tag = ParseUtil.toInt(eqment.getHnums()[0], 1) == 1;
			if(!tag){
				r1 = 72/(eqment.getVnum() + 1);
				r2 = 225/(eqment.getVnum() + 1);
			}
			for(PointInfo p : ps){
				sb.append(getStereoYuan(r1, r2, z, ts.getTset(), p, eqment, tag));
			}
			for(int i = 1; i <= maxZ; i++){
				sb.append("<span class=\"stereo-btn\" type=\"z\" val=\"").append(i)
					.append("\" style=\"position: absolute;left:")
					.append(-5).append("px;top:").append(70 + (i-1) * z).append("px;\">").append(i).append("</span>");
			}
			sb.append("<input type=\"hidden\" id=\"maxznum\" value=\"" + z + "\" />");
			String cz = "one";
			if(ParseUtil.toInt(eqment.getHnums()[0], 1) == 1){
				if(eqment.getVnum() == 1){
					
				} else if(eqment.getVnum() == 2){
					cz = "two";
				} else if(eqment.getVnum() == 3){
					cz = "three";
				} else {
					cz = "four";
				}
			} else {
				if(eqment.getVnum() == 1){
					cz = "two";
				} else if(eqment.getVnum() == 2){
					cz = "three";
				} else {
					cz = "four";
				} 
			}
			return new String[]{sb.toString(), cz};
		} else {
			TempInfo eqment = HouseUtil.get(ts.getHouseNo(), TypeHouseConf.TEMP.code(), TempInfo.class);
			int x = 825/eqment.getVnum();
			int maxY = 1;
			for(String h : eqment.getHnums()){
				maxY = Math.max(ParseUtil.toInt(h, 0), maxY);
			}
			int y = 196/maxY;
			int maxZ = 1;
			for(String l : eqment.getLnums()){
				maxZ = Math.max(ParseUtil.toInt(l, 0), maxZ);
			}
			int z = 310/maxZ;
			List<PointInfo> ps = dao.query(PointInfo.class, Cnd.where("houseNo", "=", ts.getHouseNo()));
			for(PointInfo p : ps){
				sb.append(getStereo(x, y, z, ts.getTset(), p));
			}
			for(int i = 1; i <= eqment.getVnum(); i++){
				sb.append("<span class=\"stereo-btn\" type=\"x\" val=\"").append(i)
					.append("\" style=\"position: absolute;left:")
					.append(x * i).append("px;top:").append(490).append("px;\">").append(i).append("列</span>");
			}
			for(int i = 1; i <= maxY; i++){
				sb.append("<span class=\"stereo-btn\" type=\"y\" val=\"").append(i)
					.append("\" style=\"position: absolute;left:")
					.append(1030 - i * y).append("px;top:").append(280 + i * y).append("px;\">").append(i).append("行</span>");
			}
			for(int i = 1; i <= maxZ; i++){
				sb.append("<span class=\"stereo-btn\" type=\"z\" val=\"").append(i)
					.append("\" style=\"position: absolute;left:")
					.append(-5).append("px;top:").append(158 + i * z).append("px;\">").append(i).append("层</span>");
			}
			return new String[]{sb.toString(), "one"};
		}
	}
	
	private StringBuffer getStereo(int x, int y, int z, byte[] temps, PointInfo p){
		int index = (p.getPoinNo1()-1) * 2 + 1;
		String temp;
		if(index < temps.length){
			index = MathUtil.byte2IntNb(temps[index - 1], temps[index]);
			if(index == 0){
				temp = "-";
			} else {
				temp = String.valueOf(index/10.0D);
			}
		} else {
			temp = "-";
		}
		StringBuffer sb = new StringBuffer("<span style=\"position: absolute;left:");
		sb.append(197 + (p.getXaxis() + 1) * x - (p.getYaxis() + 1) * y)
			.append("px;top:")
			.append(30 + (p.getZaxis()) * z + (p.getYaxis()) * y)
			.append("px;\" xaxis=\"").append(p.getXaxis() + 1)
			.append("\" yaxis=\"").append(p.getYaxis() + 1)
			.append("\" zaxis=\"").append(p.getZaxis() + 1).append("\">").append(temp).append("</span>");
		return sb;
	}
	
	private StringBuffer getStereoYuan(double r1, double r2, int z, byte[] temps, PointInfo p, TempInfo eqment, boolean tag){
		int index = (p.getPoinNo1()-1) * 2 + 1;
		String temp;
		if(index < temps.length){
			index = MathUtil.byte2IntNb(temps[index - 1], temps[index]);
			if(index == 0){
				temp = "-";
			} else {
				temp = String.valueOf(index/10.0D);
			}
		} else {
			temp = "-";
		}
		double hd = Math.PI * 2/ParseUtil.toInt(eqment.getHnums()[p.getYaxis()], 1);
		StringBuffer sb = new StringBuffer("<span style=\"position: absolute;left:");
		double left = 233;
		double top = 80;
		if(tag){
			left = left - (r2 + 10) * p.getYaxis() * Math.cos(hd * p.getXaxis() + Math.PI/2);
			top = top - (r1 + 5) * p.getYaxis() * Math.sin(hd * p.getXaxis() + Math.PI/2) + (z * p.getZaxis());
		} else {
			left = left - (r2 + 10) * (p.getYaxis() + 1) * Math.cos(hd * p.getXaxis() + Math.PI/2);
			top = top - (r1 + 5) * (p.getYaxis() + 1) * Math.sin(hd * p.getXaxis() + Math.PI/2) + (z * p.getZaxis());
		}
		sb.append(left)
			.append("px;top:")
			.append(top)
			.append("px;\" xaxis=\"").append(p.getXaxis() + 1)
			.append("\" yaxis=\"").append(p.getYaxis() + 1)
			.append("\" zaxis=\"").append(p.getZaxis() + 1).append("\">").append(temp).append("</span>");
		return sb;
	}
	
	/**
	 * 根据仓房编号查出最后一次的记录并返回
	 */
	public Map<String, Object> getWarnDetail(String houseNo, int key){
		Map<String, Object> req = new LinkedHashMap<String, Object>();
		TempResults res = ChecksUtil.lastChecks.get(houseNo);
		TestData ts = res.getDatas();
		AlarmNotes w = res.getAnByType(key);
		if(ts == null || w == null) return req;
		Map<Integer, PointInfo> points = (Map<Integer, PointInfo>) HouseUtil.get(houseNo, HouseUtil.TypeHouseConf.TPS1.code());
		StringBuffer sb = new StringBuffer();
		int[] nums = initWarnDetail(points, w, ts, sb);
		req.put("html", sb.toString());
		if(Constant.W_TEMP_5 == key){
			req.put("all", nums[0]/10.0D);
		}else{
			req.put("all", nums[0]);
		}
		req.put("add", nums[1]);
		return req;
	}
	/**
	 * 根据id查出温度记录并返回
	 */
	public Map<String, Object> getWarnDetailId(String testDataId, int type){
		Map<String, Object> req = new LinkedHashMap<String, Object>();
		TestData td = dao.fetch(TestData.class, Cnd.where("ID", "=", testDataId));
		Criteria cri = Cnd.cri();
		cri.where().andEquals("testCode", td.getId())
				   .andEquals("type1", type);
		AlarmNotes w = dao.fetch(AlarmNotes.class, cri);
		if(w == null || td == null) return req;
		Map<Integer, PointInfo> points = (Map<Integer, PointInfo>) HouseUtil.get(td.getHouseNo(), HouseUtil.TypeHouseConf.TPS1.code());
		StringBuffer sb = new StringBuffer();
		int[] nums = initWarnDetail(points, w, td, sb);
		req.put("html", sb.toString());
		if(Constant.W_TEMP_5 == type){
			req.put("all", nums[0]/10.0D);
		}else{
			req.put("all", nums[0]);
		}
		req.put("add", nums[1]);
		return req;
	}
	
	/**
	 * 温度异常处理
	 * @param points
	 * @param w
	 * @param ts
	 * @param sb
	 * @return
	 */
	private int[] initWarnDetail(Map<Integer, PointInfo> points, AlarmNotes w, TestData ts, StringBuffer sb){
		int add = 0;
		int all = 0;
		PointInfo p;
		byte[] ws = ts.getWarns();
		byte[] temps = ts.getTset();
		double temp = 0.0D;
		if(ws !=null && temps != null && ws.length * 2 == temps.length){
			switch (w.getType1()) {
			case Constant.W_TEMP_1://高温预警
				sb.append("<tr><td>异常点号</td><td>温度(℃)</td><td>线缆号</td><td>传感器号</td><td>坐标</td></tr>");
				for(int i = 0; i < ws.length; i++){
					all = all + 1;
					if(WarnType.isWarn1(ws[i])){
						temp = MathUtil.byte2IntNb(temps[i*2], temps[i*2+1])/10.0D;
						p = points.get(i+1);
						addPoint(p, temp, sb);
						add = add + 1;
					}
				}
				break;
			case Constant.W_TEMP_2://温度升高率预警
				sb.append("<tr><td>异常点号</td><td>温度(℃)</td><td>线缆号</td><td>传感器号</td><td>坐标</td></tr>");
				for(int i = 0; i < ws.length; i++){
					all = all + 1;
					if(WarnType.isWarn2(ws[i])){
						temp = MathUtil.byte2IntNb(temps[i*2], temps[i*2+1])/10.0D;
						p = points.get(i+1);
						addPoint(p, temp, sb);
						add = add + 1;
					}
				}
				break;
			case Constant.W_TEMP_3://温度异常点
				sb.append("<tr><td>异常点号</td><td>温度(℃)</td><td>线缆号</td><td>传感器号</td><td>坐标</td></tr>");
				for(int i = 0; i < ws.length; i++){
					all = all + 1;
					if(WarnType.isWarn4(ws[i])){
						temp = MathUtil.byte2IntNb(temps[i*2], temps[i*2+1])/10.0D;
						p = points.get(i+1);
						addPoint(p, temp, sb);
						add = add + 1;
					}
				}
				break;
			case Constant.W_TEMP_4://层均温预警
				sb.append("<tr><td>报警层</td><td>层均温(℃)</td><td>层最高温(℃)</td><td>层最低温(℃)</td></tr>");
				if(ts.getLayerTsMap() != null){
					List<Double> layers;
					for(Map.Entry<String, Object> entry : ts.getLayerTsMap().entrySet()){
						all = all + 1;
						try{
							layers = (List<Double>)entry.getValue();
							if(layers.get(0) > w.getThreshold()){
								sb.append("<tr><td>")
									.append(entry.getKey())
									.append("</td><td>")
									.append(layers.get(0))
									.append("</td><td>")
									.append(layers.get(1))
									.append("</td><td>")
									.append(layers.get(2))
									.append("</td></tr>");
								add = add + 1;
							};
						}catch(Exception e){}
					}
				}
				break;
			case Constant.W_TEMP_5://缺点率
				break;
			case Constant.W_TEMP_6://冷芯
				sb.append("<tr><td>异常点号</td><td>温度(℃)</td><td>线缆号</td><td>传感器号</td><td>坐标</td></tr>");
				for(int i = 0; i < ws.length; i++){
					all = all + 1;
					if(WarnType.isWarn8(ws[i])){
						temp = MathUtil.byte2IntNb(temps[i*2], temps[i*2+1])/10.0D;
						p = points.get(i+1);
						addPoint(p, temp, sb);
						add = add + 1;
					}
				}
				break;
			case Constant.W_DO://检测异常
				sb.append("<tr><td>序号</td><td>异常描述</td></tr>");
				String[] ds = ts.getTestWarns().split("\\|");
				for(int i = 0; i < ds.length; i++){
					if(Strings.isNotBlank(ds[i])){
						sb.append("<tr><td>")
							.append(i+1)
							.append("</td><td>")
							.append(ds[i])
							.append("</td></tr>");
						all = all + 1;
					}
				}
				break;
			default:
				break;
			}
		} else {
			switch (w.getType1()) {
			case Constant.W_DO://检测异常
				sb.append("<tr><td>序号</td><td>异常描述</td></tr>");
				String[] ds = ts.getTestWarns().split("\\|");
				for(int i = 0; i < ds.length; i++){
					if(Strings.isNotBlank(ds[i])){
						sb.append("<tr><td>")
							.append(i+1)
							.append("</td><td>")
							.append(ds[i])
							.append("</td></tr>");
						all = all + 1;
					}
				}
				break;
			default:
				break;
			}
		}
		return new int[]{all, add};
	}
	
	private void addPoint(PointInfo p, double temp, StringBuffer sb){
		sb.append("<tr><td>")
			.append(p.getPoinNo1())
			.append("</td><td>")
			.append(temp)
			.append("</td><td>")
			.append(p.getCableNo1())
			.append("</td><td>")
			.append(p.getSensorNo1())
			.append("</td><td>第")
			.append(p.getYaxis() + 1)
			.append("列-第")
			.append(p.getXaxis() + 1)
			.append("行-第")
			.append(p.getZaxis() + 1)
			.append("层</td></tr>");
	}
	
	public String getCurve(QueryTemp qt){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo",qt.getHouseNo()).andBetween("testDate", qt.getStartTime(), qt.getEndTime());
		if (qt.getFlag()) {
			cri.where().andEquals("curveFlag", "1");
		}
		cri.getOrderBy().asc("testDate");
		List<TestData> list = dao.query(TestData.class, cri);
		TestData t;
		int len = 0;
		if(list != null) len = list.size();
		double[] inTs = new double[len];
		double[] inHs = new double[len];
		double[] outTs = new double[len];
		double[] outHs = new double[len];
		double[] avgTs = new double[len];
		double[] minTs = new double[len];
		double[] maxTs = new double[len];
		double[] layTs = new double[len];
		double[] ventTs = new double[len];
		double[] topTs = new double[len];
		String[] xAs = new String[len];
		reqs.put("inTs", inTs);
		reqs.put("inHs", inHs);
		reqs.put("outTs", outTs);
		reqs.put("outHs", outHs);
		reqs.put("avgTs", avgTs);
		reqs.put("minTs", minTs);
		reqs.put("maxTs", maxTs);
		reqs.put("layTs", layTs);
		reqs.put("ventTs", ventTs);
		reqs.put("topTs", topTs);
		reqs.put("xAs", xAs);
		//拼接条件
		for(int i = 0; i < len; i++){
			t = list.get(i);
			inTs[i] = t.getInT();
			inHs[i] = t.getInH();
			outTs[i] = t.getOutT();
			outHs[i] = t.getOutH();
			avgTs[i] = t.getAvgT();
			minTs[i] = t.getMinT();
			maxTs[i] = t.getMaxT();
			layTs[i] = t.getLayT();
			ventTs[i] = t.getVentT();
			topTs[i] = t.getTopT();
			xAs[i] = DateUtil.format("yyyy-MM-dd HH:mm", t.getTestDate());
		}
		reqs.put("size", list.size());
		return JsonUtil.toJson(reqs);
	}
	
	public String getCps(QueryTemp query){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo",query.getHouseNo()).andBetween("testDate", query.getStartTime(), query.getEndTime());
		cri.getOrderBy().asc("testDate");
		List<TestData> list = dao.query(TestData.class, cri);
		TestData t;
		int len = 0;
		if(list != null) len = list.size();
		double[] inTs = new double[len];
		double[] inHs = new double[len];
		double[] outTs = new double[len];
		double[] outHs = new double[len];
		double[] avgTs = new double[len];
		double[] minTs = new double[len];
		double[] maxTs = new double[len];
		String[] xAs = new String[len];
		reqs.put("inTs", inTs);
		reqs.put("inHs", inHs);
		reqs.put("outTs", outTs);
		reqs.put("outHs", outHs);
		reqs.put("avgTs", avgTs);
		reqs.put("minTs", minTs);
		reqs.put("maxTs", maxTs);
		reqs.put("xAs", xAs);
		//拼接条件
		for(int i = 0; i < len; i++){
			t = list.get(i);
			inTs[i] = t.getInT();
			inHs[i] = t.getInH();
			outTs[i] = t.getOutT();
			outHs[i] = t.getOutH();
			avgTs[i] = t.getAvgT();
			minTs[i] = t.getMinT();
			maxTs[i] = t.getMaxT();
			xAs[i] = DateUtil.format("yyyy-MM-dd HH:mm", t.getTestDate());
		}
		reqs.put("size", list.size());
		return JsonUtil.toJson(reqs);
	}
	
	public String getTempDate(String houseNo, String date){
		StringBuffer str = new StringBuffer();
		Sql sql = Sqls.create("SELECT TestDate as TESTDATE FROM testdata WHERE HouseNo = '"+houseNo+"' AND TestDate LIKE '"+date+"%' ORDER BY TestDate DESC");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> listDates = sql.getList(Record.class);
		if(Lang.isEmpty(listDates)){
			str.append("<option value=''>-无数据-</option>");
		}
		for (Record testData : listDates) {
			str.append("<option value='" + Times.format("yyyy-MM-dd HH:mm:ss", (Date)testData.get("TESTDATE")) + "'>" + Times.format("HH:mm:ss", (Date)testData.get("TESTDATE"))  + "</option>");
		}
		return str.toString();
	}
	
	public PestResults getPestById(String testId){
		PestResults result = new PestResults();
		TestPest pest = dao.fetch(TestPest.class, testId);
		result.setPest(pest);
		if(pest != null) {
			result.setHouseNo(pest.getHouseNo());
			List<AlarmNotes> ans = dao.query(AlarmNotes.class, Cnd.where("testCode", "=", testId));
			if(!Lang.isEmpty(ans)){
				for(AlarmNotes an : ans){
					if(an != null){
						if(an.getType1() == Constant.W_PEST_1){
							result.setAn0(an);
						} else if(an.getType1() == Constant.W_PEST_2){
							result.setAn1(an);
						} else if(an.getType1() == Constant.W_DO){
							result.setAn(an);
						}
					}
				}
			}
		}
		return result;
	}
	
	public GasResults getGasById(String testId){
		GasResults result = new GasResults();
		TestGas gas = dao.fetch(TestGas.class, testId);
		result.setGas(gas);
		if(gas != null) {
			result.setHouseNo(gas.getHouseNo());
			List<AlarmNotes> ans = dao.query(AlarmNotes.class, Cnd.where("testCode", "=", testId));
			if(!Lang.isEmpty(ans)){
				for(AlarmNotes an : ans){
					if(an != null){
						if(an.getType1() == Constant.W_GAN_1){
							result.setAnPH3(an);
						} else if(an.getType1() == Constant.W_GAN_2){
							result.setAnCO2(an);
						} else if(an.getType1() == Constant.W_GAN_3){
							result.setAnO2(an);
						} else if(an.getType1() == Constant.W_DO){
							result.setAn(an);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 测气异常处理
	 */
	public String getGasWarnDetail(String houseNo, String testId, int type){
		StringBuffer sb = new StringBuffer();
		TestGas gas = null;
		if(Strings.isNotBlank(houseNo) && !"0".equals(houseNo)){
			GasResults res = GasUtil.lastChecks.get(houseNo);
			if(res != null){
				gas = res.getGas();
			}
		} else {
			gas = dao.fetch(TestGas.class, testId);
		}
		if(type == Constant.W_DO && gas != null && !Lang.isEmpty(gas.getWarnStr())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">序号</td><td>错误详情</td></tr>");
			int index = 1;
			for(String s : gas.getWarnArrs()){
				sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(s)
					.append("</td></tr>");
				index++;
			}
			sb.append("</table>");
		} else if(type == Constant.W_GAN_1 && gas != null && !Lang.isEmpty(gas.getWarnSet())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">风路</td><td>PH3浓度(ppm)</td></tr>");
			int index = 1;
			for(byte b : gas.getWarnSet()){
				if(WarnType.isWarn1(b)){
					sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(MathUtil.byte2Int(gas.getGasSet()[(index-1) * 6 + 2], gas.getGasSet()[(index-1) * 6 + 3]))
					.append("</td></tr>");
				}
				index++;
			}
			sb.append("</table>");
		} else if(type == Constant.W_GAN_2 && gas != null && !Lang.isEmpty(gas.getWarnSet())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">风路</td><td>二氧化碳浓度(ppm)</td></tr>");
			int index = 1;
			for(byte b : gas.getWarnSet()){
				if(WarnType.isWarn2(b)){
					sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(MathUtil.byte2Int(gas.getGasSet()[(index-1) * 6 + 4], gas.getGasSet()[(index-1) * 6 + 5]))
					.append("</td></tr>");
				}
				index++;
			}
			sb.append("</table>");
		} else if(type == Constant.W_GAN_3 && gas != null && !Lang.isEmpty(gas.getWarnSet())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">风路</td><td>氧气占比(%)</td></tr>");
			int index = 1;
			for(byte b : gas.getWarnSet()){
				if(WarnType.isWarn4(b)){
					sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(MathUtil.byte2Int(gas.getGasSet()[(index-1) * 6], gas.getGasSet()[(index-1) * 6 + 1])/10.0D)
					.append("</td></tr>");
				}
				index++;
			}
			sb.append("</table>");
		}
		return sb.toString();
	}
	
	/**
	 * 测虫异常处理
	 */
	public String getPestWarnDetail(String houseNo, String testId, int type){
		StringBuffer sb = new StringBuffer();
		TestPest pest = null;
		if(Strings.isNotBlank(houseNo) && !"0".equals(houseNo)){
			PestResults res = CheckPest.lastPests.get(houseNo);
			if(res != null){
				pest = res.getPest();
			}
		} else {
			pest = dao.fetch(TestPest.class, testId);
		}
		if(type == Constant.W_DO && pest != null && !Lang.isEmpty(pest.getWarnArrs())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">序号</td><td>错误详情</td></tr>");
			int index = 1;
			for(String s : pest.getWarnArrs()){
				sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(s)
					.append("</td></tr>");
				index++;
			}
			sb.append("</table>");
		} else if(type == Constant.W_PEST_1 && pest != null && !Lang.isEmpty(pest.getWarns())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">点号</td><td>虫数</td></tr>");
			int index = 1;
			for(byte b : pest.getWarns()){
				if(WarnType.isWarn1(b)){
					sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(MathUtil.byte2int(b))
					.append("</td></tr>");
				}
				index++;
			}
			sb.append("</table>");
		} else if(type == Constant.W_PEST_2 && pest != null && !Lang.isEmpty(pest.getWarns())){
			sb.append("<table class=\"checkDetail\" style=\"width:100%;\">");
			sb.append("<tr><td width=\"15%\">点号</td><td>虫数</td></tr>");
			int index = 1;
			for(byte b : pest.getWarns()){
				if(WarnType.isWarn2(b)){
					sb.append("<tr><td>")
					.append(index)
					.append("</td><td>")
					.append(MathUtil.byte2int(b))
					.append("</td></tr>");
				}
				index++;
			}
			sb.append("</table>");
		}
		return sb.toString();
	}
	
	public String getJson3d(String houseNo, String testId){
		Map<String, Object> res = new LinkedHashMap<String, Object>();
		TestData datas = null;
		if(Strings.isNotBlank(testId) && !Strings.equals("0", testId)){
			datas = dao.fetch(TestData.class, testId);
		} else {
			TempResults tr = ChecksUtil.lastChecks.get(houseNo);
			datas = tr.getDatas();
		}
		houseNo = datas.getHouseNo();
		TempInfo ti = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		Criteria cri = Cnd.cri();
		cri.where().and("testDate", "<", datas.getTestDate());
		cri.where().andEquals("houseNo", houseNo);
		cri.getOrderBy().desc("testDate");
		TestData dataOld = dao.fetch(TestData.class, cri);
		res.put("xaxis", ti.getVnum());
		int[] xaxis = new int[ti.getHnums().length];
		for(int i = 0; i < ti.getHnums().length; i++){
			xaxis[i] = ParseUtil.toInt(ti.getHnums()[i], 0);
		}
		res.put("yaxis", xaxis);
		int[] zxais = new int[ti.getLnums().length];
		for(int i = 0; i < ti.getLnums().length; i++){
			zxais[i] = ParseUtil.toInt(ti.getLnums()[i], 0);
		}
		res.put("zaxis", zxais);
		if(dataOld == null) {
			dataOld = new TestData();
			dataOld.setTset(new byte[datas.getTset().length]);
		}
		res.put("time", Times.format("yyyy-MM-dd HH:mm:ss", datas.getTestDate()));
		res.put("time1", Times.format("yyyy-MM-dd HH:mm:ss", dataOld.getTestDate()));
		Map<Integer, PointInfo> tps1 = (Map<Integer, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS1.code());
		byte[] warns = datas.getWarns();
		byte[] tset = datas.getTset();
		byte[] tset1 = dataOld.getTset();
		List<QueryPointTemp> list = new LinkedList<QueryPointTemp>();
		for(int i = 0; i < tset.length; i += 2){
			list.add(new QueryPointTemp(tset, tset1, tps1.get(i/2+1), warns, i));
		}
		res.put("body", list);
		return JsonUtil.toJson(res);
	}

	@Override
	public Map<String, Map<String, Double>> queryPowerList(QueryPower qt) {
		String sqlState = "SELECT HouseNo,ModelType,SUM(Kwhs) AS Kwhs,SUM(Kvarhs) AS Kvarhs FROM energytimer WHERE EndTime >= '" + qt.getStartTime()
				+ "' AND EndTime <= '" + qt.getEndTime() + "'";
		if (Strings.isNotBlank(qt.getHouseNo())) {
			sqlState += " AND houseNo = " + qt.getHouseNo();
		}
		sqlState += " GROUP BY HouseNo,ModelType";
		Sql sql = Sqls.create(sqlState);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> ents = sql.getList(Record.class);
		Map<String, Map<String, Double>> map = new LinkedHashMap<String, Map<String,Double>>();
		Map<String, Double> houseMap = null;
		for (Record e : ents) {;
			if (map.containsKey(e.get("houseNo"))) {//若存在该仓房的map,根据编号取出value Map,将值附进Map
				map.get(e.get("houseNo")).put(e.getString("modeltype"), e.getDouble("kwhs"));
			} else {
				houseMap = new LinkedHashMap<String, Double>();
				houseMap.put(e.getString("modeltype"), e.getDouble("kwhs"));
				map.put(e.getString("houseNo"), houseMap);
			}
		}
		return map;
	}

	@Override
	public String getPowerRank(QueryPowerRank qr) {
		String sqlState = "SELECT HouseNo,ModelType,SUM(Kwhs) AS Kwhs,SUM(Kvarhs) AS Kvarhs FROM energytimer WHERE EndTime >= '" + qr.getStartTime()
				+ "' AND EndTime <= '" + qr.getEndTime() + "'";
		if (qr.getHouseNo() != null) {
			sqlState += " AND houseNo IN (" + StringUtil.replaceAndWrap(StringUtil.join(qr.getHouseNo(), ","), ",", "','", "'", "'")+")";
		}
		sqlState += " GROUP BY HouseNo,ModelType";
		Sql sql = Sqls.create(sqlState);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> list = sql.getList(Record.class);
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		int len = 0;
		String[] houseNos = null;
		if (qr.getHouseNo() != null) {
			len = qr.getHouseNo().length;
			houseNos = qr.getHouseNo();
		} else {
			len = HouseUtil.houseMap.size();
			houseNos = new String[len];
			HouseUtil.houseMap.keySet().toArray(houseNos);
		}
		double[] vent = new double[len];//通风
		double[] nvc = new double[len];//照明
		double[] air = new double[len];//空调
		double[] inLoop = new double[len];//内环流
		double[] heat = new double[len];//排积热
		reqs.put("vent", vent);
		reqs.put("nvc", nvc);
		reqs.put("air", air);
		reqs.put("inLoop", inLoop);
		reqs.put("heat", heat);
		reqs.put("houseNos", houseNos);
		//存储上面数组的引用,能提出来判断
		double[] arr = null;
		//判断应该将值放在哪个数组里
		for(Record r : list){
			if (Strings.equals(TypeEnergy.T.code(), r.getString("modeltype"))) {
				arr = vent;
			} else if (Strings.equals(TypeEnergy.Z.code(), r.getString("modeltype"))){
				arr = nvc;
			} else if (Strings.equals(TypeEnergy.K.code(), r.getString("modeltype"))){
				arr = air;
			} else if (Strings.equals(TypeEnergy.N.code(), r.getString("modeltype"))){
				arr = inLoop;
			} else if (Strings.equals(TypeEnergy.P.code(), r.getString("modeltype"))){
				arr = heat;
			} else {
				arr = null;
			}
			//判断将值放到数组的哪个位置
			if(arr != null){
				for(int i = 0; i < houseNos.length; i++){
					if(Strings.equals(r.getString("houseNo"), houseNos[i])){
						arr[i] = r.getDouble("kwhs");
						break;
					}
				}
			}
		}
		return JsonUtil.toJson(reqs);
	}

	@Override
	public String getPowerTrend(QueryPowerTrend qt) {
		String sqlState = "SELECT sum(kwhs) AS kwhs, FLOOR((UNIX_TIMESTAMP(EndTime) - UNIX_TIMESTAMP('" + qt.getTime() + "'))/" + getTimeStamp(qt.getTimeType()) + ") as gap FROM energytimer WHERE 1=1 ";
		if (qt.getHouseNo() != null && Strings.isNotBlank(qt.getHouseNo())) {
			sqlState += " AND houseNo = '" + qt.getHouseNo() + "'";
		}
		if (qt.getModelType() != null && qt.getModelType().length != 0) {
			sqlState += " AND ModelType IN (" + StringUtil.replaceAndWrap(StringUtil.join(qt.getModelType(), ","), ",", "','", "'", "'")+ ")";
		}
		sqlState += " AND EndTime >= '" + qt.getTime() + " 00:00:00' GROUP BY gap ORDER BY gap ASC";
		Sql sql = Sqls.create(sqlState);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> list = sql.getList(Record.class);
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		int len = 0;
		if(list != null) len  = list.get(list.size() - 1).getInt("gap", 0) + 1;
		int type = qt.getTimeType();
		String date = qt.getTime();
		Record t = null;
		double[] kwhs = new double[len];
		String[] time = new String[len];
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			t = list.get(i);
			if(index == t.getInt("gap", 0)){
				t = list.get(i);
				kwhs[index] = t.getDouble("kwhs");
				time[index] = getTime(type, index, date);
				index++;
			} else {
				for(int j = index; j <= t.getInt("gap", 0); j++){
					if(j != t.getInt("gap", 0)){
						kwhs[index] = 0D;
						time[index] = getTime(type, index, date);
						index++;
					} else {
						kwhs[index] = t.getDouble("kwhs");
						time[index] = getTime(type, index, date);
						index++;
					}
					
				}
			}
		}
		reqs.put("kwhs", kwhs);
		reqs.put("times", time);
		return JsonUtil.toJson(reqs);
	}
	/**
	 * 按时间类型或是对应时间戳
	 * @param type
	 * @return
	 */
	private long getTimeStamp(int type){
		long timestamp = 60 * 60 * 24 * 7;
		if (type == 1) {
			timestamp = 60 * 60 * 24;
		} else if (type == 2) {
			timestamp = 60 * 60 * 24 * 7;
		} else if (type == 3) {
			timestamp = 60 * 60 * 24 * 30;
		} else if (type == 4) {
			timestamp = 60 * 60 * 24 * 90;
		} else if (type == 5) {
			timestamp = 60 * 60 * 24 * 365;
		}
		return timestamp;
	}
	
	private String getTime(int type, int i, String date){
		long l = Times.ams(date);
		long l_h = l + i * getTimeStamp(type) * 1000;
		String s = Times.format("yyyy-MM-dd", new Date(l_h));
		return s;
	}
	
	
	@SuppressWarnings("serial")
	@Override
	public String queryPowerPie(QueryPower qt) {
		String sqlState = "SELECT ModelType,SUM(Kwhs) AS Kwhs,SUM(Kvarhs) AS Kvarhs FROM energytimer WHERE EndTime >= '" + qt.getStartTime()
				+ "' AND EndTime <= '" + qt.getEndTime() + "'";
		if (Strings.isNotBlank(qt.getHouseNo())) {
			sqlState += " AND houseNo = " + qt.getHouseNo();
		}
		sqlState += " GROUP BY ModelType";
		Sql sql = Sqls.create(sqlState);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> ents = sql.getList(Record.class);
		List<Map<String, String>> list = new LinkedList<Map<String,String>>();
		for(final Record r : ents){
			list.add(new LinkedHashMap<String, String>(){{
				put("value", r.getString("kwhs"));
				put("name", Enums.get("TYPE_ENERGY", r.getString("modeltype")));
			}});
		}
		/*Map<String, Object> houseMap = new LinkedHashMap<String, Object>();
		for (Record e : ents) {
				houseMap.put(e.getString("modeltype"), e.getDouble("kwhs"));
		}*/
		return JsonUtil.toJson(list);
	}
	
	@Override
	public void checkTempReport(String id) {
		//根据id查出该条数据,取到该条数据的检测日期,比较是否已经存在该天设置为日标记
		TestData t = dao.fetch(TestData.class, Cnd.where("id", "=", id));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String d = sdf.format(t.getTestDate());
		Criteria cri = Cnd.cri();
		cri.where().and("reportFlag", "=", "1").andEquals("houseNo", t.getHouseNo()).andLike("testDate", d);
		List<TestData> ts = dao.query(TestData.class, cri);
		if (ts.size() != 0) {
			for (TestData testData : ts) {
				this.updateStatus("temp", "reportFlag", testData.getId(), TypeFlag.NO.val());
			}
		}
		
	}
	
	public void updateStatus(String type, String flag, String id, int status){
		if ("temp".equals(type)) {
			dao.update(TestData.class, Chain.make(flag, status == TypeFlag.YES.val() ? TypeFlag.YES.val() : TypeFlag.NO.val()), Cnd.where("id", "=", id));
		} else if ("pest".equals(type)) {
			dao.update(TestPest.class, Chain.make(flag, status == TypeFlag.YES.val() ? TypeFlag.YES.val() : TypeFlag.NO.val()), Cnd.where("id", "=", id));
		} else if ("gas".equals(type)) {
			dao.update(TestGas.class, Chain.make(flag, status == TypeFlag.YES.val() ? TypeFlag.YES.val() : TypeFlag.NO.val()), Cnd.where("id", "=", id));
		}
		
	}

	@Override
	public String queryPowerCurves(QueryPower qt) {
		String sqlState = "SELECT DATE_FORMAT(CheckTime,'%Y-%m-%d') days,sum(power) power from powernotes WHERE CheckTime BETWEEN '" + qt.getStartTime()
				+ "' AND '" + qt.getEndTime() + "'";
		if (Strings.isNotBlank(qt.getHouseNo())) {
			sqlState += " AND houseNo = " + qt.getHouseNo();
		}
		sqlState += " GROUP BY days";
		Sql sql = Sqls.create(sqlState);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> ents = sql.getList(Record.class);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		double[] kwhs = new double[ents.size()];
		String[] time = new String[ents.size()];
		
		for (int i = 0; i < ents.size(); i++) {
			Record r = ents.get(i);
			kwhs[i] = r.getDouble("power");
			time[i] = r.getString("days");
		}
		map.put("date", time);
		map.put("power", kwhs);
		return JsonUtil.toJson(map);
	}

	
}
