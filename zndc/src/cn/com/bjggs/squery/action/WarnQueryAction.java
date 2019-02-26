package cn.com.bjggs.squery.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.warns.domain.AlarmNotes;

@IocBean
@At({"/squery/warn"})
public class WarnQueryAction extends SysAction {
	
	@Inject
	private IQueryService queryService;
	@At({"/detail/?"})
	@Ok("jsp:/WEB-INF/admin/squery/warn-detail.jsp")
	public void warnDetail(String testDataId, HttpServletRequest req) {
		AlarmNotes an = queryService.fetch(AlarmNotes.class, Cnd.where("ID", "=", testDataId));
		req.setAttribute("an", an);
		req.setAttribute("html", queryService.getDetailWarn(testDataId));
	}
}
