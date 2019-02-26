package cn.com.bjggs.temp.util;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import org.nutz.lang.Strings;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.util.PropsUtil;

public class Modbus4Rtu implements Checks{
	
	private String portDef = PropsUtil.getString("temp.rtu.port");
	private int delayReadDef = PropsUtil.getInteger("temp.rtu.delayRead", 200);
	
	private String port = portDef;//端口号
	private CommPortIdentifier portId;//端口ID
	private byte number = 0x01;
	
	private static final int rate = PropsUtil.getInteger("temp.rtu.rate", 9600);//波特率
	private static final int timeout = PropsUtil.getInteger("temp.rtu.timeout", 500);//设备超时时间
	private static final int dataBits = PropsUtil.getInteger("temp.rtu.dataBits", 0x8);//数据位
	private static final int stopBits = PropsUtil.getInteger("temp.rtu.stopBits", 0x1);//停止位
	private static final int parity = PropsUtil.getInteger("temp.rtu.parity", 0x0);//无奇偶校验
	private static final int step = PropsUtil.getInteger("temp.rtu.step", 100);
	
	public static final int len = PropsUtil.getInteger("temp.rtu.len", 20);
	
	private int delayRead = delayReadDef;//延时等待端口数据准备的时间
	
	private SerialPort serialPort;

	public Modbus4Rtu(TempControl tc){
		if(!Strings.isBlank(port)) this.port = tc.getIp();
		if(!isComs(port)) throw new RuntimeException("错误的COM口配置！");
		try{
			this.portId = CommPortIdentifier.getPortIdentifier(port);
			this.serialPort = (SerialPort)portId.open("Serial" + port, timeout);
			this.serialPort.notifyOnDataAvailable(true);
			this.serialPort.setSerialPortParams(rate, dataBits, stopBits, parity);
			this.number = (byte)tc.getPort();
		} catch(Exception e){
			throw new RuntimeException("串行接口：" + port + ", 连接失败！" + e.getMessage());
		}
	}
	
	private static final boolean isComs(String s) {
		return s.matches("^COM(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])$".intern());
	}

	private byte[] T_CMD = {0x05, 0x64, 0x00, 0x02, 0x10, 0x01, 0x00, 0x6C, 0x4B, 0x53, 0x45, (byte)0xEB, (byte)0xF7};
	
	private byte[] H_CMD = {0x05, 0x64, 0x00, 0x02, 0x10, 0x01, 0x00, 0x6c, 0x4B, 0x57, 0x45, 0x1e, 0x17};
	
	private void getCMD(){
		T_CMD[5] = number;
		H_CMD[5] = number;
		byte[] crc = MathUtil.crc40xC9DA(T_CMD, 0, 7);
		T_CMD[7] = crc[0];
		T_CMD[8] = crc[1];
		byte[] crc1 = MathUtil.crc40xC9DA(H_CMD, 0, 7);
		H_CMD[7] = crc1[0];
		H_CMD[8] = crc1[1];
	}
	
	private InputStream is;
	private OutputStream os;
	
	/* (non-Javadoc)
	 * @see cn.com.bjggs.check.util.Checks#getTHs()
	 */
	@Override
	public byte[] getTHs() {
		byte[] req = new byte[28];
		//int num = Modbus.ATTEMPTS;
		try{
			os = serialPort.getOutputStream();
			getCMD();
			os.write(H_CMD);
			os.flush();
			
			is = serialPort.getInputStream();
			byte[] bytes = new byte[1];
			int i = 0;
			int steps = 0;
			Thread.sleep(delayRead);
			while (is.read(bytes) != -1) {
				if(i == 0){
					if(bytes[0] == 0x05){
						req[i] = bytes[0];
						i++;
					} else {
						steps++;
					}
				} else if(i == 1){
					if(bytes[0] == 0x64){
						req[i] = bytes[0];
						i++;
					} else {
						steps++;
					}
				} else if(i > 1 && i < 28){
					req[i] = bytes[0];
					i++;
				} else {
					break;
				}
				if(steps > step) throw new RuntimeException("温湿读取错误！");
				Thread.sleep(10);
			}
			byte[] crc = MathUtil.crc40xC9DA(req, 9, 26);
			if(crc[0] != req[26] || crc[1] != req[27]){
				throw new RuntimeException("读取温湿数据包错误！");
			}
		} catch (Exception e){
			
		} finally {
			closeAll();
		}
		return req;
	}

	private void closeAll(){
		try{
			if(os != null){
				os.flush();
				os.close();
				os = null;
			}
			if(is != null){
				is.close();
				is = null;
			}
			if(serialPort != null){
				serialPort.close();
				serialPort = null;
			}
		}catch(Exception e){
			throw new RuntimeException("串口关闭失败！");
		}
	}
	
	/* (non-Javadoc)
	 * @see cn.com.bjggs.check.util.Checks#getTH(int)
	 */
	@Override
	public byte[] getTH(int way) {
		byte[] th = getTHs();
		byte[] req = new byte[4];
		if(th == null || th.length != 28){
			throw new RuntimeException("仓温仓湿读取错误！");
		}
		System.arraycopy(th, 10 + (way - 1) * 4, req, 0, 4);
		return req;
	}

	/* (non-Javadoc)
	 * @see cn.com.bjggs.check.util.Checks#getTemps()
	 */
	@Override
	public byte[] getTemps() {
		byte[] req = new byte[4];
		// TODO Auto-generated method stub
		try{
			os = serialPort.getOutputStream();
			getCMD();
			os.write(T_CMD);
			os.flush();
			
			is = serialPort.getInputStream();
			byte[] bytes = new byte[1];
			int i = 0;
			int steps = 0;
			Thread.sleep(delayRead);
			int len = 9;
			while (is.read(bytes) != -1) {
				if(i == 0){
					if(bytes[0] == 0x05){
						req[i] = bytes[0];
						i++;
					} else {
						steps++;
					}
				} else if(i == 1){
					if(bytes[0] == 0x64){
						req[i] = bytes[0];
						i++;
					} else {
						steps++;
					}
				} else if(i == 3){
					req[3] = bytes[0];
					len = len + MathUtil.byte2Int(req[2], bytes[0]);
					req = new byte[len];
					i++;
				} else if(i > 1 && i < len){
					req[i] = bytes[0];
					i++;
				} else {
					break;
				}
				if(steps > step) throw new RuntimeException("温湿读取错误！");
			}
			if(len <= 66){
				byte[] crc = MathUtil.crc40xC9DA(req, 9, len-2);
				if(crc[0] != req[len-2] || crc[1] != req[len-1]){
					throw new RuntimeException("读取温度数据包错误！");
				} else {
					byte[] reqs = new byte[len - 3];
					System.arraycopy(req, 10, reqs, 0, len-3);
					return reqs;
				}
			} else {
				byte[] crc;
				byte[] reqs = new byte[]{};
				byte[] old = new byte[]{};
				int rlen = 0;
				for(int j = 9; j < len; j += 66){
					if(len - j > 66){
						crc = MathUtil.crc40xC9DA(req, j, j+64);
						if(crc[0] != req[j+64] || crc[1] != req[j+65]){
							throw new RuntimeException("读取温度数据包错误！");
						} else {
							rlen += 64; 
							reqs = new byte[rlen];
							if(old.length > 0){
								System.arraycopy(old, 0, reqs, 0, old.length);
							}
							System.arraycopy(req, j, reqs, old.length, 64);
							old = reqs.clone();
						}
					} else {
						crc = MathUtil.crc40xC9DA(req, j, len - 2);
						if(crc[0] != req[len - 2] || crc[1] != req[len - 1]){
							throw new RuntimeException("读取温度数据包错误！");
						} else {
							rlen += len - j; 
							reqs = new byte[rlen];
							if(old.length > 0){
								System.arraycopy(old, 0, reqs, 0, old.length);
							}
							System.arraycopy(req, j, reqs, old.length, len - j);
						}
					}
				}
				byte[] reqss = new byte[reqs.length];
				if(reqs.length > 1){
					System.arraycopy(reqs, 1, reqss, 0, reqs.length - 1);
				}
				return reqss;
			}
		} catch (Exception e){
			throw new RuntimeException(e.getMessage());
		} finally {
			closeAll();
		}
	}
	
}