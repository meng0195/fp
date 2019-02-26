package cn.com.bjggs.report.util;

import java.util.LinkedList;
import java.util.List;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.temp.domain.TestData;

public class ReportUtil {

	//上报报表数据
	public static List<TestData> reportDataList = new LinkedList<TestData>();
	
	/**
	 * 获取仓房检测列表
	 * @author	wc
	 * @date	2017年7月18日
	 * @return	void
	 */
	public static <T> String getChecksHtml(List<StoreHouse> shs){
		int cols = 10;
		StringBuffer sb = new StringBuffer();
		StoreHouse s;
		for(int i = 0; i < shs.size(); i++){
			s = shs.get(i);
			if(i % cols == 0){
				sb.append("<tr>");
			}
			sb.append("<td width=\"100px\"><label><input type=\"checkbox\" group=\"houseNos\" name=\"houseNo\" ");
			sb.append(" value=\"")
				.append(s.getHouseNo())
				.append("\" />")
				.append(s.getHouseName())
				.append("</label></td>");
			if(i % cols != (cols - 1) && (i ==  (shs.size() - 1))){
				for(int j = i % cols; j < (cols - 1); j++){
					sb.append("<td width=\"100px\"></td>");
				}
				sb.append("</tr>");
			}
			if(i % cols == (cols - 1)){
				sb.append("</tr>");
			}
		}
		return sb.toString();
	}
}
