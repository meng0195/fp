package cn.com.bjggs.gas.util;

import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.enums.TypeCtrGas;


public class GasMbsFactory {
	
	public static final GasChecks createCheck(GasInfo gi){
		if(gi.getCtrType() == TypeCtrGas.ARM.val()){
			return new GasMbs4NTCP(gi);	
		} else if(gi.getCtrType()  == TypeCtrGas.TCP.val()){
			return new GasMbs4NTCP(gi);
		} else {
			return new GasMbs4NTCP(gi);
		}
	}
	
}
