package cn.com.bjggs.ctr.util;

import java.net.InetSocketAddress;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制通风窗模式工具（开关停）
 * @author	wc
 * @date	2017-11-02
 */
public class SmartWind implements SmartModel{
	
	//private static final byte[] WRITES = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x01, 0x10, (byte)0xD9, 0x3A, 0x00, 0x0C, 0x18, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00, 0x11, 0x00};
	//写单个指令
	private static final byte[] WRITE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, 0x3A, 0x00, 0x00};
	//读指令全部
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD9, 0x39, 0x00, 0x0D};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2;
	
	private InetSocketAddress address;
	
	private CtrMbs mbs = new CtrMbs();
	
	public SmartWind(EquipIps ips, int index){
		if(index == 1){
			this.address = new InetSocketAddress(ips.getWindIp1(), ips.getWindPort1());
		} else if(index == 2){
			this.address = new InetSocketAddress(ips.getWindIp2(), ips.getWindPort2());
		} else {
			this.address = new InetSocketAddress(ips.getWindIp3(), ips.getWindPort3());
		}
	}
	
	public int open(Equipment equip, AlarmNotes an0) {
		int tag = 0;
		//验证模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.WIND) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		if(equip != null){
			byte[] cmd = WRITE.clone();
			cmd[9] = (byte)(0x3A + equip.getRegisterWay());
			cmd[11] = CtrConstant.M1O;
//			int reg = 14 + equip.getRegisterWay() * 2;
			int index = 12 + equip.getRegisterWay() * 2;
//			byte[] cmd = WRITES.clone();
//			cmd[reg] = CtrConstant.M1O;
			for (int i = 0; i < 6; i++) {
				req = mbs.sendMsg(cmd, address, 12);
				if(i == 5){
					throw new RuntimeException("启动命令发送超时！");
				}
				if(req != null && req.length == 12 && req[11] == CtrConstant.M1O){
					//前台同步讯息
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R1OI, equip.getEquipName() + ":命令已发送，正在开启！")));
					//同步当前设备状态
					equip.setStatus(CtrConstant.R1OI);
					break;
				}
				try {
					Thread.sleep(55);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//获取状态
			for (int i = 0; i < 30; i++) {
				try{
					req = mbs.sendMsg(READ, address, len);
					if(req != null){
						if(req[index] == CtrConstant.R1OA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R1OA, equip.getEquipName() + ":开窗到位！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1OA);
							tag = 1;
							break;
						} else if(req[index] == CtrConstant.R1SA){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_HALF, equip.getModel(), CtrConstant.R1SA, equip.getEquipName() + ":被干预，中途停止！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1SA);
							tag = 1;
							break;
						} else if(req[index] == CtrConstant.R1F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1F, equip.getEquipName() + ":故障！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1F);
							addError(an0, equip.getEquipName() + ":故障！");
							break;
						} else if(req[index] == CtrConstant.R1OT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1OT, equip.getEquipName() + ":开窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1OT);
							addError(an0, equip.getEquipName() + ":开窗超时！");
							break;
						}
					}
				}catch(Exception e){}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i == 29){
					throw new RuntimeException("开窗状态查询超出步长");
				}
			}
		}
		return tag;
	}
	
	public int close(Equipment equip, AlarmNotes an0){
		int tag = 0;
		//验证模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.WIND) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		if(equip != null){
			byte[] cmd = WRITE.clone();
			cmd[9] = (byte)(0x3A + equip.getRegisterWay());
			cmd[11] = CtrConstant.M1C;
//			int reg = 14 + equip.getRegisterWay() * 2;
			int index = 12 + equip.getRegisterWay() * 2;
//			byte[] cmd = WRITES.clone();
//			cmd[reg] = CtrConstant.M1C;
			for (int i = 0; i < 6; i++) {
				req = mbs.sendMsg(cmd, address, 12);
				if(i == 5){
					throw new RuntimeException("关闭命令发送超时！");
				}
				if(req != null && req.length == 12 && req[11] == CtrConstant.M1C){
					//前台同步讯息
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R1CI, equip.getEquipName() + ":命令已发送，正在关闭！")));
					//同步当前设备状态
					equip.setStatus(CtrConstant.R1CI);
					break;
				}
				try {
					Thread.sleep(55);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//获取状态
			for (int i = 0; i < 30; i++) {
				try{
					req = mbs.sendMsg(READ, address, len);
					if(req != null){
						if(req[index] == CtrConstant.R1CA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R1CA, equip.getEquipName() + ":关窗到位！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1CA);
							tag = 1;
							break;
						} else if(req[index] == CtrConstant.R1SA){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_HALF, equip.getModel(), CtrConstant.R1SA, equip.getEquipName() + ":被干预，中途停止！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1SA);
							tag = 1;
							break;
						} else if(req[index] == CtrConstant.R1F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1F, equip.getEquipName() + ":故障！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1F);
							addError(an0, equip.getEquipName() + ":故障！");
							break;
						} else if(req[index] == CtrConstant.R1CT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1CT, equip.getEquipName() + ":关窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1CT);
							addError(an0, equip.getEquipName() + ":关窗超时！");
							break;
						}
					}
				}catch(Exception e){}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i == 29){
					throw new RuntimeException("关窗状态查询超出步长");
				}
			}
		}
		return tag;
	}
	
	public int stop(Equipment equip, AlarmNotes an0){
		int tag = 0;
		//验证模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.WIND) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		if(equip != null){
			byte[] cmd = WRITE.clone();
			cmd[9] = (byte)(0x3A + equip.getRegisterWay());
			cmd[11] = CtrConstant.M1S;
//			int reg = 14 + equip.getRegisterWay() * 2;
			int index = 12 + equip.getRegisterWay() * 2;
//			byte[] cmd = WRITES.clone();
//			cmd[reg] = CtrConstant.M1S;
			for (int i = 0; i < 6; i++) {
				req = mbs.sendMsg(cmd, address, 12);
				if(i == 5){
					throw new RuntimeException("关闭命令发送超时！");
				}
				if(req != null && req.length == 12 && req[11] == CtrConstant.M1S){
					//前台同步讯息
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R1CI, equip.getEquipName() + ":停止指令已发送！")));
					//同步当前设备状态
					equip.setStatus(CtrConstant.R1S);
					break;
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//获取状态
			for (int i = 0; i < 30; i++) {
				try{
					req = mbs.sendMsg(READ, address, len);
					if(req != null){
						if(req[index] == CtrConstant.R1SA){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_HALF, equip.getModel(), CtrConstant.R1SA, equip.getEquipName() + ":被干预，中途停止！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1SA);
							tag = 1;
							break;
						} else if(req[index] == CtrConstant.R1F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1F, equip.getEquipName() + ":故障！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1F);
							addError(an0, equip.getEquipName() + ":故障！");
							break;
						} else if(req[index] == CtrConstant.R1CT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1CT, equip.getEquipName() + ":关窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1CT);
							addError(an0, equip.getEquipName() + ":关窗超时！");
							break;
						} else if(req[index] == CtrConstant.R1OT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1OT, equip.getEquipName() + ":开窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R1OT);
							addError(an0, equip.getEquipName() + ":开窗超时！");
							break;
						}
					}
				}catch(Exception e){}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i == 29){
					throw new RuntimeException("停止状态查询超出步长");
				}
			}
		}
		return tag;
	}
	
	public int openr(Equipment equip, AlarmNotes an0){
		return 0;
	}
	
	private void addError(AlarmNotes an0, String msg){
		an0.setNums(an0.getNums() + 1);
		an0.setFaultStr((an0.getFaultStr() == null ? "" : an0.getFaultStr()) + msg + "|");
	}
	
}
