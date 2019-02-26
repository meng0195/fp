package cn.com.bjggs.system.action;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import cn.com.bjggs.Constant;
import cn.com.bjggs.core.action.CommonAction;
import cn.com.bjggs.system.domain.SysMenu;
import cn.com.bjggs.system.domain.SysUser;

import org.nutz.mvc.Mvcs;

/**
 * 后台action基类（包含session中user的相关操作）
 * @author	wc
 * @date	2017年4月23日 
 */
public class SysAction extends CommonAction {

	/**
	 * 从session中获取用户
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	SysUser
	 */
	protected SysUser getSessionUser() {
		HttpSession session = Mvcs.getHttpSession(false);
		if (session == null) return null;
		SysUser user = (SysUser)session.getAttribute(Constant.SS_USER);
		if (user == null) throw new RuntimeException(Constant.NO_USER_MSG);
		return user;
	}

	/**
	 * 从session中获取用户名称
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	String
	 */
	protected String getSessionUname() {
		SysUser user = getSessionUser();
		return user != null ? user.getName() : null;
	}

	/**
	 * 从session中获取用户部门code
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	String
	 */
	protected String getSessionUDeptCode() {
		String deptCode = "".intern();
		SysUser user = getSessionUser();
		if (user != null) deptCode = user.getDeptCode();
		return deptCode;
	}

	/**
	 * 从session中获取用户ID
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	String
	 */
	protected String getSessionUid() {
		SysUser user = getSessionUser();
		return user != null ? user.getId() : null;
	}

	/**
	 * 将用户和菜单列表放置session中
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	void
	 */
	protected void setSessionUser(SysUser user, Collection<SysMenu> menus) {
		HttpSession session = Mvcs.getHttpSession(true);
		if (session != null) {
			session.setAttribute(Constant.SS_USER, user);
			session.setAttribute(Constant.SS_USER_MENUS, menus);
		}
	}
	
	/**
	 * 将用户和菜单数放到session中
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	void
	 */
	protected void setSessionUser(SysUser user, String treeHTML) {
		HttpSession session = Mvcs.getHttpSession(true);
		if (session != null) {
			session.setAttribute(Constant.SS_USER, user);
			session.setAttribute(Constant.SS_USER_MENUS, treeHTML);
		}
	}

	/**
	 * 移除session中的user
	 * @author	wc
	 * @date	2017年4月23日
	 * @return	void
	 */
	protected void removeSessionUser() {
		HttpSession session = Mvcs.getHttpSession(false);
		if ((session != null) && (session.getAttribute(Constant.SS_USER) != null)) {
			session.removeAttribute("_SYS_USER_");
			session.removeAttribute("_SYS_USER_MENUS_");
			session.invalidate();
		}
	}
}
