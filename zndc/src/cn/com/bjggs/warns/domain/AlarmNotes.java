package cn.com.bjggs.warns.domain;

import java.util.Date;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房报警记录")
@Table("AlarmNotes")
public class AlarmNotes extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("报警类型")
	@Column("Type")
	private int type;
	
	@Comment("子类型")
	@Column("Type1")
	private int type1;
	
	@Comment("批次号")
	@Column("TestDate")
	private Date testDate;
	
	@Comment("报警点数/层数/个数")
	@Column("Nums")
	private int nums = 0;
	
	@Comment("当时设定的阀值")
	@Column("Threshold")
	private double threshold;
	
	@Comment("故障详情")
	@Column("FaultStr")
	private String faultStr;
	
	@Comment("检测编号(检测记录的ID)")
	@Column("TestCode")
	private String testCode;

	public AlarmNotes(){}
	
	public AlarmNotes(int type, int type1, String houseNo, Date testDate){
		this.setId(UUID.randomUUID().toString());
		this.type = type;
		this.type1 = type1;
		this.houseNo = houseNo;
		this.testDate = testDate;
	}
	public AlarmNotes(int type, int type1, String houseNo, Date testDate, double threshold){
		this.setId(UUID.randomUUID().toString());
		this.type = type;
		this.type1 = type1;
		this.houseNo = houseNo;
		this.testDate = testDate;
		this.threshold = threshold;
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType1() {
		return type1;
	}

	public void setType1(int type1) {
		this.type1 = type1;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getFaultStr() {
		return faultStr;
	}

	public void setFaultStr(String faultStr) {
		this.faultStr = faultStr;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
}
