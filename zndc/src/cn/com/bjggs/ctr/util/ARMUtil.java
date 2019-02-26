package cn.com.bjggs.ctr.util;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

/**
 * Arm工具类
 * @author 	yucy
 * @date	2018-03-19
 */
public class ARMUtil {
	
	/**
	 * 拼接DI指令
	 * @param url 
	 * @param strings 一个或者多个查询位
	 * @return 例如：http://192.168.30.222/DI?DI1=,DI2=,DI4=,DI9=,DI16=!
	 */
	
	public static String getDIUrl(String url, int...ints){
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(url).append("/DI?");
		if(Lang.isEmpty(url) || Lang.isEmpty(ints) || ints.length > 16){
			return null;
		}
		for (int s : ints) {
			sb.append("DI").append(s).append("=").append(",");
		}
		char c = '!';
		sb.setCharAt(sb.length() - 1, c);
		return sb.toString();
	}
	
	/**
	 * 解析返回的DI数据//正则验证
	 * @param results  如：DI?DI1=0,DI2=0,DI4=0,DI9=0,DI16=0!
	 * @return Map<Integer, Integer> Map<查询位，查询位状态>
	 */
	public static int[] getDIResult(String results){
		if (Strings.isNotBlank(results) && results.length() > 3) {
			String s = results.substring(3, results.length()-1);
			String[] di = s.split(",");
			int[] outs = new int[di.length];
			for (int i = 0; i < di.length; i++) {
				String[] a = di[i].split("=");
				outs[i] = Integer.parseInt(a[1]);
			}
//			for (int i = 0; i < ins.length; i++) {
//				outs[i] = Integer.parseInt(results.substring(results.indexOf("DI" + ins[i])+4, results.indexOf("DI" + ins[i])+5));
//			}
			return outs;
		} else {
			throw new RuntimeException("模块返回指令错误！");
		}
	}
	
	//开启单指令设备，风机，内环流等
	public static String isOpenOnew(String url, int...ints){
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(url).append("/DO?");
		if(Lang.isEmpty(url) || Lang.isEmpty(ints) || ints.length > 16){
			return null;
		}
		for (int i = 0; i < ints.length; i++) {
			sb.append("DO").append(ints[i]).append("=").append(CtrConstant.IOT).append(",");
		}
		char c = '!';
		sb.setCharAt(sb.length() - 1, c);
		return sb.toString();
	}
	
	//关闭单指令设备，风机，内环流等
	public static String isCloseOnew(String url, int...ints){
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(url).append("/DO?");
		if(Lang.isEmpty(url) || Lang.isEmpty(ints) || ints.length > 16){
			return null;
		}
		for (int i = 0; i < ints.length; i++) {
			sb.append("DO").append(ints[i]).append("=").append(CtrConstant.IOC).append(",");
		}
		char c = '!';
		sb.setCharAt(sb.length() - 1, c);
		return sb.toString();
	}
	
	/**
	 * 开启双指令设备，窗户
	 * @param url
	 * @param ints
	 * @return
	 */
	public static String isOpenWind(String url, int...ints){
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(url).append("/DO?");
		if(Lang.isEmpty(url) || Lang.isEmpty(ints) || ints.length > 16){
			return null;
		}
		for (int i = 0; i < ints.length; i=i+2) {
			sb.append("DO").append(ints[i]).append("=").append(CtrConstant.IOT).append(",");
			sb.append("DO").append(ints[i+1]).append("=").append(CtrConstant.IOC).append(",");
		}
		char c = '!';
		sb.setCharAt(sb.length() - 1, c);
		return sb.toString();
	}
	
	//关闭双指令设备，窗户
	public static String isCloseWind(String url, int...ints){
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(url).append("/DO?");
		if(Lang.isEmpty(url) || Lang.isEmpty(ints) || ints.length > 16){
			return null;
		}
		for (int i = 0; i < ints.length; i=i+2) {
			sb.append("DO").append(ints[i]).append("=").append(CtrConstant.IOC).append(",");
			sb.append("DO").append(ints[i+1]).append("=").append(CtrConstant.IOT).append(",");
		}
		char c = '!';
		sb.setCharAt(sb.length() - 1, c);
		return sb.toString();
	}

	public static boolean isZero(int index, int[] outs){
		if(outs != null && outs.length > index){
			return outs[index] == 0;
		} else {
			return false;
		}
	}
	
	public static boolean isOne(int index, int[] outs){
		if(outs != null && outs.length > index){
			return outs[index] == 1;
		} else {
			return false;
		}
	}
	
	public static boolean isSuccess(String str){
		return !isFault(str);
	}
	
	public static boolean isFault(String str){
		if (!Strings.equals(str, "POST/DO? SET SUCCESSFUL")) {
			return true;
		} 
		return false;
	}
}
