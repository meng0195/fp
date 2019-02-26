package cn.com.bjggs.gas.domain;

import java.util.Date;
import java.util.UUID;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("气体检测结果")
@Table("TestGas")
public class TestGas extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("检测类型")
	@Column("TestType")
	private int type;
	
	@Comment("检测开始时间")
	@Column("StartTime")
	private Date startTime;
	
	@Comment("检测结束时间")
	@Column("EndTime")
	private Date endTime;
	
	@Comment("CO2最小值")
	@Column("MinCO2")
	private double minCO2;
	
	@Comment("CO2最大值")
	@Column("MaxCO2")
	private double maxCO2;
	
	@Comment("CO2平均值")
	@Column("AvgCO2")
	private double avgCO2;
	
	@Comment("磷化氢最小值")
	@Column("MinPH3")
	private double minPH3;
	
	@Comment("磷化氢最大值")
	@Column("MaxPH3")
	private double maxPH3;
	
	@Comment("磷化氢平均值")
	@Column("AvgPH3")
	private double avgPH3;
	
	@Comment("O2最小值")
	@Column("MinO2")
	private double minO2;
	
	@Comment("O2最大值")
	@Column("MaxO2")
	private double maxO2;
	
	@Comment("O2平均值")
	@Column("AvgO2")
	private double avgO2;
	
	@Comment("检测详情")
	@Column("GasSet")
	private byte[] gasSet;
	
	@Comment("点报警详情")
	@Column("WarnSet")
	private byte[] warnSet;
	
	@Comment("检测异常集")
	@Column("WarnStr")
	private String warnStr;
	private String[] warnArrs;
	
	@Comment("计划编码")
	@Column("PlanCode")
	private String planCode;
	
	@Comment("状态")
	@Column("TestTag")
	private int testTag;
	
	@Column("CurveFlag")
	@Comment("曲线标记")
	private int curveFlag;
	public TestGas(){};
	
	public TestGas(int type, String houseNo, String planCode, Date startTime){
		this.setId(UUID.randomUUID().toString());
		this.type = type;
		this.houseNo = houseNo;
		this.planCode = planCode;
		this.startTime = startTime == null ? new Date() : startTime;
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

	public double getMinCO2() {
		return minCO2;
	}

	public void setMinCO2(double minCO2) {
		this.minCO2 = minCO2;
	}

	public double getMaxCO2() {
		return maxCO2;
	}

	public void setMaxCO2(double maxCO2) {
		this.maxCO2 = maxCO2;
	}

	public double getAvgCO2() {
		return avgCO2;
	}

	public void setAvgCO2(double avgCO2) {
		this.avgCO2 = avgCO2;
	}

	public double getMinPH3() {
		return minPH3;
	}

	public void setMinPH3(double minPH3) {
		this.minPH3 = minPH3;
	}

	public double getMaxPH3() {
		return maxPH3;
	}

	public void setMaxPH3(double maxPH3) {
		this.maxPH3 = maxPH3;
	}

	public double getAvgPH3() {
		return avgPH3;
	}

	public void setAvgPH3(double avgPH3) {
		this.avgPH3 = avgPH3;
	}

	public double getMinO2() {
		return minO2;
	}

	public void setMinO2(double minO2) {
		this.minO2 = minO2;
	}

	public double getMaxO2() {
		return maxO2;
	}

	public void setMaxO2(double maxO2) {
		this.maxO2 = maxO2;
	}

	public double getAvgO2() {
		return avgO2;
	}

	public void setAvgO2(double avgO2) {
		this.avgO2 = avgO2;
	}

	public byte[] getGasSet() {
		return gasSet;
	}

	public void setGasSet(byte[] gasSet) {
		this.gasSet = gasSet;
	}

	public byte[] getWarnSet() {
		return warnSet;
	}

	public void setWarnSet(byte[] warnSet) {
		this.warnSet = warnSet;
	}

	public String getWarnStr() {
		return warnStr;
	}

	public void setWarnStr(String warnStr) {
		this.warnStr = warnStr;
		if(Strings.isNotBlank(warnStr)){
			this.warnArrs = warnStr.split("\\|");
		}
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public int getTestTag() {
		return testTag;
	}

	public void setTestTag(int testTag) {
		this.testTag = testTag;
	}

	public String[] getWarnArrs() {
		return warnArrs;
	}

	public void setWarnArrs(String[] warnArrs) {
		this.warnArrs = warnArrs;
	}

	public int getCurveFlag() {
		return curveFlag;
	}

	public void setCurveFlag(int curveFlag) {
		this.curveFlag = curveFlag;
	}
	
	
}
