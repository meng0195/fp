package cn.com.bjggs.gas.util;

import java.net.InetSocketAddress;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.gas.domain.GasInfo;

/**
 * 新TCP板子读取温度帮助类
 * @author	wc
 * @date	2017-07-14
 */
public class GasMbs4NTCP implements GasChecks{
	
	private static final Log log = Logs.getLog(GasMbs.class); 
	//保护
	private static final int MAX_WAY = PropsUtil.getInteger("gas.max.way", 5);
	//稳定时间
	private static final int STA_TIME = PropsUtil.getInteger("gas.sta.time", 3000);
	
	private static final int DEF_DRAIN_WAY = PropsUtil.getInteger("gas.drain.way", 6);
	
	private static final int DEF_DRAIN_TIME = PropsUtil.getInteger("gas.drain.time", 10000);
	
	private static final byte[] ING_CMD = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x01, 0x00, 0x00, 0x00, 0x08};
	
	private static final byte[] OPEN = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x05, 0x00, 0x00, (byte)0xFF, 0x00};
	
	private static final byte[] CLOSE = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x05, 0x00, 0x00, 0x00, 0x00};
	
	private static final byte[] READ = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, 0x00, 0x64, 0x00, 0x03};
	
	//private static final byte[] MODEL_CMD = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x02, 0x00, 0x00, 0x00, 0x01};
	
	private GasMbs m;
	
	private GasInfo gi;
	
	private InetSocketAddress address;
	
	private byte[] cmd;
	
	public GasMbs4NTCP(GasInfo gi){
		this.gi = gi;
		this.m = new GasMbs();
		this.address = new InetSocketAddress(gi.getCtrIp(), gi.getCtrPort());
	}
	
	public byte[] getGas(int way) {
		if(way > MAX_WAY) throw new RuntimeException("超出硬件可检测最大风路数量！");
		//返回的数组（每种气体的10倍占两个字节）
		byte[] reqs = new byte[6];
		//验证模式
		if(!isSmartMode()) throw new RuntimeException("检测风路" + way + "时，模式错误！");
		//无检测
		if(!isChecking()){
			if(gi.getTimes() == null || gi.getTimes().length < way) throw new RuntimeException("风路" + way + "时间设置不正确！");
			int ms = gi.getTimes()[way-1] * 1000;
			if(ms == 0) throw new RuntimeException("风路" + way + "时间设置为0！");
			//开始检测
			open(way);
			//等待
			try {
				Thread.sleep(ms);
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("系统错误：无法执行线程沉睡！");
				}
				//关闭设备
				close(way);
				throw new RuntimeException("系统错误：无法执行线程沉睡！");
			}
			//关闭设备
			close(way);
			//稳定等待
			try{
				Thread.sleep(STA_TIME);
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("系统错误：无法执行线程沉睡！");
				}
				throw new RuntimeException("系统错误：无法执行线程沉睡！");
			}
			//读取传感器数值
			byte[] vs = m.sendMsg(READ, address, true, 15);
			if(vs != null && vs.length == 15){
				System.arraycopy(vs, 9, reqs, 0, 6);
			}
		}
		return reqs;
	}
	
	public void clear(){
		if(!isChecking()){
			if(gi.getDrainTime() == 0){
				open(DEF_DRAIN_WAY);
				try{
					Thread.sleep(DEF_DRAIN_TIME);
				} catch (Exception e) {
					if(log.isErrorEnabled()){
						log.error("系统错误：无法执行线程沉睡！");
					}
					throw new RuntimeException("系统错误：无法执行线程沉睡！");
				}
				close(DEF_DRAIN_WAY);
				throw new RuntimeException("排空时间设置不正确，已按照默认时间排空！");
			} else {
				open(DEF_DRAIN_WAY);
				try{
					Thread.sleep(gi.getDrainTime() * 1000);
				} catch (Exception e) {
					if(log.isErrorEnabled()){
						log.error("系统错误：无法执行线程沉睡！");
					}
					throw new RuntimeException("系统错误：无法执行线程沉睡！");
				}
				close(DEF_DRAIN_WAY);
			}
		}
	}
	
	private void open(int way){
		try {
			//开启磁阀
			cmd = OPEN.clone();
			cmd[9] = (byte)way;
			m.sendMsg(cmd, address, false, 10);
			//开启气泵
			cmd = OPEN.clone();
			cmd[9] = 0x00;
			m.sendMsg(cmd, address, false, 10);
		} catch (Exception e) {
			throw new RuntimeException("风路" + way + "开启磁阀和气泵时发生异常！");
		}
	}
	
	private void close(int way){
		try {
			//关闭气泵
			cmd = CLOSE.clone();
			cmd[9] = 0x00;
			m.sendMsg(cmd, address, false, 10);
			//关闭磁阀
			cmd = CLOSE.clone();
			cmd[9] = (byte)way;
			m.sendMsg(cmd, address, false, 10);
		} catch (Exception e) {
			throw new RuntimeException("风路" + way + "关闭磁阀和气泵时发生异常！");
		}
	}
	
	private boolean isSmartMode(){
		//TODO 硬件没有问题后修改
		//byte[] req = m.sendMsg(MODEL_CMD, new InetSocketAddress(gi.getModelIp(), gi.getModelPort()), true, 1);
		return true;
	}
	
	private boolean isChecking(){
		byte[] req = m.sendMsg(ING_CMD, address, true, 10);
		if(req == null || req.length != 10) throw new RuntimeException("读取硬件设备检测状态失败！");
		byte ways = req[9];
		if(ways != 0){
			if((ways & 0x01) == 0x01) throw new RuntimeException("风路1正在检测！");
			if((ways & 0x02) == 0x02) throw new RuntimeException("风路2正在检测！");
			if((ways & 0x04) == 0x04) throw new RuntimeException("风路3正在检测！");
			if((ways & 0x08) == 0x08) throw new RuntimeException("风路4正在检测！");
			if((ways & 0x10) == 0x10) throw new RuntimeException("风路5正在检测！");
			if((ways & 0x20) == 0x20) throw new RuntimeException("正在排空操作！");
			if((ways & 0x40) == 0x40) throw new RuntimeException("风路7正在检测！");
			if((ways & 0x80) == 0x80) throw new RuntimeException("抽气泵正在工作！");
			return false;
		} else {
			return false;
		}
	}
}
