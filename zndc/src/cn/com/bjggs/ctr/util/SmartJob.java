package cn.com.bjggs.ctr.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SmartJob implements Job {
	
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		SmartUtil.refreshEv();
	}
	
}

