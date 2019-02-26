package cn.com.bjggs.squery.domain;

import java.util.Date;

import org.nutz.lang.Times;

public class QueryPowerTrend {
	
	private String[] modelType;
	
	private String houseNo;
	
	private String time;

	private int timeType;

	public String[] getModelType() {
		return modelType;
	}

	public void setModelType(String[] modelType) {
		this.modelType = modelType;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public String getTime() {
		if (time == null) {
			return Times.format("yyyy-MM-dd", new Date());
		} else {
			return time;
		}
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
