package cn.com.bjggs.power.domain;

import java.util.Date;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("能耗记录表")
@Table("PowerNotes")
public class PowerNotes extends IdEntry{
	
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	@Column("LastTime")
	@Comment("上次检测时间")
	private Date lastTime;
	
	@Column("CheckTime")
	@Comment("本次检测时间")
	private Date checkTime;
	
	@Column("IntervalT")
	@Comment("时间间隔")
	private long intervalT;
	
	@Column("CheckPower")
	@Comment("检测能耗")
	private double checkPower;
	
	@Column("Power")
	@Comment("消耗能耗")
	private double power;
	
	
	public PowerNotes(){}
	
	public PowerNotes(PowerInfo p, Date lastTime, double checkPower, double beforePower){
		this.setId(UUID.randomUUID().toString());
		this.houseNo = p.getHouseNo();
		this.checkTime = new Date();
		this.lastTime = lastTime;
		this.intervalT = checkTime.getTime() - lastTime.getTime();
		this.checkPower = checkPower;
		this.power = checkPower - beforePower;
	}
	
	public PowerNotes(PowerInfo p, double checkPower, double beforePower){
		this.setId(UUID.randomUUID().toString());
		this.houseNo = p.getHouseNo();
		this.lastTime =  new Date();
		this.checkTime = new Date();
		this.checkPower = checkPower;
		this.power = checkPower - beforePower;
	}
	
	public void setEnd(double power){
		this.power = power;
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public double getCheckPower() {
		return checkPower;
	}

	public void setCheckPower(double checkPower) {
		this.checkPower = checkPower;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public long getIntervalT() {
		return intervalT;
	}

	public void setIntervalT(long intervalT) {
		this.intervalT = intervalT;
	}

}
