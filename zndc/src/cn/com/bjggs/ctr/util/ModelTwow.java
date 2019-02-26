package cn.com.bjggs.ctr.util;

import java.net.InetSocketAddress;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制双向风机模式工具
 * @author	wc
 * @date	2017-10-01
 */
public class ModelTwow implements CtrModel{
	
	//写多个指令
	//private static final byte[] WRITES = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x01, 0x10, (byte)0xD9, 0x3A, 0x00, 0x0C, 0x18, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00, 0x14, 0x00};
	//写单个指令
	private static final byte[] WRITE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, 0x3A, 0x00, 0x00};
	//读指令全部
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD9, 0x39, 0x00, 0x0D};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2;
	
	private InetSocketAddress address;
	
	private CtrMbs mbs = new CtrMbs();
	
	public ModelTwow(EquipIps ips){
		this.address = new InetSocketAddress(ips.getTwowIp(), ips.getTwowPort());
	}
	
	@Override
	public int ctr(Equipment equip, AlarmNotes an0) {
		int tag = 0;
		//验证模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.TWOW) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		if(equip != null){
			int index = 12 + equip.getRegisterWay() * 2;
//			int reg = 14 + equip.getRegisterWay() * 2;
			//开窗到位，风机开启，风机关闭都发送关闭指令
			if(equip.getStatus() == CtrConstant.R4OA || equip.getStatus() == CtrConstant.R4OsA){
				byte[] cmd = WRITE.clone();
				cmd[9] = (byte)(cmd[9] + equip.getRegisterWay());
				cmd[11] = CtrConstant.M4S;
//				byte[] cmd = WRITES.clone();
//				cmd[reg] = CtrConstant.M4S;
				//发送关闭指令
				for (int i = 0; i < 6; i++) {
					req = mbs.sendMsg(cmd, address, 12);
					if(i == 5){
						throw new RuntimeException("关闭命令发送超时！");
					}
					if(req != null && req.length == 12 && CtrConstant.M4S == req[11]){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R4S, equip.getEquipName() + ":通讯连接成功，命令已发送，正在关闭，请稍候！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.R4S);
						break;
					}
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//获取状态
				for (int i = 0; i < 30; i++) {
					req = mbs.sendMsg(READ, address, len);
					if(req != null){
						if(req[index] == CtrConstant.R4SA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R4SA, equip.getEquipName() + ":风机已经停止！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R4SA);
							break;
						} else if(req[index] == CtrConstant.R4N){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R4N, equip.getEquipName() + ":故障:风机未运行！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R4N);
							addError(an0, equip.getEquipName() + ":风机未运行！");
							break;
						} else if(req[index] == CtrConstant.R4F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R4F, equip.getEquipName() + ":故障:负载超载！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R4F);
							addError(an0, equip.getEquipName() + ":负载超载！");
							break;
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i == 29){
						throw new RuntimeException("关闭状态查询超出步长");
					}
				}
			} else if(equip.getStatus() == CtrConstant.R4SA){//关闭状态发送开启指令
				tag = 1;
				byte[] cmd = WRITE.clone();
				cmd[9] = (byte)(cmd[9] + equip.getRegisterWay());
				cmd[11] = CtrConstant.M4O;
//				byte[] cmd = WRITES.clone();
//				cmd[reg] = CtrConstant.M4O;
				//发送关闭指令
				for (int i = 0; i < 6; i++) {
					req = mbs.sendMsg(cmd, address, 12);
					if(i == 5){
						throw new RuntimeException("启动命令发送超时！");
					}
					if(req != null && req.length == 12 && CtrConstant.M4O == req[11]){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.M4O, equip.getEquipName() + ":通讯连接成功，命令已发送，正在开启，请稍候！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.M4O);
						break;
					}
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//获取状态
				for (int i = 0; i < 30; i++) {
					req = mbs.sendMsg(READ, address, len);
					if(req != null){
						if(req[index] == CtrConstant.R4OA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R4OA, equip.getEquipName() + ":开启成功！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R4OA);
							break;
						} else if(req[index] == CtrConstant.R4N){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R4N, equip.getEquipName() + ":故障:风机没有运行！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R4N);
							addError(an0, equip.getEquipName() + ":风机没有运行！");
							break;
						} else if(req[index] == CtrConstant.R4F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R4F, equip.getEquipName() + ":故障:负载超载！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R4F);
							addError(an0, equip.getEquipName() + ":负载超载！");
							break;
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
