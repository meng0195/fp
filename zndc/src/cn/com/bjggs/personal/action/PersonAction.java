package cn.com.bjggs.personal.action;

import java.io.File;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Encoding;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.personal.service.IPersonService;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.system.domain.SysUser;

@IocBean
@At({"/admin/person"})
public class PersonAction extends SysAction {

	private static final Log log = Logs.getLog(PersonAction.class);
	
	@Inject
	private IPersonService personService;

	@At({"/to/help"})
	@Ok("jsp:/WEB-INF/admin/person/help.jsp")
	public void toHelp(HttpServletRequest req){}
	
	@At({"/to/us"})
	@Ok("jsp:/WEB-INF/admin/person/us.jsp")
	public void toUs(HttpServletRequest req){}
	
	@At({"/to/main"})
	@Ok("jsp:/WEB-INF/admin/person/person-main.jsp")
	public void toMain(HttpServletRequest req){
		req.setAttribute("u", personService.fetch(SysUser.class, getSessionUid(), true));
	}
	
	@At({"/save"})
	public Object save(@Param("::u.") SysUser u, String tid) {
		try {
			personService.updateUser(u);
			return DwzUtil.reloadTabAndCloseCurrent("用户信息保存成功, 重新登录后生效！", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("用户信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("用户信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/help/?"})
	@Ok("raw:stream")
	public Object help(int type) {
		try {
			String downFileDir = Mvcs.getServletContext().getRealPath("/help");
			String downFileName = "";
			if(type == 0){
				downFileName = downFileDir + "/智能粮仓管理系统实施手册.doc";
			} else if(type == 1){
				downFileName = downFileDir + "/智能粮仓管理系统用户操作使用说明书.doc";
			} else if(type == 2){
				downFileName = downFileDir + "/智能单仓配置说明1.0.docx";
			} else if(type == 3){
				downFileName = downFileDir + "/兼容浏览器.exe";
			} else if(type == 4){
				downFileName = downFileDir + "/摄像头插件.exe";
			} else if(type == 5){
				downFileName = downFileDir + "/3D插件.exe";
			}
			File file = new File(downFileName);
			try {
				String fileName = file.getName();
				String agent = Strings.sBlank(getRequest().getHeader("USER-AGENT"));  
		        if(agent.toLowerCase().indexOf("firefox") > 0) {
		        	fileName = new String(fileName.getBytes(Encoding.UTF8), Encoding.ISO_8859_1);
		        } else {
		        	fileName = URLEncoder.encode(fileName, Encoding.UTF8);
		        }
		        getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			} catch(Exception e) {
			}
			return file;
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("获取帮助文档失败：%s", new Object[] { e.getMessage() });
			}
		}
		return null;
	}
}