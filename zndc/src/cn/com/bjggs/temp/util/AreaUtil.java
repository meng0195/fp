package cn.com.bjggs.temp.util;

import java.util.List;

import cn.com.bjggs.basis.enums.TypeHouse;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.enums.TypePointArea;

public class AreaUtil {
	
	/**
	 * 根据线缆排布信息和线缆list集合生成页面
	 * @param	maxZ : 最大层
	 * @author	wc
	 * @date	2017年7月11日
	 * @return	String
	 */
	public static final String getSortMap(List<List<List<PointInfo>>> list, TempInfo e, int type){
		StringBuffer sb = new StringBuffer("<table id=\"points-area\" class=\"points\" style=\"width:");
		//初始化table
		sb.append(e.getMaxHNum() * 40 + 52);
		sb.append("px\">");
		
		List<PointInfo> ps;
		PointInfo p;
		int len = 0;
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
						sb.append("<li class=\"point-title\">");
						sb.append("<span class=\"cable\"></span>");
						sb.append("<span class=\"sensor\"></span>");
						sb.append("</li>");
						sb.append("</ul></td>");
						xTag += 1;
					}
				}
				//没有空列的
				//处理title
				if(p.getArea() == TypePointArea.A.val()){
					sb.append("<td><ul class=\"point-col bred\">");
				} else if(p.getArea() == TypePointArea.B.val()){
					sb.append("<td><ul class=\"point-col bgray\">");
				} else if(p.getArea() == TypePointArea.C.val()){
					sb.append("<td><ul class=\"point-col bblue\">");
				} else if(p.getArea() == TypePointArea.D.val()){
					sb.append("<td><ul class=\"point-col byellow\">");
				} else if(p.getArea() == TypePointArea.E.val()){
					sb.append("<td><ul class=\"point-col bgreen\">");
				} else {
					sb.append("<td><ul class=\"point-col\">");
				}
				//处理其他
				//z轴方向上的标记
				for(int j = 0; j < p.getZaxis(); j++){
					sb.append("<li>");
					sb.append("<span class=\"cable\"></span><span class=\"sensor\"></span></li>");
				}
				
				p = ps.get(1);
				sb.append("<li><div id=\"pointsDiv");
				sb.append(p.getXaxis()).append(p.getYaxis()).append(p.getZaxis());
				sb.append("\" pointId=\"");
				sb.append(p.getId());
				sb.append("\" class=\"points-div\" stag=\"");
				sb.append(xTag == 0);
				sb.append("\">");

				sb.append("<span class=\"cable\">");
				sb.append("<input type=\"hidden\" name=\"cable\" value=\"");
				sb.append(p.getCableNo1());
				sb.append("\"/>");
				sb.append("<input type=\"hidden\" name=\"area\" value=\"");
				sb.append(p.getArea());
				sb.append("\">");
				sb.append(p.getCableNo1());
				sb.append("</span>");
				sb.append("<span class=\"area\">");
				if(p.getArea() != 0){
					if(type == TypeHouse.WARE.val()){
						sb.append(Enums.get("TYPE_POINT_AREA", "" + p.getArea()));
					} else {
						sb.append(Enums.get("TYPE_POINT_SQUAT", "" + p.getArea()));
					}
				}
				sb.append("</span></div></li>");
				sb.append("</ul></td>");
				xTag += 1;
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
}
