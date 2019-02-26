package cn.com.bjggs.ctr.service;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.ctr.domain.PlanCtr;
import cn.com.bjggs.ctr.domain.SmartConf;
import cn.com.bjggs.system.domain.SysUser;

public interface ICtrService extends IBaseService{

	public void doall(String houseNo, int equipNo, SysUser user);
	
	public void savePlan(PlanCtr p, int[] equipNos);
	
	public void updateStatus(String planCode, int status);
	
	public void addConfs(String houseNo);
	
	public void smartSave(SmartConf s);
	
}
