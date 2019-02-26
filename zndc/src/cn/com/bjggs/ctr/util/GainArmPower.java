package cn.com.bjggs.ctr.util;


public class GainArmPower {

	public static final double getArmDate(String ip){
		String ds = "";
		double dbnh = 0;
		String data = ARMCommUtil.SendGet(ip+"/SmtMeters", "Datas=");
		//GET /SmtMeters?Datas	Ua=0	Ub=0	Uc=0	Ia=0	Ib=0	Ic=0	Psum=0	Qsum=0	Ssum=0	F=0	PF=0	YGDN=0	WGDN=0	State1=0!
		String[] split = data.split("	");
		if (split.length >= 12) {
			ds = split[12];
			dbnh = Double.parseDouble(ds.substring(5, ds.length()))/1000;
		}
		return dbnh;
	}
}
