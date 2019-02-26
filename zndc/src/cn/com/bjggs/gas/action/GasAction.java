package cn.com.bjggs.gas.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.service.IGasService;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.gas.view.ViewHouseGas;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/conf/gas"})
public class GasAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IGasService gasService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/gas/gas-conf-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewHouseGas> page = getPage("page.", "houseNo", TypeOrder.ASC);
		req.setAttribute("page", gasService.findPage(ViewHouseGas.class, page, getParams4Admin()));
	}
	
	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/gas/gas-conf-edit.jsp")
	public void edit(String houseNo, HttpServletRequest req){
		GasInfo g = gasService.fetch(GasInfo.class, "houseNo", houseNo);
		if(g == null || Strings.isBlank(g.getHouseNo())){
			g = new GasInfo();
			g.setHouseNo(houseNo);
		}
		req.setAttribute("g", g);
	}
	
	@At({"/save"})
	public Object save(@Param("::g")GasInfo g, String tid){
		try {
			if(g.getLens() != null && g.getLens().length > 0){
				byte[] bs = new byte[g.getLens().length * 2];
				for(int i = 0; i < g.getLens().length; i++){
					System.arraycopy(MathUtil.int2Bytes(g.getLens()[i]), 0, bs, i * 2, 2);
				}
				g.setWayLengths(bs);
			}
			if(g.getTimes() != null && g.getTimes().length > 0){
				byte[] bs = new byte[g.getTimes().length * 2];
				for(int i = 0; i < g.getTimes().length; i++){
					System.arraycopy(MathUtil.int2Bytes(g.getTimes()[i]), 0, bs, i * 2, 2);
				}
				g.setWayTime(bs);
			}
			//实现修改,不存在新增,存在修改
			gasService.update(g);
			GasUtil.refresh(g.getHouseNo(), g);
			return DwzUtil.reloadTabAndCloseCurrent("测气配置信息保存成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("测气配置信息保存失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("测气配置信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/rank/?"})
	@Ok("jsp:/WEB-INF/admin/gas/gas-conf-rank.jsp")
	public void rank(String houseNo, HttpServletRequest req){
		GasInfo gi = gasService.fetch(GasInfo.class, "houseNo", houseNo);
		req.setAttribute("wayNumb", gi.getWayNumb());
		req.setAttribute("wayXaxis", gi.getWayXaxis() == null ? new String[]{} : gi.getWayXaxis().split(","));
		req.setAttribute("wayYaxis", gi.getWayYaxis() == null ? new String[]{} : gi.getWayYaxis().split(","));
		req.setAttribute("houseNo", houseNo);
	}
	
	@At({"/rank/save"})
	public Object ranks(Integer[] xaxis, Integer[] yaxis, String houseNo, String tid){
		try {
			gasService.updateRanks(xaxis, yaxis, houseNo);
			GasUtil.refresh(houseNo, gasService.fetch(GasInfo.class, Cnd.where("houseNo", "=", houseNo)));
			return DwzUtil.reloadTabAndCloseCurrent("风路排布信息保存成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("风路排布信息保存失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("风路排布信息保存失败: " + e.getMessage());
		}
	}
}
