package cn.com.bjggs.basis.service;

import cn.com.bjggs.basis.domain.CopyGrainInfo;
import cn.com.bjggs.basis.domain.CopyStoreHouse;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.domain.GlobalConf;
import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.core.service.IBaseService;

public interface IBasisService extends IBaseService{

	public void update(StoreHouse s,GrainInfo g);
	
	public String getNextHouseNo();
	
	public int deleteAll(String id);
	
	public int deleteAll(String[] id);
	
	public void updateInitNo(Equipment equip);
	
	public void saveRank(int[] xaxis, int[] yaxis, String houseNo);
	
	public void updateGlobal(GlobalConf g);
	
	public void ifaceSave(CopyStoreHouse s);
	
	public void ifaceGrainSave(CopyGrainInfo g);
}
