package cn.com.bjggs.ctr.util;

import cn.com.bjggs.basis.domain.EquipIps;

public class CtrMbsFactory {

	public static final CtrModel createModel(int type, EquipIps ips, int index){
		if (type == CtrConstant.DIDO) {
			return new ModelDio(ips);
		} else if(type == CtrConstant.ONEW){
			return new ModelOnew(ips);
		} else if(type == CtrConstant.TWOW){
			return new ModelTwow(ips);
		} else if(type == CtrConstant.WIND){
			return new ModelWind(ips, index);
		} else if(type == CtrConstant.WONE){
			return new ModelWone(ips);
		} else if(type == CtrConstant.AIRC){
			return new ModelAirc(ips);
		} else if(type == CtrConstant.ARMDIO){
			return new ModelARMDio(ips);
		} else if(type == CtrConstant.ARMWIND){
			return new ModelARMWind(ips);
		} else if(type == CtrConstant.ARMONEW){
			return new ModelARMOnew(ips);
		} else if(type == CtrConstant.ARMNHL){
			return new ModelARMNHL(ips);
		} else {
			return null;
		}
	}
}
