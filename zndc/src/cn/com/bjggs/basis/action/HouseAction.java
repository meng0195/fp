package cn.com.bjggs.basis.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.service.IBasisService;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.view.ViewHouseInfo;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/basis/house"})
public class HouseAction extends SysAction{
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IBasisService basisService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/basis/house-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewHouseInfo> page = getPage("page.", "houseNo", TypeOrder.ASC);	//获取前端传来的以page开头的分页信息,并以houseNo升序排序存放进page中
		Map<String, String[]> params = getParams4Admin();	//封装管理后台查询参数(ft_查询参量_类型_变量)
		page = basisService.findPage(ViewHouseInfo.class, page, params);	//执行分页查询,参数(执行的实体类,分页条件,前台传来的条件)
		req.setAttribute("page", page);
	}

	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/basis/house-edit.jsp")
	public void edit(String id,HttpServletRequest req){
		StoreHouse s = basisService.fetch(StoreHouse.class, id, true);	//根据传来的id查询StoreHouse表,并将信息封装进StoreHouse实体类中
		GrainInfo g = new GrainInfo();
		if (Strings.isNotBlank(s.getHouseNo())) {	
			g = basisService.fetch(GrainInfo.class, "houseNo", s.getHouseNo());	//如果StoreHouse的仓房编号不为空,根据仓房编号查询GrainInfo表中信息
		} else {
			s.setHouseNo(basisService.getNextHouseNo());
		}
		req.setAttribute("s", s);
		req.setAttribute("g", g);
	}
	
	@At({"/save"})
	public Object save(@Param("::s")StoreHouse s, @Param("::g")GrainInfo g, String tid){
		try {
			if (basisService.checkUnique(StoreHouse.class, "houseNo", s.getHouseNo(), s.getId())) {
				throw new RuntimeException("仓房编号已存在!");
			}
			if (basisService.checkUnique(StoreHouse.class, "houseName", s.getHouseName(), s.getId())) {
				throw new RuntimeException("仓房名称已存在!");
			}
			//实现修改,不存在新增,存在修改
			basisService.update(s, g);
			return DwzUtil.reloadTabAndCloseCurrent("仓房信息保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("仓房信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("仓房信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/detail/?"})
	@Ok("jsp:/WEB-INF/admin/basis/house-detail.jsp")
	public void detail(String id,HttpServletRequest req){
		StoreHouse s = basisService.fetch(StoreHouse.class, id, true);
		GrainInfo g = new GrainInfo();
		if (Strings.isNotBlank(s.getHouseNo())) {
			g = basisService.fetch(GrainInfo.class, "houseNo", s.getHouseNo());
		}
		req.setAttribute("s", s);
		req.setAttribute("g", g);
	}
	
	
	
	@At({"/del/?"})
	public Object del(String id, String tid){
		try {
			StoreHouse s = basisService.fetch(StoreHouse.class, id, false);
			if (Lang.isEmpty(s)) {
				throw new RuntimeException("该仓房不存在的.");
			}
			int num = basisService.deleteAll(id);
			//删除字典表中的仓房
			HouseUtil.remove(s.getHouseNo());
			return DwzUtil.reloadCurrPage("删除成功"+num+"条数据", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("仓房信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("仓房删除失败: "+ e.getMessage());
		}
	}
	
	@At({"/del"})
	public Object deleteAll(HttpServletRequest req, String[] uids, String tid){
		try {
			if (Lang.isEmptyArray(uids)) {
				throw new RuntimeException("请至少选择一个仓");
			}
			int num = basisService.deleteAll(uids);
			//重新加载字典表
			HouseUtil.reload();
			return DwzUtil.reloadCurrPage("删除成功"+num+"条数据", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("仓房信息删除失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("仓房删除失败: "+ e.getMessage());
		}
	}
}





