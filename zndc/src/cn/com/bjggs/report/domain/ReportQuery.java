package cn.com.bjggs.report.domain;

import java.util.Date;

public class ReportQuery {
	
	private String houseNo;
	
	private String[] houseNos;
	
	private Date time;
	
	private Date startTime;
	
	private Date endTime;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String[] getHouseNos() {
		return houseNos;
	}

	public void setHouseNos(String[] houseNos) {
		this.houseNos = houseNos;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


}
