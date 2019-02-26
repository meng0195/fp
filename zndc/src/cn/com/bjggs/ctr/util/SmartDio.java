package cn.com.bjggs.ctr.util;

import java.net.InetSocketAddress;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制dio模式工具
 * @author	wc
 * @date	2017-10-01
 */
public class SmartDio implements SmartModel{
	
	private static final byte[] WRITE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, 0x3C, 0x00, 0x00};
	
	//dio模式下的读取指令:此指令能够获取板子当前的所有状态
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD9, 0x3A, 0x00, 0x02};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2;
	
	private InetSocketAddress address;
	
	private CtrMbs mbs = new CtrMbs();
	
	public SmartDio(EquipIps ips){
		this.address = new InetSocketAddress(ips.getDioIp(), ips.getDioPort());
	}
	
	private int getDiVal(byte[] req, int index){
		if(index > 24){
			return -1;
		}
		return MathUtil.getBitByIndex(req[14 - index / 8], index % 8);
	}
	
	public int open(Equipment equip,  AlarmNotes an0) {
		int tag = 0;
		//验证模块模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.DIDO) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		//验证设备模式
		if(getDiVal(req, equip.getDiWay()) != CtrConstant.DIO_STATUS) throw new RuntimeException("当前设备模式错误，不能实现远程控制！");
		//验证故障
		if(getDiVal(req, equip.getDiWay2()) == CtrConstant.DIO_FAULT) throw new RuntimeException("当前设备处于故障状态, 不能实现控制！");
		//验证do
		if(equip.getDoWay() < 0) throw new RuntimeException("DO位配置错误！");
		//发送开启命令
		byte tagH = req[15];
		byte tagL = req[16];
		byte[] cmd = WRITE.clone();
		//计算位
		if(equip != null){
			if(equip.getDoWay()/8 == 1){
				tagH = MathUtil.changeTo1ByIndex(tagH, equip.getDoWay()%8);
			} else {
				tagL = MathUtil.changeTo1ByIndex(tagL, equip.getDoWay()%8);
			}
		}
		cmd[10] = tagH;
		cmd[11] = tagL;
		req = mbs.sendMsg(cmd, address, 0);
		DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), equip.getStatus(), equip.getEquipName() + ":设备故障！")));
		for(int i = 0; i < 10; i++){
			try {
				Thread.sleep(2000);
				req = mbs.sendMsg(READ, address, len);
				if(getDiVal(req, equip.getDiWay2()) == CtrConstant.DIO_FAULT){
					equip.setStatus(CtrConstant.IOF);
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), equip.getStatus(), equip.getEquipName() + ":设备故障！")));
					break;
				}
				if(getDiVal(req, equip.getDiWay1()) == CtrConstant.IOT){
					equip.setStatus(CtrConstant.IOT);
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), equip.getStatus(), equip.getEquipName() + ":已经开启！")));
					tag = 1;
					break;
				}
			} catch (Exception e) {
			}
			if(i == 9){
				throw new RuntimeException("状态查询超出步长！");
			}
		}
		return tag;
	}

	public int close(Equipment equip,  AlarmNotes an0) {
		int tag = 0;
		//验证模块模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.DIDO) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		//验证设备模式
		if(getDiVal(req, equip.getDiWay()) != CtrConstant.DIO_STATUS) throw new RuntimeException("当前设备模式错误，不能实现远程控制！");
		//验证故障
		if(getDiVal(req, equip.getDiWay2()) == CtrConstant.DIO_FAULT) throw new RuntimeException("当前设备处于故障状态, 不能实现控制！");
		//验证do
		if(equip.getDoWay() < 0) throw new RuntimeException("DO位配置错误！");
		//发送关闭命令
		byte tagH = req[15];
		byte tagL = req[16];
		byte[] cmd = WRITE.clone();
		//计算位
		if(equip != null){
			if(equip.getDoWay()/8 == 1){
				tagH = MathUtil.changeTo0ByIndex(tagH, equip.getDoWay()%8);
			} else {
				tagL = MathUtil.changeTo0ByIndex(tagL, equip.getDoWay()%8);
			}
		}
		cmd[10] = tagH;
		cmd[11] = tagL;
		req = mbs.sendMsg(cmd, address, 0);
		for(int i = 0; i < 10; i++){
			try {
				Thread.sleep(2000);
				req = mbs.sendMsg(READ, address, len);
				if(getDiVal(req, equip.getDiWay2()) == CtrConstant.DIO_FAULT){
					equip.setStatus(CtrConstant.IOF);
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), equip.getStatus(), equip.getEquipName() + ":设备故障！")));
					break;
				}
				if(getDiVal(req, equip.getDiWay1()) == CtrConstant.IOC){
					equip.setStatus(CtrConstant.IOC);
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), equip.getStatus(), equip.getEquipName() + ":已经关闭！")));
					tag = 1;
					break;
				}
			} catch (Exception e) {
			}
			if(i == 9){
				throw new RuntimeException("状态查询超出步长！");
			}
		}
		return tag;
	}
	
	public int stop(Equipment equip,  AlarmNotes an0){
		return 0;
	}
	
	public int openr(Equipment equip,  AlarmNotes an0){
		return 0;
	}
	
}
