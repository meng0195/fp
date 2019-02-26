package cn.com.bjggs.camera.service;

import cn.com.bjggs.core.service.IBaseService;

public interface ICamService extends IBaseService{

	public void updateStatus(String id, int status);
	
	public int delete(String id);
	
	public int deleteAll(String[] uids);
	
	public void saveRank(double[] xaxis, double[] yaxis);
}
