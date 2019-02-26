package cn.com.bjggs.temp.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypeHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TestData;

@SuppressWarnings("unchecked")
public class PointUtil {
	
	private static Dao dao;
	
	private static final Map<String, Map<String, PointInfo>> points = new LinkedHashMap<String, Map<String, PointInfo>>();

	private static final Map<String, Map<String, PointInfo>> points1 = new LinkedHashMap<String, Map<String, PointInfo>>();
	
	/**
	 * 启动时初始化仓房点排布
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	void
	 */
	public static final void initPoints(final Dao d) {
		dao = d;
		List<PointInfo> ps = dao.query(PointInfo.class, Cnd.cri().asc("houseNo"));
		if (!Lang.isEmpty(ps)) {
			Map<String, PointInfo> map;
			Map<String, PointInfo> map1;
			for(PointInfo p : ps){
				if (points.containsKey(p.getHouseNo())) {
					map = points.get(p.getHouseNo());
					map.put("c" + p.getCableNo1() + "s" + p.getSensorNo1() , p);
				} else {
					map = new LinkedHashMap<String, PointInfo>();
					map.put("c" + p.getCableNo1() + "s" + p.getSensorNo1() , p);
					points.put(p.getHouseNo(), map);
				}
				if (points1.containsKey(p.getHouseNo())) {
					map1 = points1.get(p.getHouseNo());
					map1.put(String.valueOf(p.getPoinNo1()) , p);
				} else {
					map1 = new LinkedHashMap<String, PointInfo>();
					map1.put(String.valueOf(p.getPoinNo1()) , p);
					points1.put(p.getHouseNo(), map1);
				}
			}
		}
	}
	
	//刷新单个point点
	public static final void refreshSinglePoint(PointInfo p) {
		Map<String, PointInfo> map = new LinkedHashMap<String, PointInfo>();
		Map<String, PointInfo> map1 = new LinkedHashMap<String, PointInfo>();
		if (points.containsKey(p.getHouseNo())) {
			map = points.get(p.getHouseNo());
			map1 = points1.get(p.getHouseNo());
			map.put("c" + p.getCableNo1() + "s" + p.getSensorNo1() , p);
			map1.put(String.valueOf(p.getPoinNo1()), p);
		}
	}
	

	/**
	 * 重置点排布
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	void
	 */
	public static final void reload(){
		List<PointInfo> ps = dao.query(PointInfo.class, Cnd.cri().asc("houseNo"));
		points.clear();
		if (!Lang.isEmpty(ps)) {
			Map<String, PointInfo> map;
			Map<String, PointInfo> map1;
			for(PointInfo p : ps){
				if (points.containsKey(p.getHouseNo())) {
					map = points.get(p.getHouseNo());
					map.put("c" + p.getCableNo1() + "s" + p.getSensorNo1() , p);
				} else {
					map = new LinkedHashMap<String, PointInfo>();
					map.put("c" + p.getCableNo1() + "s" + p.getSensorNo1() , p);
					points.put(p.getHouseNo(), map);
				}
				if (points1.containsKey(p.getHouseNo())) {
					map1 = points1.get(p.getHouseNo());
					map1.put(String.valueOf(p.getPoinNo1()) , p);
				} else {
					map1 = new LinkedHashMap<String, PointInfo>();
					map1.put(String.valueOf(p.getPoinNo1()) , p);
					points1.put(p.getHouseNo(), map1);
				}
			}
		}
	}
	
	/**
	 * 根据仓房编号刷新点排布
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	String
	 */
	public static final void refresh(String houseNo) {
		List<PointInfo> ps = dao.query(PointInfo.class, Cnd.where("houseNo", "=", houseNo));
		if (!Lang.isEmpty(ps)) {
			Map<String, PointInfo> map = new LinkedHashMap<String, PointInfo>();
			Map<String, PointInfo> map1 = new LinkedHashMap<String, PointInfo>();
			points.put(houseNo, map);
			points1.put(houseNo, map1);
			for(PointInfo p : ps){
				map.put("c" + p.getCableNo1() + "s" + p.getSensorNo1() , p);
				map1.put(String.valueOf(p.getPoinNo1()), p);
			}
		}
	}
	/**
	 * 根据仓房编号获取点集
	 */
	public static final Map<String, PointInfo> getPoints(String houseNo){
		return points.containsKey(houseNo) ? points.get(houseNo) : null;
	}
	
	/**
	 * 根据仓房编号获取点集
	 */
	public static final Map<String, PointInfo> getPoints1(String houseNo){
		return points1.containsKey(houseNo) ? points1.get(houseNo) : null;
	}
	
	/**
	 * 生成点回显页面
	 * @author	wc
	 * @date	2017年7月9日
	 * @return	String
	 */
	public static final String getHtmls(PointInfo p, boolean tag){
		StringBuffer sb = new StringBuffer();
		String bg = p.getValid() == 1 ? "bred" : "";
		/*
		if(tag){
			sb.append("<span class=\"seq " + bg + "\">S").append(p.getZaxis() + 1).append("</span>");
		}
		*/
		sb.append("<span class=\"cable " + bg + "\">").append(p.getCableNo1()).append("</span>");
		sb.append("<span class=\"sensor " + bg + "\">").append(p.getSensorNo1()).append("</span>");
		return sb.toString();
	}
	
	/**
	 * 根据仓房信息和 仓房设备信息 获取仓房所有点
	 * @author	wc
	 * @date	2017年7月11日
	 * @return	List<PointInfo>
	 */
	public static final List<PointInfo> getPoints(StoreHouse s, TempInfo t){
		//平方仓
		if(TypeHouse.WARE.val() == s.getHouseType()){
			WareUtil wu = new WareUtil(s, t);
			t.setPointNum(wu.getPointNum());
			return wu.getPoints();
		} else {//原型仓
			SiloUtil su = new SiloUtil(s, t);
			t.setPointNum(su.getPointNum());
			return su.getPoints();
		}
	}
	
	/**
	 * 根据线缆排布信息和线缆list集合生成页面
	 * @param	maxZ : 最大层
	 * @author	wc
	 * @date	2017年7月11日
	 * @return	String
	 */
	public static final String getSortMap(List<List<List<PointInfo>>> list, TempInfo e){
		StringBuffer sb = new StringBuffer("<table id=\"points-rank\" class=\"points\" style=\"width:");
		//初始化table
		sb.append(e.getVnum() * 85);
		sb.append("px\">");
		
		List<PointInfo> ps;
		PointInfo p;
		int len = 0;
		int maxZ = e.getMaxLNum();
		//生成每行
		for(List<List<PointInfo>> pss : list){
			sb.append("<tr>");
			//全局变量 x 轴方向标记
			int xTag = 0;
			for(int i = 0; i < pss.size(); i++){
				ps = pss.get(i);
				p = ps.get(0);
				//当每个点的x轴标记大于 X轴标记时 表示此行方向上有空列 添加空列
				if(p.getXaxis() > xTag){
					len = p.getXaxis() - xTag;
					for(int j = 0; j < len; j++){
						//添加站位空格
						sb.append("<td><ul class=\"point-col\">");
						for(int k = 0; k <= maxZ; k++){
							sb.append("<li class=\"point-title\" style=\"width:");
							sb.append(80).append("px;\">");
							/*
							sb.append((xTag > 0 ? 2 : 3) * 51).append("px;\">");
							if(xTag == 0){
								sb.append("<span class=\"seq\">");
								sb.append(k == 0 ? "序号" : "S" + k);
								sb.append("</span>");
							}*/
							sb.append("<span class=\"cable\">");
							sb.append(k == 0 ? "缆号" : "");
							sb.append("</span>");
							sb.append("<span class=\"sensor\">");
							sb.append(k == 0 ? "点号" : "");
							sb.append("</span>");
							sb.append("</li>");
						}
						sb.append("</ul></td>");
						xTag += 1;
					}
				}
				//没有空列的
				//处理title
				sb.append("<td><ul class=\"point-col\"><li class=\"point-title\" style=\"width:");
				sb.append(80).append("px;\">");
				/*
				if (xTag > 0) {
					sb.append(2 * 51).append("px;\">");
				} else {
					sb.append(3 * 51).append("px;\">");
					sb.append("<span class=\"seq\">序号</span>");
				}*/
				sb.append("<span class=\"cable\">缆号</span>");
				sb.append("<span class=\"sensor\">点号</span></li>");
				//处理其他
				//z轴方向上的标记
				int maxTag = 0;
				for(int j = 0; j < p.getZaxis(); j++){
					maxTag += 1;
					sb.append("<li>");
					/*
					if (xTag == 0) {
						sb.append("<span class=\"seq\">S");
						sb.append(j + 1);
						sb.append("</span>");
					}*/
					sb.append("<span class=\"cable\"></span><span class=\"sensor\"></span></li>");
				}
				for(int j = 0; j < ps.size(); j++){
					maxTag += 1;
					p = ps.get(j);
					sb.append("<li>");
					sb.append("<div id=\"pointsDiv");
					sb.append(p.getXaxis()).append(p.getYaxis()).append(p.getZaxis());
					sb.append("\" pointId=\"");
					sb.append(p.getId());
					sb.append("\" class=\"points-div\" stag=\"");
					sb.append(xTag == 0);
					sb.append("\" style=\"height:23px\">");
					/*
					if (xTag == 0) {
						sb.append("<span class=\"seq ");
						if (p.getValid() == 1) {
							sb.append("bred");
						}
						sb.append("\">S");
						sb.append(p.getZaxis()+1);
						sb.append("</span>");
					}*/
					sb.append("<span class=\"cable ");
					if (p.getValid() == 1) {
						sb.append("bred");
					}
					sb.append("\">");
					sb.append(p.getCableNo1());
					sb.append("</span>");
					sb.append("<span class=\"sensor ");
					if (p.getValid() == 1) {
						sb.append("bred");
					}
					sb.append("\">");
					sb.append(p.getSensorNo1());
					sb.append("</span></div></li>");
				}
				for(int j = maxTag; j < maxZ; j++){
					sb.append("<li>");
					/*
					if (xTag == 0) {
						sb.append("<span class=\"seq\">S");
						sb.append(j + 1);
						sb.append("</span>");
					}*/
					sb.append("<span class=\"cable\"></span><span class=\"sensor\"></span></li>");
				}
				sb.append("</ul></td>");
				xTag += 1;
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	/**
	 * 生成温度表格
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	String
	 */
	public static final String getSheet(List<List<List<PointInfo>>> list, TempInfo e, TestData t){
		byte[] temps = t.getTset();
		byte[] warns = t.getWarns();
		double minT = t.getMinT();
		double maxT = t.getMaxT();
		StringBuffer sb = new StringBuffer("<table id=\"temp-sheet\" class=\"sheetCheckDetail ");
		if (e.getMaxHNum() == 7 && e.getMaxLNum() == 4) {
			sb.append("row7layer4\" >");
		} else if (e.getMaxHNum() == 5 && e.getMaxLNum() == 4) {
			sb.append("row5layer4\" >");
		}else {
			sb.append("detail\" >");
		}
		sb.append("<th></th>");
		for (int i = 1; i <= e.getVnum(); i++) {
			sb.append("<th>"+i+"列</th>");
		}
		
		List<PointInfo> ps;
		PointInfo p;
		int len = 0;
		int maxZ = 0;
		for(String s : e.getLnums()){
			maxZ = Math.max(ParseUtil.toInt(s, 0), maxZ);
		}
		Map<String, StringBuffer> trs = new LinkedHashMap<String, StringBuffer>();
		StringBuffer sbi;
		List<List<PointInfo>> pss;
		double temp;
		//生成每行
		for(int x = 0; x < list.size(); x++){
			//全局变量 x 轴方向标记
			int xTag = 0;
			pss = list.get(x);
			for(int z = 0; z <= maxZ; z++){
				trs.put("x" + x + "z" + z, new StringBuffer());
			}
			for(int i = 0; i < pss.size(); i++){
				ps = pss.get(i);
				p = ps.get(0);
				//当每个点的x轴标记大于 X轴标记时 表示此行方向上有空列 添加空列
				if(p.getXaxis() > xTag){
					len = p.getXaxis() - xTag;
					for(int j = 0; j < len; j++){
						for(int k = 0; k <= maxZ; k++){
							sbi = trs.get("x" + x + "z" + k);
							if(xTag == 0){
								if(k == 0){
									sbi.append("<th width=\"50\"></th>");
								} else {
									sbi.append("<th>S")
										.append(k)
										.append("</th>");
								}
							}
							if(k == 0){
								sbi.append("<td></td>");
							} else {
								sbi.append("<td></td>");
							}
						}
						xTag += 1;
					}
				}
				//没有空列的
				//处理title
				sbi = trs.get("x" + x + "z" + 0);
				if (xTag == 0) {
					sbi.append("<th width=\"50\">" + (x + 1) + "行</th>");
				}
				sbi.append("<th>C").append(p.getCableNo1()).append("</th>");
				//处理其他
				//z轴方向上的标记
				int maxTag = 0;
				for(int j = 0; j < p.getZaxis(); j++){
					maxTag += 1;
					sbi = trs.get("x" + x + "z" + maxTag);
					if (xTag == 0) {
						sbi.append("<th>S");
						sbi.append(j + 1);
						sbi.append("</th>");
					}
					sbi.append("<td></td>");
				}
				for(int j = 0; j < ps.size(); j++){
					maxTag += 1;
					p = ps.get(j);
					sbi = trs.get("x" + x + "z" + maxTag);
					if (xTag == 0) {
						sbi.append("<th>S").append(p.getZaxis()+1).append("</th>");
					}
					sbi.append("<td style=\"position:relative;\">");
					if(warns != null && warns.length > p.getPoinNo1()-1){
						byte warn = warns[p.getPoinNo1()-1];
						if(WarnType.isWarn1(warn)){
							sbi.append("<div class=\"pointW0\" />");
						}
						if(WarnType.isWarn2(warn)){
							sbi.append("<div class=\"pointW1\" />");
						}
						if(WarnType.isWarn4(warn)){
							sbi.append("<div class=\"pointW2\" />");
						}
						if(WarnType.isWarn8(warn)){
							sbi.append("<div class=\"pointW3\" />");
						}
					}
					if(temps != null && temps.length > ((p.getPoinNo1()-1) * 2 + 1)){
						if(temps[(p.getPoinNo1()-1) * 2] == 0 && temps[(p.getPoinNo1()-1) * 2 + 1] == 0){
							sbi.append("-");
						} else {
							temp = MathUtil.byte2IntNb(temps[(p.getPoinNo1()-1) * 2], temps[(p.getPoinNo1()-1) * 2 + 1])/10.0D;
							sbi.append(temp);
							if(temp == minT){
								sbi.append("↓");
							}
							if(temp == maxT){
								sbi.append("↑");
							}
						}
					}
					sbi.append("</td>");
				}
				for(int j = maxTag; j < maxZ; j++){
					sbi = trs.get("x" + x + "z" + (j+1));
					if (xTag == 0) {
						sbi.append("<th>S").append(j + 1).append("</th>");
					}
					sbi.append("<td></td>");
				}
				xTag += 1;
			}
		}
		for(Map.Entry<String, StringBuffer> entry : trs.entrySet()){
			sb.append("<tr>").append(entry.getValue()).append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	/**
	 * 生成温度表格
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	String
	 */
	public static final String getSheetYuan(List<List<PointInfo>> list, TempInfo e, TestData t){
		int maxY = 33;
		byte[] temps = t.getTset();
		byte[] warns = t.getWarns();
		double minT = t.getMinT();
		double maxT = t.getMaxT();
		StringBuffer sb = new StringBuffer("<table id=\"temp-sheet\" class=\"sheetCheckDetail\" >");
		
		List<PointInfo> ps;
		PointInfo p;
		int maxZ = e.getMaxLNum();
		Map<String, StringBuffer> trs = new LinkedHashMap<String, StringBuffer>();
		StringBuffer sbi;
		
		double temp;
		int maxX = list.size() % maxY == 0 ? list.size()/maxY  : list.size()/maxY + 1;
		if(maxY > list.size()) maxY = list.size();
		int width = (1000-60)/maxY;
		for(int x = 0; x < maxX; x++){
			for(int z = 0; z <= maxZ; z++){
				trs.put("x" + x + "z" + z, new StringBuffer());
			}
			for(int i = x * maxY; i < Math.min((x + 1) * maxY, list.size()); i++){
				ps = list.get(i);
				p = ps.get(0);
				//添加空行
				for(int z = 0; z < p.getZaxis(); z++){
					//处理首列
					sbi = trs.get("x" + x + "z" + z);
					if(i % maxY == 0){
						if(z == 0){
							sbi.append("<th width=\"60\">层\\缆</th>");
						} else {
							sbi.append("<th>S")
								.append(z)
								.append("</th>");
						}
					}
					
					if(z == 0){
						sbi.append("<th width=\"" + width + "\">C" + p.getCableNo1() + "</th>");
					} else {
						sbi.append("<td></td>");
					}
				}
				for(int z = 0; z < ps.size(); z++){
					p = ps.get(z);
					sbi = trs.get("x" + x + "z" + (p.getZaxis() + 1));
					
					if(i % maxY == 0){
						if(p.getZaxis() == 0){
							sbi = trs.get("x" + x + "z0");
							sbi.append("<th width=\"60\">层\\缆</th>");
							sbi = trs.get("x" + x + "z1");
							sbi.append("<th>S1</th>");
						} else {
							sbi.append("<th>S")
								.append(p.getZaxis() + 1)
								.append("</th>");
						}
					}
						
					if(p.getZaxis() == 0){
						sbi = trs.get("x" + x + "z0");
						sbi.append("<th width=\"" + width + "\">C" + p.getCableNo1() + "</th>");
						sbi = trs.get("x" + x + "z1");
					}
					sbi.append("<td style=\"position:relative;\">");
					if(warns != null && warns.length > p.getPoinNo1()-1){
						byte warn = warns[p.getPoinNo1()-1];
						if(WarnType.isWarn1(warn)){
							sbi.append("<div class=\"pointW0\" />");
						}
						if(WarnType.isWarn2(warn)){
							sbi.append("<div class=\"pointW1\" />");
						}
						if(WarnType.isWarn4(warn)){
							sbi.append("<div class=\"pointW2\" />");
						}
						if(WarnType.isWarn8(warn)){
							sbi.append("<div class=\"pointW3\" />");
						}
					}
					if(temps != null && temps.length > ((p.getPoinNo1()-1) * 2 + 1)){
						if(temps[(p.getPoinNo1()-1) * 2] == 0 && temps[(p.getPoinNo1()-1) * 2 + 1] == 0){
							sbi.append("-");
						} else {
							temp = MathUtil.byte2IntNb(temps[(p.getPoinNo1()-1) * 2], temps[(p.getPoinNo1()-1) * 2 + 1])/10.0D;
							sbi.append(temp);
							if(temp == minT){
								sbi.append("↓");
							}
							if(temp == maxT){
								sbi.append("↑");
							}
						}
					}
					sbi.append("</td>");
				}
				for(int z = ps.get(ps.size() - 1).getZaxis() + 1; z < maxZ; z++){
					sbi = trs.get("x" + x + "z" + (z + 1));
					if(i % maxY == 0){
						sbi.append("<th>S")
							.append(z + 1)
							.append("</th>");
					} else {
						sbi.append("<th></th>");
					}
				}
				if(i == list.size()-1 && i%maxY != 0){
					for(int j = i%maxY; j < maxY-1; j++){
						for(int z = 0; z <= maxZ; z++){
							sbi = trs.get("x" + x + "z" + z);
							if(z==0){
								sbi.append("<th></th>");
							} else {
								sbi.append("<td></td>");
							}
						}
					}
				}
			}
		}
		for(Map.Entry<String, StringBuffer> entry : trs.entrySet()){
			sb.append("<tr>").append(entry.getValue()).append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	public static List<List<List<PointInfo>>> getPointLevel(String houseNo){
		//获取所有点
		Map<String, PointInfo> maps = (Map<String, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS.code());
		List<List<List<PointInfo>>> points = new LinkedList<List<List<PointInfo>>>();
		List<List<PointInfo>> xps;
		List<PointInfo> ps;
		int[] x = {-1, -1};
		int[] y = {-1, -1};
		PointInfo p;
		//拼装参数
		for(Map.Entry<String, PointInfo> entry : maps.entrySet()){
			p = entry.getValue();
			if(y[1] == p.getYaxis()){
				xps = points.get(y[0]);
			} else {
				//新建子集
				xps = new LinkedList<List<PointInfo>>();
				points.add(xps);
				//重置子集参数
				x[0] = -1;
				x[1] = -1;
				y[0] = (y[0] == -1) ? 0 : (y[0] + 1);
				y[1] = p.getYaxis();
 			}
			if(x[1] == p.getXaxis()){
				ps = xps.get(x[0]);
			} else {
				ps = new LinkedList<PointInfo>();
				xps.add(ps);
				x[0] = (x[0] == -1) ? 0 : (x[0] + 1);
				x[1] = p.getXaxis();
			}
			ps.add(p);
		}
		return points;
	}
}
