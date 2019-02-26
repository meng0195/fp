package cn.com.bjggs.ctr.util;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.lang.Strings;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.msg.enums.TypeMsg;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmTT;

/**
 * 手动控制
 * @author	wc
 * @date	2017-10-02
 */
public class CtrPlan extends Thread{
	
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
	private int tag = 2;
	
	public CtrPlan(){}
	
	public CtrPlan(Equipment equip, int tag, int modelType, String taskCode, Date ctrDate){
		this.tag = tag;
		this.equip = equip;
		this.an = new AlarmNotes(Constant.W_CTR, Constant.W_DO, equip.getHouseNo(), ctrDate);
		this.an0 = new AlarmNotes(Constant.W_CTR, Constant.W_CTR_1, equip.getHouseNo(), ctrDate);
		this.att = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.WARNS.code(), AlarmTT.class);
		if(att == null){
			addError("该仓房预警信息未配置！");
		}
	}
	
	public void doCtr(){
		try {
			if(equip != null && equip.getModel() > 0 && Strings.isNotBlank(equip.getHouseNo())){
				EquipIps ips = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.EIPS.code(), EquipIps.class);
				SmartModel sm = SmartMbsFactory.createModel(equip.getModel(), ips, equip.getModelWay());
				if(tag == CtrConstant.CTR_OPEN_TAG){
					sm.open(equip, an0);
				} else if(tag == CtrConstant.CTR_CLOSE_TAG){
					sm.close(equip, an0);
				} else if(tag == CtrConstant.CTR_STOP_TAG){
					sm.stop(equip, an0);
				} else if(tag == CtrConstant.CTR_OPENR_TAG){
					sm.openr(equip, an0);
				}
				insertAll();
			}
		} catch (Exception e) {
			addError(e.getMessage());
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
