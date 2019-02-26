package cn.com.bjggs.squery.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.squery.domain.QueryTTwo;
import cn.com.bjggs.squery.domain.QueryTemp;
import cn.com.bjggs.squery.service.IQueryService;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.ChecksUtil;
import cn.com.bjggs.warns.domain.AlarmNotes;

@IocBean
@At({"/squery/temp"})
public class TempQueryAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	private final boolean changeTag = PropsUtil.getBoolean("temp.change.tag");
	
	@Inject
	private IQueryService queryService;
	
	@At({"/warn/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-warn.jsp")
	public void showWarn(String houseNo, String testDataId, HttpServletRequest req) {
		TempResults res = new TempResults();
		if (Strings.isNotBlank(testDataId) && "0".equals(houseNo)) {//根据id查
			TestData td = queryService.fetch(TestData.class, Cnd.where("ID", "=", testDataId));
			List<AlarmNotes> ans = queryService.query(AlarmNotes.class, Cnd.where("testCode", "=", td.getId()));
			res.setDatas(td);
			res.setAns(ans);
			req.setAttribute("houseNo", houseNo);
			req.setAttribute("id", testDataId);
			req.setAttribute("res", res);
		} else {//根据仓房编号查最后一条记录
			res = ChecksUtil.lastChecks.get(houseNo);
			req.setAttribute("houseNo", houseNo);
			req.setAttribute("id", testDataId);
			req.setAttribute("res", res);
		}
	}
	
	@At({"/sheet/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-sheet.jsp")
	public void showSheet(String houseNo, String testDataId, HttpServletRequest req) {
		Record r;
		if(!Strings.isBlank(testDataId) && !Strings.equals("0", testDataId)){
			r = queryService.findSheetByTsId(testDataId);
			req.setAttribute("m", r);
		} else {
			r = queryService.findSheetByHouseNo(houseNo);
			req.setAttribute("m", r);
		}
	}
	
	@At({"/3d/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-3d.jsp")
	public void show3d(String houseNo, String testDataId, HttpServletRequest req) {
		req.setAttribute("datas", queryService.getJson3d(houseNo, testDataId));
	}
	
	@At({"/stereo/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-stereo.jsp")
	public void showStereo(String houseNo, String testDataId, HttpServletRequest req) {
		StoreHouse house = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		if(!Strings.isBlank(testDataId) && !Strings.equals("0", testDataId)){
			String[] ss = queryService.findStereoByTsId(testDataId);
			req.setAttribute("html", ss[0]);
			req.setAttribute("sTag", ss[1]); 
			req.setAttribute("tabTag", true);
			req.setAttribute("back", ss[2]);
		} else {
			String[] ss = queryService.findStereoByHouseNo(houseNo);
			System.out.println(ss[0]);
			req.setAttribute("html", ss[0]);
			req.setAttribute("back", ss[1]);
			req.setAttribute("sTag", house == null ? "" : "type" + house.getHouseType());
		}
	}
	
	@At({"/show/detail/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/squery/check-warn-detail.jsp")
	public void showDetail(String houseNo, String testDataId, int key, HttpServletRequest req) {
		if (!Strings.isEmpty(houseNo) && !"0".equals(houseNo)) {
			req.setAttribute("r", queryService.getWarnDetail(houseNo, key));
		} else {
			req.setAttribute("r", queryService.getWarnDetailId(testDataId, key));
		}
    }
	
	@At({"/curve"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-curve.jsp")
	public void curve(@Param("::q")QueryTemp q, HttpServletRequest req) {
		if(q == null) q = new QueryTemp();
		String json = queryService.getCurve(q);
		req.setAttribute("json", json);
		req.setAttribute("q", q);
    }
	
	@At({"/history"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-history.jsp")
	public void list(String date, HttpServletRequest req) {
		Page<TestData> page = getPage("page.", "testDate", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		if (date != null && !"".equals(date)) {
			params.put("ft_GE_DT_testDate", new String[]{date+" 00:00:00"});
			params.put("ft_LE_DT_testDate", new String[]{date+" 23:59:59"});
		}
		page = queryService.findPage(TestData.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("date", date);
		req.setAttribute("changeTag", changeTag);
	}
	
	
	@At({"/cps1h2ts"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-cps1h2ts.jsp")
	public void cps1h2ts(@Param("::t1")QueryTemp t1, @Param("::t2")QueryTemp t2, HttpServletRequest req) {
		if(t1 == null) t1 = new QueryTemp();
		if(t2 == null) t2 = new QueryTemp();
		t2.setHouseNo(t1.getHouseNo());
		String jsonL = queryService.getCps(t1);
		String jsonR = queryService.getCps(t2);
		req.setAttribute("t1", t1);
		req.setAttribute("t2", t2);
		req.setAttribute("jsonL", jsonL);
		req.setAttribute("jsonR", jsonR);
    }
	
	@At({"/cps2h1ts"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-cps2h1ts.jsp")
	public void cps2h1ts(@Param("::t1")QueryTemp t1, @Param("::t2")QueryTemp t2, HttpServletRequest req) {
		if(t1 == null) t1 = new QueryTemp();
		if(t2 == null) t2 = new QueryTemp();
		t2.setStartTime(t1.getStartTime());
		t2.setEndTime(t1.getEndTime());
		String jsonL = queryService.getCps(t1);
		String jsonR = queryService.getCps(t2);
		req.setAttribute("t1", t1);
		req.setAttribute("t2", t2);
		req.setAttribute("jsonL", jsonL);
		req.setAttribute("jsonR", jsonR);
    }
	
	/**
	 * 加载出上面的查询条件
	 */
	@At({"/cps1h2t"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-cps1h2t.jsp")
	public void cps1h2t(HttpServletRequest req) {
    }
	
	/**
	 * 加载出下面的比较结果
	 * @param dates
	 * @param req
	 */
	@At({"/cpsList"})
	@Ok("jsp:/WEB-INF/admin/squery/temp-cps1h2t-list.jsp")
	public void cps1h2t_list(@Param("::t")QueryTTwo t, HttpServletRequest req) {
		Criteria cri1 = Cnd.cri();
		cri1.where().andEquals("houseNo", t.getHouseNo()).andEquals("testdate", t.getTime1());
		TestData t1 = queryService.fetch(TestData.class, cri1);
		Criteria cri2 = Cnd.cri();
		cri2.where().andEquals("houseNo", t.getHouseNo()).andEquals("testdate", t.getTime2());
		TestData t2 = queryService.fetch(TestData.class, cri2);
		t.setResult1(t1);
		t.setResult2(t2);
		req.setAttribute("t", t);
    }
	
	@At({"/warn/list"})
	@Ok("jsp:/WEB-INF/admin/squery/warn-list.jsp")
	public void warnList(String id, HttpServletRequest req) {
		Page<AlarmNotes> page = getPage("page.", "testDate", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		page = queryService.findPage(AlarmNotes.class, page, params);
		req.setAttribute("page", page);
    }
	
	@At({"/getDate/?/?"})
	public String getDate(String houseNo, String date, HttpServletRequest req) {
		return queryService.getTempDate(houseNo, date);
	}
	
	@At({"/report/start/?"})
	public Object dayFlag(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.checkTempReport(id);
			queryService.updateStatus("temp", "reportFlag", id, TypeFlag.YES.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("日标记设置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
	
	@At({"/report/stop/?"})
	public Object dayClose(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.updateStatus("temp", "reportFlag", id, TypeFlag.NO.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("日标记设置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
	
	@At({"/curve/start/?"})
	public Object weekFlag(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.updateStatus("temp", "curveFlag", id, TypeFlag.YES.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("周标记设置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
	
	@At({"/curve/stop/?"})
	public Object weekClose(String id, String tid,  HttpServletRequest req) {
		try {
			queryService.updateStatus("temp", "curveFlag", id, TypeFlag.NO.val());
			return DwzUtil.reloadCurrPage("设置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("周标记设置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("设置失败: " + e.getMessage());
		}
		
	}
}
