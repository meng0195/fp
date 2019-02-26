package cn.com.bjggs.pest.action;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.pest.domain.CheckPoints;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.service.IPestService;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/check/pest"})
public class PestCheckAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IPestService pestService;
	
	@At({"/main"})
	@Ok("jsp:/WEB-INF/admin/pest/pest-check-main.jsp")
	public void list(HttpServletRequest req){
		req.setAttribute("cs", JsonUtil.toJson(CheckPest.checks.keySet()));
		Set<String> warns = new LinkedHashSet<String>();
		for(Map.Entry<String, PestResults> entry : CheckPest.lastPests.entrySet()){
			if(entry.getValue() != null && entry.getValue().getPest() != null && entry.getValue().getPest().getTestTag() == TypeFlag.YES.val()){
				warns.add(entry.getKey());
			}
		}
		req.setAttribute("warns", JsonUtil.toJson(warns));
	}

	@At({"/detail/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/pest/pest-check-detail.jsp")
	public void detail(String houseNo, String ptId, String type, HttpServletRequest req){
		req.setAttribute("datas", JsonUtil.toJson(HouseUtil.get(houseNo, TypeHouseConf.PPS.code())));
		if(Strings.equals(type, "view")){
			req.setAttribute("reqs", JsonUtil.toJson(CheckPest.lastPests.get(houseNo)));
		}
		req.setAttribute("houseNo", houseNo);
		req.setAttribute("type", type);
		req.setAttribute("layers", HouseUtil.get(houseNo, TypeHouseConf.PEST.code(), PestInfo.class).getLayers());
	}
	
	@At({"/point/begin"})
	public Object save(@Param("::p")CheckPoints cps, String tid){
		try {
			//实现修改,不存在新增,存在修改
			pestService.checkPoints(cps);
			return DwzUtil.reloadTabAndCloseCurrent("检测开启成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("检测开启失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("检测开启失败: " + e.getMessage());
		}
	}
	
	@At({"/house/begin"})
	public Object houseBegin(String[] houses, String tid){
		try {
			pestService.checkHouses(houses);
			return DwzUtil.reloadTabAndCloseCurrent("检测开启成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("检测开启失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("检测开启失败: " + e.getMessage());
		}
	}
	
	@At({"/to/houses"})
	@Ok("jsp:/WEB-INF/admin/pest/pest-check-houses.jsp")
	public void toHouses(HttpServletRequest req){
	}
}
