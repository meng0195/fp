package cn.com.bjggs.power.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.power.domain.PowerInfo;
import cn.com.bjggs.power.service.IPowerService;
import cn.com.bjggs.power.util.PowerUtil;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/power"})
public class PowerAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IPowerService powerService;
	
	@At({"/conf/list"})
	@Ok("jsp:/WEB-INF/admin/power/power-conf-list.jsp")
	public void list(HttpServletRequest req){
		Page<PowerInfo> page = getPage("page.", "houseNo", TypeOrder.ASC);
		Map<String,String[]> param = getParams4Admin();
		page = powerService.findPage(PowerInfo.class, page, param);
		req.setAttribute("page", page);
	}
	
	@At({"/conf/edit/?"})
	@Ok("jsp:/WEB-INF/admin/power/power-conf-edit.jsp")
	public void edit(String id, HttpServletRequest req){
		PowerInfo p = powerService.fetch(PowerInfo.class, "ID", id);
		req.setAttribute("p", p);
	}
	
	@At({"/conf/save"})
	public Object save(@Param("::p")PowerInfo p, String tid){
		try {
			powerService.update(p);
			PowerUtil.refresh(p);
			return DwzUtil.reloadTabAndCloseCurrent("电表信息保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("电表信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("电表信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/conf/detail/?"})
	@Ok("jsp:/WEB-INF/admin/power/power-conf-detail.jsp")
	public void detail(String id, HttpServletRequest req){
		PowerInfo p = powerService.fetch(PowerInfo.class, "ID", id);
		req.setAttribute("p", p);
	}
	
	@At({"/conf/del/?"})
	public Object del(String id, String tid){
		try {
			powerService.delete(PowerInfo.class, Cnd.where("ID", "=", id));
			return DwzUtil.reloadCurrPage("成功删除!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("删除失败: "+ e.getMessage());
		}
	}
}
