package cn.com.bjggs.basis.action;

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

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.enums.TypeEquip;
import cn.com.bjggs.basis.service.IBasisService;
import cn.com.bjggs.basis.util.EquipUtil;
import cn.com.bjggs.basis.view.ViewHouseEquip;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/basis/equip"})
public class EquipAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IBasisService basisService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/basis/equip-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewHouseEquip> page = getPage("page.", "houseNo", TypeOrder.ASC);	//获取前端传来的以page开头的分页信息,并以houseNo升序排序存放进page中
		Map<String, String[]> params = getParams4Admin();	//封装管理后台查询参数(ft_查询参量_类型_变量)
		page = basisService.findPage(ViewHouseEquip.class, page, params);	//执行分页查询,参数(执行的实体类,分页条件,前台传来的条件)
		req.setAttribute("page", page);
	}
	
	@At({"/dlist/?"})
	@Ok("jsp:/WEB-INF/admin/basis/equip-dlist.jsp")
	public void dlist(String houseNo, HttpServletRequest req){
		Page<Equipment> page = getPage("page.", "houseNo", TypeOrder.ASC);	//获取前端传来的以page开头的分页信息,并以houseNo升序排序存放进page中
		Map<String, String[]> params = getParams4Admin();	//封装管理后台查询参数(ft_查询参量_类型_变量)
		params.put("ft_EQ_S_houseNo", new String[]{houseNo});
		page = basisService.findPage(Equipment.class, page, params);	//执行分页查询,参数(执行的实体类,分页条件,前台传来的条件)
		req.setAttribute("page", page);
		req.setAttribute("houseNo", houseNo);
	}
	
	@At({"/edit/?/?"})
	@Ok("jsp:/WEB-INF/admin/basis/equip-edit.jsp")
	public void edit(String id, String houseNo, HttpServletRequest req){
		Equipment equip = basisService.fetch(Equipment.class, id, true);
		if(Strings.isBlank(equip.getHouseNo())){
			equip.setHouseNo(houseNo);
		}
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo).andEquals("Type", TypeEquip.NHL.val());
		List<Equipment> list = basisService.query(Equipment.class, cri);
		req.setAttribute("q", equip);
		req.setAttribute("list", list);
	}
	
	@At({"/ips/edit/?"})
	@Ok("jsp:/WEB-INF/admin/basis/equip-ips.jsp")
	public void ipsEdit(String houseNo, HttpServletRequest req){
		EquipIps equip = basisService.fetch(EquipIps.class, Cnd.where("houseNo", "=", houseNo), true);
		if(Strings.isBlank(equip.getHouseNo())){
			equip.setHouseNo(houseNo);
		}
		req.setAttribute("q", equip);
	}
	
	@At({"/ips/save"})
	public Object ipsSave(@Param("::q")EquipIps q, String tid){
		try {
			basisService.update(q);
			EquipUtil.refresh(q);
			return DwzUtil.reloadTabAndCloseCurrent("模式配置保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("模式配置保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("模式配置保存失败: " + e.getMessage());
		}
	}
	
	@At({"/save"})
	public Object save(@Param("::q")Equipment q, String tid){
		try {
			Criteria cri = Cnd.cri();
			cri.where().and("equipName", "=", q.getEquipName()).and("houseNo", "=", q.getHouseNo()).and("type", "=", q.getType());
			if (basisService.checkUnique(Equipment.class, cri, "id", q.getId())) {
				throw new RuntimeException("设备名称已存在！");
			}
			//实现修改,不存在新增,存在修改
			basisService.updateInitNo(q);
			return DwzUtil.reloadTabAndCloseCurrent("设备保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("设备保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("设备保存失败: " + e.getMessage());
		}
	}
	
	@At({"/del/?"})
	public Object del(String id, String tid){
		try {
			int num = basisService.delete(Equipment.class, new String[]{id});
			return DwzUtil.reloadCurrPage("成功删除" + num + "条数据!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("设备信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("设备信息删除失败: "+ e.getMessage());
		}
	}
	
	@At({"/del"})
	public Object deleteAll(HttpServletRequest req, String[] uids, String tid){
		try {
			if (Lang.isEmptyArray(uids)) {
				throw new RuntimeException("请至少选择一个仓");
			}
			int num = basisService.delete(Equipment.class, uids);
			return DwzUtil.reloadCurrPage("成功删除" + num + "条数据", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("设备信息删除失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("设备信息删除失败: "+ e.getMessage());
		}
	}
	
	@At({"/rank/?"})
	@Ok("jsp:/WEB-INF/admin/basis/equip-rank.jsp")
	public void rank(String houseNo, HttpServletRequest req){
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("equipNo");
		cri.where().and("houseNo", "=", houseNo);
		List<Equipment> list = basisService.query(Equipment.class, cri);
		req.setAttribute("list", list);
		req.setAttribute("houseNo", houseNo);
	}
	
	@At({"/rank/save"})
	public Object save(int[] xaxis, int[] yaxis, String houseNo, String tid){
		try {
			//实现修改,不存在新增,存在修改
			basisService.saveRank(xaxis, yaxis, houseNo);
			return DwzUtil.reloadTabAndCloseCurrent("排布保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("拍不保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("排布保存失败: " + e.getMessage());
		}
	}
	
	@At({"/sel/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/basis/equip-sels.jsp")
	public void sel(String houseNo, int equipNo, int type, HttpServletRequest req){
		List<Equipment> list = basisService.query(Equipment.class, Cnd.where("houseNo", "=", houseNo));
		req.setAttribute("list", list);
		req.setAttribute("equipNo", equipNo);
		req.setAttribute("type", type);
	}
}
