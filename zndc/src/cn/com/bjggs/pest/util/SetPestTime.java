package cn.com.bjggs.pest.util;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.pest.domain.PestInfo;

public class SetPestTime {

	private static final Log log = Logs.getLog(SetPestTime.class);
	
	//设置时间指令
	private static final byte[] TIME_CMD = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x00, 0x06, (byte)0x9C, 0x72, 0x00, 0x0A};
	
	//设置时间方法
	private static Socket socket;
	private static OutputStream os;
	
	/**
	 * 同步测虫检测抽气时间
	 * @author	wc
	 * @date	2017年5月31日
	 * @return
	 */
	public synchronized static final void setTime(PestInfo p){
		if(Strings.isBlank(p.getHouseNo())) return;
		//判断仓房 是否在检测 如果在检测则不修改
		if(CheckPest.checks.containsKey(p.getHouseNo())) return;
		//小于10s的时间不设置
		if(p.getTimes() <= 10) return;
		TIME_CMD[10] = MathUtil.int2HByte(p.getTimes());
		TIME_CMD[11] = MathUtil.int2HByte(p.getTimes());
		try {
			//创建信道
			socket = new Socket();
			socket.connect(new InetSocketAddress(p.getCtrIp(), p.getCtrPort()), 2500);
			//发送指令
			os = socket.getOutputStream();
			//通电
			os.write(TIME_CMD);
		} catch (Exception e){
			if(log.isInfoEnabled()){
				log.info("修改空气泵抽气时间异常：" + e.getMessage());
			}
		} finally{
			try{
				if (os != null) {
					os.flush();
					os.close();
				}
				if (socket != null){
					socket.close();
				}
			} catch (Exception ee) {}
		}
	}
}
