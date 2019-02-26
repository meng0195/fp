package cn.com.bjggs.basis.domain;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房储粮信息")
@Table("GrainInfo")
public class CopyGrainInfo extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
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
	private String otherCode;
	
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

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
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

	public String getProof3() {
		return proof3;
	}

	public void setProof3(String proof3) {
		this.proof3 = proof3;
	}

	public String getOtherCode() {
		return otherCode;
	}

	public void setOtherCode(String otherCode) {
		this.otherCode = otherCode;
		if(Strings.isNotBlank(otherCode)){
			if("1120000".equals(otherCode)){
				this.grainCode = 2;
			} else if("1110000".equals(otherCode)){
				this.grainCode = 1;
			} else if("1130000".equals(otherCode)){
				this.grainCode = 4;
			} else if("1140000".equals(otherCode)){
				this.grainCode = 5;
			}
		}
	}
	
}
