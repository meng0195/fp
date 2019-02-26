package cn.com.bjggs.basis.service;

import cn.com.bjggs.basis.domain.PlanTask;
import cn.com.bjggs.core.service.IBaseService;

public interface ITaskService extends IBaseService{
	
	public void timeSave(PlanTask p, String[] houseNo);
	
	public void updateStatus(String planCode, int status);
	
	public void starts(int type);
	
	public void stops(int type);
	
}
