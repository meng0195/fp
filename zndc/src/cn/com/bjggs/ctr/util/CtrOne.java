package cn.com.bjggs.ctr.util;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.lang.Strings;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.msg.enums.TypeMsg;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.squery.util.QueryUtil;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmTT;

/**
 * 手动控制
 * @author	wc
 * @date	2017-10-02
 */
public class CtrOne extends Thread{
	
	public static void initDao(Dao d){
		dao = d;
	};
	private static Dao dao;
	
	/**
	 * 仓房编号
	 */
	private Equipment equip;
	private AlarmTT att;
	private AlarmNotes an;	//检测异常
	private AlarmNotes an0;	//控制异常
	private Date date = new Date();;
	
	public CtrOne(){}
	
	public CtrOne(Equipment equip, SysUser user){
		this.equip = equip;
		
		this.an = new AlarmNotes(Constant.W_CTR, Constant.W_DO, equip.getHouseNo(), date);
		this.an0 = new AlarmNotes(Constant.W_CTR, Constant.W_CTR_1, equip.getHouseNo(), date);
		this.att = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.WARNS.code(), AlarmTT.class);
		if(att == null){
			addError("该仓房预警信息未配置！");
		}
	}
	
	private void doCtr(){
		try {
			if(equip != null && equip.getModel() > 0 && Strings.isNotBlank(equip.getHouseNo())){
				EquipIps ips = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.EIPS.code(), EquipIps.class);
				CtrModel cm = CtrMbsFactory.createModel(equip.getModel(), ips, equip.getModelWay());
				cm.ctr(equip, an0);
				insertAll();
				QueryUtil.refreshEquip(equip.getHouseNo());
			}
		} catch (Exception e) {
			DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), equip.getStatus(), equip.getEquipName() + e.getMessage())));
			addError(e.getMessage());
			insertAll();
		}
	}
	
	private void insertAll(){
		List<AlarmNotes> as = new LinkedList<AlarmNotes>();
		if(an != null && an.getNums() > 0){
			as.add(an);
			MsgUtil.insertMsg(equip.getHouseNo(), "设备控制异常!", TypeMsg.WARN.val(), Constant.W_CTR, null);
		}
		if(an0 != null && an0.getNums() > 0){
			as.add(an0);
			CtrResults res = CtrUtil.lasts.get(equip.getHouseNo());
			if(res != null){
				res.setWarn(res.getWarn()+1);
			}
			MsgUtil.insertMsg(equip.getHouseNo(), "设备控制异常!", TypeMsg.WARN.val(), Constant.W_CTR, null);
		}
		dao.insert(as);
	}
	
	private void addError(String msg){
		an.setNums(an.getNums() + 1);
	}
	
	@Override
	public void run() {
		doCtr();
	}
	
}
