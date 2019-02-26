package cn.com.bjggs.power.util;

import cn.com.bjggs.basis.domain.EquipIps;

public class PowerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EquipIps ei = new EquipIps();
		ei.setDioIp("192.168.30.120");
		ei.setDioPort(503);
		PowerTCP pt = new PowerTCP("192.168.30.120", 503, 0);
		byte[] ps = pt.getDatas();
		
		double d = (((ps[22] & 0xFF) << 24) + ((ps[23] & 0xFF) << 16) + ((ps[24] & 0xFF) << 8) + (ps[25] & 0xFF))/1000.000D;
		System.out.println(d);
		
	}

}
