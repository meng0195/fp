package cn.com.bjggs.weather.service.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.weather.domain.TestWeather;
import cn.com.bjggs.weather.domain.WeatherInfo;
import cn.com.bjggs.weather.service.IWertherService;
import cn.com.bjggs.weather.util.WeatherUtil;

@IocBean(name = "weatherService", args = { "refer:dao" })
public class WeatherServiceImpl extends BaseServiceImpl implements IWertherService{

	public WeatherServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void update(WeatherInfo w){
		//修改最新的配置信息
		super.update(w);
		//判断最新的刷新时间跟之前的刷新时间是否一致,如果不一致,则删除定时器,新增定时器
		if(WeatherUtil.weatherInfo != null){
			if (WeatherUtil.weatherInfo.getRefreTime() != w.getRefreTime()) {
				WeatherUtil.weatherInfo = w;
				QuartzUtil.removeJob(WeatherUtil.CODE);
				WeatherUtil.initQuart();
			}
		} else {
			WeatherUtil.weatherInfo = w;
			WeatherUtil.initQuart();
		}
	}
	
	public TestWeather refresh(){
		TestWeather tw = WeatherUtil.getWeaTest();
		return tw;
	}
	
}
