package cn.com.bjggs.ctr.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("智能模式配置表")
@Table("SmartConf")
public class SmartConf extends IdEntry{
	
	@Comment("仓房")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("粮食品种")
	@Column("GrainCode")
	@ColDefine(update=false, insert=true)
	private int grainCode;
	
	@Comment("模式编码")
	@Column("ModelCode")
	@ColDefine(update=false, insert=true)
	private String modelCode;
	
	@Comment("模式类型")
	@Column("ModelType")
	@ColDefine(update=false, insert=true)
	private int modelType;
	
	@Comment("参数1")
	@Column("Param1")
	private double param1;
	
	@Comment("是否开启")
	@Column("Status")
	private int status = 0;
	
	@Comment("是否运行")
	@Column("IngTag")
	private int ingTag = 0;
	
	@Comment("参数2")
	@Column("Param2")
	private double param2;
	
	@Comment("开启设备集合")
	@Column("Equips")
	private String equips;
	
	@Comment("开启设备名称集合")
	@Column("EquipNames")
	private String equipNames;
	
	public SmartConf(){}
	
	public SmartConf(String houseNo, int grainCode, String modelCode, int modelType){
		this.grainCode = grainCode;
		this.houseNo = houseNo;
		this.modelCode = modelCode;
		this.modelType = modelType;
	}
	
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public int getModelType() {
		return modelType;
	}
	public void setModelType(int modelType) {
		this.modelType = modelType;
	}
	public double getParam1() {
		return param1;
	}
	public void setParam1(double param1) {
		this.param1 = param1;
	}
	public double getParam2() {
		return param2;
	}
	public void setParam2(double param2) {
		this.param2 = param2;
	}
	public String getEquips() {
		return equips;
	}
	public void setEquips(String equips) {
		this.equips = equips;
	}

	public String getEquipNames() {
		return equipNames;
	}

	public void setEquipNames(String equipNames) {
		this.equipNames = equipNames;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(int grainCode) {
		this.grainCode = grainCode;
	}

	public int getIngTag() {
		return ingTag;
	}

	public void setIngTag(int ingTag) {
		this.ingTag = ingTag;
	}
	
	public void refresh(){
		this.ingTag = 0;
		this.equipNames = "";
		this.equips = "";
		this.param1 = 0;
		this.param2 = 0;
		this.status = 0;
	}
	
}
