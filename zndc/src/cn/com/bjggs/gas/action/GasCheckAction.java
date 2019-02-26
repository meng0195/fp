package cn.com.bjggs.gas.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.service.IGasService;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/check/gas"})
public class GasCheckAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IGasService gasService;
	
	@At({"/main"})
	@Ok("jsp:/WEB-INF/admin/gas/gas-check-main.jsp")
	public void list(HttpServletRequest req){
		req.setAttribute("house", gasService.getMains(getSessionUser()));
	}
	
	@At({"/conf"})
	@Ok("jsp:/WEB-INF/admin/gas/gas-check-conf.jsp")
	public void conf(HttpServletRequest req) {
		req.setAttribute("r", gasService.getGasConf(getSessionUser()));
	}
	
	@At({"/save/conf"})
	public Object saveConf(int maxNum, String[] houseNo, String tid) {
		try {
			//TODO 正在检测的仓房不能修改设置
			gasService.saveConf(getSessionUser(), houseNo);
			return DwzUtil.reloadTabAndCloseCurrent("气体检测配置保存成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("气体检测配置保存失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("气体检测配置保存失败: " + e.getMessage());
		}
	}

	@At({"/start"})
	public Object start(int[] ways, String houseNo, String tid, HttpServletRequest req){
		try {
			//TODO 正在检测的仓房不能修改设置
			gasService.start(ways, houseNo);
			return DwzUtil.reloadTabAndCloseCurrent("气体检测开启成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("气体检测开启失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("气体检测开启失败: " + e.getMessage());
		}
	}
	
	@At({"/starts"})
	public Object starts(String tid){
		try {
			gasService.starts(getSessionUid());
			return DwzUtil.reloadCurrPage("气体检测开启成功！", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("气体检测开启失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("气体检测开启失败: " + e.getMessage());
		}
	}
	
	@At({"/stops"})
	public Object stops(String tid){
		try {
			gasService.stops();
			return DwzUtil.reloadCurrPage("气体检测开启成功！", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("气体检测开启失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("气体检测开启失败: " + e.getMessage());
		}
	}
	
	
	
	
	@At({"/detail/?"})
	@Ok("jsp:/WEB-INF/admin/gas/gas-check-detail.jsp")
	public void checkDetail(String houseNo, HttpServletRequest req) {
		GasInfo gi = null;
		gi = HouseUtil.get(houseNo, TypeHouseConf.GAS.code(), GasInfo.class);
		if(GasUtil.lastChecks.get(houseNo) != null){
			req.setAttribute("gset", GasUtil.lastChecks.get(houseNo).getGas());
			req.setAttribute("gas", GasUtil.lastChecks.get(houseNo));
		}
		if(gi != null){
			req.setAttribute("wayNumb", gi.getWayNumb());
			req.setAttribute("wayXaxis", gi.getWayXaxis() == null ? new String[]{} : gi.getWayXaxis().split(","));
			req.setAttribute("wayYaxis", gi.getWayYaxis() == null ? new String[]{} : gi.getWayYaxis().split(","));
		}
    }
	
	
	
	
}
