package cn.com.bjggs.squery.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/squery/gas"})
public class GasQueryAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IQueryService queryService;
	
	@At({"/curve"})
	@Ok("jsp:/WEB-INF/admin/squery/gas-curve.jsp")
	public void curve(HttpServletRequest req) {
		req.setAttribute("json", queryService.getGasCurve(getParams4Admin()));
    }
	
	@At({"/history"})
	@Ok("jsp:/WEB-INF/admin/squery/gas-history.jsp")
	public void historys(String gasDate, HttpServletRequest req) {
		Page<TestGas> page = getPage("page.", "startTime", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		if (gasDate != null && !"".equals(gasDate)) {
			params.put("ft_GE_DT_startTime", new String[]{gasDate+" 00:00:00"});
			params.put("ft_LE_DT_startTime", new String[]{gasDate+" 23:59:59"});
		}
		page = queryService.findPage(TestGas.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("gasDate", gasDate);
    }
	
	@At({"/warn/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/gas-warn.jsp")
	public void warn(String houseNo, String testId, HttpServletRequest req) {
		req.setAttribute("testId", testId);
		req.setAttribute("houseNo", houseNo);
		if(Strings.isNotBlank(houseNo) && !"0".equals(houseNo)){
			req.setAttribute("r", GasUtil.lastChecks.get(houseNo));
		} else {
			req.setAttribute("r", queryService.getGasById(testId));
		}
    }
	
	@At({"/warn/detail/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/gas-warn-detail.jsp")
	public void warnDetail(String houseNo, String testId, int type, HttpServletRequest req) {
		req.setAttribute("html", queryService.getGasWarnDetail(houseNo, testId, type));
    }
	
	@At({"/sheet/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/gas-sheet.jsp")
	public void sheetDetail(String houseNo, String testId, HttpServletRequest req) {
		GasInfo gi = null;
		if(Strings.isNotBlank(houseNo) && !"0".equals(houseNo)){
			gi = HouseUtil.get(houseNo, TypeHouseConf.GAS.code(), GasInfo.class);
			if(GasUtil.lastChecks.get(houseNo) != null){
				req.setAttribute("gset", GasUtil.lastChecks.get(houseNo).getGas());
			}
		} else {
			TestGas gas = queryService.fetch(TestGas.class, testId, true);
			if(gas != null){
				gi = HouseUtil.get(gas.getHouseNo(), TypeHouseConf.GAS.code(), GasInfo.class);
				req.setAttribute("gset", gas);
			}
		}
		if(gi != null){
			req.setAttribute("wayNumb", gi.getWayNumb());
			req.setAttribute("wayXaxis", gi.getWayXaxis() == null ? new String[]{} : gi.getWayXaxis().split(","));
			req.setAttribute("wayYaxis", gi.getWayYaxis() == null ? new String[]{} : gi.getWayYaxis().split(","));
		}
    }
	

	@At({"/curve/start/?"})
	public Object weekFlag(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.updateStatus("gas", "curveFlag", id, TypeFlag.YES.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("标记曲线数据失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
	
	@At({"/curve/stop/?"})
	public Object weekClose(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.updateStatus("gas", "curveFlag", id, TypeFlag.NO.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("取消标记曲线数据失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
}
