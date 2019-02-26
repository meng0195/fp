package cn.com.bjggs.temp.util;

import cn.com.bjggs.basis.enums.TypeProtocol;


public class ModbusFactory {
	
	public static final Checks createCheck(int type, TempControl tc){
		if(type == TypeProtocol.NTCP.val()){
			return new Modbus4NTCP(tc);	
		} else if(type == TypeProtocol.RTU.val()){
			return new Modbus4Rtu(tc);
		} else if(type == TypeProtocol.WEA.val()){
			return new Modbus4Wea();
		} else if(type == TypeProtocol.ARM.val()){
			return new Modbus4ARM(tc);
		} else {
			return new Modbus4NTCP(tc);
		}
	}
	
	public static final Checks createCheck(int type, String ip, int port){
		return createCheck(type, new TempControl(ip, port));
	}
	
}
