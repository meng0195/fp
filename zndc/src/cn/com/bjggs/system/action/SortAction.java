package cn.com.bjggs.system.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.InvokeUtil;
import cn.com.bjggs.core.util.StringUtil;
import cn.com.bjggs.core.util.TagsUtil;
import cn.com.bjggs.system.service.ISystemService;

@IocBean
@At({ "/admin/system/sort" })
public class SortAction extends SysAction {
	private final Log log = Logs.getLog(getClass());
	@Inject
	private ISystemService systemService;

	@At({ "/before/?" })
	@Ok("jsp:/WEB-INF/admin/system/sort-before.jsp")
	public Object treeSameLevelSortBefore(String tag) throws Exception {
		return treeSameLevelSortBefore(tag, null, null, null);
	}

	@At({ "/before/?/?/?" })
	@Ok("jsp:/WEB-INF/admin/system/sort-before.jsp")
	public Object treeSameLevelSortBefore(String tag, String pPropName,
			String id, String otherPropParams) throws Exception {
		List<?> objs = null;
		try {
			Class<?> clazz = TagsUtil.get(Mvcs.getServletContext(), tag);
			Map<String, String[]> params = new LinkedHashMap<String, String[]>();
			if (!Strings.isBlank(id)) {
				Object object = this.systemService.fetch(clazz, id, false);
				if (object == null) {
					throw new Exception("未获取到数据,实体：".concat(tag).concat(",ID：").concat(id));
				}
				String pPropValue = (String) InvokeUtil.getValue(object, pPropName);
				params.put("ft_EQ_S_".concat(pPropName), new String[]{pPropValue});
				if ((!Strings.isBlank(otherPropParams))
						&& (otherPropParams.indexOf(":") != -1)) {
					String[] otherPropsArr = { otherPropParams };
					if (otherPropParams.indexOf(",") != -1) {
						otherPropsArr = Strings.splitIgnoreBlank(
								otherPropParams.trim(), ",");
					}
					String propName = null;
					String propValue = null;
					for (String otherProp : otherPropsArr) {
						if ((otherProp != null)
								&& (otherProp.indexOf(":") != -1)) {
							propName = StringUtil.substringBefore(otherProp, ":").trim();
							propValue = StringUtil.substringAfterLast(otherProp, ":").trim();
							if ((!Strings.isBlank(propName)) && (InvokeUtil.containsProperty(object, propName))) {
								params.put("ft_EQ_S_".concat(propName), new String[]{propValue});
							}
						}
					}
				}
			}
			objs = this.systemService.findList(clazz, params, "seqno", TypeOrder.ASC);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("同级项排序失败：%s", new Object[] { e.getMessage() });
			}
		}
		getRequest().setAttribute("objs", objs);
		getRequest().setAttribute("tag", tag);
		return objs;
	}

	@At({ "/update/?" })
	public Object treeSameLevelSort(String tag, String sortedIds, String tid)
			throws Exception {
		try {
			if (Strings.isBlank(sortedIds)) {
				throw new Exception("未找到需要排序的数据");
			}
			Class<?> clazz = TagsUtil.get(Mvcs.getServletContext(), tag);
			this.systemService.updateTreeSameLevelSeqno(clazz, sortedIds);
			return DwzUtil.reloadTabAndCloseCurrent("同级项排序成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("同级项排序失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("同级项排序失败: " + e.getMessage());
		}
	}
}
