package cn.com.bjggs.temp.util;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.util.MathUtil;

/**
 * 新TCP板子读取温度帮助类
 * @author	wc
 * @date	2017-07-14
 */
public class Modbus4NTCP implements Checks{
	
	private static final Log log = Logs.getLog(Modbus4NTCP.class);
	
	private Modbus m = new Modbus();
	
	private TempControl tc;
	
	public Modbus4NTCP(TempControl tc){
		this.tc = tc;
	}
	
	public Modbus4NTCP(String ip, int port){
		this.tc = new TempControl(ip, port);
	}
	
	public Modbus4NTCP(String ip, int port, int way){
		this.tc = new TempControl(ip, port, way);
	}
	
	public Modbus4NTCP(String ip){
		this.tc = new TempControl(ip);
	}
	
	/**
	 * 获取新板子的仓温仓湿,有尝试次数在Modbus 类中设定
	 */
	public byte[] getTHs(){
		byte[] req = null;
		int num = Modbus.ATTEMPTS;
		while(num > 0){
			try{
				req = m.sendMsg(CmdsNewTcp.R_4_THI, tc, true, 128);
				break;
			}catch(Exception e){
				num = num - 1;
				if(num != 0){
					log.info("读取仓温仓湿失败！500ms后再次尝试");
				} else {
					throw new RuntimeException("读取仓温仓湿集合失败!");
				}
				try{Thread.sleep(500);}catch(Exception ee){};
			}
		}
		return req;
	}
	
	/**
	 * 根据通道号获取仓温仓湿，有尝试次数在Modbus 类中设定
	 */
	public byte[] getTH(int way){
		byte[] th = getTHs();
		byte[] req = new byte[4];
		if(th == null || th.length != 128){
			throw new RuntimeException("仓温仓湿读取错误！");
		}
		System.arraycopy(th, (way - 1) * 4, req, 0, 4);
		return req;
	}
	
	/**
	 * 获取所有粮温
	 */
	public byte[] getTemps(){
		byte[] req = null;
		int num = Modbus.ATTEMPTS;
		while(num > 0){
			try{
				byte[] count = m.sendMsg(CmdsNewTcp.R_COUNT, tc, true, 16);
				if(count == null || count.length < 4) throw new RuntimeException("读取传感器总数错误！");
				req = m.sendMsg(CmdsNewTcp.R_TEMPS, tc, true, MathUtil.byte2Int(count[2], count[3])*4);
				break;
			}catch(Exception e){
				num = num - 1;
				if(num != 0){
					log.info("读取温度失败！500ms后再次尝试");
				} else {
					throw new RuntimeException("温度读取失败！");
				}
				try{Thread.sleep(500);}catch(Exception ee){};
			}
		}
		return req;
	}
}
