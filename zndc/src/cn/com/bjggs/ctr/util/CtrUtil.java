package cn.com.bjggs.ctr.util;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.domain.CtrsNotes;
import cn.com.bjggs.ctr.domain.WaterDatars;
import cn.com.bjggs.power.enums.TypePowerBoard;

public class CtrUtil{
	
	public static final Map<String, CtrResults> lasts = new LinkedHashMap<String, CtrResults>();
	
	private static final Log log = Logs.getLog(CtrUtil.class);
	
	public static Dao dao;
	
	public static final void initCtrLast(final Dao d) {
		dao = d;
		List<Equipment> list;
		CtrResults res;
		Map<Integer, Equipment> es;
		Criteria cri;
		for(String houseNo : HouseUtil.houseMap.keySet()){
			cri = Cnd.cri();
			cri.where().andEquals("houseNo", houseNo);
			cri.getOrderBy().asc("type");
			cri.getOrderBy().asc("equipNo");
			list = dao.query(Equipment.class, cri);
			res = new CtrResults(houseNo);
			es = new LinkedHashMap<Integer, Equipment>();
			for(Equipment equip : list){
				es.put(equip.getEquipNo(), equip);
			}
			res.setEquips(es);
			lasts.put(houseNo, res);
			try {
				EquipIps ips = HouseUtil.get(houseNo, TypeHouseConf.EIPS.code(), EquipIps.class);
				if (ips != null) {
					if (ips.getBoardType() == TypePowerBoard.ARM.val()) {
						ScanARMUtil.refreshSmartStatus(houseNo);
					} else {
						CtrUtil.refreshEquipStatus(houseNo);
					}
				}
			} catch (Exception e) {}
		}
		//设备状态刷新定时任务
		QuartzUtil.addJob("scanEquipStatus", EquipScanJob.class, "0 0/" + PropsUtil.getInteger("ctr.scan.time", 1) + " * * * ?");
	}
	
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD9, 0x39, 0x00, 0x0D};
	
	private static final byte[] READ_AIR = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, 0x01, (byte)0x8F, 0x00, (byte)0x98};
	
	private static final byte[] READ_DIO = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0xD6, (byte)0xD7, 0x00, 0x02};
	
	private static final int len = CtrConstant.CTR_READ_LEN + READ[11] * 2; 
	
	public static final void helps(CtrResults res, Equipment equip, byte[] bs){
		if(bs == null){
			res.setNone(res.getNone() + 1);
		} else {
			byte tag = bs[12 + equip.getRegisterWay() * 2];
			if((tag & 0xFF) >= 0x90){
				res.setWarn(res.getWarn() + 1);
				res.setTag(1);
			} else {
				res.setNarmal(res.getNarmal() + 1);
			}
			equip.setStatus(tag);
		}
	}
	
	public static final void helps2(CtrResults res, Equipment equip, byte[] bs){
		if(bs == null){
			res.setNone(res.getNone() + 1);
		} else {
			byte tag = bs[equip.getDiWay1()/3];
			if((tag & 0xFF) >= 0x90){
				res.setWarn(res.getWarn() + 1);
				res.setTag(1);
			} else {
				res.setNarmal(res.getNarmal() + 1);
			}
			equip.setStatus(tag);
		}
	}
	
	public static final void helps1(CtrResults res, Equipment equip, byte[] bs){
		if(bs == null || bs.length < 300){
			res.setNone(res.getNone() + 1);
		} else {
			int index = 9 + equip.getRegisterWay() * 38;
			byte tag = bs[index + 1];
			if(bs[index + 2] != 0 || bs[index + 3] != 0 
					|| bs[index + 4] != 0 || bs[index + 5] != 0
					|| bs[index + 6] != 0 || bs[index + 7] != 0
					|| bs[index + 8] != 0 || bs[index + 9] != 0){
				res.setWarn(res.getWarn() + 1);
				res.setTag(1);
				equip.setStatus(CtrConstant.IOF);
			} else {
				res.setNarmal(res.getNarmal() + 1);
				equip.setStatus(tag);
			}
		}
	}
	
	public static final void refreshEquipStatus(final String houseNo){
		EquipIps ips = HouseUtil.get(houseNo, TypeHouseConf.EIPS.code(), EquipIps.class);
		CtrMbs mbs;
		//获取所有状态;
		byte[] dio = null;
		byte[] airs = null;
		try {
			if(Strings.isNotBlank(ips.getDioIp())){
				mbs = new CtrMbs();
				dio = mbs.sendMsg(READ_DIO, new InetSocketAddress(ips.getDioIp(), ips.getDioPort()), 11);
				dio = new byte[]{dio[9], dio[10]};
				Thread.sleep(157);
				airs = mbs.sendMsg(READ_AIR, new InetSocketAddress(ips.getDioIp(), ips.getDioPort()), 320);
			}
		} catch (Exception e) {}
		byte[] wind1 = null;
		try {
			if(Strings.isNotBlank(ips.getWindIp1())){
				mbs = new CtrMbs();
				wind1 = mbs.sendMsg(READ, new InetSocketAddress(ips.getWindIp1(), ips.getWindPort1()), len);
			}
		} catch (Exception e) {}
		byte[] wind2 = null;
		try {
			if(Strings.isNotBlank(ips.getWindIp2())){
				mbs = new CtrMbs();
				wind2 = mbs.sendMsg(READ, new InetSocketAddress(ips.getWindIp2(), ips.getWindPort2()), len);
			}
		} catch (Exception e) {}
		byte[] wind3 = null;
		try {
			if(Strings.isNotBlank(ips.getWindIp3())){
				mbs = new CtrMbs();
				wind3 = mbs.sendMsg(READ, new InetSocketAddress(ips.getWindIp3(), ips.getWindPort3()), len);
			}
		} catch (Exception e) {}
		byte[] wone1 = null;
		try {
			if(Strings.isNotBlank(ips.getWoneIp())){
				mbs = new CtrMbs();
				wone1 = mbs.sendMsg(READ, new InetSocketAddress(ips.getWoneIp(), ips.getWonePort()), len);
			}
		} catch (Exception e) {}
		byte[] onew = null;
		try {
			if(Strings.isNotBlank(ips.getOnewIp())){
				mbs = new CtrMbs();
				onew = mbs.sendMsg(READ, new InetSocketAddress(ips.getOnewIp(), ips.getOnewPort()), len);
			}
		} catch (Exception e) {}
		byte[] twow = null;
		try {
			if(Strings.isNotBlank(ips.getTwowIp())){
				mbs = new CtrMbs();
				twow = mbs.sendMsg(READ, new InetSocketAddress(ips.getTwowIp(), ips.getTwowPort()), len);
			}
		} catch (Exception e) {}
		//将状态与相应的设备绑定
		CtrResults res = lasts.get(houseNo);
		if(res != null){
			Map<Integer, Equipment> es = res.getEquips();
			if(es != null){
				Equipment equip;
				for(Map.Entry<Integer, Equipment> entry : es.entrySet()){
					equip = entry.getValue();
					//上次检测设备状态
					byte oldStatus = equip.getStatus();
					byte nowStatus;
					switch (equip.getModel()) {
					case CtrConstant.DIDO:
						if(dio == null){
							res.setNone(res.getNone() + 1);
						} else {
							if(equip.getDiWay1()/8 == 1){
								equip.setStatus(MathUtil.getBitByIndex(dio[0], equip.getDiWay1()%8));
							} else {
								equip.setStatus(MathUtil.getBitByIndex(dio[1], equip.getDiWay1()%8));
							}
							res.setNarmal(res.getNarmal() + 1);
							//本次检测窗户状态
							nowStatus = equip.getStatus();
							
							//如果上次非关闭，本次关闭
							if (nowStatus == CtrConstant.IOC && oldStatus != CtrConstant.IOC) {
								CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
								if (pns != null) {
									pns.setEnd();
									dao.update(pns);
								}
							}
							//如果上次非开启，本次开启
							if (nowStatus == CtrConstant.IOT && oldStatus != CtrConstant.IOT){
								CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
								dao.insert(pns);
								ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
							}
						}
						break;
					case CtrConstant.WIND:
						if(equip.getModelWay() == 1){
							helps(res, equip, wind1);
						} else if(equip.getModelWay() == 2){
							helps(res, equip, wind2);
						} else {
							helps(res, equip, wind3);
						}
						nowStatus = equip.getStatus();
						if (nowStatus == CtrConstant.R1CA && oldStatus != CtrConstant.R1CA) {
							CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
							if (pns != null) {
								pns.setEnd();
								dao.update(pns);
							}
						}
						if (nowStatus == CtrConstant.R1OA && oldStatus != CtrConstant.R1OA){
							CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
							dao.insert(pns);
							ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
						}
						break;
					case CtrConstant.WONE:
						helps(res, equip, wone1);
						
						nowStatus = equip.getStatus();
						//上次非关闭状态，本次关闭状态
						if ((nowStatus == CtrConstant.R3SWA && nowStatus == CtrConstant.R3SFA) && (oldStatus != CtrConstant.R3SWA || oldStatus != CtrConstant.R3SFA)) {
							CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
							if (pns != null) {
								pns.setEnd();
								dao.update(pns);
							}
						}
						if ((nowStatus == CtrConstant.R3OWA || nowStatus == CtrConstant.R3OFA) && (oldStatus != CtrConstant.R3OWA || oldStatus != CtrConstant.R3OFA)){
							CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
							dao.insert(pns);
							ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
						}
						break;
					case CtrConstant.ONEW:
						helps(res, equip, onew);
						
						nowStatus = equip.getStatus();
						if (nowStatus == CtrConstant.R2SA && oldStatus != CtrConstant.R2SA) {
							CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
							if (pns != null) {
								pns.setEnd();
								dao.update(pns);
							}
						}
						if (nowStatus == CtrConstant.R2OA && oldStatus != CtrConstant.R2OA){
							CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
							dao.insert(pns);
							ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
						}
						break;
					case CtrConstant.TWOW:
						helps(res, equip, twow);
						
						nowStatus = equip.getStatus();
						if (nowStatus == CtrConstant.R4SA && oldStatus != CtrConstant.R4SA) {
							CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
							if (pns != null) {
								pns.setEnd();
								dao.update(pns);
							}
						}
						if ((nowStatus == CtrConstant.R4OA || nowStatus == CtrConstant.R4OsA) && (oldStatus != CtrConstant.R4OA && oldStatus != CtrConstant.R4OsA)){
							CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
							dao.insert(pns);
							ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
						}
						break;
					case CtrConstant.AIRC:
						helps1(res, equip, airs);
						
						nowStatus = equip.getStatus();
						if (nowStatus == CtrConstant.IOC && oldStatus != CtrConstant.IOC) {
							CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
							if (pns != null) {
								pns.setEnd();
								dao.update(pns);
							}
						}
						if (nowStatus == CtrConstant.IOT  && oldStatus != CtrConstant.IOT){
							CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
							dao.insert(pns);
							ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
						}
						
						break;
					case CtrConstant.ARMDIO:
						helps2(res, equip, airs);
						nowStatus = equip.getStatus();
						if (nowStatus == CtrConstant.IOC && oldStatus != CtrConstant.IOC) {
							CtrsNotes pns = ScanARMUtil.smartNotes.get(equip.getEquipNo());
							if (pns != null) {
								pns.setEnd();
								dao.update(pns);
							}
						}
						if (nowStatus == CtrConstant.IOT  && oldStatus != CtrConstant.IOT){
							CtrsNotes pns = new CtrsNotes(equip, CtrConstant.IOT);
							dao.insert(pns);
							ScanARMUtil.smartNotes.put(equip.getEquipNo(), pns);
						}
						break;
					default:
						res.setNone(res.getNone() + 1);
						break;
					}
				}
			}
		}
	}
	
	public static void clear(String houseNo){
		byte[] cmd = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x01, 0x10, (byte)0xD9, 0x3A, 0x00, 0x0C, 0x18, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0, 0x00, (byte)0xA0};
		EquipIps ips = HouseUtil.get(houseNo, TypeHouseConf.EIPS.code(), EquipIps.class);
		if(ips != null){
			CtrMbs mbs;
			try {
				if(Strings.isNotBlank(ips.getWindIp1())){
					mbs = new CtrMbs();
					mbs.sendMsg(cmd, new InetSocketAddress(ips.getWindIp1(), ips.getWindPort1()), 10);
				}
			} catch (Exception e) {
				log.error("通风模块1状态清空失败！");
			}
			try {
				if(Strings.isNotBlank(ips.getWindIp2())){
					mbs = new CtrMbs();
					mbs.sendMsg(cmd, new InetSocketAddress(ips.getWindIp2(), ips.getWindPort2()), 10);
				}
			} catch (Exception e) {
				log.error("通风模块2状态清空失败！");
			}
			try {
				if(Strings.isNotBlank(ips.getWindIp3())){
					mbs = new CtrMbs();
					mbs.sendMsg(cmd, new InetSocketAddress(ips.getWindIp3(), ips.getWindPort3()), 10);
				}
			} catch (Exception e) {
				log.error("通风模块3状态清空失败！");
			}
			try {
				if(Strings.isNotBlank(ips.getOnewIp())){
					mbs = new CtrMbs();
					mbs.sendMsg(cmd, new InetSocketAddress(ips.getOnewIp(), ips.getOnewPort()), 10);
				}
			} catch (Exception e) {
				log.error("单向风机模块状态清空失败！");
			}
			try {
				if(Strings.isNotBlank(ips.getTwowIp())){
					mbs = new CtrMbs();
					mbs.sendMsg(cmd, new InetSocketAddress(ips.getTwowIp(), ips.getTwowPort()), 10);
				}
			} catch (Exception e) {
				log.error("双向风机模块状态清空失败！");
			}
			try {
				if(Strings.isNotBlank(ips.getArmDioIp())){
					Map<Integer, Equipment> equips = CtrUtil.lasts.get(houseNo).getEquips();
					for (Map.Entry<Integer, Equipment> entry : equips.entrySet()) {
						Equipment e = entry.getValue();
						if (e.getModel() == CtrConstant.ARMDIO) {
							e.setStatus(CtrConstant.IOC);
						}
					}
				}
			} catch (Exception e) {
				log.error("ARM内环流状态清空失败！");
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			refreshEquipStatus(houseNo);
		}
	}
	
	public static boolean isClose(Equipment ep){
		if((ep.getStatus() & 0xFF) > 0x90){
			return true;
		}
		switch (ep.getModel()) {
		case 1: return ep.getStatus() == CtrConstant.R1CA; 
		case 2: return ep.getStatus() == CtrConstant.R2SA; 
		case 3: return ep.getStatus() == CtrConstant.R3SWA;
		case 4: return ep.getStatus() == CtrConstant.R4SA;
		case 5: return ep.getStatus() == CtrConstant.IOC;
		case 6: return ep.getStatus() == CtrConstant.IOC;
		default: return true;
		}
	}
	
	public static boolean isOpen(Equipment ep){
		if((ep.getStatus() & 0xFF) > 0x90){
			return true;
		}
		switch (ep.getModel()) {
		case 1: return ep.getStatus() == CtrConstant.R1OA; 
		case 2: return ep.getStatus() == CtrConstant.R2OA; 
		case 3: return ep.getStatus() == CtrConstant.R3OFA;
		case 4: return ep.getStatus() == CtrConstant.R4OA;
		case 5: return ep.getStatus() == CtrConstant.IOT;
		case 6: return ep.getStatus() == CtrConstant.IOT;
		case 7: return ep.getStatus() == CtrConstant.IOT;
		default: return true;
		}
	}
	
	public static WaterDatars getWaterDatars(int hum, int temp, int type){
		return dao.fetch(WaterDatars.class, Cnd.where("airHumidity", "=", hum).and("airTemp", "=", temp).and("grainCode", "=", type));
	}
	
	/*
	public static void initWaters(){
		try {
			InputStream stream = new FileInputStream("d:\\waters.xls");
			Workbook rwb =  Workbook.getWorkbook(stream);
			Sheet sheet = rwb.getSheet(4);
			Cell cell = null;
			double d1;
			double d2;
			List<WaterDatars> list = new ArrayList<WaterDatars>(2600);
			for(int i = 2; i < 73; i++){
				for(int j = 2; j < 38; j++){
					cell = sheet.getCell(j, i);
					d1 = Double.valueOf(cell.getContents());
					cell = sheet.getCell(j, i + 74);
					d2 = Double.valueOf(cell.getContents());
					list.add(new WaterDatars(5, 18+i, j-2, d2, d1));
				}
			}
			dao.insert(list);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}
	*/
}
