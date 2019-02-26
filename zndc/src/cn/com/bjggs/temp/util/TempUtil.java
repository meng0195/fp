package cn.com.bjggs.temp.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.lang.Lang;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TempResults;

@SuppressWarnings("unchecked")
public class TempUtil {

	private static Dao dao;
	
	public static final void clearTempForConfs(){
		for(Map.Entry<String, Map<String, Object>> param : HouseUtil.houseConfs.entrySet()){
			if (param.getValue() != null) {
				if (param.getValue().containsKey(TypeHouseConf.TEMP.code())) {
					param.getValue().remove(TypeHouseConf.TEMP.code());
				}
				if (param.getValue().containsKey(TypeHouseConf.BOARD.code())) {
					param.getValue().remove(TypeHouseConf.BOARD.code());
				}
				if (param.getValue().containsKey(TypeHouseConf.TPS.code())) {
					param.getValue().remove(TypeHouseConf.TPS.code());
				}
				if (param.getValue().containsKey(TypeHouseConf.TPS1.code())) {
					param.getValue().remove(TypeHouseConf.TPS1.code());
				}
			}
		}
	}
	
	/**
	 * 启动时更新测温配置信息
	 * @author	yucy
	 * @date	2017年8月29日
	 * @return	void
	 */
	
	public static final void initTempConfs(final Dao d){
		dao = d;
		Map<String, Object> param;
		
		/*
		 * 将测温信息存进内存中
		 */
		List<TempInfo> temps = dao.query(TempInfo.class, Cnd.cri().asc("houseNo"));
		if (!Lang.isEmpty(temps)) {
			for (TempInfo t : temps) {
				if (HouseUtil.houseConfs.containsKey(t.getHouseNo())) {
					param = HouseUtil.houseConfs.get(t.getHouseNo());
					param.put(TypeHouseConf.TEMP.code(), t);
				}
			}
		}
		
		/*
		 * 将测温板信息存进内存中
		 */
		//获取所有测温板信息,一个list里存着所有仓的TempBoaInfo对象
		List<TempBoaInfo> boards = dao.query(TempBoaInfo.class, Cnd.cri().asc("houseNo").asc("TempType"));
		//根据仓房编号将同一个仓的TempBoaInfo对象存进list
		List<TempBoaInfo> board = null;
		//判断是够查出数据
		if (!Lang.isEmpty(boards)) {
			//一个tb就是一个TempBoaInfo对象
			for (TempBoaInfo tb : boards) {
				//判断houseConfs中是否存在该仓房的信息
				if (HouseUtil.houseConfs.containsKey(tb.getHouseNo())) {
					//将board赋值为null,不然一次赋值后,之后判断就不能用
					board = null;
					//根据仓房编号取到对应的Map对象
					param = HouseUtil.houseConfs.get(tb.getHouseNo());
					//判断取到的Map对象中的Key是否已经存在BOARD,如果存在,根据Key为BOARD的条件 取出该List对象
					if (param.containsKey(TypeHouseConf.BOARD.code())) {
						board = (List<TempBoaInfo>) param.get(TypeHouseConf.BOARD.code());
					}
					//如果不存在该Key,则新建一个List集合
					if (board == null) {
						board = new LinkedList<TempBoaInfo>();
						//将新建的或者已经有的存入Map中
						param.put(TypeHouseConf.BOARD.code(), board);
					}
					//将TempBoaInfo对象存进List中
					board.add(tb);
				}
			}
		}
		
		/*
		 * 将测温温度点位置信息存进内存中
		 */
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("houseNo");
		cri.getOrderBy().asc("yAxis");
		cri.getOrderBy().asc("xAxis");
		cri.getOrderBy().asc("zAxis");
		List<PointInfo> tps = dao.query(PointInfo.class, cri);
		Map<String, PointInfo> tp = null;
		Map<Integer, PointInfo> tp1 = null;
		if (!Lang.isEmpty(tps)) {
			for (PointInfo p : tps) {
				if (HouseUtil.houseConfs.containsKey(p.getHouseNo())) {
					tp = null;
					param = HouseUtil.houseConfs.get(p.getHouseNo());
					if (param.containsKey(TypeHouseConf.TPS.code())) {
						tp = (Map<String, PointInfo>)param.get(TypeHouseConf.TPS.code());
					}
					if (tp == null) {
						tp = new LinkedHashMap<String, PointInfo>();
						param.put(TypeHouseConf.TPS.code(), tp);
					}
					tp.put("C" + p.getCableNo1() + "S" + p.getSensorNo1(), p);
				}
				if (HouseUtil.houseConfs.containsKey(p.getHouseNo())) {
					tp1 = null;
					param = HouseUtil.houseConfs.get(p.getHouseNo());
					if (param.containsKey(TypeHouseConf.TPS1.code())) {
						tp1 = (Map<Integer, PointInfo>)param.get(TypeHouseConf.TPS1.code());
					}
					if (tp1 == null) {
						tp1 = new LinkedHashMap<Integer, PointInfo>();
						param.put(TypeHouseConf.TPS1.code(), tp1);
					}
					tp1.put(p.getPoinNo1(), p);
				}
			}
		}
	}
	
	/**
	 * 重置测温配置信息
	 * @author	yucy
	 * @data	2017-08-29
	 * @param	houseNo
	 * @param	t
	 */
	public static final void refresh(String houseNo, TempInfo t){
		if (HouseUtil.houseConfs.containsKey(houseNo)) {
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if (param != null) {
				param.put(TypeHouseConf.TEMP.code(), t);
			}
		}
	}
	
	/**
	 * 重置测温板配置信息
	 * @author	yucy
	 * @data	2017-08-29
	 * @param	houseNo
	 * @param	t
	 */
	public static final void refresh(String houseNo, List<TempBoaInfo> tb){
		if (HouseUtil.houseConfs.containsKey(houseNo)) {
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if (param != null) {
				param.put(TypeHouseConf.BOARD.code(), tb);
			}
		}
	}
	
	/**
	 * 重置测温温度点配置信息
	 * @author	yucy
	 * @data	2017-08-29
	 * @param	houseNo
	 * @param	t
	 */
	public static final void refreshTPS(String houseNo, Map<String, PointInfo> tps){
		if (HouseUtil.houseConfs.containsKey(houseNo)) {
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if (param != null) {
				param.put(TypeHouseConf.TPS.code(), tps);
			}
		} else {
			Map<String, Object> param = new LinkedHashMap<String, Object>();
			param.put(TypeHouseConf.TPS.code(), tps);
			HouseUtil.houseConfs.put(houseNo, param);
		}
	}
	public static final void refreshTPS1(String houseNo, Map<Integer, PointInfo> tps1){
		if (HouseUtil.houseConfs.containsKey(houseNo)) {
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if (param != null) {
				param.put(TypeHouseConf.TPS1.code(), tps1);
			}
		}
	}
	
	public static synchronized final String getOneJson(){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		reqs.put("oneing", ChecksUtil.ONEINGS);
		reqs.put("ones", ChecksUtil.ONES);
		reqs.put("waits", ChecksUtil.WAITS);
		TempResults res;
		int[] tags = new int[ChecksUtil.ONES.size()];
		int index = 0;
		for(String houseNo : ChecksUtil.ONES){
			res = ChecksUtil.lastChecks.get(houseNo);
			if(res == null || res.getDatas() == null) {
				tags[index] = 2;
			}else{
				tags[index] = res.getDatas().getTestTag();
			}
			index = index + 1;
		}
		reqs.put("tags", tags);
		return JsonUtil.toJson(reqs);
	}
	
	public static synchronized final String getLoopJson(){
		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
		reqs.put("looping", ChecksUtil.LOOPINGS);
		reqs.put("loops", ChecksUtil.LOOPS);
		TempResults res;
		int[] tags = new int[ChecksUtil.LOOPS.size()];
		int index = 0;
		for(String houseNo : ChecksUtil.LOOPS){
			res = ChecksUtil.lastChecks.get(houseNo);
			if(res == null || res.getDatas() == null) {
				tags[index] = 2;
			}else{
				tags[index] = res.getDatas().getTestTag();
			}
			index = index + 1;
		}
		reqs.put("tags", tags);
		return JsonUtil.toJson(reqs);
	}
}
