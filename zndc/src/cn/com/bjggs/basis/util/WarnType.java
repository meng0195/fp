package cn.com.bjggs.basis.util;

public class WarnType {

	/**
	 * 高温报警|磷化氢|预警
	 */
	public static final byte WARN_1 = 1;
	
	/**
	 * 温度升高率|二氧化碳|报警
	 */
	public static final byte WARN_2 = 2;
	
	/**
	 * 温度异常点|氧气
	 */
	public static final byte WARN_4 = 4;
	
	/**
	 * 冷芯
	 */
	public static final byte WARN_8 = 8;
	
	
	public static final boolean isWarn1(byte b){
		return (b & 1) == 1;
	}
	
	public static final boolean isWarn2(byte b){
		return (b & 2) == 2;
	}
	
	public static final boolean isWarn4(byte b){
		return (b & 4) == 4;
	}
	
	public static final boolean isWarn8(byte b){
		return (b & 8) == 8;
	}
	
	public static final boolean isWarn16(byte b){
		return (b & 16) == 16;
	}
	
	public static final boolean isWarn32(byte b){
		return (b & 32) == 32;
	}
	
	public static final boolean isWarn64(byte b){
		return (b & 64) == 64;
	}
}
