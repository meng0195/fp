package cn.com.bjggs.system.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.annotation.ActionDesc;
import cn.com.bjggs.core.enums.TypeStatus;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.domain.SysDept;
import cn.com.bjggs.system.domain.SysMenu;
import cn.com.bjggs.system.domain.SysRole;
import cn.com.bjggs.system.service.ISystemService;

@IocBean
@At({ "/admin/system/role" })
@ActionDesc("角色信息")
public class RoleAction extends SysAction {
	private final Log log = Logs.getLog(getClass());
	@Inject
	private ISystemService systemService;

	@At({ "/user/list/?" })
	@Ok("jsp:/WEB-INF/admin/system/userrole-list.jsp")
	public Object listUserRole(String deptId, String urids, HttpServletRequest req) {
		Map<String, String[]> paramsMap = getParams4Admin();
		paramsMap.put("ft_EQ_S_deptId", new String[]{deptId});
		paramsMap.put("ft_EQ_S_status", new String[]{TypeStatus.TRUE.code()});
		List<SysRole> roles = this.systemService.findList(SysRole.class, paramsMap, "id", TypeOrder.DESC);
		req.setAttribute("roles", roles);
		req.setAttribute("deptId", deptId);
		req.setAttribute("urids", urids);
		return roles;
	}

	@At({ "/list/?" })
	@Ok("jsp:/WEB-INF/admin/system/role-list.jsp")
	public Object list(String deptId, HttpServletRequest req) {
		Map<String, String[]> paramsMap = getParams4Admin();
		paramsMap.put("ft_EQ_S_deptId", new String[]{deptId});
		Page<SysRole> page = getPage("page.", "id", TypeOrder.DESC);
		page = this.systemService.findPage(SysRole.class, page, null, paramsMap);
		req.setAttribute("page", page);
		req.setAttribute("deptId", deptId);
		return page;
	}

	@At({ "/edit/?/?" })
	@Ok("jsp:/WEB-INF/admin/system/role-edit.jsp")
	public Object edit(String id, String did, HttpServletRequest req) {
		SysRole r = (SysRole) this.systemService.findBaseAndLinks(SysRole.class, id, "menus", "id");
		SysDept d = (SysDept) this.systemService.fetch(SysDept.class, did, true);
		String jsonString = this.systemService.getTreeJsonString(SysMenu.class);
		req.setAttribute("r", r != null ? r : new SysRole());
		req.setAttribute("d", d);
		req.setAttribute("jsonString", jsonString);
		return r;
	}

	@At({ "/save" })
	public Object save(@Param("::r.") SysRole r, String menuIds) {
		try {
			if (r == null) {
				throw new Exception("未找到对应的角色信息!");
			}
			if (this.systemService.checkUnique(SysRole.class, "code",
					r.getCode(), r.getId())) {
				throw new Exception("编码已存在!".intern());
			}
			r = this.systemService.updateRoleAndMenus(r, menuIds);
			return DwzUtil.reloadTabAndCloseCurrent("角色信息保存成功", "");
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("角色信息保存失败：", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("角色信息保存失败: " + e.getMessage());
		}
	}

	@At({ "/enable/?/?" })
	public Object enable(String id, int status) {
		try {
			if (id == null) {
				throw new Exception("未找到对应的角色信息!");
			}
			SysRole d = (SysRole) this.systemService.fetch(SysRole.class, id, false);
			if (d == null) {
				throw new Exception("未找到对应的角色信息!");
			}
			this.systemService.updateStatus(SysRole.class, id, status);

			return DwzUtil.reloadCurrPage("角色信息"+ (status == TypeStatus.FALSE.val() ? "禁用" : "启用") + "操作成功", "");
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("角色信息" + (status == 0 ? "禁用" : "启用") + "操作失败：",
						new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("角色信息"
					+ (status == TypeStatus.FALSE.val() ? "禁用" : "启用")
					+ "操作失败: " + e.getMessage());
		}
	}

	@At({ "/del/?" })
	public Object del(String id) {
		try {
			if (id == null) {
				throw new Exception("未找到对应的角色信息!");
			}
			SysRole d = (SysRole) this.systemService.fetch(SysRole.class, id,
					false);
			if (d == null) {
				throw new Exception("未找到对应的角色信息!");
			}
			this.systemService.deleteBaseAndLinks(d, "^(users|menus)$");

			return DwzUtil.reloadCurrPage("角色信息删除成功", "");
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("角色信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("角色信息删除失败: " + e.getMessage());
		}
	}

	@At({ "/del" })
	public Object deleteAll(HttpServletRequest req, String[] dids, String tid) {
		int i = 0;
		try {
			if (Lang.isEmptyArray(dids)) {
				throw new Exception("请至少勾选一条记录!");
			}
			i += this.systemService.delete(SysRole.class, dids);
			return DwzUtil.reloadCurrPage("角色信息批量删除操作，成功处理 " + i + "/"
					+ dids.length + " 条!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("角色信息批量删除失败：%s",
						new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("角色信息批量删除操作，成功处理 " + i + "/"
					+ dids.length + " 条!" + e.getMessage());
		}
	}
}
