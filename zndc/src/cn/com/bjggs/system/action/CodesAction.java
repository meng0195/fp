package cn.com.bjggs.system.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.annotation.ActionDesc;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.core.enums.TypeStatusCode;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.domain.SysCode;
import cn.com.bjggs.system.service.ISystemService;

@IocBean
@At({ "/admin/system/code" })
@ActionDesc("系统编码信息")
public class CodesAction extends SysAction {

	private final Log log = Logs.getLog(getClass());

	@Inject
	private ISystemService systemService;

	@At({ "/list" })
	@Ok("jsp:/WEB-INF/admin/system/code-list.jsp")
	public Object list(String type, HttpServletRequest req) {
		Page<SysCode> page = getPage("page.");
		page.addOrder("type", TypeOrder.ASC);
		page.addOrder("seqno", TypeOrder.ASC);
		page = this.systemService.findPage(SysCode.class, page, null,
				getParams4Admin());
		req.setAttribute("page", page);

		return page;
	}

	@At({ "/edit/?" })
	@Ok("jsp:/WEB-INF/admin/system/code-edit.jsp")
	public Object edit(String id, HttpServletRequest req) {
		SysCode c = (SysCode) this.systemService.fetch(SysCode.class, id, true);
		req.setAttribute("c", c);
		return c;
	}

	@At({ "/save" })
	public Object save(@Param("::c.") SysCode c, String tid) {
		try {
			if (c == null) {
				throw new Exception("未找到对应的编码信息!");
			}
			if ((!Strings.isBlank(c.getId()))
					&& (c.getStatus() == TypeStatusCode.FIXED.val())) {
				throw new Exception("当前编码已为固化状态，不能修改!");
			}
			if (this.systemService.checkCodeUnique(SysCode.class, c.getType(),
					c.getCode(), c.getId())) {
				throw new Exception("编码已存在!".intern());
			}
			c = (SysCode) this.systemService.update(c);
			return DwzUtil.reloadTabAndCloseCurrent("编码信息保存成功", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("编码信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("编码信息保存失败: " + e.getMessage());
		}
	}

	@At({ "/fix/?" })
	public Object fix(String id, String tid) {
		try {
			if (id == null) {
				throw new Exception("未找到对应的编码信息!");
			}
			SysCode c = (SysCode) this.systemService.fetch(SysCode.class, id,
					false);
			if (c == null) {
				throw new Exception("未找到对应的编码信息!");
			}
			this.systemService.updateStatus(SysCode.class, id,
					TypeStatusCode.FIXED.val());
			Codes.reload(c.getType());

			return DwzUtil.reloadCurrPage("编码信息固化操作成功", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("编码信息固化操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("编码信息固化操作失败: " + e.getMessage());
		}
	}

	@At({ "/fix" })
	public Object fixAll(HttpServletRequest req, String[] cids, String tid) {
		int i = 0;
		try {
			if (Lang.isEmptyArray(cids)) {
				throw new Exception("请至少勾选一条记录!");
			}
			Map<String, Integer> typesMap = this.systemService
					.updateCodeStatus(cids, TypeStatusCode.FIXED.val());
			for (Map.Entry<String, Integer> m : typesMap.entrySet()) {
				i += ((Integer) m.getValue()).intValue();
				Codes.reload((String) m.getKey());
			}
			return DwzUtil.reloadCurrPage("编码信息批量固化操作，成功处理 " + i + " 条!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("编码信息批量固化失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("编码信息批量固化操作，成功处理 " + i + " 条!"
					+ e.getMessage());
		}
	}

	@At({ "/del/?" })
	public Object del(String id, String tid) {
		try {
			if (Strings.isBlank(id)) {
				throw new Exception("未找到对应的编码信息!");
			}
			SysCode c = (SysCode) this.systemService.fetch(SysCode.class, id,
					false);
			if (c == null) {
				throw new Exception("未找到对应的编码信息!");
			}
			if (c.getStatus() == TypeStatusCode.FIXED.val()) {
				throw new Exception("当前编码已为固化状态，不能删除!");
			}
			this.systemService.delete(c);
			return DwzUtil.reloadCurrPage("编码信息删除成功", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("编码信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("编码信息删除失败: " + e.getMessage());
		}
	}

	@At({ "/del" })
	public Object deleteAll(HttpServletRequest req, String[] cids, String tid) {
		int i = 0;
		try {
			if (Lang.isEmptyArray(cids)) {
				throw new Exception("请至少勾选一条记录!");
			}
			i = this.systemService.deleteCodes(cids);
			return DwzUtil.reloadCurrPage("编码信息批量删除操作，成功处理 " + i + "/"
					+ cids.length + " 条!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("编码信息批量删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("编码信息批量删除操作，成功处理 " + i + "/"
					+ cids.length + " 条!" + e.getMessage());
		}
	}
}
