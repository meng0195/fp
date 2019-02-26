package cn.com.bjggs.weather.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.temp.util.Modbus;
import cn.com.bjggs.weather.domain.WeatherInfo;


public class WeatherOldTCP implements Weathers{
	
	private static final Log log = Logs.getLog(Modbus.class); 
	
//	private static final byte[] cmd = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0x00, (byte)0x6F, 0x00, 0x0B};
	private static final byte[] old_cmd = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, (byte)0x00, (byte)0x8D, 0x00, 0x0B};
	
	private WeatherInfo w;
	
	public WeatherOldTCP(){}
	
	public WeatherOldTCP(WeatherInfo w){
		this.w = w;
	}
	
	private static final int WTIME = PropsUtil.getInteger("weather.wait.time", 5000);
	
	private static final int STIME = PropsUtil.getInteger("weather.sleep.time", 5000);
	
	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	
	private byte[] sendMsg(byte[] msg, int len){
		byte[] req = new byte[31];
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(w.getIp(), w.getPort()), WTIME);
			//发送指令
			os = socket.getOutputStream();
			os.write(msg);
			//处理返回流
			Thread.sleep(STIME);
			is = socket.getInputStream();
			byte[] bytes = new byte[1];
			int i = 0;
			while (is.available() > 0 && is.read(bytes) != -1) {
				if(i < 31){
					req[i] = bytes[0];
					i++;
				}
				if(is.available() == 0){
					break;
				}
			}
			//尝试销毁所有流对象
			this.destroyAll();
			return req;
		} catch (Exception e){
			//尝试销毁所有流对象
			this.destroyAll();
			if(log.isInfoEnabled()){
				log.info("向下位机发送指令失败：" + e.getMessage());
			}
			throw new RuntimeException("连接不可用，请修改IP或配置默认端口！");
		}
	}
	
	private void destroyAll(){
		try{
			if (this.os != null) {
				this.os.flush();
				this.os.close();
			}
			if (this.is != null) {
				this.is.close();
			}
			if (this.socket != null){
				this.socket.close();
			}
		} catch (Exception e) {
			if(log.isInfoEnabled()){
				log.info("销毁socket流异常：" + e.getMessage());
			}
		}
	}
	
	public byte[] getDatas(){
		byte[] req1 = sendMsg(old_cmd, 11);
		byte[] req = new byte[22];
		System.arraycopy(req1, 9, req, 0, req.length);
		return req;
	}

}
