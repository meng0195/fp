package cn.com.bjggs.warns.service.impl;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.warns.domain.Alarm;
import cn.com.bjggs.warns.domain.Alarms;
import cn.com.bjggs.warns.service.IWarnsService;
import cn.com.bjggs.warns.util.WarnsUtil;

@IocBean(name = "warnsService", args = { "refer:dao" })
public class WarnsServiceImpl extends BaseServiceImpl implements IWarnsService{

	public WarnsServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	/**
	 * 保存报警配置信息并跟新缓存
	 * @author	wc
	 * @date	2017-08-29
	 */
	public void save(Alarms as){
		//删除原有记录
		this.delete(Alarm.class, Cnd.where("houseNo", "=", as.getHouseNo()));
		//插入新记录
		List<Alarm> list = as.getAlarms();
		dao.insert(list);
		//更新报警缓存
		WarnsUtil.refresh(as.getHouseNo(), list);
	}
}
