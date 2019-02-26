package cn.com.bjggs.weather.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class WeatherJob implements Job {
	
	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		//定时将数据插入数据库
		WeatherUtil.getWeaTest();
	}
    
}
