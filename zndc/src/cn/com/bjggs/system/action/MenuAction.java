package cn.com.bjggs.system.action;

import cn.com.bjggs.core.annotation.ActionDesc;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.domain.SysMenu;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.system.service.ISystemService;
import javax.servlet.http.HttpServletRequest;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

@IocBean
@At({ "/admin/system/menu" })
@ActionDesc("菜单信息")
public class MenuAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ISystemService systemService;

	@At({ "/list" })
	@Ok("jsp:/WEB-INF/admin/system/menu-list.jsp")
	public Object list(HttpServletRequest req) {
		String jsonString = this.systemService.getTreeJsonString(SysMenu.class);
		req.setAttribute("jsonString", jsonString);
		return jsonString;
	}

	@At({ "/add/?" })
	@Ok("jsp:/WEB-INF/admin/system/menu-edit.jsp")
	public Object add(int level, HttpServletRequest req) {
		SysMenu m = (SysMenu) this.systemService.genSysEntryCode(SysMenu.class, "", level);
		req.setAttribute("m", m);
		return m;
	}

	@At({ "/edit/?" })
	@Ok("jsp:/WEB-INF/admin/system/menu-edit.jsp")
	public Object edit(String id, HttpServletRequest req) {
		SysMenu m = (SysMenu) this.systemService.fetch(SysMenu.class, id, true);
		req.setAttribute("m", m);
		return m;
	}

	@At({ "/save" })
	public Object save(@Param("::m.") SysMenu m, String tid) {
		try {
			if (m == null) {
				throw new Exception("未找到对应的菜单信息!");
			}
			if (this.systemService.checkUnique(SysMenu.class, "code",
					m.getCode(), m.getId())) {
				throw new Exception("编码已存在!".intern());
			}
			m = (SysMenu) this.systemService.update(m);

			SysUser user = getSessionUser();
			String treeHTML = this.systemService.buildDWZMenuTreeHTML(user,
					getContextPath());
			setSessionUser(user, treeHTML);

			return DwzUtil.reloadCurrPage("菜单信息保存成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("菜单信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("菜单信息保存失败: " + e.getMessage());
		}
	}

	@At({ "/save/next/before/?/?" })
	@Ok("jsp:/WEB-INF/admin/system/menu-edit.jsp")
	public Object saveChildBefore(String pid, int level, String tid,
			HttpServletRequest req) {
		SysMenu m = (SysMenu) this.systemService.genSysEntryCode(SysMenu.class,
				pid, level);
		m.setPid(pid);
		m.setLevel(level + 1);

		req.setAttribute("m", m);
		return m;
	}

	@At({ "/save/next" })
	public Object saveChild(@Param("::m.") SysMenu m, String tid) {
		try {
			if ((m != null) && (m.getLevel() >= 2)) {
				throw new RuntimeException("暂时只支持添加两级菜单!");
			}
			this.systemService.saveNextLevel(SysMenu.class, m);

			SysUser user = getSessionUser();
			String treeHTML = this.systemService.buildDWZMenuTreeHTML(user,
					getContextPath());
			setSessionUser(user, treeHTML);

			return DwzUtil.reloadCurrPage("下级菜单信息保存成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("下级菜单信息保存失败：%s",
						new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("下级菜单信息保存失败: " + e.getMessage());
		}
	}

	@At({ "/del/?" })
	public Object del(String id, String tid) {
		try {
			if (id == null) {
				throw new Exception("未找到对应的菜单信息!");
			}
			SysMenu d = (SysMenu) this.systemService.fetch(SysMenu.class, id,
					false);
			if (d == null) {
				throw new Exception("未找到对应的菜单信息!");
			}
			this.systemService.deleteSelfAndChilds(SysMenu.class, id);

			SysUser user = getSessionUser();
			String treeHTML = this.systemService.buildDWZMenuTreeHTML(user,
					getContextPath());
			setSessionUser(user, treeHTML);

			return DwzUtil.reloadCurrPage("菜单信息删除成功", tid, id);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("菜单信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("菜单信息删除失败: " + e.getMessage());
		}
	}
}
