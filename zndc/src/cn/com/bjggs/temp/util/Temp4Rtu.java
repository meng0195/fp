package cn.com.bjggs.temp.util;


public class Temp4Rtu implements Temps{
	
	public double getT(byte[] bs, int h, int l){
		return Math.round((((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF) - 0x0910)/1.8D)/10.0D;
	}
	
	public int getT10(byte[] bs, int h, int l){
		return (int)Math.round((((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF) - 0x0910)/1.8D);
	}
	
	public double getH(byte[] bs, int h, int l){
		return Math.round(((((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF))/51.0D-0.833)/0.003)/10.0D;
	}
	
	public int getH10(byte[] bs, int h, int l){
		return (int)Math.round(((((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF))/51.0D-0.833)/0.003);
	}
	
}
