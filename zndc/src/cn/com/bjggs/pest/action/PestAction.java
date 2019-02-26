package cn.com.bjggs.pest.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.pest.domain.GateInfos;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestPoint;
import cn.com.bjggs.pest.domain.PestPoints;
import cn.com.bjggs.pest.service.IPestService;
import cn.com.bjggs.pest.view.ViewHousePest;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/conf/pest"})
public class PestAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IPestService pestService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/pest/pest-conf-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewHousePest> page = getPage("page.", "houseNo", TypeOrder.ASC);
		req.setAttribute("page", pestService.findPage(ViewHousePest.class, page, getParams4Admin()));
	}
	
	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/pest/pest-conf-edit.jsp")
	public void edit(String houseNo, HttpServletRequest req){
		PestInfo p = pestService.fetch(PestInfo.class, "houseNo", houseNo);
		if(p == null){
			p = new PestInfo();
			p.setHouseNo(houseNo);
		}
//		Criteria cri = Cnd.cri();
//		cri.where().andEquals("houseNo", houseNo);
//		cri.getOrderBy().asc("GateNo");
//		List<GateInfo> list = pestService.query(GateInfo.class, cri);
		req.setAttribute("p", p);
//		req.setAttribute("list", list);
//		req.setAttribute("size", list.size());
	}
	
	@At({"/save"})
	public Object save(@Param("::p")PestInfo p, @Param("::g")GateInfos gis, String tid){
		try {
			//实现修改,不存在新增,存在修改
			pestService.updateConf(p, gis);
			return DwzUtil.reloadTabAndCloseCurrent("测虫配置信息保存成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("测虫配置信息保存失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("测虫配置信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/point/?"})
	@Ok("jsp:/WEB-INF/admin/pest/pest-conf-point.jsp")
	public void point(String houseNo, HttpServletRequest req){
		PestInfo pi = HouseUtil.get(houseNo, TypeHouseConf.PEST.code(), PestInfo.class);
		req.setAttribute("layers", pi == null ? 3 : pi.getLayers());
		req.setAttribute("houseNo", houseNo);
		req.setAttribute("datas", JsonUtil.toJson(pestService.query(PestPoint.class, Cnd.where("houseNo", "=", houseNo))));
	}
	
	@At({"/point/save"})
	public Object pointSave(@Param("::p")PestPoints pis, String tid){
		try {
			//实现修改,不存在新增,存在修改
			pestService.updatePoint(pis);
			return DwzUtil.reloadTabAndCloseCurrent("检测点排布信息保存成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("检测点排布信息保存失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("检测点排布信息保存失败: " + e.getMessage());
		}
	}
}
