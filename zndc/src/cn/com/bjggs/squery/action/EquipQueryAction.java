package cn.com.bjggs.squery.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.ctr.domain.CtrsNotes;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/squery/equip"})
public class EquipQueryAction extends SysAction {
	
	@Inject
	private IQueryService queryService;
	
	@At({"/sheet/?"})
	@Ok("jsp:/WEB-INF/admin/squery/equip-detail.jsp")
	public void warnDetail(String houseNo, HttpServletRequest req) {
		req.setAttribute("equips", CtrUtil.lasts.get(houseNo));
	}
	
	@At({"/history"})
	@Ok("jsp:/WEB-INF/admin/squery/equip-scan-history.jsp")
	public void list(HttpServletRequest req) {
		Page<CtrsNotes> page = getPage("page.", "startTime", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		page = queryService.findPage(CtrsNotes.class, page, params);
		req.setAttribute("page", page);
	}
	
}
