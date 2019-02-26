package cn.com.bjggs.temp.util;

import cn.com.bjggs.basis.enums.TypeProtocol;


public class TempFactory {
	
	public static final Temps createCheck(int type){
		if(type == TypeProtocol.NTCP.val()){
			return new Temp4NTCP();
		} else if(type == TypeProtocol.RTU.val()){
			return new Temp4Rtu();
		}  else if(type == TypeProtocol.ARM.val()){
			return new Temp4ARM();
		} else {
			return new Temp4NTCP();
		}
	}
	
}
