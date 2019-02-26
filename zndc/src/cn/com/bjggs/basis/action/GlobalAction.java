package cn.com.bjggs.basis.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.domain.GlobalConf;
import cn.com.bjggs.basis.service.IBasisService;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.system.util.PassUtil;

@IocBean
@At({"/global"})
public class GlobalAction extends SysAction{
	private final Log log = Logs.getLog(GlobalAction.class);
	@Inject
	private IBasisService basisService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/basis/global-edit.jsp")
	public void list(HttpServletRequest req){
		req.setAttribute("base_code", PassUtil._CODE);
		req.setAttribute("base_name", PassUtil._NAME);
		req.setAttribute("base_weaTag", PassUtil._WEATAG);
	}
	@At({"/save"})
	public Object save(@Param("::g")GlobalConf g, String tid, HttpServletRequest req){
		try {	
			if(!Lang.isEmpty(g)){
				PropsUtil.writeProperties("base.name.md5", g.getIdentCode());
				PropsUtil.writeProperties("base.name.real", g.getIdentName());
				PropsUtil.writeProperties("temp.weather.conf", g.getIdentName());
				PassUtil._CODE = g.getIdentCode();
				PassUtil._NAME = g.getIdentName();
				PassUtil._WEATAG = g.getWeaTag();
			}
			return DwzUtil.nothing("信息保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("信息保存失败: " + e.getMessage());
		}
		
	}

}





