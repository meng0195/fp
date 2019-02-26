package cn.com.bjggs.squery.service;

import java.util.Map;

import org.nutz.dao.entity.Record;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.squery.domain.QueryPower;
import cn.com.bjggs.squery.domain.QueryPowerRank;
import cn.com.bjggs.squery.domain.QueryPowerTrend;
import cn.com.bjggs.squery.domain.QueryTemp;

public interface IQueryService extends IBaseService {
	
	public String getGasCurve(Map<String, String[]> params);
	
	public String getPestCurve(Map<String, String[]> params);
	
	public String getPestCompare(Map<String, String[]> params);
	
	public String getPestTimes(String houseNo);
	
	public String getDetailWarn(String testDataId);
	
	public Record findSheetByTsId(String id);
	
	public Record findSheetByHouseNo(String houseNo);
	
	public String[] findStereoByHouseNo(String houseNo);
	
	public String[] findStereoByTsId(String id);
	
	public Map<String, Object> getWarnDetail(String houseNo, int key);
	
	public Map<String, Object> getWarnDetailId(String testDataId, int key);
	
	public String getCurve(QueryTemp t);
	
	public String getCps(QueryTemp query);
	
	public String getTempDate(String houseNo, String date);
	
	public PestResults getPestById(String testId);
	
	public GasResults getGasById(String testId);
	
	public String getPestWarnDetail(String houseNo, String testId, int type);
	
	public String getGasWarnDetail(String houseNo, String testId, int type);
	
	public String getJson3d(String houseNo, String testId);
	//能耗
	public Map<String, Map<String, Double>> queryPowerList(QueryPower qt);
	
	public String getPowerRank(QueryPowerRank qr);
	
	public String getPowerTrend(QueryPowerTrend qt);
	
	public String queryPowerPie(QueryPower qt);
	
	public String queryPowerCurves(QueryPower qt);
	
	public void updateStatus(String type, String flag, String id, int status);
	
	public void checkTempReport(String id);
}
