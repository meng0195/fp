package cn.com.bjggs.pest.util;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.bjggs.basis.domain.ChecksTime;
import cn.com.bjggs.basis.domain.PlanTask;
import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.core.util.PropsUtil;

public class PestJob implements Job {
	
	private static Dao dao;
	
	public static final void initDao(Dao d){
		dao = d;
	}
	
	private ExecutorService esTime = null;
	
	private static final int NUM = PropsUtil.getInteger("pest.max.time", 5);
	
	@Override
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		String planCode = jec.getJobDetail().getKey().getName();
		PlanTask p = dao.fetch(PlanTask.class, Cnd.where("planCode", "=", planCode));
		if(p != null){
			try {
				//根据数量起线程池
				esTime = Executors.newFixedThreadPool(NUM);
				List<ChecksTime> list = dao.query(ChecksTime.class, Cnd.where("planCode", "=", planCode));
				Date testDate = new Date();
				for(ChecksTime c : list){
					Thread.sleep(100);
					if(!CheckPest.checks.containsKey(c.getHouseNo())){
						Thread checks = new CheckPest(c.getHouseNo(), TypeCheck.TIME, null, planCode, testDate);
						esTime.execute(checks);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(null != esTime){
					//关闭线程池
					esTime.shutdown();
					esTime = null;
				}
			}
		}
	}
}
