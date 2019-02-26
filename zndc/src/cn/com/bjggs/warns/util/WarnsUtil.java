package cn.com.bjggs.warns.util;

import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.warns.domain.Alarm;
import cn.com.bjggs.warns.domain.AlarmTT;

public class WarnsUtil {
	
	private static Dao dao;
	
	public static final void clearWarnsForConfs(){
		for(Map.Entry<String, Map<String, Object>> param : HouseUtil.houseConfs.entrySet()){
			if(param.getValue() != null){
				if(param.getValue().containsKey(TypeHouseConf.WARNS.code())){
					param.getValue().remove(TypeHouseConf.WARNS.code());
				}
			}
		}
	}
	
	/**
	 * 启动时更新报警配置信息
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	void
	 */
	public static final void initWarnsConfs(final Dao d) {
		dao = d;
		List<Alarm> ws = dao.query(Alarm.class, Cnd.cri().asc("houseNo").asc("type").asc("type1"));
		AlarmTT att;
		if(!Lang.isEmpty(ws)){
			for(Alarm a : ws){
				att = null;
				if(HouseUtil.houseConfs.containsKey(a.getHouseNo())){
					Map<String, Object> param = HouseUtil.houseConfs.get(a.getHouseNo());
					if(param.containsKey(TypeHouseConf.WARNS.code())){
						att = (AlarmTT)param.get(TypeHouseConf.WARNS.code());
					}
					if(null == att) {
						att = new AlarmTT();
						param.put(TypeHouseConf.WARNS.code(), att);
					}
					att.setArrs(a);
				}			
			}
		}
	}
	
	public static final void refresh(String houseNo, List<Alarm> as){
		if(HouseUtil.houseConfs.containsKey(houseNo)){
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if(param != null){
				AlarmTT att = (AlarmTT)param.get(TypeHouseConf.WARNS.code());
				if(att == null) {
					att = new AlarmTT();
					param.put(TypeHouseConf.WARNS.code(), att);
				}
				for(Alarm a : as){
					att.setArrs(a);
				}
			}
		}
	}
	
}
