package cn.com.bjggs.gas.service;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.system.domain.SysUser;

public interface IGasService extends IBaseService{

	public void updateRanks(Integer[] xaxis, Integer[] yaxis, String houseNo);
	
	public String getGasConf(SysUser user);
	
	public void saveConf(SysUser user, String[] houseNos);
	
	public void start(int[] ways, String houseNo);
	
	public void starts(String uid);
	
	public void stops();
	
	public String getMains(SysUser user);
}
