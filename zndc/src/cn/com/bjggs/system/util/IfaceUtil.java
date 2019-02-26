package cn.com.bjggs.system.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class IfaceUtil {
	
	public static final String SUCCESS = "suc";
	
	public static final String FAIL = "fail";
	
	public static final String MSG = "操作成功！";
	
	public static final Map<String, Object> reqSUC(){
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.put("tag", SUCCESS);
		jsonMap.put("msg", MSG);
		return jsonMap;
	}
	
	public static final Map<String, Object> reqFAIL(String msg){
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.put("tag", FAIL);
		jsonMap.put("msg", msg);
		return jsonMap;
	}
	
	public static final Map<String, Object> reqSUC(Object body){
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.put("tag", SUCCESS);
		jsonMap.put("msg", MSG);
		jsonMap.put("body", body);
		return jsonMap;
	}
	
}
