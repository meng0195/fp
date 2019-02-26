package cn.com.bjggs.squery.action;

import java.util.LinkedHashMap;
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
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/squery/pest"})
public class PestQueryAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IQueryService queryService;
	
	@At({"/curve"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-curve.jsp")
	public void curve(HttpServletRequest req) {
		req.setAttribute("json", queryService.getPestCurve(getParams4Admin()));
    }
	
	@At({"/history"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-history.jsp")
	public void historys(String pestDate, HttpServletRequest req) {
		Page<TestPest> page = getPage("page.", "startTime", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		if (pestDate != null && !"".equals(pestDate)) {
			params.put("ft_GE_DT_startTime", new String[]{pestDate+" 00:00:00"});
			params.put("ft_LE_DT_startTime", new String[]{pestDate+" 23:59:59"});
		}
		page = queryService.findPage(TestPest.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("pestDate", pestDate);
    }
	
	@At({"/compare"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-compare.jsp")
	public void compare(HttpServletRequest req) {}
	
	@At({"/compare/detail"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-compare-detail.jsp")
	public void compareDetail(HttpServletRequest req) {
		req.setAttribute("json", queryService.getPestCompare(getParams4Admin()));
    }
	
	@At({"/compare/times"})
	public Object times(String houseNo, HttpServletRequest req) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("json", queryService.getPestTimes(houseNo));
		return map;
    }
	
	@At({"/pic/detail/?"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-pic-detail.jsp")
	public void picDetail(String picSrc, HttpServletRequest req) {
		req.setAttribute("picSrc", picSrc.replaceAll("-", "/") + ".jpg");
    }
	
	@At({"/warn/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-warn.jsp")
	public void warn(String houseNo, String testId, HttpServletRequest req) {
		req.setAttribute("testId", testId);
		req.setAttribute("houseNo", houseNo);
		if(Strings.isNotBlank(houseNo) && !"0".equals(houseNo)){
			req.setAttribute("r", CheckPest.lastPests.get(houseNo));
		} else {
			req.setAttribute("r", queryService.getPestById(testId));
		}
    }
	
	@At({"/warn/detail/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-warn-detail.jsp")
	public void warnDetail(String houseNo, String testId, int type, HttpServletRequest req) {
		req.setAttribute("html", queryService.getPestWarnDetail(houseNo, testId, type));
    }
	
	@At({"/sheet/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/pest-sheet.jsp")
	public void sheetDetail(String houseNo, String testId, HttpServletRequest req) {
		if(Strings.isNotBlank(houseNo) && !"0".equals(houseNo)){
			req.setAttribute("datas", JsonUtil.toJson(HouseUtil.get(houseNo, TypeHouseConf.PPS.code())));
			req.setAttribute("reqs", JsonUtil.toJson(CheckPest.lastPests.get(houseNo)));
			req.setAttribute("layers", HouseUtil.get(houseNo, TypeHouseConf.PEST.code(), PestInfo.class).getLayers());
		} else {
			TestPest testPest = queryService.fetch(TestPest.class, testId, false);
			if(testPest != null){
				req.setAttribute("datas", JsonUtil.toJson(HouseUtil.get(testPest.getHouseNo(), TypeHouseConf.PPS.code())));
				PestResults res = new PestResults();
				res.setPest(testPest);
				req.setAttribute("reqs", JsonUtil.toJson(res));
				req.setAttribute("layers", HouseUtil.get(testPest.getHouseNo(), TypeHouseConf.PEST.code(), PestInfo.class).getLayers());
			}
		}
    }
	

	@At({"/curve/start/?"})
	public Object weekFlag(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.updateStatus("pest", "curveFlag", id, TypeFlag.YES.val());
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
			queryService.updateStatus("pest", "curveFlag", id, TypeFlag.NO.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("取消标记曲线数据失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
}
