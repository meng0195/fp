package cn.com.bjggs.ctr.util;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制ARM窗户模式工具
 * @author	yucy
 * @date	2018-03-17
 */
public class ModelARMWind implements CtrModel{
	
	String armIP = "";
	
	public ModelARMWind(EquipIps ips){
		this.armIP = ips.getArmDioIp();
	}
	
	@Override
	public int ctr(Equipment equip, AlarmNotes an0) {
		int tag = 0;
		//验证位的地址
//		int verifyModel = PropsUtil.getInteger("verify.model.one");
		//板子返回的String串
		String url;
		String val;
		int[] armArr;
//		synchronized (armIP) {
//			url = ARMUtil.getDIUrl(armIP, verifyModel, equip.getDiWay()); //将两种模式的位传入
//			val = ARMCommUtil.SendGet(url);//拼接好命令
//		}
//		armArr = ARMUtil.getDIResult(val);
//		//首先验证是哪种模式:本地1/联网0,然后验证模式是否为自动(手动,停止0/自动1)
//		if(ARMUtil.isOne(0, armArr) || ARMUtil.isOne(1, armArr)){
//			throw new RuntimeException("当前非联网模式,请调整模式后再执行操作！");
//		}
		
		//判断是开启设备还是关闭设备
		if (equip.getStatus() == CtrConstant.R2C) {//如果关闭状态
			//发送指令控制设备
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
		} else {//设备开启,发送关闭指令
			//发送指令控制设备
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
		}
		return tag;
	}
	
	@Override
	public void ctrs(String houseNo, int[] equipNos) {
	}
	
}
