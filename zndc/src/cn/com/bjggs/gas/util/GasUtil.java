package cn.com.bjggs.gas.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.warns.domain.AlarmNotes;

public class GasUtil {
	
	private static Dao dao;
	
	public static int MAX_CHECK_NUM = PropsUtil.getInteger("gas.max.one", 5);
	
	public static final Map<String, GasResults> lastChecks = new LinkedHashMap<String, GasResults>();
	/**
	 * 要检测的仓房
	 */
	public static final Set<String> checks = new LinkedHashSet<String>();
	
	/**
	 * 正要检测的仓房
	 */
	public static final Set<String> checking = new LinkedHashSet<String>();
	
	public static final void initLastChecks(final Dao d){
		dao = d;
		Sql sql = Sqls.create("SELECT t.* FROM testgas t INNER JOIN (SELECT MAX(StartTime) as StartTime, HouseNo FROM testgas GROUP BY HouseNo) t2 on t.HouseNo = t2.HouseNo and t.StartTime = t2.StartTime");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(TestGas.class));
		dao.execute(sql);
		List<TestGas> gass = sql.getList(TestGas.class);
		GasResults res;
		for(TestGas gas : gass){
			res = new GasResults();
			res.setTag(TypeCT.END.val());
			res.setHouseNo(gas.getHouseNo());
			res.setGas(gas);
			List<AlarmNotes> ans = dao.query(AlarmNotes.class, Cnd.where("testCode", "=", gas.getId()));
			for (AlarmNotes an : ans) {
				if(an != null){
					if(an.getType1() == Constant.W_GAN_1){
						res.setAnPH3(an);
					} else if(an.getType1() == Constant.W_GAN_2){
						res.setAnCO2(an);
					} else if(an.getType1() == Constant.W_GAN_3){
						res.setAnO2(an);
					} else if(an.getType1() == Constant.W_DO){
						res.setAn(an);
					}
				}
			}
			lastChecks.put(gas.getHouseNo(), res);
		}
	}
	
	public static final String getGasJson(){
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		GasResults pr = null;
		for(Entry<String, GasResults> entry : lastChecks.entrySet()){
			pr = entry.getValue();
			if(pr == null || pr.getGas() == null || pr.getGas().getTestTag() == 0){
				map.put(entry.getKey(), 0);
			} else if(pr.getTag() == TypeCT.WAIT.val()){
				map.put(entry.getKey(), 5);
			} else {
				map.put(entry.getKey(), pr.getGas().getTestTag());
			}
		}
		//正在检测的仓房
		for(String key : GasUtil.checking){
			map.put(key, 4);
		}
		return JsonUtil.toJson(map);
		
		
		
//		Map<String, Object> reqs = new LinkedHashMap<String, Object>();
//		reqs.put("checking", checking);
//		GasResults r;
//		Map<String, Integer> tags = new LinkedHashMap<String, Integer>();
//		for(String houseNo : HouseUtil.houseMap.keySet()){
//			r = lastChecks.get(houseNo);
//			if(r == null || r.getGas() == null || r.getGas().getTestTag() == TypeTestTag.NORMAL.val()) {
//				tags.put(houseNo, TypeTestTag.NORMAL.val());
//			} else {
//				tags.put(houseNo, TypeTestTag.ABNORMAL.val());
//			}
//		}
//		reqs.put("tags", tags);
//		return JsonUtil.toJson(reqs);
	}
	
	public static final void clearGasForConfs(){
		for(Map.Entry<String, Map<String, Object>> param : HouseUtil.houseConfs.entrySet()){
			if(param.getValue() != null){
				if(param.getValue().containsKey(TypeHouseConf.GAS.code())){
					param.getValue().remove(TypeHouseConf.GAS.code());
				}
			}
		}
	}
	
	/**
	 * 启动时更新测气配置信息
	 * @author	wc
	 * @date	2017年9月11日
	 * @return	void
	 */
	public static final void initGasConfs(final Dao d) {
		dao = d;
		List<GasInfo> gis = dao.query(GasInfo.class, Cnd.cri().asc("houseNo"));
		Map<String, Object> param;
		if (!Lang.isEmpty(gis)) {
			for(GasInfo g : gis){
				if(HouseUtil.houseConfs.containsKey(g.getHouseNo())){
					param = HouseUtil.houseConfs.get(g.getHouseNo());
					param.put(TypeHouseConf.GAS.code(), g);
				}
			}
		}
	}
	
	/**
	 * 重置测虫配置信息
	 * @author	wc
	 * @date	2017年7月7日
	 */
	public static final void refresh(String houseNo, GasInfo g) {
		if(HouseUtil.houseConfs.containsKey(houseNo)){
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if(param != null){
				param.put(TypeHouseConf.GAS.code(), g);
			}
		}
	}
	
}
