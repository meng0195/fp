package cn.com.bjggs.temp.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempInfo;

public class TempRealUtil {
	/**
	 * 所有检测温度集合
	 */
	private static byte[][] allTemps;
	private static TempInfo ti;
	private static List<TempBoaInfo> tbis;
	private static Map<String, PointInfo> points;
	private static TempBoaInfo tb;
	private static String[][] cableRanges = new String[4][];
	
	@SuppressWarnings("unchecked")
	public static final byte[] getRealTemp(String houseNo){
		ti = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		tbis = (List<TempBoaInfo>) HouseUtil.get(houseNo, TypeHouseConf.BOARD.code(), TempBoaInfo.class);
		points = (Map<String, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS.code());
		if(ti == null && tbis.size() == 0 && points == null) throw new RuntimeException("无仓房线缆配置信息!");
		
		for (int i = 0; i < 4; i++) {
			tb = tbis.get(i);
			if (Strings.isNotBlank(tb.getCableNo())) {
				cableRanges[i] = tb.getCableNo().split(",");
			}
		}
		
		allTemps = new byte[4][];
		for (int i = 0; i < 4; i++) {
			tb = tbis.get(i);
			if (Strings.isNotBlank(tb.getIp())) {
				getTemp(allTemps, 0, tb.getBoardType(), tb.getIp(), tb.getPort());
			}
		}
		
		StringBuffer key = new StringBuffer("C");
		PointInfo p;
		int temp;
		byte[] ds = new byte[ti.getPointNum() * 2];
		byte[] bs;
		Temps tUtil;
		int i = 0;
		int len = 0;
		TempBoaInfo tb;
		for(int j = 0; j < 4; j++){
			bs = allTemps[j];
			tb = tbis.get(j);
			tUtil = TempFactory.createCheck(tb.getTempType());
			i = 0;
			if(bs != null){
				len = bs.length - 3;
				while(len > i){
					key = new StringBuffer("C");
					key.append(MathUtil.byte2int(bs[i])).append("S").append(MathUtil.byte2int(bs[i+1]));
					if(points.containsKey(key.toString()) && (isComplie(bs[i], j))){
						p = points.get(key.toString());
						//获取10倍温度
						temp = tUtil.getT10(bs, i+2, i+3);
						if(temp == 0) temp = 1;
						//设置对应点温度
						if((p.getPoinNo1() - 1) * 2 <= ds.length - 2){
							System.arraycopy(MathUtil.int2Bytes(temp), 0, ds, (p.getPoinNo1() - 1) * 2, 2);
						}
					}
					i = i + 4;
				}
			}
		}
		return ds;
	}
	
	private static void getTemp(byte[][] bss,int i, int type, String ip, int port){
		try{
			Checks check =ModbusFactory.createCheck(type, ip, port);
			bss[i] = check.getTemps();
		} catch(Exception e) {
		}
	}
	
	private static boolean isComplie(byte cable, int index){
		String[] cs; 
		String[] ss;
		int c = 0;
		try{
			cs = cableRanges[index];
			if(!Lang.isEmpty(cs)){
				for(String s : cs){
					ss = s.split("-");
					c = cable & 0xFF;
					if(ParseUtil.toInt(ss[0], 0) <= c && ParseUtil.toInt(ss[1], 0) >= c){
						return true;
					}
				}
			} else {
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 生成实时数据表格
	 * @author	yucy
	 * @date	2017年9月11日
	 * @return	String
	 */
	public static final String getSheetReal(List<List<List<PointInfo>>> list, TempInfo e, byte[] temps){
		StringBuffer sb = new StringBuffer("<table id=\"temp-sheet\" class=\"checkDetail\" style=\"margin:0 auto;\">");
		
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
									sbi.append("<th width=\"80\">层\\缆</th>");
								} else {
									sbi.append("<td>S")
										.append(k)
										.append("</td>");
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
					sbi.append("<th width=\"80\">层\\缆</th>");
				}
				sbi.append("<th>C").append(p.getCableNo1()).append("</th>");
				//处理其他
				//z轴方向上的标记
				int maxTag = 0;
				for(int j = 0; j < p.getZaxis(); j++){
					maxTag += 1;
					sbi = trs.get("x" + x + "z" + maxTag);
					if (xTag == 0) {
						sbi.append("<td>S");
						sbi.append(j + 1);
						sbi.append("</td>");
					}
					sbi.append("<td></td>");
				}
				for(int j = 0; j < ps.size(); j++){
					maxTag += 1;
					p = ps.get(j);
					sbi = trs.get("x" + x + "z" + maxTag);
					if (xTag == 0) {
						sbi.append("<td>S").append(p.getZaxis()+1).append("</td>");
					}
					sbi.append("<td style=\"position:relative;\">");
					if(temps != null && temps.length > ((p.getPoinNo1()-1) * 2 + 1)){
						if(temps[(p.getPoinNo1()-1) * 2] == 0 && temps[(p.getPoinNo1()-1) * 2 + 1] == 0){
							sbi.append("-");
						} else {
							temp = MathUtil.byte2IntNb(temps[(p.getPoinNo1()-1) * 2], temps[(p.getPoinNo1()-1) * 2 + 1])/10.0D;
							sbi.append(temp);
						}
					}
					sbi.append("</td>");
				}
				for(int j = maxTag; j < maxZ; j++){
					sbi = trs.get("x" + x + "z" + (j+1));
					if (xTag == 0) {
						sbi.append("<td>S").append(j + 1).append("</td>");
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
	
}
