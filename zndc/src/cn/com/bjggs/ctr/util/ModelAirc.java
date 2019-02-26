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
 * 设备控制空调模式工具
 * @author	wc
 * @date	2017-10-01
 */
public class ModelAirc implements CtrModel{
	
	private static final byte[] WRITE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x09, 0x01, 0x06, 0x01, (byte)0x8F, 0x00, 0x00};
	
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, 0x01, (byte)0x8F, 0x00, 0x13};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2;
	
	private InetSocketAddress address;
	
	private CtrMbs mbs = new CtrMbs();
	
	public ModelAirc(EquipIps ips){
		this.address = new InetSocketAddress(ips.getDioIp(), ips.getDioPort());
	}
	
	@Override
	public int ctr(Equipment equip,  AlarmNotes an0) {
		int tag = 0;
		byte adh = MathUtil.int2HByte((399 + equip.getRegisterWay() * 19));
		byte adl = MathUtil.int2LByte((399 + equip.getRegisterWay() * 19));
		byte[] cmd = READ.clone();
		cmd[8] = adh;
		cmd[9] = adl;
		byte[] req = mbs.sendMsg(cmd, address, len);
		if(req[10] == 1){//开启发关闭
			cmd = WRITE.clone();
			cmd[8] = adh;
			cmd[9] = adl;
			for (int i = 0; i < 6; i++) {
				req = mbs.sendMsg(cmd, address, 12);
				if(i == 5){
					throw new RuntimeException("关闭命令发送超时！");
				}
				if(req != null && req.length == 12 && CtrConstant.IOC == req[11]){
					//前台同步讯息
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.IOI, equip.getEquipName() + ":正在关闭设备，请稍候！")));
					//同步当前设备状态
					equip.setStatus(CtrConstant.IOI);
					break;
				}
				try {
					Thread.sleep(157);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			cmd = READ.clone();
			cmd[8] = adh;
			cmd[9] = adl;
			for (int i = 0; i < 30; i++) {
				req = mbs.sendMsg(cmd, address, len);
				if(req != null){
					if(req[10] == CtrConstant.IOC){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_CLOSE, equip.getModel(), CtrConstant.IOC, equip.getEquipName() + ":设备已经停止！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.IOC);
						tag = 1;
						break;
					} else if(req[11] != 0 || req[12] != 0 
							|| req[13] != 0 || req[14] != 0
							|| req[15] != 0 || req[16] != 0
							|| req[17] != 0 || req[18] != 0){
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.IOF, equip.getEquipName() + ":故障！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.IOF);
						tag = 0;
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
		} else {//其他发开启
			cmd = WRITE.clone();
			cmd[8] = adh;
			cmd[9] = adl;
			cmd[11] = 0x01;
			for (int i = 0; i < 6; i++) {
				req = mbs.sendMsg(cmd, address, 12);
				if(i == 5){
					throw new RuntimeException("开启命令发送超时！");
				}
				if(req != null && req.length == 12 && CtrConstant.IOT == req[11]){
					//前台同步讯息
					DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_ING, equip.getModel(), CtrConstant.IOI, equip.getEquipName() + ":正在开启设备，请稍候！")));
					//同步当前设备状态
					equip.setStatus(CtrConstant.IOI);
					break;
				}
				try {
					Thread.sleep(157);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			cmd = READ.clone();
			cmd[8] = adh;
			cmd[9] = adl;
			for (int i = 0; i < 30; i++) {
				req = mbs.sendMsg(cmd, address, len);
				if(req != null){
					if(req[10] == CtrConstant.IOT){
						//前台同步讯息
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_OPEN, equip.getModel(), CtrConstant.IOT, equip.getEquipName() + ":设备已经开启！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.IOT);
						tag = 1;
						break;
					} else if(req[11] != 0 || req[12] != 0 
							|| req[13] != 0 || req[14] != 0
							|| req[15] != 0 || req[16] != 0
							|| req[17] != 0 || req[18] != 0){
						DwrUtil.sendCtr(JsonUtil.toJson(new CtrMsg(equip.getHouseNo(), equip.getEquipNo(), CtrConstant.C_BAD, equip.getModel(), CtrConstant.IOF, equip.getEquipName() + ":故障！")));
						//同步当前设备状态
						equip.setStatus(CtrConstant.IOF);
						tag = 0;
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
		return tag;
	}
	
	@Override
	public void ctrs(String houseNo, int[] equipNos) {
	}
	
}
