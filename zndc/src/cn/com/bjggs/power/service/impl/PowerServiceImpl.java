package cn.com.bjggs.power.service.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.power.service.IPowerService;

@IocBean(name = "powerService", args = { "refer:dao" })
public class PowerServiceImpl extends BaseServiceImpl implements IPowerService{
	
	public PowerServiceImpl(Dao dao){
		this.dao = dao;
	}
	

}
