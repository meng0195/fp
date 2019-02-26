package cn.com.bjggs.ctr.util;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制ARMdio模式工具
 * @author	yucy
 * @date	2018-02-26
 */
public class SmartARMWind implements SmartModel{
	
	String armIP = "";
	
	public SmartARMWind(EquipIps ips){
		this.armIP = ips.getArmDioIp();
	}
	

	public int open(Equipment equip,  AlarmNotes an0) {
		String url;
		String val;
		int[] armArr;
		synchronized (armIP) {
			url = ARMUtil.isOpenWind(armIP, equip.getDoWay(), equip.getDoWay1());
			val = ARMCommUtil.sendPost(url);
		}
		if (ARMUtil.isFault(val)) {
			throw new RuntimeException("发送开启指令失败！");
		}
		DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R2OW, equip.getEquipName() + ":设备正在开启！")));
		//查看返回的状态
		for (int i = 0; i < 30; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//验证故障和是否开启
			synchronized (armIP) {
				url = ARMUtil.getDIUrl(armIP, equip.getDiWay2(),equip.getDiWay1(),equip.getDiWay3());
				val = ARMCommUtil.SendGet(url);
			}
				armArr = ARMUtil.getDIResult(val);
			if (ARMUtil.isOne(0, armArr)) {//验证故障
				DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2F, equip.getEquipName() + ":设备故障！")));
				equip.setStatus(CtrConstant.R2F);
				break;
			}
			if (ARMUtil.isOne(1, armArr) && ARMUtil.isZero(2, armArr)) {//已开启
				DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R2O, equip.getEquipName() + ":设备已经开启！")));
				equip.setStatus(CtrConstant.R2OA);
				break;
			}
			if(i == 29){
				throw new RuntimeException("获取状态超时！");
			}
		}		
		return 0;
	}

	public int close(Equipment equip,  AlarmNotes an0) {//验证位的地址
		//板子返回的String串
		String url;
		String val;
		int[] armArr;
		
		synchronized (armIP) {
			url = ARMUtil.isCloseWind(armIP, equip.getDoWay(), equip.getDoWay1());
			val = ARMCommUtil.sendPost(url);
		}
		if (ARMUtil.isFault(val)) {
			throw new RuntimeException("发送关闭指令失败！");
		}
		DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R2CW, equip.getEquipName() + ":设备正在关闭！")));
		//查看返回的状态
		for (int i = 0; i < 30; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//验证故障和是否开启
			synchronized (armIP) {
				url = ARMUtil.getDIUrl(armIP, equip.getDiWay2(),equip.getDiWay1(),equip.getDiWay3());
				val = ARMCommUtil.SendGet(url);
			}
			armArr = ARMUtil.getDIResult(val);
				if (ARMUtil.isOne(0, armArr)) {//验证故障
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2F, equip.getEquipName() + ":设备故障！")));
					equip.setStatus(CtrConstant.R2F);
					break;
				}
				if (ARMUtil.isZero(1, armArr) && ARMUtil.isOne(2, armArr)) {//已关闭
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R2C, equip.getEquipName() + ":设备已经关闭！")));
					equip.setStatus(CtrConstant.R2C);
					break;
				}
			if(i == 29){
				throw new RuntimeException("获取状态超时！");
			}
		}
		return 0;
	}
	
	public int stop(Equipment equip,  AlarmNotes an0){
		return 0;
	}
	
	public int openr(Equipment equip,  AlarmNotes an0){
		return 0;
	}
	
}
