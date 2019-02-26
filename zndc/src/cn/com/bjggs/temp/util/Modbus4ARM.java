package cn.com.bjggs.temp.util;

import java.util.HashMap;
import java.util.Map;

import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.util.MathUtil;

/**
 * 老ARM板子读取温度帮助类
 * @author	yucy	
 * @date	2018-01-29
 */
public class Modbus4ARM implements Checks{
	
	private static final Log log = Logs.getLog(Modbus4ARM.class);
	
	private Modbus m = new Modbus();
	
	private TempControl tc;
	
	public Modbus4ARM(TempControl tc){
		this.tc = tc;
	}
	
	public Modbus4ARM(String ip, int port){
		this.tc = new TempControl(ip, port);
	}
	
	public Modbus4ARM(String ip, int port, int way){
		this.tc = new TempControl(ip, port, way);
	}
	
	public Modbus4ARM(String ip){
		this.tc = new TempControl(ip);
	}

	/**
	 * 获取ARM的仓温仓湿,有尝试次数在Modbus 类中设定
	 */
	public byte[] getTHs(){
		byte[] req = null;
		int num = Modbus.ATTEMPTS;
		while(num > 0){
			try{
				req = m.sendMsg(CmdsNewTcp.R_4_THI, tc, true, 16);
				break;
			}catch(Exception e){
				num = num - 1;
				if(num != 0){
					log.info("读取仓温仓湿失败！500ms后再次尝试");
				} else {
					throw new RuntimeException("读取仓温仓湿集合失败!");
				}
				try{Thread.sleep(150*num);}catch(Exception ee){};
			}
		}
		return req;
	}
	
	/**
	 * 根据通道号获取仓温仓湿，有尝试次数在Modbus 类中设定
	 */
	public byte[] getTH(int way){
		byte[] req = new byte[4];
		String datas;
		synchronized (tc.getIp()) {
			datas = TempARMUtil.SendGet(tc.getIp(), "TAndMDatas");
		}
		String[] split = datas.split("	");
		Map<String,String> wsdMap = new HashMap<String,String>();
		for(int i=1;i<split.length;i++){
 			String key = split[i].split("=")[0];
			String value = split[i].split("=")[1];
			wsdMap.put(key, value);
		}
		String Hum = wsdMap.get("Mdata"+way)==null?"0":wsdMap.get("Mdata"+way).substring(0, wsdMap.get("Mdata"+way).length()-1);
		String Temp = wsdMap.get("TempData"+(way-1))==null?"0x00":wsdMap.get("TempData"+(way-1));
		int hum = Integer.parseInt(Hum)*10;
		int temp = Integer.parseInt(Temp.substring(2),16);
		req[0] = MathUtil.int2HByte(temp);
		req[1] = MathUtil.int2LByte(temp);
		req[2] = MathUtil.int2HByte(hum);
		req[3] = MathUtil.int2LByte(hum);
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
//				String countUrl = "http://"+tc.getIp()+"/RTU?TotalSensorNum=!";
//				String countUrl = "http://192.168.30.101/RTU?TotalSensorNum=!";
//				int count = Integer.parseInt(getResult(countUrl, Request.METHOD.GET));
//				if(count <= 0) throw new RuntimeException("读取传感器总数错误！");
//				req = new byte[count*4];
////				String url = "http://"+tc.getIp()+"/RTU?ALLTempDatas=!";
//				String url = "http://192.168.30.101/RTU?ALLTempDatas=!";
//				String datas = getResult(url, Request.METHOD.GET);
				String cstr;
				synchronized (tc.getIp()) {
					cstr = TempARMUtil.SendGet(tc.getIp(), "TotalSensorNum");
				}
				
				if (Strings.isNotBlank(cstr)) {
					String[] c = cstr.split("=");
					String cou = c[1].substring(0, c[1].length()-1);
					int count = Integer.parseInt(cou);
					if(count <= 0) throw new RuntimeException("读取传感器总数错误！");
					req = new byte[count*4];
					String datas;
					synchronized (tc.getIp()) {
						datas = TempARMUtil.SendGet(tc.getIp(), "ALLTempDatas");
					}
					String[] data1 = datas.split("	");
					String[] data = new String[data1.length-2];
					System.arraycopy(data1, 1, data, 0, data.length);
					int temp;
					int j = 0;
					for (int i = 0; i < data.length;i=i+5) {
						req[j++] = (byte)Integer.parseInt(data[i+1]);
						req[j++] = (byte)Integer.parseInt(data[i+2]);
						temp = Integer.parseInt(data[i+3].substring(2),16);
						req[j++] = MathUtil.int2HByte(temp);
						req[j++] = MathUtil.int2LByte(temp);
					}
				}
				break;
			}catch(Exception e){
				e.printStackTrace();
				num = num - 1;
				if(num != 0){
					log.info("读取温度失败！500ms后再次尝试");
				} else {
					throw new RuntimeException("温度读取失败！");
				}
				try{Thread.sleep(150*num);}catch(Exception ee){};
			}
		}
		return req;
	}
	
//	public String getResult(final String url, final Request.METHOD method) {
//		final Request request = Request.create(url, method);
//		final GetSender sender = new GetSender(request);
//		final Response resp = sender.send();
//		return resp.getContent();
//	}
	
}
