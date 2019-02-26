package cn.com.bjggs.temp.util;

import java.net.InetSocketAddress;

import cn.com.bjggs.core.util.PropsUtil;

/**
 * 
 * @author	wc
 * @date	2017-07-13
 */
public class TempControl {
	/**
	 * 默认ip
	 */
	private static final String IP = PropsUtil.getString("temp.def.ip");
	/**
	 * 默认端口
	 */
	private static final int PORT = PropsUtil.getInteger("temp.def.port", 502);
	
	private String ip = IP;
	
	private int port = PORT;
	
	private int way = 1;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}

	/**
	 * 192.168.2.253 ：502
	 */
	public TempControl(){}
	
	/**
	 * ip ：502
	 */
	public TempControl(String ip){
		this.ip = ip;
	}
	
	/**
	 * 192.168.2.253 ：port
	 */
	public TempControl(int port){
		this.port = port;
	}
	
	/**
	 * ip ：port
	 */
	public TempControl(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	/**
	 * ip ：port:way
	 */
	public TempControl(String ip, int port, int way){
		this.ip = ip;
		this.port = port;
		this.way = way;
	}
	
	/**
	 * 获取sorket 适应的地址
	 */
	public InetSocketAddress getAddress(){
		return new InetSocketAddress(ip, port);
	}
	
}
