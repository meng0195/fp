package cn.com.bjggs.report.service;

import java.util.List;
import java.util.Map;

import cn.com.bjggs.core.service.IBaseService;
import cn.com.bjggs.squery.domain.QueryTemp;
import cn.com.bjggs.temp.domain.TestData;

public interface IReportService extends IBaseService{
	
	public List<TestData> getReportTemp(QueryTemp t);
	
	public Map<String, Object> getReportAll(QueryTemp t, String houseNo);
	
	public String xlsAll(String path, String testDate);
	
	public String xlsAll1(String path, String testDate, String houseNo);
	
	public String xlsAll2(String path, String testDate, String houseNo);
	
	public String getPrints(String testDate);
	
	public String xlsDataExp(String downFileDir);
}
