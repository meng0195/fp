package cn.com.bjggs.basis.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.ChecksTime;
import cn.com.bjggs.basis.domain.PlanTask;
import cn.com.bjggs.basis.service.ITaskService;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.ParseUtil;

@IocBean(name = "taskService", args = { "refer:dao" })
public class TaskServiceImpl extends BaseServiceImpl implements ITaskService{

	public TaskServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void timeSave(PlanTask p, String[] houseNo){
		p.initCronAndDesc();
		//处理计划编号
		if(Strings.isBlank(p.getPlanCode())){
			p.setPlanCode(getNextPlanCode());
		}
		update(p);
		List<ChecksTime> list = new LinkedList<ChecksTime>();
		ChecksTime ct;
		for(String s : houseNo){
			ct = new ChecksTime(s, p.getPlanCode());
			list.add(ct);
		}
		delete(ChecksTime.class, Cnd.where("planCode", "=", p.getPlanCode()));
		dao.insert(list);
	}
	
	private String getNextPlanCode(){
		Sql sql = Sqls.create("SELECT PlanCode as PLANCODE FROM PlanTask ORDER BY PlanCode ASC");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> pcs = sql.getList(Record.class);
		int number = 0;
		int planCode = 0;
		boolean tag = true;
		for(Record r : pcs){
			number = number + 1;
			planCode = ParseUtil.toInt(r.get("PLANCODE"), 0);
			if(number != planCode){
				tag = false;
				break;
			}
		}
		if(tag){
			planCode = number + 1;
		} else {
			planCode = number;
		}
		
		String str;
		if(0 < planCode && planCode < 10){
			str = "00" + planCode;
		}else if(9 < planCode && planCode < 100){
			str = "0" + planCode;
		}else { 
			str = "" + planCode;
		}
		return str;
	}
	
	public void updateStatus(String planCode, int status){
		dao.update(PlanTask.class, Chain.make("status", status == TypeFlag.YES.val() ? TypeFlag.YES.val() : TypeFlag.NO.val()), Cnd.where("planCode", "=", planCode));
	}
	
	public void starts(int type){
		List<PlanTask> ps = dao.query(PlanTask.class, Cnd.where("status", "=", TypeFlag.NO.val()).and("type", "=", type));
		if(ps != null){
			int len = ps.size();
			String[] codes = new String[len];
			String[] times = new String[len];
			PlanTask p;
			for(int i = 0; i < len; i++){
				p = ps.get(i);
				codes[i] = p.getPlanCode();
				times[i] = p.getTimeCron();
			}
			QuartzUtil.startJobs(codes, QuartzUtil.getJobClass(type), times);
			dao.update(PlanTask.class, Chain.make("status", TypeFlag.YES.val()), Cnd.where("status", "=", TypeFlag.NO.val()).and("type", "=", type));
		}
	}
	
	public void stops(int type){
		List<PlanTask> ps = dao.query(PlanTask.class, Cnd.where("status", "=", TypeFlag.YES.val()).and("type", "=", type));
		QuartzUtil.removeJobs(ps);
		dao.update(PlanTask.class, Chain.make("status", TypeFlag.NO.val()), Cnd.where("status", "=", TypeFlag.YES.val()).and("type", "=", type));
	}
}
