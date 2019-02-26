package cn.com.bjggs.ctr.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.domain.PlanCtr;
import cn.com.bjggs.ctr.domain.PlanEquip;
import cn.com.bjggs.ctr.domain.SmartConf;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.ctr.enums.TypeSmart;
import cn.com.bjggs.ctr.service.ICtrService;
import cn.com.bjggs.ctr.util.CtrConstant;
import cn.com.bjggs.ctr.util.CtrOne;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.ctr.util.SmartUtil;
import cn.com.bjggs.system.domain.SysUser;

@IocBean(name = "ctrService", args = { "refer:dao" })
public class CtrServiceImpl extends BaseServiceImpl implements ICtrService{

	public CtrServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	private static final ExecutorService CTs = Executors.newFixedThreadPool(100);
	
	public void doall(String houseNo, int equipNo, SysUser user){
		CtrResults ctr = CtrUtil.lasts.get(houseNo);
		if(ctr == null) throw new RuntimeException("未获取到当前设备的状态，请检查网络和配置！");
		Equipment equip = ctr.getEquips().get(equipNo);
		if(equip == null) throw new RuntimeException("未获取到当前设备的状态，请检查网络和配置！");
		if((equip.getStatus() & 0xFF) > 0x90){
			throw new RuntimeException("该设备当前处于故障状态，不能操作！");
		}
		if(equip.getStatus() == CtrConstant.R1O || equip.getStatus() == CtrConstant.R2O || equip.getStatus() == CtrConstant.R3O || equip.getStatus() == CtrConstant.R4O){
			throw new RuntimeException("开指令已发送请稍后！");
		}
		if(CtrConstant.R1C == equip.getStatus() || equip.getStatus() == CtrConstant.R2S || equip.getStatus() == CtrConstant.R3S || equip.getStatus() == CtrConstant.R4S){
			throw new RuntimeException("关指令已发送请稍后！");
		}
		if(CtrConstant.IOI == equip.getStatus()){
			throw new RuntimeException("指令已发送请稍后！");
		}
		Thread doCtr = new CtrOne(equip, user);
		CTs.execute(doCtr);
	}
	
	public int getNextNo(){
		Sql sql = Sqls.create("SELECT max(modelCode) as MODELCODE FROM smartconf");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> list = sql.getList(Record.class);
		int num = 0;
		if(list != null && list.size() > 0){
			num = list.get(0).getInt("MODELCODE", 0);
		}
		return num + 1;
	}
	
	public String changeNo(int num){
		String str;
		if(0 < num && num < 10){
			str = "00" + num;
		}else if(9 < num && num < 100){
			str = "0" + num;
		}else { 
			str = "" + num;
		}
		return str;
	}
	
	private String getNextPlanCode(){
		Sql sql = Sqls.create("SELECT PlanCode as PLANCODE FROM PlanCtr ORDER BY PlanCode ASC");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> pcs = sql.getList(Record.class);
		int number = 0;
		int planCode = 0;
		boolean tag = true;
		for(Record r : pcs){
			number = number + 1;
			planCode = ParseUtil.toInt(r.get("PLANCODE"), 0);
			if(number != planCode){
				tag = false;
				break;
			}
		}
		if(tag){
			planCode = number + 1;
		} else {
			planCode = number;
		}
		
		String str;
		if(0 < planCode && planCode < 10){
			str = "00" + planCode;
		}else if(9 < planCode && planCode < 100){
			str = "0" + planCode;
		}else { 
			str = "" + planCode;
		}
		return str;
	}
	
	public void savePlan(PlanCtr p, int[] equipNos){
		p.initCronAndDesc();
		if(Strings.isBlank(p.getPlanCode())){
			p.setPlanCode(getNextPlanCode());
		}
		this.update(p);
		List<PlanEquip> list = new LinkedList<PlanEquip>();
		for(int en : equipNos){
			list.add(new PlanEquip(en, p.getPlanCode()));
		}
		this.delete(PlanEquip.class, Cnd.where("planCode", "=", p.getPlanCode()));
		dao.insert(list);
	}
	
	public void updateStatus(String planCode, int status){
		dao.update(PlanCtr.class, Chain.make("status", status), Cnd.where("planCode", "=", planCode));
	}
	
	public void addConfs(String houseNo){
		List<SmartConf> scs = dao.query(SmartConf.class, Cnd.where("houseNo", "=", houseNo));
		if(scs != null && scs.size() > 0){
			for(SmartConf sc : scs){
				sc.refresh();
				dao.update(sc);
			}
		} else {
			SmartConf sc;
			GrainInfo gi = HouseUtil.get(houseNo, TypeHouseConf.GRAIN.code(), GrainInfo.class);
			for(String key : Enums.get("TYPE_SMART").keySet()){
				sc = new SmartConf(houseNo, gi.getGrainCode(), changeNo(getNextNo()), ParseUtil.toInt(key, 2));
				sc.setId(UUID.randomUUID().toString());
				dao.insert(sc);
			}
		}
	}
	
	private boolean isOpenTag(SmartConf s){
		boolean tag = false;
		//验证当前是否是定时模式
		if(SmartUtil.models.get(s.getHouseNo()) == TypeCtrTag.DS.val()){
			throw new RuntimeException("当前仓房正在执行计划模式，请关闭计划后再开启智能模式！");
		}
		//验证当前模式能否开启
		if(s.getModelType() == TypeSmart.YXBH.val() || s.getModelType() == TypeSmart.PJRTF.val()){
			return true;
		} else if(s.getModelType() == TypeSmart.NHL.val() || s.getModelType() == TypeSmart.ZNKT.val()){
			List<SmartConf> list = dao.query(SmartConf.class, Cnd.where("houseNo", "=", s.getHouseNo()).and("modelType", "<", 5).and("status", "=", 1));
			if(Lang.isEmpty(list)){
				tag = true; 
			} else {
				throw new RuntimeException("智能通风和低温储粮模式不能共存，请停止其他模式在开启！");
			}
		} else {
			List<SmartConf> list = dao.query(SmartConf.class, Cnd.where("houseNo", "=", s.getHouseNo()).and("status", "=", 1).andNot("modelType", "in", new int[]{TypeSmart.YXBH.val(), TypeSmart.PJRTF.val(), s.getModelType()}));
			if(Lang.isEmpty(list)){
				tag = true; 
			} else {
				throw new RuntimeException("智能通风(只能开启一种)和低温储粮模式不能共存，请停止其他模式在开启！");
			}
		}
		return tag;
	}
	
	public void smartSave(SmartConf s){
		if(s.getStatus() == TypeFlag.NO.val()){
			SmartUtil.removeSmart(s.getHouseNo(), s.getModelCode());
		} else {
			//校验其他模式
			if(isOpenTag(s)){
				SmartUtil.openSmart(s.getHouseNo(), s.getModelCode());
			}
		}
		dao.update(s);
	}
}
