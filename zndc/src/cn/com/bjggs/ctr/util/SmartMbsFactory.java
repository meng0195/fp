package cn.com.bjggs.ctr.util;

import cn.com.bjggs.basis.domain.EquipIps;

public class SmartMbsFactory {

	public static final SmartModel createModel(int type, EquipIps ips, int index){
		if (type == CtrConstant.DIDO) {
			return new SmartDio(ips);
		} else if(type == CtrConstant.ONEW){
			return new SmartOnew(ips);
		} else if(type == CtrConstant.TWOW){
			return new SmartTwow(ips);
		} else if(type == CtrConstant.WIND){
			return new SmartWind(ips, index);
		} else if(type == CtrConstant.WONE){
			return new SmartWone(ips);
		} else if(type == CtrConstant.AIRC){
			return new SmartAirc(ips);
		} else if(type == CtrConstant.ARMDIO){
			return new SmartARMDio(ips);
		} else if(type == CtrConstant.ARMONEW){
			return new SmartARMOnew(ips);
		} else if(type == CtrConstant.ARMWIND){
			return new SmartARMWind(ips);
		} else {
			return null;
		}
	}
}
