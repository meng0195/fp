package cn.com.bjggs.ctr.domain;

import java.util.Date;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.ctr.util.CtrConstant;

@Comment("设备操作记录表")
@Table("CtrsNotes")
public class CtrsNotes extends IdEntry {
	
	@Column("EquipNo")
	@Comment("设备编号")
	private int equipNo;
	
	@Column("EquipName")
	@Comment("设备名称")
	private String equipName;
	
	@Column("EquipType")
	@Comment("设备类型")
	private int equipType;
	
	@Column("EquipPower")
	@Comment("设备能耗")
	private double equipPower;
	
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	@Column("StartTime")
	@Comment("启动时间")
	private Date startTime;
	
	@Column("EndTime")
	@Comment("结束时间")
	private Date endTime;
	
	@Column("RunTime")
	@Comment("运行时间")
	private Long runTime;
	
	@Column("Status")
	@Comment("运行状态")
	private int status;
	
	public CtrsNotes(){}
	
	public CtrsNotes(Equipment e, int status){
		setId(UUID.randomUUID().toString());
		this.equipNo = e.getEquipNo();
		this.equipName = e.getEquipName();
		this.equipType = e.getType();
		this.equipPower = e.getPower();
		this.houseNo = e.getHouseNo();
		this.startTime = new Date();
		this.status = status;
	}
	
	public void setEnd(){
		this.endTime = new Date();
		this.runTime = endTime.getTime() - startTime.getTime();
		this.status = CtrConstant.IOC;
	}

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public double getEquipPower() {
		return equipPower;
	}

	public void setEquipPower(double equipPower) {
		this.equipPower = equipPower;
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

	public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
