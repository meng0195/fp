package cn.com.bjggs.squery.domain;

import java.util.Date;

import org.nutz.lang.Times;

import cn.com.bjggs.core.enums.TypeDate;
import cn.com.bjggs.core.util.DateUtil;

public class QueryPowerRank {
	
	private String[] houseNo;
	
	private String startTime;
	
	private String endTime;

	public String[] getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String[] houseNo) {
		this.houseNo = houseNo;
	}

	public String getStartTime() {
		if (startTime == null) {
			return Times.format("yyyy-MM-dd HH:mm:ss", DateUtil.add(new Date(), -7, TypeDate.D));
		} else {
			return startTime;
		}
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		if (endTime == null) {
			return Times.format("yyyy-MM-dd HH:mm:ss", new Date());
		} else {
			return endTime;
		}
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
