package cn.com.bjggs.pest.service;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.pest.domain.CheckPoints;
import cn.com.bjggs.pest.domain.GateInfos;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestPoints;

public interface IPestService extends IBaseService{

	public void updateConf(PestInfo p, GateInfos gis);
	
	public void updatePoint(PestPoints pis);
	
	public void checkPoints(CheckPoints cps);
	
	public void checkHouses(String[] houses);
	
}
