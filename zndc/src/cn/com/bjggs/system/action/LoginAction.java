package cn.com.bjggs.system.action;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.view.JspView;

import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.StringUtil;
import cn.com.bjggs.msg.view.ViewMsgInfo;
import cn.com.bjggs.msg.view.ViewUserMsg;
import cn.com.bjggs.squery.util.QueryUtil;
import cn.com.bjggs.system.domain.SysHouses;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.system.service.ISystemService;
import cn.com.bjggs.system.util.PassUtil;
import cn.com.bjggs.weather.util.WeatherUtil;

/**
 * 后台登录监听action
 * @author	wc
 * @date	2017年4月23日 
 */
@IocBean
@At({"/admin"})
public class LoginAction extends SysAction {

	@Inject
	private ISystemService systemService;

	@GET
	@At({"/login"})
	@Ok("jsp:/WEB-INF/admin/login.jsp")
	public void login() {}

	@At({"/login/dialog"})
	@Ok("jsp:/WEB-INF/admin/login_dialog.jsp")
	public void loginDialog() {}
	  
	@POST
	@At({"/login/validate"})
	@Ok("redirect:/admin/main")
	public Object valdiate(String loginName, String loginPass) {
		try {
			if (Strings.isBlank(loginName)) {
				throw new RuntimeException("请输入您的登录名！");
			}
			if (Strings.isBlank(loginPass)) {
				throw new RuntimeException("请输入您的密码！");
			}
			String code = PassUtil.getMD5Name(PassUtil._NAME);
			if (!Strings.equals(code, PassUtil._CODE)){
				return new JspView("/WEB-INF/admin/warn-code.jsp");
			}
			loginProcess(loginName, loginPass);
			return null;
		} catch (Exception e) {
			String errorMessage = Strings.sBlank(e.getMessage(), "未知错误");
			Mvcs.getReq().setAttribute("errorMessage", errorMessage);
		}
		return new JspView("/WEB-INF/admin/login.jsp");
	}
	
	@At({"/main"})
	@Ok("jsp:/WEB-INF/admin/main.jsp")
	public void main(HttpServletRequest req) {
		req.setAttribute("msg", systemService.fetch(ViewUserMsg.class, Cnd.where("userCode", "=", getSessionUid())));
		Criteria cri = Cnd.cri();
		cri.where().and("userCode", "=", getSessionUid()).and("status", "=", 0);
		cri.getOrderBy().desc("time");
		Page<ViewMsgInfo> page = new Page<ViewMsgInfo>();
		page.setPageSize(10);
		req.setAttribute("msgs", systemService.findPage(ViewMsgInfo.class, page, cri));
		req.setAttribute("list", QueryUtil.list);
		req.setAttribute("wea", WeatherUtil.testWeather);
	}

	@At({"/login/ajax"})
	public Object ajaxLogin(String loginName, String loginPass) {
		try {
			if (Strings.isBlank(loginName)) {
				throw new RuntimeException("请输入登录名");
			}
			if (Strings.isBlank(loginPass)) {
				throw new RuntimeException("请输入登录密码");
			}
			String maxTid = loginProcess(loginName, loginPass);
			return DwzUtil.reloadTabAndCloseCurrent("登录成功!", "", maxTid);
		} catch (Exception e) {
			return DwzUtil.stopPageError("登录失败：" + e.getMessage());
		}
	}
	  
	@At({"/login/after/user"})
	@Ok("jsp:/WEB-INF/admin/login-after-user.jsp")
	public void ajaxLoginUser() {}
	  
	@At({"/login/after/menu"})
	@Ok("jsp:/WEB-INF/admin/login-after-menu.jsp")
	public void ajaxLoginMenu() {}
	  
	@At({"/logout"})
	@Ok("redirect:/admin/login")
	public void logout() {
		removeSessionUser();
	}

	private String loginProcess(String loginName, String loginPass) {
		SysUser user = this.systemService.loginUser(loginName, loginPass);
		if ((user == null) || (Strings.isBlank(user.getId()))) {
			throw new RuntimeException("登录名和密码不匹配");
		}
		if (user.getStatus() == 0) {
			throw new RuntimeException("您的账号已被锁定");
		}
		List<SysHouses> list = systemService.query(SysHouses.class, Cnd.where("uid", "=", user.getId()));
		if(!Lang.isEmpty(list)){
			String[] houses = new String[list.size()];
			for(int i = 0; i < list.size(); i++){
				if (list.get(i) != null) {
					houses[i] = list.get(i).getHouseNo();
				}
			}
			user.setHouses(houses);
		}
		String treeHTML = this.systemService.buildDWZMenuTreeHTML(user, getContextPath());
		setSessionUser(user, treeHTML);
	    return fetchMaxTid(treeHTML);
	}
	
	private String fetchMaxTid(String str) {
		Pattern p = Pattern.compile("page_[0-9]{1,}");
		Matcher m = p.matcher(str);
		String rel = null;
		while (m.find()) {
			rel = m.group(0);
		}
		return Strings.isBlank(rel) ? "" : StringUtil.substringAfterLast(rel, "_");
	}
}
