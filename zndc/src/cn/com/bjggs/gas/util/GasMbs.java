package cn.com.bjggs.gas.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.core.util.PropsUtil;

public class GasMbs {
	
	private static final Log log = Logs.getLog(GasMbs.class); 
	
	private static final int WTIME = PropsUtil.getInteger("gas.wait.time", 5000);
	
	private static final int STIME = PropsUtil.getInteger("gas.sleep.time", 1000);
	
	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	
	public byte[] sendMsg(byte[] msg, InetSocketAddress address, boolean tag, int len){
		return sendMsg(msg, address, tag, len, Math.min(len, STIME));
	}
	
	public byte[] sendMsg(byte[] msg, InetSocketAddress address, boolean tag, int len, int sleep){
		byte[] req = new byte[len];
		try {
			socket = new Socket();
			socket.connect(address, WTIME);
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
