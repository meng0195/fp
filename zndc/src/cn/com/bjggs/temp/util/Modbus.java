package cn.com.bjggs.temp.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.core.util.PropsUtil;

public class Modbus {
	
	private static final Log log = Logs.getLog(Modbus.class); 
	
	private static final int WTIME = PropsUtil.getInteger("temp.wait.time", 5000);
	
	private static final int STIME = PropsUtil.getInteger("temp.sleep.time", 1250);
	
	public static final int ATTEMPTS = PropsUtil.getInteger("temp.attempts", 5);
	
	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	
	public byte[] sendMsg(byte[] msg, TempControl tc, boolean tag, int len){
		return sendMsg(msg, tc, tag, len, STIME);
	}
	
	public byte[] sendMsg(byte[] msg, TempControl tc, boolean tag, int len, int sleep){
		byte[] req = new byte[len];
		try {
			socket = new Socket();
			System.out.println(tc.getAddress());
			socket.connect(tc.getAddress(), WTIME);
			//发送指令
			os = socket.getOutputStream();
			os.write(msg);
			//处理返回流
			if(tag){
				Thread.sleep(sleep);
				is = socket.getInputStream();
				is.read(req);
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
}
