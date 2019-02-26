package cn.com.bjggs.weather.util;

import java.util.Date;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.enums.TypeProtocol;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.temp.util.Checks;
import cn.com.bjggs.temp.util.ModbusFactory;
import cn.com.bjggs.temp.util.TempFactory;
import cn.com.bjggs.temp.util.Temps;
import cn.com.bjggs.weather.domain.TestWeather;
import cn.com.bjggs.weather.domain.WeatherInfo;

public class WeatherUtil {
	
	private static final Log log = Logs.getLog(WeatherUtil.class);
	private static Dao dao;
	public static WeatherInfo weatherInfo = null;
	public static TestWeather testWeather = new TestWeather();
	public static Date lastTime = null;
	public static final String CODE = PropsUtil.getString("weather.plan.code");
	public static void initWea(Dao d){
		dao = d;
		try {
			//1将配置信息放入静态资源中
			weatherInfo = dao.fetch(WeatherInfo.class, Cnd.cri());
			//2获取一次气象站信息,放入最后一次
			getWeaTest();
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("气象站初始化失败,请检查配置信息和网络!");
			}
		}
		
		//3将定时器写入
		if (weatherInfo != null) {
			initQuart();
		}
		
	}

	public static void initQuart(){
		QuartzUtil.addJob(CODE, WeatherJob.class, weatherInfo.initCron());
	}
	
	public synchronized static final TestWeather getWeaTest(){
		try {
			byte[] data = new byte[22];
			if(Strings.isNotBlank(weatherInfo.getIp()) && weatherInfo.getPort() != 0){
				Weathers ws = WeatherFactory.getWeatherInfo(weatherInfo);
				data = ws.getDatas();
			}
			Date nowTime = new Date();
			if(weatherInfo.getOutType() == 1 && Strings.isNotBlank(weatherInfo.getOutIp()) && weatherInfo.getOutPort() != 0){
				//重新获取温湿
				Checks check =ModbusFactory.createCheck(TypeProtocol.NTCP.val(), weatherInfo.getOutIp(), weatherInfo.getOutPort());
				Temps tUtil = TempFactory.createCheck(TypeProtocol.NTCP.val());
				byte[] outTH = check.getTH(weatherInfo.getOutWay());
				data[2] = outTH[2];
				data[3] = outTH[3];
				int temp10 = tUtil.getT10(outTH, 0, 1);
				data[0] = MathUtil.int2HByte(temp10);
				data[1] = MathUtil.int2LByte(temp10);
			}
			testWeather = new TestWeather(nowTime, data);
			if(lastTime == null || nowTime.getTime() - lastTime.getTime() > weatherInfo.getSaveTime() * 1000 * 60){
				lastTime = nowTime;
				dao.insert(testWeather);
			}
		} catch (Exception e) {
			if(log.isErrorEnabled()){
				log.error("气象站初始化失败,请检查配置信息和网络!");
			}
		}
		return testWeather;
	}
	
}
