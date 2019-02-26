package cn.com.bjggs.temp.service;

import org.nutz.dao.entity.Record;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.temp.domain.LoopConf;
import cn.com.bjggs.temp.domain.TempBoaInfos;
import cn.com.bjggs.temp.domain.TempInfo;

public interface ITempService extends IBaseService {

	public void save(StoreHouse s, TempBoaInfos tb, TempInfo t, String houseNo, int tag);

	public Record getHouseAndPoints(String houseNo);
	
	public String getChecksOne(SysUser user);
	
	public String getChecksOneConf(SysUser user);
	
	public LoopConf getLoopConf(SysUser user);
	
	public void oneSave(SysUser user, String[] houseNo);
	
	public void loopSave(SysUser user, String[] houseNos);
	//手动检测
	public void oneStart(SysUser user, int reportFlag);
	//循环检测
	public void loopStart(SysUser user);
	//显示实时数据
	public String showRealData(String id);
	//区域划分
	public Record getHouseAndArea(String houseNo);
	//保存区域
	public void updateArea(String houseNo, String cable, String area, String[] t1, String[] t2, String[] t3, String[] t4);
	
	public String getChange(String id);
	
	public void saveChange(String id, double[] temps, int[] indexs);
}
