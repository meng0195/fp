package cn.com.bjggs.ctr.util;

import java.net.InetSocketAddress;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制通风窗模式工具
 * @author	wc
 * @date	2017-10-01
 */
public class ModelWone implements CtrModel{
	
	//写多个指令
	//private static final byte[] WRITES = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x01, 0x10, (byte)0xD9, 0x3A, 0x00, 0x0C, 0x18, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00, 0x13, 0x00};
	//写单个指令
	private static final byte[] WRITE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, 0x3A, 0x00, 0x00};
	//读指令全部
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD9, 0x39, 0x00, 0x0D};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2;
	
	private InetSocketAddress address;
	
	private CtrMbs mbs = new CtrMbs();
	
	public ModelWone(EquipIps ips){
		this.address = new InetSocketAddress(ips.getWoneIp(), ips.getWonePort());
	}
	
	@Override
	public int ctr(Equipment equip, AlarmNotes an0) {
		int tag = 0;
		//验证模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.WONE) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		if(equip != null){
			int index = 12 + equip.getRegisterWay() * 2;
//			int reg = 14 + equip.getRegisterWay() * 2;
			//开窗到位，风机开启，风机关闭都发送关闭指令
			if(equip.getStatus() == CtrConstant.R3OFA || equip.getStatus() == CtrConstant.R3OWA || equip.getStatus() == CtrConstant.R3SFA){
				byte[] cmd = WRITE.clone();
				cmd[9] = (byte)(cmd[9] + equip.getRegisterWay());
				cmd[11] = CtrConstant.M3S;
//				byte[] cmd = WRITES.clone();
//				cmd[reg] = CtrConstant.M3S;
				//发送关闭指令
				for (int i = 0; i < 6; i++) {
					req = mbs.sendMsg(cmd, address, 12);
					if(i == 5){
						throw new RuntimeException("关闭命令发送超时！");
					}
					if(req != null && req.length == 12 && CtrConstant.M3S == req[11]){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R3S, equip.getEquipName() + ":命令已发送，正在关闭！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.R3S);
						break;
					}
				}
				//获取状态
				for (int i = 0; i < 30; i++) {
					req = mbs.sendMsg(READ, address, len);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(req != null){
						if(req[index] == CtrConstant.R3SWA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R3SWA, equip.getEquipName() + "：关闭成功！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3SWA);
							break;
						} else if(req[index] == CtrConstant.R3SWT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R3SWT, equip.getEquipName() + "：关窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3SWT);
							addError(an0, equip.getEquipName() + ":关窗超时！");
							break;
						} else if(req[index] == CtrConstant.R3WN){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R3WN, equip.getEquipName() + "：窗户故障！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3WN);
							addError(an0, equip.getEquipName() + ":窗户故障！");
							break;
						} else if(req[index] == CtrConstant.R3OWT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R3OWT, equip.getEquipName() + "：开窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3OWT);
							addError(an0, equip.getEquipName() + ":开窗超时！");
							break;
						} else if(req[index] == CtrConstant.R3FN){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD1, equip.getModel(), CtrConstant.R3FN, equip.getEquipName() + "：风机未运行！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3FN);
							addError(an0, equip.getEquipName() + ":风机未运行！");
							break;
						} else if(req[index] == CtrConstant.R3F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD1, equip.getModel(), CtrConstant.R3F, equip.getEquipName() + "：负载超载！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3F);
							addError(an0, equip.getEquipName() + ":负载超载！");
							break;
						}
					}
					if(i == 29){
						throw new RuntimeException("关闭状态查询超出步长");
					}
				}
			} else if(equip.getStatus() == CtrConstant.R3SWA || equip.getStatus() == CtrConstant.R3SWM){//关闭状态发送开启指令
				byte[] cmd = WRITE.clone();
				cmd[9] = (byte)(cmd[9] + equip.getRegisterWay());
				cmd[11] = CtrConstant.M3O;
//				byte[] cmd = WRITES.clone();
//				cmd[reg] = CtrConstant.M3O;
				//发送关闭指令
				for (int i = 0; i < 6; i++) {
					req = mbs.sendMsg(cmd, address, 12);
					if(i == 5){
						throw new RuntimeException("启动命令发送超时！");
					}
					if(req != null && req.length == 12 && CtrConstant.M3O == req[11]){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.M3O, equip.getEquipName() + ":通讯连接成功，命令已发送，正在开启，请稍候！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.M3O);
						break;
					}
				}
				//获取状态
				for (int i = 0; i < 30; i++) {
					req = mbs.sendMsg(READ, address, len);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(req != null){
						if(req[index] == CtrConstant.R3OFA){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R3OFA, equip.getEquipName() + ":风机已经启动！")));
							equip.setStatus(CtrConstant.R3OFA);
							break;
						} else if (req[index] == CtrConstant.R3OWI){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R3OWI, equip.getEquipName() + ":正在开窗！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3OWI);
						} else if(req[index] == CtrConstant.R3OWA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING1, equip.getModel(), CtrConstant.R3OWA, equip.getEquipName() + ":窗户已经开启！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3OWA);
						} else if(req[index] == CtrConstant.R3SWT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R3SWT, equip.getEquipName() + "：关窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3SWT);
							addError(an0, equip.getEquipName() + ":关窗超时！");
							break;
						} else if(req[index] == CtrConstant.R3WN){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R3WN, equip.getEquipName() + "：窗户故障！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3WN);
							addError(an0, equip.getEquipName() + ":窗户故障！");
							break;
						} else if(req[index] == CtrConstant.R3OWT){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R3OWT, equip.getEquipName() + "：开窗超时！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3OWT);
							addError(an0, equip.getEquipName() + ":开窗超时！");
							break;
						} else if(req[index] == CtrConstant.R3FN){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD1, equip.getModel(), CtrConstant.R3FN, equip.getEquipName() + "：风机未运行！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3FN);
							addError(an0, equip.getEquipName() + ":风机未运行！");
							break;
						} else if(req[index] == CtrConstant.R3F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD1, equip.getModel(), CtrConstant.R3F, equip.getEquipName() + "：负载超载！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R3F);
							addError(an0, equip.getEquipName() + ":负载超载！");
							break;
						}
					}
					if(i == 29){
						throw new RuntimeException("开启状态查询超出步长");
					}
				}
			}
		}
		return tag;
	}
	
	@Override
	public void ctrs(String houseNo, int[] equipNos) {
		
	}
	
	private void addError(AlarmNotes an0, String msg){
		an0.setNums(an0.getNums() + 1);
		an0.setFaultStr((an0.getFaultStr() == null ? "" : an0.getFaultStr()) + msg + "|");
	}
}
