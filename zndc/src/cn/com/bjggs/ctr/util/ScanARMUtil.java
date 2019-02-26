package cn.com.bjggs.ctr.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.domain.CtrsNotes;

public class ScanARMUtil {
	
	public static final Map<Integer, Integer> smartStatus = new LinkedHashMap<Integer, Integer>();
	
	public static final Map<Integer, CtrsNotes> smartNotes = new LinkedHashMap<Integer, CtrsNotes>();
	
	public static final Map<String, List<Equipment>> oneDiEquip = new LinkedHashMap<String, List<Equipment>>();
	
	public static final Map<String, List<Equipment>> twoDiEquip = new LinkedHashMap<String, List<Equipment>>();
	
	private static final Log log = Logs.getLog(CtrUtil.class);
	
	public static Dao dao;
	
	public static final void initSmartLast(final Dao d) {
		dao = d;
		//将各设备最后一次的能耗记录放入内存
		List<Equipment> equips = dao.query(Equipment.class, Cnd.cri());
		for (Equipment e : equips) {
			Sql sql = Sqls.create("SELECT * FROM CtrsNotes where equipNo = " + e.getEquipNo() +  " Order by startTime limit 0,1");
			sql.setCallback(Sqls.callback.entities());
			sql.setEntity(dao.getEntity(CtrsNotes.class));
			dao.execute(sql);
			List<CtrsNotes> ctrs = sql.getList(CtrsNotes.class);
			for (CtrsNotes ctr : ctrs) {
				smartNotes.put(e.getEquipNo(), ctr);
			}
			smartStatus.put(e.getEquipNo(), 0);
		}
		
		//初始化设备状态信息
		for(String houseNo : HouseUtil.houseMap.keySet()){
			//将所有设备按照不同的DI放入内存，供扫描设备使用
			CtrResults res = CtrUtil.lasts.get(houseNo);
			List<Equipment> oneDiList = new LinkedList<Equipment>();
			List<Equipment> twoDiList = new LinkedList<Equipment>();
			if(res != null){
				Map<Integer, Equipment> es = res.getEquips();
				if(es != null){
					for (Map.Entry<Integer, Equipment> entry : es.entrySet()) {
						Equipment equip = entry.getValue();
						if (equip.getModel() == CtrConstant.ARMWIND) {
							twoDiList.add(equip);
						} else {
							oneDiList.add(equip);
						}
					}
				}
			}
			oneDiEquip.put(houseNo, oneDiList);
			twoDiEquip.put(houseNo, twoDiList);
			
			//调用扫描设备的方法
//			refreshSmartStatus(houseNo);
		}
	}
	
	/**
	 * 扫描顺义一体化，记录设备启停记录
	 * @author	yucy
	 * @date	2018-04-3
	 * @param	houseNo
	 */
	public static final void refreshSmartStatus(final String houseNo){
		EquipIps ips = HouseUtil.get(houseNo, TypeHouseConf.EIPS.code(), EquipIps.class);
		List<Equipment> oneDiList = ScanARMUtil.oneDiEquip.get(houseNo);
		List<Equipment> twoDiList = ScanARMUtil.twoDiEquip.get(houseNo);
		String url;
		String val;
		int[] armArr;
		//遍历所有单DI的设备
		try {
			if (!Lang.isEmpty(oneDiList) && Strings.isNotBlank(ips.getArmDioIp())) {
				for (int i = 0; i < oneDiList.size();i=+16) {
//				int index = 0; //用于表示list下标
					int[] diArr = null;
					if ((oneDiList.size()-i)/16 >= 1) {//如果剩余长度大于等于16,创建长度为16的数组
						diArr = new int[16];
						for (int j = 0; j < 16; j++) {
							diArr[j] = oneDiList.get(i+j).getDiWay1();
						}
					} else if ((oneDiList.size()-i)/16 == 0) {//如果剩余长度小于16，创建长度为剩余长度
						diArr = new int[oneDiList.size()-i];
						for (int j = 0; j < (oneDiList.size()-i); j++) {
							diArr[j] = oneDiList.get(i+j).getDiWay1();
						}
					}
					synchronized (ips.getArmDioIp()) {
						url = ARMUtil.getDIUrl(ips.getArmDioIp(), diArr);
						val = ARMCommUtil.SendGet(url);
					}
					armArr = ARMUtil.getDIResult(val);
					//将值取回后，for循环依次判断状态有没有改变
					for (int j = 0; j < diArr.length; j++) {
						int equipNo = oneDiList.get(i+j).getEquipNo();
						int ns = smartStatus.get(equipNo);
						boolean tag = false;
						if (smartNotes.get(equipNo) != null) {
							tag = smartNotes.get(equipNo).getStatus() == 1 ? true : false;
						}
						if (ARMUtil.isOne(j, armArr)) ns = 1; //开启状态
						if (ARMUtil.isZero(j, armArr)) ns = 0; //关闭状态
						
						if(tag){//如果原来是开启的
							if(ns == 0){//现在关闭,说明设备停止
								oneDiList.get(i+j).setStatus(CtrConstant.R2SA);
								CtrsNotes pns = smartNotes.get(equipNo);
								if (pns != null) {
									pns.setEnd();
									dao.update(pns);
								}
							}
						} else {
							if(ns == 1){
								oneDiList.get(i+j).setStatus(CtrConstant.R2OA);
								CtrsNotes pns = new CtrsNotes(oneDiList.get(i+j), CtrConstant.IOT);
								dao.insert(pns);
								smartNotes.put(equipNo, pns);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		
		//遍历所有通风窗设备
		try {
			if (!Lang.isEmpty(twoDiList) && Strings.isNotBlank(ips.getArmDioIp())) {
				for (int i = 0; i < twoDiList.size(); i=i+8) {
//				int index = 0;
					int[] diArr = null;
					if ((twoDiList.size()-i)/8 >= 1) {//如果剩余长度大于等于16,创建长度为16的数组
						diArr = new int[16];
						for (int j = 0; j < diArr.length; j=j+2) {
							diArr[j] = twoDiList.get(i+(j/2)).getDiWay1();
							diArr[j+1] = twoDiList.get(i+(j/2)).getDiWay3();
						}
					} else if ((twoDiList.size()-i)/8 == 0) {//如果剩余长度小于16，创建长度为剩余长度
						diArr = new int[(twoDiList.size()-i)*2];
						for (int j = 0; j < diArr.length; j=j+2) {
							diArr[j] = twoDiList.get(i+(j/2)).getDiWay1();
							diArr[j+1] = twoDiList.get(i+(j/2)).getDiWay3();
						}
					}
					synchronized (ips.getArmDioIp()) {
						url = ARMUtil.getDIUrl(ips.getArmDioIp(), diArr);
						val = ARMCommUtil.SendGet(url);
					}
					armArr = ARMUtil.getDIResult(val);
					//将值取回后，for循环依次判断状态有没有改变
					for (int j = 0; j < diArr.length; j=j+2) {
						int equipNo = twoDiList.get(i+(j/2)).getEquipNo();
						int ns = smartStatus.get(equipNo);
						boolean tag = false;
						if (smartNotes.get(equipNo) != null) {
							tag = smartNotes.get(equipNo).getStatus() == 1 ? true : false;
						}
						if (ARMUtil.isOne(j, armArr) && ARMUtil.isZero(j+1, armArr)) ns = 1;//开启状态
						if (ARMUtil.isZero(j, armArr) && ARMUtil.isOne(j+1, armArr)) ns = 0;//关闭状态
						
						if(tag){//如果原来是开启的
							if(ns == 0){//现在关闭,说明设备停止
								twoDiList.get(i+(j/2)).setStatus(CtrConstant.R1CA);
								CtrsNotes pns = smartNotes.get(equipNo);
								if (pns != null) {
									pns.setEnd();
									dao.update(pns);
								}
							}
						} else {
							if(ns == 1){
								twoDiList.get(i+(j/2)).setStatus(CtrConstant.R1OA);
								CtrsNotes pns = new CtrsNotes(twoDiList.get(i+(j/2)), CtrConstant.IOT);
								dao.insert(pns);
								smartNotes.put(equipNo, pns);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}
}
