package cn.com.bjggs.ctr.util;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.ctr.enums.TypeCtrTag;

public class ScanSmartJob implements Job {
	
	private static final int max = PropsUtil.getInteger("smart.eps.max", 3);
	
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		ExecutorService sScan = Executors.newFixedThreadPool(max);
		try {
			for(Map.Entry<String, Integer> entry : SmartUtil.models.entrySet()){
				if(entry.getValue() == TypeCtrTag.ZN.val()){
					SmartScan ss = new SmartScan(entry.getKey());
					sScan.execute(ss);
				}
			}
		} catch(Exception e) {
			
		} finally {
			if(null != sScan){
				//关闭线程池
				sScan.shutdown();
				sScan = null;
			}
		}
	}
	
}

