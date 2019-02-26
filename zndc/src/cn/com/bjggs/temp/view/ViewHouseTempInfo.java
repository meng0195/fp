package cn.com.bjggs.temp.view;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房测温信息表")
@View("view_house_tempInfo")
public class ViewHouseTempInfo extends IdEntry {

	@Comment("仓房编号")
	@Column("HouseNo")
	public String houseNo;
	
	@Comment("仓房姓名")
	@Column("HouseName")
	public String houseName;
	
	@Comment("仓房类型")
	@Column("HouseType")
	public int houseType;
	
	@Comment("长")
	@Column("Dim1")
	public double dim1;
	
	@Comment("宽")
	@Column("Dim2")
	public double dim2;
	
	@Comment("高")
	@Column("Dim3")
	public double dim3;
	
	@Comment("设计储量")
	@Column("StoreCount")
	public double storeCount;
	
	@Comment("粮库编号")
	@Column("GranNo")
	public String granNo;
	
	@Comment("角度")
	@Column("Angle")
	public int angle;
	
	@Comment("起始电缆编号")
	@Column("BeginNum")
	public int beginNum;
	
	@Comment("列数")
	@Column("VNum")
	public int vnum;
	
	@Comment("行数")
	@Column("HNum")
	public String hnum;
	
	@Comment("层数")
	@Column("LNum")
	public String lnum;
	
	@Comment("最大行数")
	@Column("MaxHNum")
	public int maxHNum;
	
	@Comment("最大层数")
	@Column("MaxLNum")
	public int maxLNum;
	
	@Comment("起始位置")
	@Column("Begins")
	public int begins;
	
	@Comment("线缆排布方向")
	@Column("SortOri")
	public int sortOri;
	
	@Comment("线缆排布方式")
	@Column("SortRule")
	public int sortRule;
	
	@Comment("传感器排布方式")
	@Column("PointRule")
	public int pointRule;
	
	@Comment("传感器数量")
	@Column("PointNum")
	public int pointNum;

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

	public int getBeginNum() {
		return beginNum;
	}

	public void setBeginNum(int beginNum) {
		this.beginNum = beginNum;
	}

	public int getVnum() {
		return vnum;
	}

	public void setVnum(int vnum) {
		this.vnum = vnum;
	}

	public String getHnum() {
		return hnum;
	}

	public void setHnum(String hnum) {
		this.hnum = hnum;
	}

	public String getLnum() {
		return lnum;
	}

	public void setLnum(String lnum) {
		this.lnum = lnum;
	}

	public int getMaxHNum() {
		return maxHNum;
	}

	public void setMaxHNum(int maxHNum) {
		this.maxHNum = maxHNum;
	}

	public int getMaxLNum() {
		return maxLNum;
	}

	public void setMaxLNum(int maxLNum) {
		this.maxLNum = maxLNum;
	}

	public int getBegins() {
		return begins;
	}

	public void setBegins(int begins) {
		this.begins = begins;
	}

	public int getSortOri() {
		return sortOri;
	}

	public void setSortOri(int sortOri) {
		this.sortOri = sortOri;
	}

	public int getSortRule() {
		return sortRule;
	}

	public void setSortRule(int sortRule) {
		this.sortRule = sortRule;
	}

	public int getPointRule() {
		return pointRule;
	}

	public void setPointRule(int pointRule) {
		this.pointRule = pointRule;
	}

	public int getPointNum() {
		return pointNum;
	}

	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}
	
	

}
