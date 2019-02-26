package cn.com.bjggs.system.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.system.domain.SysCode;
import cn.com.bjggs.system.domain.SysDept;

public class CodesUtil {
	
	private static Dao dao;
	
	private static final Map<String, Map<String, String>> codesMap = new LinkedHashMap<String, Map<String, String>>();

	public static final Map<String, Map<String, String>> getSysCodesMap(final ServletContext sc, final Dao d) {
		
		dao = d;

		List<SysCode> codes = dao.query(SysCode.class, Cnd.where("status", "=", 1).asc("type").asc("seqno"));
		
		SysCode c;
		
		if (!Lang.isEmpty(codes)) {
			Map<String, String> detailsMap = null;
			String type = null;
			for (Iterator<SysCode> localIterator = codes.iterator(); localIterator.hasNext();) {
				c = localIterator.next();
				type = c.getType();
				if (codesMap.containsKey(type)) {
					codesMap.get(type).put(c.getCode(), c.getName());
				} else {
					detailsMap = new LinkedHashMap<String, String>();
					detailsMap.put(c.getCode(), c.getName());
					codesMap.put(type, detailsMap);
				}
			}
		}
		sc.setAttribute("codes", codesMap);

		List<SysDept> depts = dao.query(SysDept.class, Cnd.where("status", "=", 1).asc("level").asc("seqno"));
		for (SysDept dept : depts) {
			update("sys_dept", dept.getCode(), dept.getName());
		}
		return codesMap;
	}

	public static final Map<String, String> get(String type) {
		if (Strings.isBlank(type)) {
			return null;
		}
		return codesMap.containsKey(type) ? codesMap.get(type) : null;
	}

	public static final String get(String type, String code) {
		if ((Strings.isBlank(type)) || (Strings.isBlank(code))) {
			return "".intern();
		}
		Map<String, String> values = codesMap.containsKey(type) ? codesMap.get(type) : null;
		return !Lang.isEmpty(values) ? Strings.sBlank(values.get(code), "") : "".intern();
	}

	public static final void reload(String type) {
		if ((dao != null) && (!Strings.isBlank(type))) {
			List<SysCode> codes = dao.query(SysCode.class, Cnd.where("status", "=", 1).and("type", "=", type).asc("seqno"));
			if (!Lang.isEmpty(codes)) {
				Map<String, String> detailsMap = new LinkedHashMap<String, String>();
				for (SysCode c : codes) {
					detailsMap.put(c.getCode(), c.getName());
				}
				codesMap.put(type, detailsMap);
			}
		}
	}

	public static final void update(String type, String code, String name) {
		if (codesMap.containsKey(type)) {
			codesMap.get(type).put(code, name);
		} else {
			Map<String, String> values = new LinkedHashMap<String, String>();
			values.put(code, name);
			codesMap.put(type, values);
		}
	}

	public static final void update(String type, Map<String, String> values) {
		codesMap.put(type, values);
	}

	public static final void delete(String type) {
		if (codesMap.containsKey(type)) {
			codesMap.remove(type);
		}
	}

	public static final void delete(String type, String code) {
		if (codesMap.containsKey(type)) {
			Map<String, String> values = codesMap.get(type);
			if ((!Lang.isEmpty(values)) && (values.containsKey(code))) {
				values.remove(code);
			}
		}
	}
}
