package cn.com.bjggs.squery.domain;

import java.util.Date;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.core.enums.TypeDate;
import cn.com.bjggs.core.util.DateUtil;

public class QueryTemp {
	
	private String houseNo;
	
	private Date startTime;
	
	private Date endTime;
	
	private boolean flag = false;

	public String getHouseNo() {
		if (Strings.isEmpty(houseNo)) {
			return Lang.isEmpty(HouseUtil.houseMap) ? null : HouseUtil.houseMap.keySet().iterator().next();
		} else {
			return houseNo;
		}
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public Date getStartTime() {
		if (startTime == null) {
			return DateUtil.add(new Date(), -7, TypeDate.D);
		} else {
			return startTime;
		}
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		if (endTime == null) {
			return new Date();
		} else {
			return endTime;
		}
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
