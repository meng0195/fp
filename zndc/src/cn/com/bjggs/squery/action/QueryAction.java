package cn.com.bjggs.squery.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.basis.view.ViewHouseInfo;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.squery.view.ViewPlanInfo;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.ChecksUtil;

@IocBean
@At({"/squery"})
public class QueryAction extends SysAction {
	
	@Inject
	private IQueryService queryService;
	
	@At({"/show/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/show-check-detail.jsp")
	public void show(String houseNo, String id, int type, HttpServletRequest req) {
		req.setAttribute("hosueNo", houseNo);
		req.setAttribute("id", id);
		req.setAttribute("type", type);
    }
	
	@At({"/detail/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/show-house-detail.jsp")
	public void detail(String houseNo, String id, HttpServletRequest req) {
		ViewHouseInfo house = queryService.fetch(ViewHouseInfo.class, Cnd.where("houseNo", "=", houseNo));
		TempResults res = ChecksUtil.lastChecks.get(houseNo);
		TestData temp = res.getDatas();
		PestResults pest = CheckPest.lastPests.get(houseNo);
		GasResults gas = GasUtil.lastChecks.get(houseNo);  
		List<ViewPlanInfo> plans = queryService.query(ViewPlanInfo.class, Cnd.where("houseNo", "=", houseNo));
		
		req.setAttribute("house", house);
		req.setAttribute("temp", temp);
		req.setAttribute("pest", pest);
		req.setAttribute("gas", gas);
		req.setAttribute("plans", plans);
		req.setAttribute("equips", CtrUtil.lasts.get(houseNo));
	}
}
