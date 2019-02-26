package cn.com.bjggs.temp.util;

import cn.com.bjggs.weather.util.WeatherFactory;
import cn.com.bjggs.weather.util.WeatherUtil;
import cn.com.bjggs.weather.util.Weathers;


/**
 * 气象站读取外温外湿
 * @author	wc
 * @date	2017-10-02
 */
public class Modbus4Wea implements Checks{
	
	public byte[] getTHs(){
		Weathers ws = WeatherFactory.getWeatherInfo(WeatherUtil.weatherInfo);
		byte[] datas = ws.getDatas();
		return datas;
	}
	
	public byte[] getTH(int way){
		byte[] th = getTHs();
		byte[] req = new byte[4];
		if(th == null || th.length != 22){
			throw new RuntimeException("外温外湿读取错误！");
		}
		System.arraycopy(th, 0, req, 0, 4);
		return req;
	}
	
	public byte[] getTemps(){
		return null;
	}
}
