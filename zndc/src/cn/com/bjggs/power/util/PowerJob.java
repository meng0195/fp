package cn.com.bjggs.power.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PowerJob implements Job {
	
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		PowerUtil.savePower();
	}
}

