package cn.com.bjggs.temp.util;


public class Temp4ARM implements Temps{
	
	public double getT(byte[] bs, int h, int l){
		int temp = ((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF);
		if(temp > 32768){
			temp = -((~temp & 0xFFFF) + 1);
		}
		return Math.round(temp*0.0625*10)/10.0D;
	}
	
	public int getT10(byte[] bs, int h, int l){
		int temp = ((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF);
		if(temp > 32768){
			temp = -((~temp & 0xFFFF) + 1);
		}
		return (int) (temp*0.625);
//		return (int)Math.round(((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF))*625/1000;
	}
	
	public double getH(byte[] bs, int h, int l){
		return (((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF))/10.0D;
	}
	
	public int getH10(byte[] bs, int h, int l){
		return ((bs[h] & 0xFF) << 8) + (bs[l] & 0xFF);
	}
	
}
