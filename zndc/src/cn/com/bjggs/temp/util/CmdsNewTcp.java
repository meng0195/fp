package cn.com.bjggs.temp.util;

/**
 * 良安自定义指令集
 * @author	wc
 * @date	2017-07-13
 */
public class CmdsNewTcp {
	/**
	 * 读取板子基本信息 ip mac 子网掩码 默认网关 库号 仓号等
	 */
	public static final byte[] R_BASE = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x63, (byte)0x9C, 0x47, 0x00, 0x20};
	/**
	 * 复位指令
	 */
	public static final byte[] RESET = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0x9C, 0x62, 0x11, 0x02};
	/**
	 * 读取所有通道传感器统计
	 */
	public static final byte[] R_COUNT = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0x9C, 0x3F, 0x00, 0x01};
	/**
	 * 读取所有温度传感器指令
	 */
	public static final byte[] R_TEMPS = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x63, (byte)0xA0, (byte)0x8C, 0x00, 0x01};
	/**
	 * 4通道仓温仓湿
	 */
	public static final byte[] R_4_THI = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x63, (byte)0xCF, 0x08, 0x00, 0x40};
	
}