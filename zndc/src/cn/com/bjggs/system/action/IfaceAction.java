package cn.com.bjggs.system.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;

import cn.com.bjggs.basis.domain.CopyGrainInfo;
import cn.com.bjggs.basis.domain.CopyStoreHouse;
import cn.com.bjggs.basis.service.IBasisService;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.util.CardUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.core.util.StringUtil;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.ctr.service.ICtrService;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.ctr.util.SmartUtil;
import cn.com.bjggs.gas.util.GasOne;
import cn.com.bjggs.system.domain.IfaceAll;
import cn.com.bjggs.system.domain.IfaceEqsInfo;
import cn.com.bjggs.system.domain.IfaceEquip;
import cn.com.bjggs.system.domain.IfaceEquips;
import cn.com.bjggs.system.domain.IfaceGas;
import cn.com.bjggs.system.domain.IfaceTemp;
import cn.com.bjggs.system.domain.SysHouses;
import cn.com.bjggs.system.domain.SysRole;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.system.service.ISystemService;
import cn.com.bjggs.system.util.IfaceUtil;
import cn.com.bjggs.system.util.PassUtil;
import cn.com.bjggs.temp.util.TempOne;

/**
 * 后台登录监听action
 * @author	wc
 * @date	2017年4月23日 
 */
@IocBean
@At({"/iface"})
public class IfaceAction extends SysAction {

	private final Log log = Logs.getLog(IfaceAction.class);
	
	@Inject
	private ISystemService systemService;
	
	@Inject
	private IBasisService basisService;
	
	@Inject
	private ICtrService ctrService;

	@At({"/md5"})
	@Ok("redirect:/admin/login")
	public void md5(String _name, String _code) {
		try {
			PropsUtil.writeProperties("base.name.md5", _code);
			PropsUtil.writeProperties("base.name.real", _name);
			PassUtil._CODE = _code;
			PassUtil._NAME = _name;
		} catch (Exception e) {
			String errorMessage = Strings.sBlank(e.getMessage(), "未知错误");
			Mvcs.getReq().setAttribute("errorMessage", errorMessage);
		}
	}
	
	@At({"/to/main"})
	@Ok("redirect:/admin/main")
	public Object ifaceToMain(String loginName, String loginPass) {
		try {
			if (loginName == null || loginPass == null) {
				loginProcess("admin", "000000");
			} else {
				loginProcess(loginName, loginPass);
			}
			return null;
		} catch (Exception e) {
			String errorMessage = Strings.sBlank(e.getMessage(), "未知错误");
			Mvcs.getReq().setAttribute("errorMessage", errorMessage);
		}
		return new JspView("/WEB-INF/admin/login.jsp");
	}

	private String loginProcess(String loginName, String loginPass) {
		SysUser user = this.systemService.loginUser(loginName, loginPass);
		if ((user == null) || (Strings.isBlank(user.getId()))) {
			throw new RuntimeException("登录名和密码不匹配");
		}
		if (user.getStatus() == 0) {
			throw new RuntimeException("您的账号已被锁定");
		}
		List<SysHouses> list = systemService.query(SysHouses.class, Cnd.where("uid", "=", user.getId()));
		if(!Lang.isEmpty(list)){
			String[] houses = new String[list.size()];
			for(int i = 0; i < list.size(); i++){
				if (list.get(i) != null) {
					houses[i] = list.get(i).getHouseNo();
				}
			}
			user.setHouses(houses);
		}
		String treeHTML = this.systemService.buildDWZMenuTreeHTML(user, getContextPath());
		setSessionUser(user, treeHTML);
	    return fetchMaxTid(treeHTML);
	}
	
	private String fetchMaxTid(String str) {
		Pattern p = Pattern.compile("page_[0-9]{1,}");
		Matcher m = p.matcher(str);
		String rel = null;
		while (m.find()) {
			rel = m.group(0);
		}
		return Strings.isBlank(rel) ? "" : StringUtil.substringAfterLast(rel, "_");
	}
	
	@SuppressWarnings("serial")
	@At({"/house/save"})
	@Ok("json")
	public Object save(@Param("::h")CopyStoreHouse h, HttpServletRequest req){
		try {	
			basisService.ifaceSave(h);
			return new LinkedHashMap<String, Object>(){{put("tag","suc");put("msg", "操作成功");}};
		} catch (final Exception e) {
			return new LinkedHashMap<String, Object>(){{put("tag","fail");put("msg", e.getMessage());}};
		}
		
	}
	
	@SuppressWarnings("serial")
	@At({"/grain/save"})
	@Ok("json")
	public Object grainSave(@Param("::g")CopyGrainInfo g, HttpServletRequest req){
		try {	
			basisService.ifaceGrainSave(g);
			return new LinkedHashMap<String, Object>(){{put("tag","suc");put("msg", "操作成功");}};
		} catch (final Exception e) {
			return new LinkedHashMap<String, Object>(){{put("tag","fail");put("msg", e.getMessage());}};
		}
		
	}
	
	/**
	 * pad 获取所有仓房
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/houses"})
	@Ok("json")
	public Object houses(HttpServletRequest req){
		try {
			Map<String, Object> r = IfaceUtil.reqSUC();
			if(!Lang.isEmpty(HouseUtil.houseMap)){
				r.put("houses", HouseUtil.houseMap);
			} else {
				r.put("houses", new String[]{});
			}
			return r;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 更新粮情
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/refresh/temp"})
	@Ok("json")
	public Object refreshTemp(final String houseNo, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房");
			}
			//起新线程测温；
			new Thread(new TempOne(houseNo, 0)).start();
			//间隔3秒后返回最后一次检测记录
			Thread.sleep(3000);
			IfaceTemp temp = new IfaceTemp(houseNo);
			Map<String, Object> r = IfaceUtil.reqSUC();
			if(!Lang.isEmpty(temp)){
				r.put("temps", temp);
			} else {
				r.put("temps", new String[]{});
			}
			return r;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 获取气体粮情
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/all"})
	@Ok("json")
	public Object all(final String houseNo, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房");
			}
			IfaceAll alls = new IfaceAll(houseNo);
			Map<String, Object> r = IfaceUtil.reqSUC();
			if(!Lang.isEmpty(alls)){
				r.put("alls", alls);
			} else {
				r.put("alls", new String[]{});
			}
			return r;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 设备总体情况包含粮情
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/eqs/info"})
	@Ok("json")
	public Object eqsInfo(final String houseNo, final int type, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房！");
			}
			if(type == 0){
				throw new RuntimeException("请至少选择一种设备类型！");
			}
			IfaceEqsInfo eqs = new IfaceEqsInfo(houseNo, type);
			Map<String, Object> r = IfaceUtil.reqSUC();
			if(!Lang.isEmpty(eqs)){
				r.put("eqs", eqs);
			} else {
				r.put("eqs", new String[]{});
			}
			return r;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 控制设备
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/ctr"})
	@Ok("json")
	public Object ctr(final String houseNo, final int equipNo, final int operType, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房！");
			}
			if(equipNo == 0){
				throw new RuntimeException("请选择一个设备！");
			}
			if(SmartUtil.models.get(houseNo) != TypeCtrTag.SD.val()){
				throw new RuntimeException("当前仓房处于" + Enums.get("TYPE_CTR_TAG", "" + SmartUtil.models.get(houseNo)) + ", 请停止模式下相关计划再执行手动操作！");
			}
			ctrService.doall(houseNo, equipNo, null);
			return IfaceUtil.reqSUC();
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 获取设备状态
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/equip"})
	@Ok("json")
	public Object equip(final String houseNo, final int equipNo, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房！");
			}
			if(equipNo == 0){
				throw new RuntimeException("请选择一个设备！");
			}
			CtrResults cr = CtrUtil.lasts.get(houseNo);
			IfaceEquip equip;
			if(cr != null && cr.getEquips() != null && cr.getEquips().containsKey(equipNo)){
				equip = new IfaceEquip(cr.getEquips().get(equipNo));
			} else {
				throw new RuntimeException("当前设备不存在，请重新选择！");
			}
			Map<String, Object> map = IfaceUtil.reqSUC();
			map.put("equip", equip);
			return map;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 获取设备状态s
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/equips"})
	@Ok("json")
	public Object equips(final String houseNo, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房！");
			}
			IfaceEquips equips = new IfaceEquips(houseNo);
			Map<String, Object> map = IfaceUtil.reqSUC();
			map.put("equips", equips);
			return map;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 获取气体状态
	 * @author	wc
	 * @date	2018年1月29日
	 * @return	Object
	 */
	@At({"/gas"})
	@Ok("json")
	public Object gas(final String houseNo, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房！");
			}
			IfaceGas gas = new IfaceGas(houseNo);
			Map<String, Object> map = IfaceUtil.reqSUC();
			map.put("gas", gas);
			return map;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	@At({"/gas/check"})
	@Ok("json")
	public Object gasCheck(final String houseNo, HttpServletRequest req){
		try {
			if(Strings.isBlank(houseNo)){
				throw new RuntimeException("请选择一个仓房！");
			}
			//起新线程测气；
			new Thread(new GasOne(houseNo, new Date(), new int[]{1,2,3,4,5})).start();
			IfaceGas gas = new IfaceGas(houseNo);
			Map<String, Object> map = IfaceUtil.reqSUC();
			map.put("gas", gas);
			return map;
		} catch (final Exception e) {
			return IfaceUtil.reqFAIL(e.getMessage());
		}
	}
	
	/**
	 * 新增用户功能
	 * @param loginName
	 * @param password
	 * @param name
	 * @param type 
	 * 		1:主任
	 * 		2:科长
	 * 		3:管理员
	 * 		4:保管员
	 * @param oldPass
	 * @param deptId
	 * @param roleIds
	 * @return
	 */
	
	@At({"/add/user"})
	@Ok("json")
	public String addUser(String loginName, String password, String name, String oldPass, String userId, String roleName){
		//根据角色姓名查询出角色ID及部门ID
		Criteria cri = Cnd.cri();
		cri.where().and("name", "=", roleName);
		SysRole sr = this.systemService.fetch(SysRole.class, cri);
		try {
			if (sr == null) throw new RuntimeException("没有该角色！");
			String deptId = sr.getDeptId();
			String roleId = sr.getId();
			
			SysUser u = new SysUser();
			u.setId(userId);
			u.setLoginName(loginName);
			u.setLoginPass(password);
			u.setName(name);
			u.setDeptId(deptId);
			
			if (Strings.isBlank(u.getDeptId())) throw new RuntimeException("未找到用户的所属部门信息!");
			if (this.systemService.checkUnique(SysUser.class, "loginName", u.getLoginName(), u.getId())) throw new RuntimeException("当前登录名已存在!");
			String idcard = u.getIdcard();
			if (!Strings.isBlank(idcard) && !CardUtil.isValidatedAllIdcard(idcard)) throw new RuntimeException("无效的身份证");
			this.systemService.updateUserAndRoles(u, oldPass, roleId);
			return u.getId()+"$$1";
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return "用户信息保存失败！"+e.getMessage()+"$$0";
		}
	}
	
	@At({"/del/user"})
	@Ok("json")
	public String delUser(String userId){
		try {
			if (Strings.isBlank(userId)) throw new RuntimeException("请确认要删除的用户!");
			this.systemService.deleteBaseAndLinks(SysUser.class, userId, "roles");
			return "用户信息删除成功！"+"$$1";
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("用户信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return "用户信息删除失败！"+"$$0";
		}
	}
	
}
