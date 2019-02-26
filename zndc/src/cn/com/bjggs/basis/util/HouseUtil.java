package cn.com.bjggs.basis.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.view.ViewHouseInfo;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.core.util.InvokeUtil;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.temp.domain.ChecksOne;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.warns.domain.AlarmTT;

@SuppressWarnings({"serial", "unchecked"})
public class HouseUtil {
	
	private static Dao dao;
	
	public static final Map<String, String> houseMap = new LinkedHashMap<String, String>();
	
	public static final Map<String, String> houseKeppers = new LinkedHashMap<String, String>();
	
	public static final Map<String, Map<String, Object>> houseConfs = new LinkedHashMap<String, Map<String, Object>>();
	
	public enum TypeHouseConf {
		HOUSE("house", StoreHouse.class),
		GRAIN("grain", GrainInfo.class),
		WARNS("warn", AlarmTT.class),
		TEMP("temp", TempInfo.class),//测温信息,行列层等
		BOARD("board", List.class),//测温板信息,ip等
		TPS("tps", Map.class),//点排布信息.C3S2(组合后)-点
		TPS1("tps1", Map.class),//点排布信息.电缆编号-点
		PEST("pest", PestInfo.class),
		GATE("gate", List.class),
		PPS("pps", List.class),
		EIPS("eips", EquipIps.class),
		GAS("gas", Object.class);

		private String code;
		
		private Class<?> clazz;

		private TypeHouseConf(String code, Class<?> clazz) {
			this.code = code;
			this.clazz = clazz;
		}

		public String code() {
			return this.code;
		}
		
		public Class<?> clazz(){
			return this.clazz;
		}
	}
	
	/**
	 * 启动时初始化仓房编号字典
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	void
	 */
	public static final void initHouseNo(final ServletContext sc, final Dao d) {
		dao = d;
		List<ViewHouseInfo> houses = dao.query(ViewHouseInfo.class, Cnd.cri().asc("houseNo"));
		if (!Lang.isEmpty(houses)) {
			for(ViewHouseInfo h : houses){
				houseMap.put(h.getHouseNo(), h.getHouseName());
				houseKeppers.put(h.getHouseNo(), h.getKeeperName());
			}
		}
		Codes.update("houses", houseMap);
		Codes.update("keppers", houseKeppers);
	}
	
	/**
	 * 初始化仓房基本信息和储粮信息
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	void
	 */
	public static final void initHouseConfs(final Dao d) {
		dao = d;
		List<StoreHouse> houses = dao.query(StoreHouse.class, Cnd.cri().asc("houseNo"));
		if(!Lang.isEmpty(houses)){
			for(final StoreHouse h : houses){
				houseConfs.put(h.getHouseNo(), new LinkedHashMap<String, Object>(){
					{put("house", h);}
				});
			}
		}
		List<GrainInfo> grains = dao.query(GrainInfo.class, Cnd.cri().asc("houseNo"));
		if(!Lang.isEmpty(grains)){
			Map<String, Object> param;
			for(GrainInfo g : grains){
				if(houseConfs.containsKey(g.getHouseNo())){
					param = houseConfs.get(g.getHouseNo());
					param.put("grain", g);
				}
			}
		}
	}
	
	/**
	 * 重置仓房编号字典和仓房字典
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	void
	 */
	public static final void reload(){
		List<StoreHouse> houses = dao.query(StoreHouse.class, Cnd.cri().asc("houseNo"));
		houseMap.clear();
		if (!Lang.isEmpty(houses)) {
			for(StoreHouse h : houses){
				houseMap.put(h.getHouseNo(), h.getHouseName());
			}
		}
	}
	
	public static final void reloadHouseConfs(){
		List<StoreHouse> houses = dao.query(StoreHouse.class, Cnd.cri().asc("houseNo"));
		houseConfs.clear();
		final String key = TypeHouseConf.HOUSE.code();
		if(!Lang.isEmpty(houses)){
			for(final StoreHouse h : houses){
				houseConfs.put(h.getHouseNo(), new LinkedHashMap<String, Object>(){
					{put(key, h);}
				});
			}
		}
		List<GrainInfo> grains = dao.query(GrainInfo.class, Cnd.cri().asc("houseNo"));
		final String key1 = TypeHouseConf.GRAIN.code();
		if(!Lang.isEmpty(grains)){
			Map<String, Object> param;
			for(GrainInfo g : grains){
				if(houseConfs.containsKey(g.getHouseNo())){
					param = houseConfs.get(g.getHouseNo());
					param.put(key1, g);
				}
			}
		}
	}
	
	/**
	 * 根据仓房对象刷新仓房编号字典
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	String
	 */
	public static final void refresh(StoreHouse h) {
		if(Strings.isBlank(h.getHouseNo())){
			return;
		}
		houseMap.put(h.getHouseNo(), h.getHouseName());
	}
	
	/**
	 * 根据仓房编号，参数类型，参数修改仓房配置信息
	 * @author	wc
	 * @date	2017年7月7日
	 */
	public static final void refresh(String key, String type, Object obj) {
		if(houseConfs.containsKey(key)){//如果存在取出
			Map<String, Object> param = houseConfs.get(key);
			if(param != null && Strings.isNotBlank(type)){
				param.put(type, obj);
			}
		} else {//如果不存在新增
			Map<String, Object> param = new LinkedHashMap<String, Object>();
			param.put(type, obj);
			houseConfs.put(key, param);
		}
	}
	
	/**
	 * 获取一些配置类信息并根据传入的clazz转换类型
	 * @author	wc
	 * @date	2017年8月29日
	 * @return	T
	 */
	public static <T> T get(String key, String type, Class<T> clazz) {
		if(houseConfs.containsKey(key)){
			Map<String, Object> param = houseConfs.get(key);
			if(param != null && param.containsKey(type)){
				return (T)param.get(type);
			}
		}
		return null;
	}
	
	/**
	 * 获取一些配置信息 返回对象需要转型
	 * @author	wc
	 * @date	2017年8月29日
	 * @return	Object
	 */
	public static Object get(String key, String type){
		if(houseConfs.containsKey(key)){
			Map<String, Object> param = houseConfs.get(key);
			if(param != null && param.containsKey(type)){
				return param.get(type);
			}
		}
		return null;
	}

	/**
	 * 根据key和value 更新仓房编号字典
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	String
	 */
	public static final void refresh(String key, String val) {
		if(Strings.isBlank(key) || Strings.isBlank(val)){
			return;
		}
		houseMap.put(key, val);
	}
	
	/**
	 * 移除某个字典项
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	String
	 */
	public static final void remove(String key) {
		if(houseMap.containsKey(key)) houseMap.remove(key);
	}
	
	/**
	 * 获取仓房检测列表
	 * @author	wc
	 * @date	2017年7月18日
	 * @return	void
	 */
	public static <T> String getChecksHtml(String[] hs, List<T> cos){
		int cols = 10;
		StringBuffer sb = new StringBuffer();
		String houseNo;
		String s;
		boolean tag = false;
		if(hs != null){
			for(int i = 0; i < hs.length; i++){
				s = hs[i];
				if(i % cols == 0){
					sb.append("<tr>");
				}
				tag = false;
				for(Object obj : cos){
					houseNo = (String)InvokeUtil.getValue(obj, "houseNo");
					if (Strings.equals(s, houseNo)) {
						tag = true;
						cos.remove(obj);
						break;
					}
				}
				sb.append("<td width=\"100px\"><label><input type=\"checkbox\" group=\"houseNos\" name=\"houseNo\" ");
				if(tag) sb.append(" checked=\"true\" ");
				sb.append(" value=\"")
					.append(s)
					.append("\" />")
					.append(houseMap.get(s))
					.append("</label></td>");
				if(i % cols != (cols - 1) && (i ==  (hs.length - 1))){
					for(int j = i % cols; j < (cols - 1); j++){
						sb.append("<td width=\"100px\"></td>");
					}
					sb.append("</tr>");
				}
				if(i % cols == (cols - 1)){
					sb.append("</tr>");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取仓房检测列表
	 * @author	wc
	 * @date	2017年7月18日
	 * @return	void
	 */
	public static <T> String getChecksHtml(String[] hs, String[] houses){
		List<ChecksOne> list = new LinkedList<ChecksOne>();
		if(houses != null){
			for(String s : houses){
				list.add(new ChecksOne(s));
			}
		}
		return getChecksHtml(hs, list);
	}
}
