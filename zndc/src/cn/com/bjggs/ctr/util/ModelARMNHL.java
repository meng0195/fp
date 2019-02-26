package cn.com.bjggs.ctr.util;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制ARMnhl模式工具
 * @author	yucy
 * @date	2018-03-22
 */
public class ModelARMNHL implements CtrModel{
	
	private static final Log log = Logs.getLog(ModelARMNHL.class); 
	
	String armIP = "";
	
	public ModelARMNHL(EquipIps ips){
		this.armIP = ips.getArmDioIp();
	}
	
	@Override
	public int ctr(Equipment equip,  AlarmNotes an0) {
		
		//先找到内环流的对应设备，查询DIDO
		CtrResults ctr = CtrUtil.lasts.get(equip.getHouseNo());
		Equipment equip1 = ctr.getEquips().get(equip.getCaEquipNo());
		
		int tag = 0;
		//验证位的地址
		int verifyModel = PropsUtil.getInteger("verify.model.one");
		//板子返回的String串
		String url;
		String val;
		int[] armArr;
		boolean overTimeTag = false;
		boolean overTimeTag1 = false;
		synchronized (armIP) {
			url = ARMUtil.getDIUrl(armIP, verifyModel, equip.getDiWay()); //将两种模式的位传入
			val = ARMCommUtil.SendGet(url);//拼接好命令
		}
		armArr = ARMUtil.getDIResult(val);
		//首先验证是哪种模式:本地1/联网0,然后验证模式是否为自动(手动,停止0/自动1)
		if(ARMUtil.isOne(0, armArr) || ARMUtil.isOne(1, armArr)){
			throw new RuntimeException("当前非联网模式,请调整模式后再执行操作！");
		}
		if (equip1 != null) {
			synchronized (armIP) {
				url = ARMUtil.getDIUrl(armIP, verifyModel, equip1.getDiWay()); //将两种模式的位传入
				val = ARMCommUtil.SendGet(url);//拼接好命令
			}
			armArr = ARMUtil.getDIResult(val);
			//首先验证是哪种模式:本地1/联网0,然后验证模式是否为自动(手动,停止0/自动1)
			if(ARMUtil.isOne(0, armArr) || ARMUtil.isOne(1, armArr)){
				throw new RuntimeException("当前非联网模式,请调整模式后再执行操作！");
			}
		}
		
		
		//判断是开启设备还是关闭设备
		if (equip.getStatus() == CtrConstant.IOC) {//如果关闭状态
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
						Thread.sleep(1000);
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
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.IOF, equip.getEquipName() + ":设备故障！")));
							equip.setStatus(CtrConstant.IOF);
							break;
						}
						if (ARMUtil.isOne(1, armArr)) {//验证是否开启
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.IOT, equip.getEquipName() + ":设备已经开启！")));
							equip.setStatus(CtrConstant.IOT);
							break;
						}
					} catch (Exception e) {
						log.info("开启风机失败！");
					}
					if(i == 4){
//						throw new RuntimeException("获取状态超时！");
						overTimeTag = true;
					}
				}
				//查看对应设备返回的状态
				if (equip1 != null) {
					for (int i = 0; i < 5; i++) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//验证故障和是否开启
						synchronized (armIP) {
							url = ARMUtil.getDIUrl(armIP, equip1.getDiWay2(),equip1.getDiWay1());
							val = ARMCommUtil.SendGet(url);
						}
						try {
							armArr = ARMUtil.getDIResult(val);
							if (ARMUtil.isOne(0, armArr)) {//验证故障
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip1.getHouseNo(), equip1.getEquipNo(), CtrConstant.C_BAD, equip1.getModel(), CtrConstant.IOF, equip1.getEquipName() + ":设备故障！")));
								equip1.setStatus(CtrConstant.IOF);
								break;
							}
							if (ARMUtil.isOne(1, armArr)) {//验证是否开启
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip1.getHouseNo(), equip1.getEquipNo(), CtrConstant.C_OPEN, equip1.getModel(), CtrConstant.IOT, equip1.getEquipName() + ":设备已经开启！")));
								equip1.setStatus(CtrConstant.IOT);
								break;
							}
						} catch (Exception e) {
							log.info("开启风机失败！");
						}
						if(i == 4){
//							throw new RuntimeException("获取状态超时！");
							overTimeTag1 = true;
						}
					}
				}
				
				if (overTimeTag || overTimeTag1) {
					String ss = "";
					if(overTimeTag) ss += "主箱状态获取超时！";
					if(overTimeTag1) ss += "副箱状态获取超时！";
					throw new RuntimeException(ss);
				}
		} else {//设备开启,发送关闭指令
			//发送指令控制设备
			synchronized (armIP) {
				url = ARMUtil.isCloseOnew(armIP, equip.getDoWay());
				val = ARMCommUtil.sendPost(url);
			}
			if (ARMUtil.isFault(val)) {
				throw new RuntimeException("发送关闭指令失败！");
			}
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(1000);
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
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.IOF, equip.getEquipName() + ":故障！")));
						equip.setStatus(CtrConstant.IOF);
						break;
					}
					if (ARMUtil.isZero(1, armArr)) {//验证是否关闭
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.IOC, equip.getEquipName() + ":设备已经停止！")));
						equip.setStatus(CtrConstant.IOC);
						break;
					}
				} catch (Exception e) {
					log.info("设备关闭失败！");
				}
				if(i == 4){
//					throw new RuntimeException("获取状态超时！");
					overTimeTag = true;					
				}
			}
			//如果对应设备存在，则
			if (equip1 != null) {
				for (int i = 0; i < 5; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (armIP) {
						url = ARMUtil.getDIUrl(armIP, equip1.getDiWay2(),equip1.getDiWay1());
						val = ARMCommUtil.SendGet(url);
					}
					try {
						armArr = ARMUtil.getDIResult(val);
						if (ARMUtil.isOne(0, armArr)) {//验证故障
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip1.getHouseNo(), equip1.getEquipNo(), CtrConstant.C_BAD, equip1.getModel(), CtrConstant.IOF, equip1.getEquipName() + ":故障！")));
							equip1.setStatus(CtrConstant.IOF);
							break;
						}
						if (ARMUtil.isZero(1, armArr)) {//验证是否关闭
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip1.getHouseNo(), equip1.getEquipNo(), CtrConstant.C_CLOSE, equip1.getModel(), CtrConstant.IOC, equip1.getEquipName() + ":设备已经停止！")));
							equip1.setStatus(CtrConstant.IOC);
							break;
						}
					} catch (Exception e) {
						log.info("设备关闭失败！");
					}
					if(i == 4){
//						throw new RuntimeException("获取状态超时！");
						overTimeTag1 = true;
					}
				}
			}
			if (overTimeTag || overTimeTag1) {
				String ss = "";
				if(overTimeTag) ss += "主箱状态获取超时！";
				if(overTimeTag1) ss += "副箱状态获取超时！";
				throw new RuntimeException(ss);
			}
		}
		return tag;
	}
	
	@Override
	public void ctrs(String houseNo, int[] equipNos) {
	}
	
}
