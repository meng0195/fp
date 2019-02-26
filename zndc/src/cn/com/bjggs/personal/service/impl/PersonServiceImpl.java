package cn.com.bjggs.personal.service.impl;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.personal.service.IPersonService;
import cn.com.bjggs.system.domain.SysUser;

@IocBean(name = "personService", args = { "refer:dao" })
public class PersonServiceImpl extends BaseServiceImpl implements IPersonService{

	public PersonServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void updateUser(SysUser user){
		Chain c = Chain.make("idcard", user.getIdcard()).add("name", user.getName()).add("email", user.getEmail()).add("phone", user.getPhone());
		dao.update(SysUser.class, c, Cnd.where("id", "=", user.getId()));
	}
}
