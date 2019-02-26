package cn.com.bjggs.temp.util;


public class Temp4NTCP implements Temps{
	
	public double getT(byte[] bs, int h, int l){
		return Math.round((((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF) - 0x0910)/1.8D)/10.0D;
	}
	
	public int getT10(byte[] bs, int h, int l){
		return (int)Math.round((((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF) - 0x0910)/1.8D);
	}
	
	public double getH(byte[] bs, int h, int l){
		return (((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF))/10.0D;
	}
	
	public int getH10(byte[] bs, int h, int l){
		return ((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF);
	}
	
}
