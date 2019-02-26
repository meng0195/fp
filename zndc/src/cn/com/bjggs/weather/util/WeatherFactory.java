package cn.com.bjggs.weather.util;

import cn.com.bjggs.weather.domain.WeatherInfo;
import cn.com.bjggs.weather.enums.TypeWeather;


public class WeatherFactory {
	
	public static final Weathers getWeatherInfo(WeatherInfo w){
		if(w.getType() == TypeWeather.NET.val()){
			return new WeatherTCP(w);
		} else if (w.getType() == TypeWeather.COM.val()){
			//return new WeatherTCP(w);
		} else if (w.getType() == TypeWeather.ONET.val()){
			return new WeatherOldTCP(w);
		}
		return null;
	}
	
}
