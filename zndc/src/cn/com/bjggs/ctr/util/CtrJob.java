package cn.com.bjggs.ctr.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.domain.PlanCtr;
import cn.com.bjggs.ctr.domain.PlanEquip;
import cn.com.bjggs.ctr.enums.TypeCtrTag;
import cn.com.bjggs.ctr.enums.TypeOnOrOff;

public class CtrJob implements Job {
	
	private static Dao dao;
	
	public static void initDao(Dao d){
		dao = d;
	}
	
	private ExecutorService esTime = null;
	
	public void execute(JobExecutionContext jec) throws JobExecutionException {
		String planCode = jec.getJobDetail().getKey().getName();
		planCode = planCode.replace("c", "");
		PlanCtr pc = dao.fetch(PlanCtr.class, Cnd.where("planCode", "=", planCode));
		if(pc != null && Strings.isNotBlank(pc.getHouseNo())){
			//处理开启计划
			CtrResults crs = CtrUtil.lasts.get(pc.getHouseNo());
			List<PlanEquip> list = dao.query(PlanEquip.class, Cnd.where("planCode", "=", planCode));
			Map<Integer, Equipment> es = crs.getEquips();
			Equipment ep = null;
			if(pc.getType() == TypeOnOrOff.ON.val()){
				try {
					//根据数量起线程池
					Date now = new Date();
					esTime = Executors.newFixedThreadPool(30);
					for(PlanEquip pe : list){
						ep = es.get(pe.getEquipNo());
						if(ep != null && !CtrUtil.isOpen(ep)){
							Thread ctr = new CtrPlan(ep, CtrConstant.CTR_OPEN_TAG, TypeCtrTag.DS.val(), planCode, now);
							esTime.execute(ctr);
							Thread.sleep(300);
						}
					}
				} catch(Exception e) {
					
				} finally {
					if(null != esTime){
						//关闭线程池
						esTime.shutdown();
						esTime = null;
					}
				}
			} else {
				try {
					//根据数量起线程池
					Date now = new Date();
					esTime = Executors.newFixedThreadPool(30);
					for(PlanEquip pe : list){
						ep = es.get(pe.getEquipNo());
						if(ep != null && !CtrUtil.isClose(ep)){
							Thread ctr = new CtrPlan(ep, CtrConstant.CTR_CLOSE_TAG, TypeCtrTag.DS.val(), planCode, now);
							esTime.execute(ctr);
							Thread.sleep(300);
						}
					}
				} catch(Exception e) {
					
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
}

