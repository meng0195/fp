package cn.com.bjggs.ctr.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.core.util.PropsUtil;

public class ScanARMJob implements Job {

	public void execute(JobExecutionContext jec) throws JobExecutionException {
		ExecutorService eScan = Executors.newFixedThreadPool(PropsUtil.getInteger("nhl.scan.max", 5));
		for(final String houseNo : HouseUtil.houseMap.keySet()){
			eScan.execute(new Runnable() {
				public void run() {
					ScanARMUtil.refreshSmartStatus(houseNo);
				}
			});
		}
	}
	
}

