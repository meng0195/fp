package cn.com.bjggs.basis.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房信息")
@Table("StoreHouse")
public class StoreHouse extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("仓房名称")
	@Column("HouseName")
	private String houseName;
	
	@Comment("仓房类型")
	@Column("HouseType")
	@ColDefine(update=false, insert=true)
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
	
	@Comment("建筑年份")
	@Column("BuiltYear")
	private int builtYear;
	
	@Column("MbTag")
	@Comment("密闭与否")
	private int mbTag;
	
	@Column("XzTag")
	@Comment("熏蒸与否")
	private int xzTag;
	
	@Column("JccTag")
	@Comment("进出仓状态")
	private int jccTag;
	
	//与其他系统同步仓房用
	private String otherNo;
	
	private String hType;

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

	public int getBuiltYear() {
		return builtYear;
	}

	public void setBuiltYear(int builtYear) {
		this.builtYear = builtYear;
	}

	public String getOtherNo() {
		return otherNo;
	}

	public void setOtherNo(String otherNo) {
		this.otherNo = otherNo;
	}

	public String gethType() {
		return hType;
	}

	public void sethType(String hType) {
		this.hType = hType;
	}

	public int getMbTag() {
		return mbTag;
	}

	public void setMbTag(int mbTag) {
		this.mbTag = mbTag;
	}

	public int getXzTag() {
		return xzTag;
	}

	public void setXzTag(int xzTag) {
		this.xzTag = xzTag;
	}

	public int getJccTag() {
		return jccTag;
	}

	public void setJccTag(int jccTag) {
		this.jccTag = jccTag;
	}
	
}
