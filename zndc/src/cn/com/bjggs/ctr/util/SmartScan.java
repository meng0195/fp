package cn.com.bjggs.ctr.util;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.ctr.domain.SmartCondition;
import cn.com.bjggs.ctr.domain.SmartConf;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.ctr.enums.TypeSmart;
import cn.com.bjggs.msg.enums.TypeMsg;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmTT;

/**
 * 扫描处理智能模式
 * @author	wc
 * @date	2017-10-02
 */
public class SmartScan extends Thread{
	
	public static void initDao(Dao d){
		dao = d;
	};
	private static Dao dao;
	
	private SmartCondition sc;
	
	private Map<Integer, Equipment> equips = null;
	
	private String houseNo;
	
	private Date time = new Date();
	
	public SmartScan(){}
	 
	private Equipment equip;
	private AlarmTT att;
	private AlarmNotes an;	//检测异常
	private AlarmNotes an0;	//控制异常
	private int tag = 2;
	
	
	public int CtrPlan(Equipment equip, int tag, int modelType, String taskCode, Date ctrDate){
		this.tag = tag;
		this.equip = equip;
		this.an = new AlarmNotes(Constant.W_CTR, Constant.W_DO, equip.getHouseNo(), ctrDate);
		this.an0 = new AlarmNotes(Constant.W_CTR, Constant.W_CTR_1, equip.getHouseNo(), ctrDate);
		this.att = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.WARNS.code(), AlarmTT.class);
		if(att == null){
		}
		int a = doCtr();
		return a;
	}
	
	public SmartScan(String houseNo){
		this.houseNo = houseNo;
		this.sc = SmartUtil.cons.get(houseNo);
		if(CtrUtil.lasts.get(houseNo) != null){
			this.equips = CtrUtil.lasts.get(houseNo).getEquips();
		}
	}
	
	private void doScan(){
		if(equips == null) return;
		//获取所有处于开启状态的模式
		List<SmartConf> list = dao.query(SmartConf.class, Cnd.where("houseNo", "=", houseNo).and("status", "=", 1));
		if(!Lang.isEmpty(list)){
			boolean tag = false;//雨雪保护
			for(SmartConf s : list){
				if(s.getModelType() == TypeSmart.YXBH.val()){
					tag = true;
					list.remove(s);
					break;
				}
			}
			//优先处理雨雪保护
			if(tag && sc.isYX()){
				//关闭所有设备
				ExecutorService es = Executors.newFixedThreadPool(30);
				Equipment eqment;
				try{
					for(Map.Entry<Integer, Equipment> entry : equips.entrySet()){
						eqment = entry.getValue();
						Thread ctr = new CtrPlan(eqment, CtrConstant.CTR_CLOSE_TAG, TypeCtrTag.ZN.val(), houseNo + TypeSmart.YXBH.val(), time);
						Thread.sleep(157);
						es.execute(ctr);
					}
				} catch(Exception e) {
				} finally {
					if(null != es){
						//关闭线程池
						es.shutdown();
						es = null;
					}
				}
			} else {
				for(SmartConf s : list){
					switch (s.getModelType()) {
					case 1: toJWTF(s); break;
					case 2: toZRTF(s); break;
					case 3: toJSTF(s); break;
					case 4: toBSTF(s); break;
					case 5: toNHL(s); break;
					case 6: toZNKT(s); break;
					case 7: toPJRTF(s); break;
					default: break;
					}
				}
			}
		}
	}
	public int doCtr(){
		int a = 0;
		try {
			if(equip != null && equip.getModel() > 0 && Strings.isNotBlank(equip.getHouseNo())){
				EquipIps ips = HouseUtil.get(equip.getHouseNo(), TypeHouseConf.EIPS.code(), EquipIps.class);
				SmartModel sm = SmartMbsFactory.createModel(equip.getModel(), ips, equip.getModelWay());
				if(tag == CtrConstant.CTR_OPEN_TAG){
					a = sm.open(equip, an0);
				} else if(tag == CtrConstant.CTR_CLOSE_TAG){
					a = sm.close(equip, an0);
				} else if(tag == CtrConstant.CTR_STOP_TAG){
					sm.stop(equip, an0);
				} else if(tag == CtrConstant.CTR_OPENR_TAG){
					sm.openr(equip, an0);
				}
				insertAll();
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}
		return a;
	}
	
	private void insertAll(){
		List<AlarmNotes> as = new LinkedList<AlarmNotes>();
		if(an != null && an.getNums() > 0){
			as.add(an);
			MsgUtil.insertMsg(equip.getHouseNo(), "设备控制异常!", TypeMsg.WARN.val(), Constant.W_CTR, null);
		}
		if(an0 != null && an0.getNums() > 0){
			as.add(an0);
			MsgUtil.insertMsg(equip.getHouseNo(), "设备控制异常!", TypeMsg.WARN.val(), Constant.W_CTR, null);
		}
		dao.insert(as);
	}
	
	private void addError(String msg){
		an.setNums(an.getNums() + 1);
	}
	private void toJWTF(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isCloseJWTF(s.getParam2())){
					//关闭需要关闭的设备
					initEs(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.JWTF);
					//重置当前模式状态
					updateTag(TypeFlag.NO.val(), s.getId());
				}
			} else {//未执行
				if(sc.isOpenJWTF(s.getParam1())){
					//开启需要开启的设备
					initEs(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.JWTF);
					
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
				}
			}
		}
	}
	
	private void toZRTF(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isCloseZRTF()){
					//关闭需要关闭的设备
					initEs(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.ZRTF);
					//重置当前模式状态
					updateTag(TypeFlag.NO.val(), s.getId());
				}
			} else {//未执行
				if(sc.isOpenZRTF()){
					//开启需要开启的设备
					initEs(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.ZRTF);
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
				}
			}
		}
	}
	
	private void toJSTF(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isCloseJSTF(s.getParam2())){
					//关闭需要关闭的设备
					initEs(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.JSTF);
					//重置当前模式状态
					updateTag(TypeFlag.NO.val(), s.getId());
				}
			} else {//未执行
				if(sc.isOpenJSTF(s.getParam1())){
					//开启需要开启的设备
					initEs(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.JSTF);
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
				}
			}
		}
	}
	private void toBSTF(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isCloseBSTF(s.getParam1(), s.getParam2())){
					//关闭需要关闭的设备
					initEs(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.BSTF);
					//重置当前模式状态
					updateTag(TypeFlag.NO.val(), s.getId());
				}
			} else {//未执行
				if(sc.isOpenBSTF(s.getParam1(), s.getParam2())){
					//开启需要开启的设备
					initEs(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.BSTF);
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
				}
			}
		}
	}
	private void toNHL(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isCloseNHL(s.getParam2())){
					//关闭需要关闭的设备
					int a = initEsNHL(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.NHL);
					if(a == 1){
					//重置当前模式状态
					updateTag(TypeFlag.NO.val(), s.getId());
					}
				}
			} else {//未执行
				if(sc.isOpenNHL(s.getParam1())){
					//开启需要开启的设备
					int a=initEsNHL(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.NHL);
					if(a == 1){
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
					}
				}
			}
		}
	}
	private void toZNKT(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isCloseZNKT(s.getParam2())){
					//关闭需要关闭的设备
					initEs(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.ZNKT);
					//重置当前模式状态
					updateTag(TypeFlag.NO.val(), s.getId());
				}
			} else {//未执行
				if(sc.isOpenZNKT(s.getParam1())){
					//开启需要开启的设备
					initEs(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.ZNKT);
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
				}
			}
		}
	}
	private void toPJRTF(SmartConf s){
		if(s != null && Strings.isNotBlank(s.getEquips())){
			//正在执行
			if(s.getIngTag() == TypeFlag.YES.val()){
				//获取当前模式
				if(sc.isClosePJRTF(true)){
					//关闭需要关闭的设备
					initEs(s, CtrConstant.CTR_CLOSE_TAG, TypeSmart.PJRTF);
					//重置当前模式状态
//					if(a == 1){
					updateTag(TypeFlag.NO.val(), s.getId());
					}
				}
			} else {//未执行
				if(sc.isOpenPJRTF(true)){
					//开启需要开启的设备
					initEs(s, CtrConstant.CTR_OPEN_TAG, TypeSmart.PJRTF);
					//重置当前模式的状态
					updateTag(TypeFlag.YES.val(), s.getId());
				}
			}
		}
	
	@Override
	public void run() {
		doScan();
	}
	//内环流专用方法
	private int initEsNHL(SmartConf s, int tag, TypeSmart ts){
		int a = 0;
		String[] equipss = s.getEquips().split(",");
		if(Lang.isEmpty(equipss)) return 0;
//		ExecutorService es = Executors.newFixedThreadPool(equipss.length + 1);
		try {
			Equipment e1;
			for(String eno : equipss){
				e1 = equips.get(Integer.valueOf(eno));
				if(e1 != null){
//					Thread ctr;
					if(tag == CtrConstant.CTR_CLOSE_TAG && CtrUtil.isClose(e1)){
						a = CtrPlan(e1, CtrConstant.CTR_CLOSE_TAG, TypeCtrTag.ZN.val(), houseNo + ts.val(), time);
						
					} else if(tag == CtrConstant.CTR_OPEN_TAG && CtrUtil.isOpen(e1)){
					   a = CtrPlan(e1, CtrConstant.CTR_OPEN_TAG, TypeCtrTag.ZN.val(), houseNo + ts.val(), time);
					}
//					Thread.sleep(157);
				}
			}
		} catch(Exception e) {
			
		} finally {
//			if(null != es){
//				//关闭线程池
//				es.shutdown();
//				es = null;
//			}
		}
		return a;
	}
	private void initEs(SmartConf s, int tag, TypeSmart ts){
		String[] equipss = s.getEquips().split(",");
		if(Lang.isEmpty(equipss)) return;
		ExecutorService es = Executors.newFixedThreadPool(equipss.length + 1);
		try {
			Equipment e1;
			for(String eno : equipss){
				e1 = equips.get(Integer.valueOf(eno));
				if(e1 != null){
					Thread ctr;
					if(tag == CtrConstant.CTR_CLOSE_TAG && CtrUtil.isClose(e1)){
						ctr = new CtrPlan(e1, CtrConstant.CTR_CLOSE_TAG, TypeCtrTag.ZN.val(), houseNo + ts.val(), time);
						es.execute(ctr);
					} else if(tag == CtrConstant.CTR_OPEN_TAG && CtrUtil.isOpen(e1)){
						ctr = new CtrPlan(e1, CtrConstant.CTR_OPEN_TAG, TypeCtrTag.ZN.val(), houseNo + ts.val(), time);
						es.execute(ctr);
					}
					Thread.sleep(157);
				}
			}
		} catch(Exception e) {
			
		} finally {
			if(null != es){
				//关闭线程池
				es.shutdown();
				es = null;
			}
		}
	}
	
	private void updateTag(int tag, String id){
		dao.update(SmartConf.class, Chain.make("ingTag", tag), Cnd.where("id", "=", id));
	}
}
