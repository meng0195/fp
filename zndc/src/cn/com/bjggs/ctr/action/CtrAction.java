package cn.com.bjggs.ctr.action;

import java.util.LinkedHashMap;
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
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.ctr.domain.PlanCtr;
import cn.com.bjggs.ctr.domain.PlanEquip;
import cn.com.bjggs.ctr.domain.SmartConf;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.ctr.service.ICtrService;
import cn.com.bjggs.ctr.util.CtrJob;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.ctr.util.SmartUtil;
import cn.com.bjggs.ctr.view.ViewCtrTask;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/control"})
public class CtrAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ICtrService ctrService;
	
	@At({"/main"})
	@Ok("jsp:/WEB-INF/admin/ctr/ctr-main.jsp")
	public void main(HttpServletRequest req){
		req.setAttribute("last", JsonUtil.toJson(CtrUtil.lasts));
	}
	
	@At({"/smart/list"})
	@Ok("jsp:/WEB-INF/admin/ctr/smart-list.jsp")
	public void smartList(String houseNo, int grainCode, HttpServletRequest req){
		Page<SmartConf> page = getPage("page.", "houseNo", TypeOrder.ASC);
		page.getOrderBy().put("modelType", TypeOrder.ASC);
		page = ctrService.findPage(SmartConf.class, page, getParams4Admin());
		req.setAttribute("page", page);
	}
	
	@At({"/smart/edit/?"})
	@Ok("jsp:/WEB-INF/admin/ctr/smart-edit.jsp")
	public void smartEdit(String id, HttpServletRequest req){
		req.setAttribute("s", ctrService.fetch(SmartConf.class, id, true));
	}

	@At({"/smart/equip/?"})
	@Ok("jsp:/WEB-INF/admin/ctr/smart-equip.jsp")
	public void smartEquip(String id, HttpServletRequest req){
		SmartConf s = ctrService.fetch(SmartConf.class, id, true);
		List<Equipment> eps = ctrService.query(Equipment.class, Cnd.where("houseNo", "=", s.getHouseNo()));
		if(s != null && Strings.isNotBlank(s.getEquips())){
			String[] es =  s.getEquips().split(",");
			for(String ep1 : es){
				for(Equipment equip : eps){
					if(equip.getEquipNo() == ParseUtil.toInt(ep1, 0)){
						equip.setStatus((byte)1);
					}
				}
			}
		}
		req.setAttribute("eps", eps);
	}
	
	@At({"/smart/save"})
	public Object save(@Param("::s")SmartConf s, String tid){
		try {
			ctrService.smartSave(s);
			return DwzUtil.reloadTabAndCloseCurrent("操作成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("操作失败: " + e.getMessage());
		}
	}
	
	@At({"/house/?"})
	@Ok("jsp:/WEB-INF/admin/ctr/ctr-house.jsp")
	public void house(String houseNo, HttpServletRequest req){
		if(CtrUtil.lasts.get(houseNo) != null){
			req.setAttribute("maps", CtrUtil.lasts.get(houseNo).getEquips());
		}
		req.setAttribute("houseNo", houseNo);
	}
	
	@At({"/doall/?/?"})
	public Object doall(String houseNo, int equipNo, String tid){
		try {
			if(SmartUtil.models.get(houseNo) != TypeCtrTag.SD.val()){
				throw new RuntimeException("当前仓房处于" + Enums.get("TYPE_CTR_TAG", "" + SmartUtil.models.get(houseNo)) + ", 请停止模式下相关计划再执行手动操作！");
			}
			ctrService.doall(houseNo, equipNo, getSessionUser());
			return DwzUtil.nothing("操作成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("操作失败: " + e.getMessage());
		}
	}
		
	@At({"/reload/?"})
	@Ok("redirect:/control/house/${a.houseNo}")
	public void reload(String houseNo, String tid, HttpServletRequest req){
		try {
			CtrUtil.refreshEquipStatus(houseNo);
			req.setAttribute("houseNo", houseNo);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
		}
	}
	
	@At({"/clear/?"})
	@Ok("redirect:/control/house/${a.houseNo}")
	public void clear(String houseNo, String tid, HttpServletRequest req){
		try {
			CtrUtil.clear(houseNo);
			req.setAttribute("houseNo", houseNo);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
		}
	}
	
	
	@At({"/plan/list"})
	@Ok("jsp:/WEB-INF/admin/ctr/plan-list.jsp")
	public void planList(HttpServletRequest req){
		Page<PlanCtr> page = getPage("page.", "houseNo", TypeOrder.ASC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_EQ_S_userCode", new String[]{getSessionUid()});
		page = ctrService.findPage(PlanCtr.class, page, params);
		req.setAttribute("page", page);
	}
	
	@At({"/plan/edit/?"})
	@Ok("jsp:/WEB-INF/admin/ctr/plan-edit.jsp")
	public void planEdit(String id, HttpServletRequest req){
		PlanCtr p = ctrService.fetch(PlanCtr.class, id, true);
		if(Strings.isNotBlank(p.getPlanCode())){
			List<PlanEquip> list1 = ctrService.query(PlanEquip.class, Cnd.where("planCode", "=", p.getPlanCode()));
			List<Equipment> list = ctrService.query(Equipment.class, Cnd.where("houseNo", "=", p.getHouseNo()));
			for(Equipment ep : list){
				for(PlanEquip pe :list1){
					if(ep.getEquipNo() == pe.getEquipNo()){
						ep.setSel(TypeFlag.YES.val());
						break;
					}
				}
			}
			req.setAttribute("es", list);
		}
		req.setAttribute("p", p);
	}
	
	@At({"/plan/save"})
	public Object savePlan(@Param("::p")PlanCtr p, int[] equipNo, String tid){
		try {
			if(Strings.isBlank(p.getUserCode())){
				p.setUserCode(getSessionUid());
			}
			if(Lang.isEmpty(equipNo)){
				throw new RuntimeException("请选择至少一个设备！");
			}
			ctrService.savePlan(p, equipNo);
			return DwzUtil.reloadTabAndCloseCurrent("排布保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("拍不保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("排布保存失败: " + e.getMessage());
		}
	}
	
	@At({"/del/?"})
	public Object del(String planCode, String tid, HttpServletRequest req) {
		try {
			PlanCtr p = ctrService.fetch(PlanCtr.class, "planCode", planCode);
			if (p.getStatus() == 0) {
				ctrService.delete(PlanCtr.class, "planCode", planCode);
				//TODO 将删除方法转移至 service 同步删除计划列表
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
			PlanCtr p = ctrService.fetch(PlanCtr.class, id, false);
			if(null != p){
				if(SmartUtil.models.get(p.getHouseNo()) == TypeCtrTag.ZN.val()) throw new RuntimeException("当前仓房处于智能模式状态，不能开启计划，请关闭所有智能模式后尝试开启！");
				QuartzUtil.addJob("c" + p.getPlanCode(), CtrJob.class, p.getTimeCron());
				ctrService.updateStatus(p.getPlanCode(), TypeFlag.YES.val());
				SmartUtil.changePlans(p.getHouseNo(), 1);
				SmartUtil.changeModel(p.getHouseNo(), TypeCtrTag.DS.val());
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
	@At({"/stop/?/?"})
	public Object stop(String planCode, String houseNo, String tid) {
		try {
			QuartzUtil.removeJob("c" + planCode);
			ctrService.updateStatus(planCode, TypeFlag.NO.val());
			//更新该仓房的计划状态
			SmartUtil.changePlans(houseNo, -1);
			if(SmartUtil.plans.get(houseNo) == 0){
				SmartUtil.changeModel(houseNo, TypeCtrTag.SD.val());
			}
			return DwzUtil.reloadCurrPage("关闭定时任务成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("关闭定时任务失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("关闭定时任务失败: " + e.getMessage());
		}
	}
	
	@At({"/plan/equip/?"})
	public Object planEquip(String houseNo, String tid){
		try {
			Map<String, Object> req = new LinkedHashMap<String, Object>();
			List<Equipment> list = new LinkedList<Equipment>();
			Criteria cri = Cnd.cri();
			cri.where().andEquals("houseNo", houseNo);
			cri.getOrderBy().asc("type");
			cri.getOrderBy().asc("equipNo");
			if(Strings.isNotBlank(houseNo)){
				list = ctrService.query(Equipment.class, cri);
			}
			req.put("statusCode", DwzUtil.SUCCESS);
			req.put("message", "设备获取成功！");
			req.put("body", list);
			return req;
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("获取设备失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("获取设备失败: " + e.getMessage());
		}
	}
	
	@At({"/plan/exes/?/?"})
	@Ok("jsp:/WEB-INF/admin/ctr/plan-exes.jsp")
	public void planExes(String modelType, String taskCode, HttpServletRequest req){
		Page<ViewCtrTask> page = getPage("page.", "times", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_EQ_I_modelType", new String[]{modelType});
		params.put("ft_EQ_S_taskCode", new String[]{taskCode});
		page = ctrService.findPage(ViewCtrTask.class, page, params);
		req.setAttribute("page", page);
		req.setAttribute("modelType", modelType);
		req.setAttribute("taskCode", taskCode);
	}
	
	@At({"/conf/add"})
	public Object confAdd(String houseNo, String tid){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房后再执行初始化操作！");
			}
			ctrService.addConfs(houseNo);
			return DwzUtil.reloadCurrPage("初始化智能模式成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("初始化智能模式失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("初始化智能模式失败：" + e.getMessage());
		}
	}
}
