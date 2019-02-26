package cn.com.bjggs.basis.action;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.domain.ChecksTime;
import cn.com.bjggs.basis.domain.PlanTask;
import cn.com.bjggs.basis.service.ITaskService;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.basis.view.ViewTaskExes;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.temp.domain.TestData;

@IocBean
@At({"/basis/task"})
public class TaskAction extends SysAction{
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ITaskService taskService;
	
	/**
	 * 定时列表
	 * @param req
	 */
	@At({"/list/?"})
	@Ok("jsp:/WEB-INF/admin/task/task-list.jsp")
	public void list(String type, HttpServletRequest req) {
		Page<PlanTask> page = getPage("page.", "planCode", TypeOrder.ASC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_EQ_I_type", new String[]{type});
		params.put("ft_EQ_S_userCode", new String[]{getSessionUid()});
		page = taskService.findPage(PlanTask.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("type", type);
	}
	
	/**
	 * 定时配置
	 * @param id
	 * @param req
	 */
	@At({"/edit/?/?"})
	@Ok("jsp:/WEB-INF/admin/task/task-edit.jsp")
	public void edit(int type, String id, HttpServletRequest req) {
		PlanTask p = taskService.fetch(PlanTask.class, id, true);
		List<ChecksTime> cts;
		if(Strings.isNotBlank(p.getPlanCode())){
			cts = taskService.query(ChecksTime.class, Cnd.where("planCode", "=", p.getPlanCode()));
		} else {
			cts = new LinkedList<ChecksTime>();
		}
		if(p.getType() == 0) p.setType(type);
		String[] hs = new String[HouseUtil.houseMap.keySet().size()];
		if(Lang.isEmptyArray(getSessionUser().getHouses())){
			HouseUtil.houseMap.keySet().toArray(hs);
		} else {
			hs = getSessionUser().getHouses();
		}
	    req.setAttribute("p", p);
	    req.setAttribute("cts", HouseUtil.getChecksHtml(hs, cts));
	}
	
	/**
	 * 定时配置保存
	 */
	@At({"/save"})
	public Object save(@Param("::p.")PlanTask p, String[] houseNo, String tid) {
		try {
			
			if(taskService.checkUnique(PlanTask.class, Cnd.where("planName", "=", p.getPlanName()), p.getId())){
				throw new RuntimeException("计划名重复！");
			}
			//计划编号验重
			if(taskService.checkUnique(PlanTask.class, Cnd.where("planCode", "=", p.getPlanCode()), p.getId())){
				throw new RuntimeException("计划编号重复！");
			}
			if (houseNo == null) {
				throw new RuntimeException("请选择仓房编号！");
			}
			if (p.getTimeOne()!=1 && p.getTimeOne()!=5 && p.getTimeTwo()==null) {
				throw new RuntimeException("请选择日期！");
			}
			p.setUserCode(getSessionUid());
			taskService.timeSave(p, houseNo);
			return DwzUtil.reloadTabAndCloseCurrent("计划任务配置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("计划任务配置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("定时检测配置失败: " + e.getMessage());
		}
	}
	
	/**
	 * 显示定时任务的仓
	 */
	@At({"/houses/?"})
	@Ok("jsp:/WEB-INF/admin/task/task-houses.jsp")
	public void detail(String planCode, String tid, HttpServletRequest req) {
		List<ChecksTime> list = taskService.query(ChecksTime.class, Cnd.where("planCode", "=", planCode));
		req.setAttribute("list", list);
	}
	
	@At({"/exes/?/?"})
	@Ok("jsp:/WEB-INF/admin/task/task-exes.jsp")
	public void exes(String planCode, String planType, String tid, HttpServletRequest req) {
		Page<ViewTaskExes> page = getPage("page.", "times", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_EQ_S_planCode", new String[]{planCode});
		try {
			if(params.containsKey("ft_GE_D_times")){
				long begin = (Times.parse("yyyy-MM-dd HH:mm:ss", params.get("ft_GE_D_times")[0])).getTime();
				params.remove("ft_GE_D_times");
				params.put("ft_GE_N_times", new String[]{String.valueOf(begin)});
			}
			if(params.containsKey("ft_LE_D_times")){
				long begin = (Times.parse("yyyy-MM-dd HH:mm:ss", params.get("ft_LE_D_times")[0])).getTime();
				params.remove("ft_LE_D_times");
				params.put("ft_LE_N_times", new String[]{String.valueOf(begin)});
			}
		} catch (ParseException e) {
			log.error("参数修改异常！");
		}
		page = taskService.findPage(ViewTaskExes.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("planCode", planCode);
		req.setAttribute("planType", planType);
	}
	
	@At({"/exes/detail/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/task/task-exes-main.jsp")
	public void exesDetail(String planCode, long time, String planType, HttpServletRequest req) {
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("houseNo");
		if ("1".equals(planType)) {//温度计划
			cri.where().and("planCode", "=", planCode).and("testDate", "=", new Date(time));
			List<TestData> tds = taskService.query(TestData.class, cri);
			req.setAttribute("planType", planType);
			req.setAttribute("tds", tds);
		} else if ("2".equals(planType)) {//测虫计划
			cri.where().and("planCode", "=", planCode).and("startTime", "=", new Date(time));
			List<TestPest> tps = taskService.query(TestPest.class, cri);
			req.setAttribute("planType", planType);
			req.setAttribute("tps", tps);
		} else if ("4".equals(planType)) {//测气计划
			cri.where().and("planCode", "=", planCode).and("startTime", "=", new Date(time));
			List<TestGas> tgs = taskService.query(TestGas.class, cri);
			req.setAttribute("planType", planType);
			req.setAttribute("tgs", tgs);
		}
	}
	
	
	@At({"/del/?"})
	public Object del(String planCode, String tid, HttpServletRequest req) {
		try {
			PlanTask p = taskService.fetch(PlanTask.class, "planCode", planCode);
			if (p.getStatus() == 0) {
				taskService.delete(PlanTask.class, "planCode", planCode);
				return DwzUtil.reloadCurrPage("计划删除成功!", tid);
			} else {
				throw new RuntimeException("当前计划正在执行,请关闭任务后再执行删除!");
			}
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("计划删除失败!", e.getMessage());
			}
			return DwzUtil.stopPageError("计划删除失败: " + e.getMessage());
		}
	}
	
	/**
	 * 启动单个定时器,并修改状态
	 */
	@At({"/start/?"})
	public Object start(String id, String tid) {
		try {
			PlanTask p = taskService.fetch(PlanTask.class, id, false);
			if(null != p){
				QuartzUtil.addJob(p);
				taskService.updateStatus(p.getPlanCode(), TypeFlag.YES.val());
			}
			return DwzUtil.reloadCurrPage("开启定时任务成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("开启定时任务失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("开启定时任务失败: " + e.getMessage());
		}
	}
	
	/**
	 * 关闭单个定时器,并修改状态
	 */
	@At({"/stop/?"})
	public Object stop(String planCode, String tid) {
		try {
			QuartzUtil.removeJob(planCode);
			taskService.updateStatus(planCode, TypeFlag.NO.val());
			return DwzUtil.reloadCurrPage("关闭定时任务成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("关闭定时任务失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("关闭定时任务失败: " + e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@At({"/starts/?"})
	public Object starts(int type, String tid) {
		try {
			taskService.starts(type);
			return DwzUtil.reloadCurrPage("定时任务已经全部开启！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("开启定时任务失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("开启定时任务失败: " + e.getMessage());
		}
	}
	
	@At({"/stops/?"})
	public Object stops(int type, String tid) {
		try {
			taskService.stops(type);
			return DwzUtil.reloadCurrPage("定时任务已经全部关闭！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("关闭定时任务失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("关闭定时任务失败: " + e.getMessage());
		}
	}
}
