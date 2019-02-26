package cn.com.bjggs.basis.util;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.PlanTask;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.ctr.domain.PlanCtr;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.ctr.util.CtrJob;
import cn.com.bjggs.ctr.util.SmartUtil;
import cn.com.bjggs.gas.util.GasJob;
import cn.com.bjggs.pest.util.PestJob;
import cn.com.bjggs.temp.util.TempJob;

public class QuartzUtil {
	
	private static final Log log = Logs.getLog(QuartzUtil.class);
	
	private static SchedulerFactory sFactory = new StdSchedulerFactory();
	private static Scheduler sched;
	private static String JG = "JOBS";
	private static String TG = "TRIS";
	
	static {
		try {
			sched = sFactory.getScheduler();
			sched.start();
		} catch (Exception e){
			if(log.isErrorEnabled()){
				log.error("启动定时调度器失败！");
			}
		}
	}
	
	/**
	 *  添加任务根据任务名称
	 * @author	wc
	 * @date	2017年7月21日
	 * @return	void
	 */
	public static void addJob(String jobName, Class<? extends Job> clazz, String time){
		addJob(jobName, jobName, clazz, time);
	}
	
	/**
	 * 
	 */
	public static void addJob(PlanTask p){
		addJob(p.getPlanCode(), p.getPlanCode(), getJobClass(p), p.getTimeCron());
	}
	
	/** 
	 * 添加任务根据任务名称定时器名称
	 * @author	wc
	 * @date	2017年7月21日
	 * @return	void
	 */
    public static void addJob(String jobName, String triggerName, Class<? extends Job> clazz, String time){  
        try {
        	//重复校验
        	JobKey jobKey = JobKey.jobKey(jobName, JG);
        	JobDetail jobDetail = sched.getJobDetail(jobKey);
        	if(jobDetail != null){
        		throw new RuntimeException("定时任务已添加，不能重复添加！");
        	}
            jobDetail = JobBuilder.newJob(clazz)
            		.withIdentity(jobName, JG)
            		.build();// 任务名，任务组，任务执行类  
            // 触发器  
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, TG)
                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
                    .build();// 触发器名,触发器组  
            sched.scheduleJob(jobDetail, trigger);
            if(!sched.isStarted()) sched.start();
        } catch (Exception e) {
        	if(log.isErrorEnabled()){
        		log.error("添加定时任务失败：" + e.getMessage());
        	}
            throw new RuntimeException("添加定时任务失败：" + e.getMessage());  
        }  
    }
    
    /**
     * 修改任务触发器(触发器名称默认)
     * @author	wc
     * @date	2017年7月21日
     * @return	void
     */
    public static void modifyJobTime(String jobName, String time){
    	modifyJobTime(jobName, TG, time);
    }
    
    /**
     * 修改任务触发器
     * @author	wc
     * @date	2017年7月21日
     * @return	void
     */
    public static void modifyJobTime(String triggerName, String triggerGroupName, String time){
    	try{
    		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
	    	CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);  
	        if (trigger == null) return;
	        String oldTime = trigger.getCronExpression();  
            if (!Strings.equalsIgnoreCase(oldTime, time)) { 
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
                trigger = (CronTrigger)triggerBuilder.build();
                sched.rescheduleJob(triggerKey, trigger);
            }
    	} catch(Exception e) {
    		if(log.isErrorEnabled()){
        		log.error("修改定时触发器失败：" + e.getMessage());
        	}
            throw new RuntimeException("修改定时触发器失败：" + e.getMessage());  
    	}
    }
    
    /**
     * 删除定时任务(触发器名称默认)  
     * @author	wc
     * @date	2017年7月21日
     * @return	void
     */
    public static void removeJob(String jobName) {
    	removeJob(jobName, jobName);
    }
    
    /**
     * 删除定时任务  
     * @author	wc
     * @date	2017年7月21日
     * @return	void
     */
    public static void removeJob(String jobName, String triggerName) {  
        try {  
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TG);
            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(JobKey.jobKey(jobName, JG));// 删除任务  
        } catch (Exception e) {
        	if(log.isErrorEnabled()){
        		log.error("删除定时任务失败：" + e.getMessage());
        	}
            throw new RuntimeException("删除定时任务失败：" + e.getMessage());    
        }  
    }
    
    /**
     * 开启所有任务(并不添加)
     * @author	wc
     * @date	2017年7月21日
     * @return	void
     */
    public static void startJobs() {  
        try {
        	if(!sched.isStarted()) sched.start();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    /**
     * 关闭所有任务
     * @author	wc
     * @date	2017年7月21日
     * @return	void
     */
    public static void shutdownJobs() {  
        try {  
            if (!sched.isShutdown()) sched.shutdown();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    /**
     * 开启全部
     * @author	wc
     * @date	2017年7月21日
     * @return	int
     */
    public static int startJobs(String[] jobNames,  Class<? extends Job> clazz, String[] times){
    	int len = Math.min(jobNames.length, times.length);
    	int num = 0;
    	for(int i = 0; i < len; i++){
    		try {
    			addJob(jobNames[i], clazz, times[i]);
    			num = num + 1;
			} catch (Exception e) {}
    	}
    	return num;
    }
    
    /**
     * 停止全部
     * @author	wc
     * @date	2017年7月22日
     */
    public static int removeJobs(String[] jobNames){
    	int num = 0;
    	for(String jobName : jobNames){
    		try {
    			removeJob(jobName);
    			num = num + 1;
			} catch (Exception e) {}
    	}
    	return num;
    }
    
    /**
     * 停止全部
     * @author	wc
     * @date	2017年7月22日
     */
    public static int removeJobs(List<PlanTask> list){
    	int num = 0;
    	for(PlanTask p : list){
    		try {
    			removeJob(p.getPlanCode());
    			num = num + 1;
			} catch (Exception e) {}
    	}
    	return num;
    }
    
    public static final Class<? extends Job> getJobClass(PlanTask p){
    	if(p.getType() == Constant.PT_T){
    		return TempJob.class;
    	}
    	if(p.getType() == Constant.PT_P){
    		return PestJob.class;
    	}
    	if(p.getType() == Constant.PT_G){
    		return GasJob.class;
    	}
    	return null; 
    }
    
    public static final Class<? extends Job> getJobClass(int type){
    	if(type == Constant.PT_T){
    		return TempJob.class;
    	}
    	if(type == Constant.PT_P){
    		return PestJob.class;
    	}
    	if(type == Constant.PT_G){
    		return GasJob.class;
    	}
    	return null; 
    }
    
    public static final void initAll(Dao dao){
    	List<PlanTask> ps = dao.query(PlanTask.class, Cnd.where("status", "=", TypeFlag.YES.val()));
    	if(ps != null){
			for(PlanTask p : ps){
				addJob(p);
			}
		}
    	//调用时先添加定时再添加定量
    	List<PlanCtr> pc = dao.query(PlanCtr.class, Cnd.where("status", "=", TypeFlag.YES.val()));
    	if(pc != null){
    		for(PlanCtr p : pc){
    			QuartzUtil.addJob("c" + p.getPlanCode(), CtrJob.class, p.getTimeCron());
				SmartUtil.changePlans(p.getHouseNo(), 1);
				SmartUtil.changeModel(p.getHouseNo(), TypeCtrTag.DS.val());
    		}
    	}
    }
}
