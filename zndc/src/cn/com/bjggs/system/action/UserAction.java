package cn.com.bjggs.system.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.CardUtil;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.system.domain.SysDept;
import cn.com.bjggs.system.domain.SysHouses;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.system.service.ISystemService;
import cn.com.bjggs.system.util.PassUtil;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

@IocBean
@At({"/admin/system/user"})
public class UserAction extends SysAction {
  
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ISystemService systemService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/system/user-list.jsp")
	public Object list(HttpServletRequest req) {
		Page<SysUser> page = getPage("page.", "id", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_LIKEL_S_deptCode", new String[]{getSessionUDeptCode()});
		params.put("ft_NE_I_type", new String[]{"1"});
		page = this.systemService.findPage(SysUser.class, page, params);
		req.setAttribute("page", page);
	    return page;
	}
	
	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/system/user-edit.jsp")
	public Object edit(String id, HttpServletRequest req) {
	    SysUser u = this.systemService.findBaseAndLinks(SysUser.class, id, "roles", "id");
	    
	    String jsonString = this.systemService.getTreeJsonString(SysDept.class, false);
	    req.setAttribute("u", u != null ? u : new SysUser());
	    req.setAttribute("jsonString", jsonString);
	    return u;
    }
	
	@At({"/save"})
	public Object save(@Param("::u.") SysUser u, String oldPass, String roleIds, String tid) {
		try {
			if (u == null) throw new RuntimeException("未找到对应的用户信息!");
			if (Strings.isBlank(u.getDeptId())) throw new RuntimeException("未找到用户的所属部门信息!");
			if (this.systemService.checkUnique(SysUser.class, "loginName", u.getLoginName(), u.getId())) throw new RuntimeException("当前登录名已存在!");
			String idcard = u.getIdcard();
			if (!Strings.isBlank(idcard) && !CardUtil.isValidatedAllIdcard(idcard)) throw new RuntimeException("无效的身份证");
			this.systemService.updateUserAndRoles(u, oldPass, roleIds);
			return DwzUtil.reloadTabAndCloseCurrent("用户信息保存成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("用户信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/enable/?/?"})
	public Object enable(String id, int status, String tid) {
		try {
			if (id == null) throw new RuntimeException("未找到对应的用户信息!");
			SysUser u = this.systemService.fetch(SysUser.class, id, false);
			if (u == null) throw new RuntimeException("未找到对应的用户信息!");
			this.systemService.updateStatus(SysUser.class, id, status);
			return DwzUtil.reloadCurrPage("用户信息" + (status == 0 ? "禁用" : "启用") + "操作成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户信息" + (status == 0 ? "禁用" : "启用") + "操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("用户信息" + (status == 0 ? "禁用" : "启用") + "操作失败: " + e.getMessage());
		}
	}

	@At({"/passwd/init/?"})
	public Object initLoginPass(String id, String tid) {
		try {
			SysUser user = getSessionUser();
			if (user == null) throw new RuntimeException("请先登录本系统!");
			SysUser target = this.systemService.fetch(SysUser.class, id, false);
			if (target == null) throw new RuntimeException("未找到对应的用户信息!");
			target.setLoginPass(PropsUtil.getActiveString("passwd.init"));
			target = this.systemService.initPass(target);
			return DwzUtil.reloadCurrPage("密码初始化成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("密码初始化失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("密码初始化失败: " + e.getMessage());
		}
	}
	
	@At({"/passwd/before"})
	@Ok("jsp:/WEB-INF/admin/system/user-passwd.jsp")
	public Object updateLoginPassBefore() {
		return null;
	}

	@At({"/passwd/update"})
	public Object updateLoginPass(String oldPass, String loginPass, String confirmPass) {
		try {
			SysUser user = getSessionUser();
			if (user == null) throw new RuntimeException("请先登录本系统!");
			if (Strings.isBlank(oldPass) 
					|| Strings.isBlank(loginPass)
					|| Strings.isBlank(confirmPass)) {
				throw new RuntimeException("请填写原密码、新密码和确认密码!");
			}
			String realOldPass = PassUtil.genSysUserPass(user.getLoginName(), oldPass);
			if (!realOldPass.equals(user.getLoginPass())) throw new RuntimeException("原密码不正确!");
			if (!loginPass.equals(confirmPass)) throw new RuntimeException("两次输入的密码不一致!");
			user.setLoginPass(loginPass);
			user = this.systemService.initPass(user);
			if (user != null) removeSessionUser();
			return DwzUtil.reloadTabAndCloseCurrent("密码修改成功，请重新登录!", "");
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("密码初始化失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("密码初始化失败: " + e.getMessage());
		}
	}
	
	@At({"/del/?"})
	public Object del(String id, String tid) {
		try {
			if (id == null) throw new RuntimeException("未找到对应的用户信息!");
			SysUser d = this.systemService.fetch(SysUser.class, id, false);
			if (d == null) throw new RuntimeException("未找到对应的用户信息!");
			this.systemService.deleteBaseAndLinks(d, "roles");
			return DwzUtil.reloadCurrPage("用户信息删除成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("用户信息删除失败: " + e.getMessage());
		}
	}
	
	@At({"/del"})
	public Object deleteAll(HttpServletRequest req, String[] uids, String tid) {
		int i = 0;
		try {
			if (Lang.isEmptyArray(uids)) throw new RuntimeException("请至少勾选一条记录!");
			for (String id : uids) {
				if (!Strings.isBlank(id)) {
					i += this.systemService.deleteBaseAndLinks(SysUser.class, id, "roles");
				}
			}
			return DwzUtil.reloadCurrPage("用户信息批量删除操作，成功处理 " + i + " 条!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户信息批量删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("用户信息批量删除操作，成功处理 " + i + " 条!" + e.getMessage());
		}
	}

	@At({"/look"})
	@Ok("jsp:/WEB-INF/admin/system/look-list.jsp")
	public void look(HttpServletRequest req) {
		Page<SysUser> page = getPage("page.", "id", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_NE_I_type", new String[]{"1"});
		page = systemService.findPage(SysUser.class, page, params);
		req.setAttribute("page", page);
	}
	
	@At({"/power/house/?"})
	@Ok("jsp:/WEB-INF/admin/system/power-houses.jsp")
	public void powerHouse(String id, HttpServletRequest req) {
		req.setAttribute("uid", id);
		req.setAttribute("houses", systemService.query(SysHouses.class, Cnd.where("uid", "=", id)));
	}
	
	@At({"/power/house/save"})
	public Object save(HttpServletRequest req, String[] houses, String uid, String tid) {
		try {
			if(Strings.isBlank(uid)){
				throw new RuntimeException("请至少选择一个用户！");
			}
			systemService.savePowerHouses(houses, uid);
			return DwzUtil.reloadTabAndCloseCurrent("用户仓房权限修改成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户仓房权限修改失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("用户仓房权限修改成功失败：" + e.getMessage());
		}
	}
}