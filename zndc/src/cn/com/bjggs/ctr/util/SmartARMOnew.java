package cn.com.bjggs.ctr.util;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.enums.TypeEquip;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制ARMdio模式工具
 * @author	yucy
 * @date	2018-02-26
 */
public class SmartARMOnew implements SmartModel{
	
	private static final Log log = Logs.getLog(SmartARMOnew.class); 
	
	String armIP = "";
	
	public SmartARMOnew(EquipIps ips){
		this.armIP = ips.getArmDioIp();
	}
	

	public int open(Equipment equip,  AlarmNotes an0) {
		String url;
		String val;
		int[] armArr;	
		boolean isOpenTag = true;
		if (equip.getType() == TypeEquip.ZLFJ.val()) {//如果类型是有窗单项风机，先开窗户  TODO 加判断DIDO是否写
			isOpenTag = false;
			synchronized (armIP) {
				url = ARMUtil.isOpenWind(armIP, equip.getWindDo1(), equip.getWindDo2());
				val = ARMCommUtil.sendPost(url);
			}
			if (ARMUtil.isFault(val)) {
				throw new RuntimeException("发送开启指令失败！");
			}
			DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R2OW, equip.getEquipName() + ":窗户正在开启！")));
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
					url = ARMUtil.getDIUrl(armIP, equip.getWindDi1(),equip.getWindDi2());
					val = ARMCommUtil.SendGet(url);
				}
				try {
					armArr = ARMUtil.getDIResult(val);
//					if (ARMUtil.isOne(index, outs)) {//验证故障
//						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.IOF, equip.getEquipName() + ":设备故障！")));
//						equip.setStatus(CtrConstant.IOF);
//						break;
//					}
					if (ARMUtil.isOne(0, armArr) && ARMUtil.isZero(1, armArr)) {//已开启
						isOpenTag = true;
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R2O, equip.getEquipName() + ":窗户已经开启！")));
						equip.setStatus(CtrConstant.IOT);
						break;
					}
				} catch (Exception e) {
					log.info(e.getMessage());
				}
				if(i == 29){
					throw new RuntimeException("获取状态超时！");
				}
			}
		}
		if (isOpenTag) {//如果窗户正常开启，再开启风机
			//发送指令控制设备
			synchronized (armIP) {
				url = ARMUtil.isOpenOnew(armIP, equip.getDoWay());
				val = ARMCommUtil.sendPost(url);
			}
			if (ARMUtil.isFault(val)) {
				throw new RuntimeException("发送开启指令失败！");
			}
			
			//查看返回的状态
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//验证故障和是否开启
				synchronized (armIP) {
					url = ARMUtil.getDIUrl(armIP, equip.getDiWay2(),equip.getDiWay1());
					val = ARMCommUtil.SendGet(url);
				}
				try {
					armArr = ARMUtil.getDIResult(val);
					if (ARMUtil.isOne(0, armArr)) {//验证故障
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2F, equip.getEquipName() + ":风机故障！")));
						equip.setStatus(CtrConstant.R2F);
						break;
					}
					if (ARMUtil.isOne(1, armArr)) {//验证是否开启
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R2OA, equip.getEquipName() + ":风机已经开启！")));
						equip.setStatus(CtrConstant.R2OA);
						break;
					}
				} catch (Exception e) {
					log.info("开启风机失败！");
				}
				if(i == 4){
					throw new RuntimeException("获取状态超时！");
				}
			}
		}		
		return 0;
	}

	public int close(Equipment equip,  AlarmNotes an0) {//验证位的地址
		String url;
		String val;
		int[] armArr;
		boolean isCloseTag = false;
		synchronized (armIP) {
			url = ARMUtil.isCloseOnew(armIP, equip.getDoWay());
			val = ARMCommUtil.sendPost(url);
		}
		if (ARMUtil.isFault(val)) {
			throw new RuntimeException("发送关闭指令失败！");
		}
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//验证故障和是否关闭
			synchronized (armIP) {
				url = ARMUtil.getDIUrl(armIP, equip.getDiWay2(),equip.getDiWay1());
				val = ARMCommUtil.SendGet(url);
			}
			try {
				armArr = ARMUtil.getDIResult(val);
				if (ARMUtil.isOne(0, armArr)) {//验证故障
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2F, equip.getEquipName() + ":风机故障！")));
					equip.setStatus(CtrConstant.R2F);
					break;
				}
				if (ARMUtil.isZero(1, armArr)) {//验证是否关闭
					isCloseTag = true;
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R2SA, equip.getEquipName() + ":风机已经停止！")));
					equip.setStatus(CtrConstant.R2SA);
					break;
				}
			} catch (Exception e) {
				log.info("风机关闭失败！");
			}
			if(i == 4){
				throw new RuntimeException("获取状态超时！");
			}
		}
		
		if (isCloseTag && equip.getType() == TypeEquip.ZLFJ.val()) {//如果风机关闭，并且是有窗单项风机模式，发送关闭窗户指令
			//发送指令控制设备
			synchronized (armIP) {
				url = ARMUtil.isCloseWind(armIP, equip.getWindDo1(), equip.getWindDo2());
				val = ARMCommUtil.sendPost(url);
			}
				
			if (ARMUtil.isFault(val)) {
				throw new RuntimeException("发送关闭指令失败！");
			}
			DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R2CW, equip.getEquipName() + ":窗户正在关闭！")));
			//查看返回的状态
			for (int i = 0; i < 30; i++) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (armIP) {
					url = ARMUtil.getDIUrl(armIP, equip.getWindDi1(),equip.getWindDi2());
					val = ARMCommUtil.SendGet(url);
				}
				//验证故障和是否开启
				armArr = ARMUtil.getDIResult(val);
				
//					if (outs[0] == 1) {//验证故障
//						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.IOF, equip.getEquipName() + ":设备故障！")));
//						equip.setStatus(CtrConstant.IOF);
//						break;
//					}
					if (ARMUtil.isZero(0, armArr) && ARMUtil.isOne(1, armArr)) {//已关闭
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R2C, equip.getEquipName() + ":设备已经关闭！")));
						equip.setStatus(CtrConstant.R2C);
						break;
					}
//					if (outs[1] == 0 && outs[2] == 0) {//正在关闭
//						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.IOT, equip.getEquipName() + ":设备正在关闭！")));
//						equip.setStatus(CtrConstant.IOT);
//					}
				if(i == 29){
					throw new RuntimeException("获取状态超时！");
				}
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
