package cn.com.bjggs.system.util;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.util.PropsUtil;

public class PassUtil {

	public static final String MD5 = "良安科技MD5";
	
	public static String _CODE = PropsUtil.getString("base.name.md5");
	
	public static String _NAME = PropsUtil.getString("base.name.real");
	
	public static int _WEATAG = PropsUtil.getInteger("temp.weather.conf", 0);
	
	public static String getMD5Name(String name){
		return Lang.md5(name.trim().concat(MD5));
	}
	
	public static String genSysUserPass(String loginName, String loginPass) {
		return Lang.md5(loginName.trim().concat(loginPass.trim()));
	}

	public static String genWebUserPass(String loginName, String loginPass, String salt) {
		loginName = Strings.trim(loginName);
		loginPass = Strings.trim(loginPass);
	    
		int BASE_LEN = loginPass.length();
	    int SALT_LEN = salt.length();
	    int SUB_LEN = BASE_LEN - SALT_LEN;
	    int MAX_LEN = Math.max(BASE_LEN, SALT_LEN);
	    int MIN_LEN = Math.min(BASE_LEN, SALT_LEN);
	    
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < MIN_LEN; i++) {
	    	sb.append(loginPass.charAt(i)).append(salt.charAt(i));
	    }
	    sb.append(loginName);
	    
	    String surplus = SUB_LEN == 0 ? "" : Strings.cutRight(SUB_LEN > 0 ? loginPass : salt, MAX_LEN - MIN_LEN, '\000');
	    sb.append(surplus);
	    return Lang.md5(sb.toString());
	}
}
