package cn.com.bjggs.squery.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Dao;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.squery.domain.QueryMain;

public class QueryUtil {
	
	public static final Map<String, QueryMain> maps = new LinkedHashMap<String, QueryMain>();
	
	public static final List<QueryMain> list = new LinkedList<QueryMain>();
	
	public static Dao dao;
	
	public static final void initQueryMain(final Dao d) {
		dao = d;
		QueryMain qm;
		for(String houseNo : HouseUtil.houseMap.keySet()){
			qm = new QueryMain(houseNo);
			maps.put(houseNo, qm);
			list.add(qm);
		}
	}
	
	public static final void reload(){
		QueryMain qm;
		for(String houseNo : HouseUtil.houseMap.keySet()){
			qm = new QueryMain(houseNo);
			maps.put(houseNo, qm);
			list.add(qm);
		}
	}
	
	public static final void refreshPest(String houseNo){
		if(maps.containsKey(houseNo)){
			maps.get(houseNo).refreshPest();
		}
	}
	
	public static final void refreshGas(String houseNo){
		if(maps.containsKey(houseNo)){
			maps.get(houseNo).refreshGas();
		}
	}
	
	public static final void refreshTemp(String houseNo){
		if(maps.containsKey(houseNo)){
			maps.get(houseNo).refreshTemp();
		}
	}
	
	public static final void refreshEquip(String houseNo){
		if(maps.containsKey(houseNo)){
			maps.get(houseNo).refreshEquip();
		}
	}
	
	public static final void refreshHouse(String houseNo){
		QueryMain qm = maps.get(houseNo);
		if (qm == null) qm = new QueryMain(houseNo);
		qm.setHouseNo(houseNo);
		//同步仓房信息
		StoreHouse s = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		qm.setHouseName(s.getHouseName());
		qm.setTypeName(Enums.get("TYPE_HOUSE","" + s.getHouseType())); 
		qm.setStoreCount(s.getStoreCount());
		qm.setBuiltYear(s.getBuiltYear());
		//同步储粮信息
		GrainInfo g = HouseUtil.get(houseNo, TypeHouseConf.GRAIN.code(), GrainInfo.class);
		qm.setGrainName(Codes.get("TYPE_GRAIN", "" + g.getGrainCode()));
		qm.setNatureName(Codes.get("TYPE_NATURE", "" + g.getNature()));
		qm.setGrainCount(g.getGrainCount());
		qm.setGradeName(Codes.get("TYPE_GRADE", "" + g.getGrade()));
		qm.setGainYear(g.getGainYear());
		qm.setOrigin(g.getOrigin());
		qm.setDateOfIn(g.getDateOfIn());
		qm.setProof3(g.getProof3());
		qm.setKeeperName(g.getKeeperName());
	}
}
