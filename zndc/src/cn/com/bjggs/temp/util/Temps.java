package cn.com.bjggs.temp.util;


public interface Temps {
	
	public double getT(byte[] bs, int h, int l);
	
	public int getT10(byte[] bs, int h, int l);
	
	public double getH(byte[] bs, int h, int l);
	
	public int getH10(byte[] bs, int h, int l);
	
}
