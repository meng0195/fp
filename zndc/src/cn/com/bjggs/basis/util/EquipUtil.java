package cn.com.bjggs.basis.util;

import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;

public class EquipUtil {
	
	private static Dao dao;
	
	public static final void clearEipsForConfs(){
		for(Map.Entry<String, Map<String, Object>> param : HouseUtil.houseConfs.entrySet()){
			if(param.getValue() != null){
				if(param.getValue().containsKey(TypeHouseConf.EIPS.code())){
					param.getValue().remove(TypeHouseConf.EIPS.code());
				}
			}
		}
	}
	
	/**
	 * @author	wc
	 * @date	2017年10月1日
	 * @return	void
	 */
	public static final void initEips(final Dao d) {
		dao = d;
		List<EquipIps> eips = dao.query(EquipIps.class, Cnd.cri().asc("houseNo"));
		Map<String, Object> param;
		if (!Lang.isEmpty(eips)) {
			for(EquipIps ip : eips){
				if(HouseUtil.houseConfs.containsKey(ip.getHouseNo())){
					param = HouseUtil.houseConfs.get(ip.getHouseNo());
					param.put(TypeHouseConf.EIPS.code(), ip);
				}			
			}
		}
	}
	
	/**
	 * @author	wc
	 * @date	2017年10月1日
	 */
	public static final void refresh(String houseNo, EquipIps ip) {
		if(HouseUtil.houseConfs.containsKey(houseNo)){
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if(param != null){
				param.put(TypeHouseConf.EIPS.code(), ip);
			}
		}
	}
	
	public static final void refresh(EquipIps ip) {
		if(ip != null){
			refresh(ip.getHouseNo(), ip);
		}
	}
}
