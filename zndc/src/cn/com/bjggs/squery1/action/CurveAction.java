package cn.com.bjggs.squery1.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.squery.domain.QueryTemp;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/squery1/curve"})

public class CurveAction extends SysAction {
	
//	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IQueryService queryService;
	
	
	
	@At({"/temp"})
	@Ok("jsp:/WEB-INF/admin/squery1/temp-curve.jsp")
	public void tempCurve(@Param("::q")QueryTemp q, HttpServletRequest req) {
		if(q == null) q = new QueryTemp();
		q.setFlag(true);
		String json = queryService.getCurve(q);
		req.setAttribute("json", json);
		req.setAttribute("q", q);
    }
	@At({"/pest"})
	@Ok("jsp:/WEB-INF/admin/squery1/pest-curve.jsp")
	public void pestCurve(HttpServletRequest req) {
		Map<String, String[]> param = getParams4Admin();
		param.put("ft_EQ_S_curveFlag", new String[]{"1"});
		req.setAttribute("json", queryService.getPestCurve(param));
    }
	
	@At({"/gas"})
	@Ok("jsp:/WEB-INF/admin/squery1/gas-curve.jsp")
	public void gasCurve(HttpServletRequest req) {
		Map<String, String[]> param = getParams4Admin();
		param.put("ft_EQ_S_curveFlag", new String[]{"1"});
		req.setAttribute("json", queryService.getGasCurve(param));
    }
	
}
