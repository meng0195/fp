package cn.com.bjggs.basis.view;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房及储粮信息")
@View("View_house_grain")
public class ViewHouseInfo extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("仓房名称")
	@Column("HouseName")
	private String houseName;
	
	@Comment("仓房类型")
	@Column("HouseType")
	private int houseType;
	
	@Comment("物理地址1")
	@Column("Dim1")
	private double dim1;
	
	@Comment("物理地址2")
	@Column("Dim2")
	private double dim2;
	
	@Comment("物理地址3")
	@Column("Dim3")
	private double dim3;
	
	@Comment("设计储量")
	@Column("StoreCount")
	private double storeCount;
	
	@Comment("粮库编号")
	@Column("GranNo")
	private String granNo;
	
	@Comment("角度")
	@Column("Angle")
	private int angle;
	
	@Comment("实际储量")
	@Column("GrainCount")
	private double grainCount;
	
	@Comment("保管员姓名")
	@Column("KeeperName")
	private String keeperName;
	
	@Comment("保管员id")
	@Column("KeeperCode")
	private String keeperCode;
	
	@Comment("粮食品种")
	@Column("GrainCode")
	private int grainCode;
	
	@Comment("入仓时间")
	@Column("DateOfIn")
	private Date dateOfIn;
	
	@Comment("入仓水分")
	@Column("GrainInWater")
	private double grainInWater;
	
	@Comment("当前水分")
	@Column("GrainWater")
	private double grainWater;
	
	@Comment("风机个数")
	@Column("FanNumber")
	private int fanNumber;
	
	@Comment("出仓日期")
	@Column("DateOfOut")
	private Date dateOfOut;
	
	@Comment("产地")
	@Column("Origin")
	private String origin;
	
	@Comment("杂质")
	@Column("Impurity")
	private double impurity;
	
	@Comment("单位重量")
	@Column("Unitweight")
	private double unitweight;
	
	@Comment("等级")
	@Column("Grade")
	private String grade;
	
	@Comment("脂肪酸")
	@Column("Fattyacid")
	private double fattyacid;
	
	@Comment("储粮性质")
	@Column("Nature")
	private int nature;
	
	@Comment("不完善粒")
	@Column("UnsoKer")
	private double unsoKer;
	
	@Comment("收获年份")
	@Column("GainYear")
	private int gainYear;

	@Comment("存储方式")
	@Column("StorageMode")
	private int storageMode;
	
	@Comment("三防设备")
	@Column("Proof3")
	private String proof3;
	
	@Comment("建筑年份")
	@Column("BuiltYear")
	private int builtYear;
	
	//临时变量 在预警列表中辅助展示用 wc 2017-08-29
	private int[] warnTag;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

	public double getDim1() {
		return dim1;
	}

	public void setDim1(double dim1) {
		this.dim1 = dim1;
	}

	public double getDim2() {
		return dim2;
	}

	public void setDim2(double dim2) {
		this.dim2 = dim2;
	}

	public double getDim3() {
		return dim3;
	}

	public void setDim3(double dim3) {
		this.dim3 = dim3;
	}

	public double getStoreCount() {
		return storeCount;
	}

	public void setStoreCount(double storeCount) {
		this.storeCount = storeCount;
	}

	public String getGranNo() {
		return granNo;
	}

	public void setGranNo(String granNo) {
		this.granNo = granNo;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public double getGrainCount() {
		return grainCount;
	}

	public void setGrainCount(double grainCount) {
		this.grainCount = grainCount;
	}

	public String getKeeperName() {
		return keeperName;
	}

	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}

	public String getKeeperCode() {
		return keeperCode;
	}

	public void setKeeperCode(String keeperCode) {
		this.keeperCode = keeperCode;
	}

	public int getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(int grainCode) {
		this.grainCode = grainCode;
	}

	public Date getDateOfIn() {
		return dateOfIn;
	}

	public void setDateOfIn(Date dateOfIn) {
		this.dateOfIn = dateOfIn;
	}

	public double getGrainInWater() {
		return grainInWater;
	}

	public void setGrainInWater(double grainInWater) {
		this.grainInWater = grainInWater;
	}

	public double getGrainWater() {
		return grainWater;
	}

	public void setGrainWater(double grainWater) {
		this.grainWater = grainWater;
	}

	public int getFanNumber() {
		return fanNumber;
	}

	public void setFanNumber(int fanNumber) {
		this.fanNumber = fanNumber;
	}

	public Date getDateOfOut() {
		return dateOfOut;
	}

	public void setDateOfOut(Date dateOfOut) {
		this.dateOfOut = dateOfOut;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public double getImpurity() {
		return impurity;
	}

	public void setImpurity(double impurity) {
		this.impurity = impurity;
	}

	public double getUnitweight() {
		return unitweight;
	}

	public void setUnitweight(double unitweight) {
		this.unitweight = unitweight;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public double getFattyacid() {
		return fattyacid;
	}

	public void setFattyacid(double fattyacid) {
		this.fattyacid = fattyacid;
	}

	public int getNature() {
		return nature;
	}

	public void setNature(int nature) {
		this.nature = nature;
	}

	public double getUnsoKer() {
		return unsoKer;
	}

	public void setUnsoKer(double unsoKer) {
		this.unsoKer = unsoKer;
	}

	public int getGainYear() {
		return gainYear;
	}

	public void setGainYear(int gainYear) {
		this.gainYear = gainYear;
	}

	public int getStorageMode() {
		return storageMode;
	}

	public void setStorageMode(int storageMode) {
		this.storageMode = storageMode;
	}

	public int[] getWarnTag() {
		return warnTag;
	}

	public void setWarnTag(int[] warnTag) {
		this.warnTag = warnTag;
	}

	public String getProof3() {
		return proof3;
	}

	public void setProof3(String proof3) {
		this.proof3 = proof3;
	}

	public int getBuiltYear() {
		return builtYear;
	}

	public void setBuiltYear(int builtYear) {
		this.builtYear = builtYear;
	}
	
}
