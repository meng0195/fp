package cn.com.bjggs.ctr.util;

import java.net.InetSocketAddress;

import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.ctr.domain.CtrMsg;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 设备控制单项风机模式工具
 * @author	wc
 * @date	2017-10-01
 */
public class ModelOnew implements CtrModel{
	
	//写多个指令
	//private static final byte[] WRITES = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x01, 0x10, (byte)0xD9, 0x3A, 0x00, 0x0D, 0x18, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00, 0x12, 0x00};
	//写单个指令
	private static final byte[] WRITE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, 0x3A, 0x00, 0x00};
	//读指令全部
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD9, 0x39, 0x00, 0x0D};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2;
	
	private InetSocketAddress address;
	
	private CtrMbs mbs = new CtrMbs();
	
	public ModelOnew(EquipIps ips){
		this.address = new InetSocketAddress(ips.getOnewIp(), ips.getOnewPort());
	}
	
	@Override
	public int ctr(Equipment equip,  AlarmNotes an0) {
		int tag = CtrConstant.CTR_OPEN_TAG;
		//验证模式是否正确
		byte[] req = mbs.sendMsg(READ, address, len);
		if(req == null || req.length < 11 || req[10]%16 != CtrConstant.ONEW) throw new RuntimeException("控制板模式验证错误！");
		if(req[10]/16 != CtrConstant.LINE) throw new RuntimeException("当前非联网模式，不能通过系统完成控制！");
		if(equip != null){
			int index = 12 + equip.getRegisterWay() * 2;
//			int reg = 14 + equip.getRegisterWay() * 2;
			if(equip.getStatus() == CtrConstant.R2OA){
				tag = CtrConstant.CTR_CLOSE_TAG;
				byte[] cmd = WRITE.clone();
				cmd[9] = (byte)(cmd[9] + equip.getRegisterWay());
				cmd[11] = CtrConstant.M2S;
//				byte[] cmd = WRITES.clone();
//				cmd[reg] = CtrConstant.M2S;
				//发送关闭指令
				for (int i = 0; i < 6; i++) {
					req = mbs.sendMsg(cmd, address, 12);
					if(i == 5){
						throw new RuntimeException("关闭命令发送超时！");
					}
					if(req != null && req.length == 12 && CtrConstant.M2S == req[11]){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.M2S, equip.getEquipName() + ":正在关闭设备，请稍候！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.M2S);
						break;
					}
					try {
						Thread.sleep(157);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//获取状态
				for (int i = 0; i < 60; i++) {
					req = mbs.sendMsg(READ, address, len);
					if(req != null){
						if(req[index] == CtrConstant.R2SA){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R2SA, equip.getEquipName() + ":设备已经停止！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R2SA);
							break;
						} else if(req[index] == CtrConstant.R2N){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2N, equip.getEquipName() + ":故障！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R2N);
							addError(an0, equip.getEquipName() + ":故障！");
							break;
						} else if(req[index] == CtrConstant.R2F){
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2F, equip.getEquipName() + ":负载超载！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R2F);
							addError(an0, equip.getEquipName() + ":负载超载！");
							break;
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(i == 59){
						throw new RuntimeException("关闭状态查询超出步长");
					}
				}
				if(Strings.isNotBlank(equip.getBindIp())){
					//关闭绑定风窗
					byte[] bind = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, (byte)(0x3A + equip.getBindRegister()), 0x00, CtrConstant.M1C};
					CtrMbs bm = new CtrMbs();
					InetSocketAddress ad = null;
					EquipIps ips = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.EIPS.code(), EquipIps.class);
					if(equip.getBindModel() == 1){
						ad = new InetSocketAddress(ips.getWindIp1(), ips.getWindPort1());
					} else if(equip.getBindModel() == 2){
						ad = new InetSocketAddress(ips.getWindIp2(), ips.getWindPort2());
					} else {
						ad = new InetSocketAddress(ips.getWindIp3(), ips.getWindPort3());
					}
					//发送关闭指令
					for (int i = 0; i < 6; i++) {
						req = bm.sendMsg(bind, ad, 12);
						if(i == 5){
							throw new RuntimeException("关窗命令发送超时！");
						}
						if(req != null && req.length == 12 && req[11] == CtrConstant.M1C){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING1, equip.getModel(), CtrConstant.R2CW, equip.getEquipName() + ":正在关窗！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R2CW);
							break;
						}
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					index = 12 + equip.getBindRegister() * 2;
					//获取状态
					for (int i = 0; i < 60; i++) {
						req = mbs.sendMsg(READ, ad, len);
						if(req != null){
							if(req[index] == CtrConstant.R1CA){
								//前台同步讯息
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.R2SA, equip.getEquipName() + ":关闭成功！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R2SA);
								break;
							} else if(req[index] == CtrConstant.R1SA){
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_HALF, equip.getModel(), CtrConstant.R1SA, equip.getEquipName() + ":被干预，中途停止！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R2SA);
								break;
							} else if(req[index] == CtrConstant.R1F){
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1F, equip.getEquipName() + ":故障！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R1F);
								addError(an0, equip.getEquipName() + ":故障！");
								break;
							} else if(req[index] == CtrConstant.R1CT){
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R1CT, equip.getEquipName() + ":关闭超时！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R1CT);
								addError(an0, equip.getEquipName() + ":关闭超时！");
								break;
							}
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(i == 59){
							throw new RuntimeException("关窗状态查询超出步长");
						}
					}
				}
			} else if(equip.getStatus() == CtrConstant.R2SA){//关闭状态发送开启指令
				if(Strings.isNotBlank(equip.getBindIp())){
					byte[] bind = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0xD9, (byte)(0x3A + equip.getBindRegister()), 0x00, CtrConstant.M1O};
					CtrMbs bm = new CtrMbs();
					InetSocketAddress ad = null;
					EquipIps ips = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.EIPS.code(), EquipIps.class);
					if(equip.getBindModel() == 1){
						ad = new InetSocketAddress(ips.getWindIp1(), ips.getWindPort1());
					} else if(equip.getBindModel() == 2){
						ad = new InetSocketAddress(ips.getWindIp2(), ips.getWindPort2());
					} else {
						ad = new InetSocketAddress(ips.getWindIp3(), ips.getWindPort3());
					}
					for (int i = 0; i < 6; i++) {
						req = bm.sendMsg(bind, ad, 12);
						if(i == 5){
							throw new RuntimeException("启动命令发送超时！");
						}
						if(req != null && req.length == 12 && req[11] == CtrConstant.M1O){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING1, equip.getModel(), CtrConstant.R2OW, equip.getEquipName() + ":正在开窗！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R2OW);
							break;
						}
					}
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					index = 12 + equip.getBindRegister() * 2;
					//获取状态
					for (int i = 0; i < 60; i++) {
						try{
							req = bm.sendMsg(READ, ad, len);
							if(req != null){
								if(req[index] == CtrConstant.R1OA){
									//前台同步讯息
									DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R2O, equip.getEquipName() + ":开窗到位！")));
									//同步当前设备状态
									equip.setStatus(CtrConstant.R1OA);
									tag = 4;
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
						if(i == 59){
							throw new RuntimeException("开窗状态查询超出步长");
						}
					}
				}
				if(Strings.isBlank(equip.getBindIp()) || tag == 4){
					tag = 1;
					byte[] cmd = WRITE.clone();
					cmd[9] = (byte)(cmd[9] + equip.getRegisterWay());
					cmd[11] = CtrConstant.M2O;
	//				byte[] cmd = WRITES.clone();
	//				cmd[reg] = CtrConstant.M2O;
					for (int i = 0; i < 6; i++) {
						req = mbs.sendMsg(cmd, address, 12);
						if(i == 5){
							throw new RuntimeException("启动命令发送超时！");
						}
						if(req != null && req.length == 12 && CtrConstant.M2O == req[11]){
							//前台同步讯息
							DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.R2O, equip.getEquipName() + ":正在开启风机，请稍候！")));
							//同步当前设备状态
							equip.setStatus(CtrConstant.R2O);
							break;
						}
					}
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					index = 12 + equip.getRegisterWay() * 2;
					//获取状态
					for (int i = 0; i < 60; i++) {
						req = mbs.sendMsg(READ, address, 35);
						if(req != null){
							if(req[index] == CtrConstant.R2OA){
								//前台同步讯息
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.R2OA, equip.getEquipName() + ":风机已经开启！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R2OA);
								break;
							} else if(req[index] == CtrConstant.R2N){
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2N, equip.getEquipName() + ":故障！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R2N);
								addError(an0, equip.getEquipName() + ":故障！");
								break;
							} else if(req[index] == CtrConstant.R2F){
								DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.R2F, equip.getEquipName() + ":负载超载！")));
								//同步当前设备状态
								equip.setStatus(CtrConstant.R2F);
								addError(an0, equip.getEquipName() + ":负载超载！");
								break;
							}
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(i == 59){
							throw new RuntimeException("开窗状态查询超出步长");
						}
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
