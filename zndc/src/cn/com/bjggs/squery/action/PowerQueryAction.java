package cn.com.bjggs.squery.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.power.domain.PowerNotes;
import cn.com.bjggs.power.util.PowerUtil;
import cn.com.bjggs.squery.domain.QueryPower;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/squery/power"})	
public class PowerQueryAction extends SysAction {
	
	@Inject
	private IQueryService queryService;
	//能耗查询
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/squery/power-query-list.jsp")
	public void powerList(String date, HttpServletRequest req) {
		Page<PowerNotes> page = getPage("page.", "checkTime", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		if (date != null && !"".equals(date)) {
			String lastDay = PowerUtil.getLastDay(date);
			params.put("ft_GE_DT_checkTime", new String[]{date+" 00:00:00"});
			params.put("ft_LE_DT_checkTime", new String[]{lastDay+" 00:00:30"});
		}
		page = queryService.findPage(PowerNotes.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("date", date);
	}
	//能耗曲线
	@At({"/curves"})
	@Ok("jsp:/WEB-INF/admin/squery/power-query-curves.jsp")
	public void powerCurves(@Param("::qt")QueryPower qt, HttpServletRequest req) {
		if (qt == null) {
			qt = new QueryPower();
		}
		String json = queryService.queryPowerCurves(qt);
		req.setAttribute("json", json);
		req.setAttribute("qt", qt);
	}

}
