package cn.com.bjggs.personal.service;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.system.domain.SysUser;

public interface IPersonService extends IBaseService{
	
	public void updateUser(SysUser user);
	
}
