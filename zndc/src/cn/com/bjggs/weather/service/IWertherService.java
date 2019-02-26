package cn.com.bjggs.weather.service;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.weather.domain.TestWeather;
import cn.com.bjggs.weather.domain.WeatherInfo;

public interface IWertherService extends IBaseService{

	public void update(WeatherInfo w);
	
	public TestWeather refresh();
}
