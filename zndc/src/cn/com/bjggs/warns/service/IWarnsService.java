package cn.com.bjggs.warns.service;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.warns.domain.Alarms;

public interface IWarnsService extends IBaseService{

	public void save(Alarms as);
	
}
