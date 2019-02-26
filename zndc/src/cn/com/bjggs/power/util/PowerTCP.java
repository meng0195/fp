package cn.com.bjggs.power.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.core.util.PropsUtil;


public class PowerTCP{
	
	private static final Log log = Logs.getLog(PowerTCP.class); 
	
//	private EquipIps w;
	
	private String powerIp;
	
	private int powerPort;
	
	private int l;
	
	public PowerTCP(){}
	
	public PowerTCP(String ip, int port, int l){
		this.powerIp = ip;
		this.powerPort = port;
		this.l = l;
	}
	
	private static final int WTIME = PropsUtil.getInteger("power.wait.time", 5000);
	
	private static final int STIME = PropsUtil.getInteger("power.sleep.time", 500);
	
	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	
	private byte[] sendMsg(byte[] msg, int len){
		byte[] req = new byte[len * 2 + 9];
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(powerIp, powerPort), WTIME);
//			socket.connect(new InetSocketAddress("192.168.30.89", 503), WTIME);
			//发送指令
			os = socket.getOutputStream();
			os.write(msg);
			//处理返回流
			Thread.sleep(STIME);
			is = socket.getInputStream();
			byte[] bytes = new byte[1];
			int i = 0;
			while (is.available() > 0 && is.read(bytes) != -1) {
				if(i < len * 2 + 9){
					req[i] = bytes[0];
					i++;
				}
				if(is.available() == 0){
					break;
				}
			}
			//尝试销毁所有流对象
			this.destroyAll();
			//返回的是41位,把前9位无用信息截取掉
			byte[] ps = new byte[32];
	        System.arraycopy(req, 9, ps, 0, 32);
			return ps;
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
		byte[] cmd = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, (byte)0xF0, 0x03, (byte)0x00, (byte)(l * 16 +  100), 0x00, 0x10};
//		byte[] cmd = {0x00, 0x00, 0x00, 0x00, 0x00, 0x06, (byte)0xF0, 0x03, (byte)0x00, (byte)0x64, 0x00, 0x10};
		return sendMsg(cmd, 16);
	}

}
