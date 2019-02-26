package cn.com.bjggs.basis.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.ctr.util.CtrConstant;

@Comment("设备管理表")
@Table("Equipment")
public class Equipment extends IdEntry {
	
	@Comment("设备类型")
	@Column("Type")
	private int type;
	
	@Comment("能耗类型")
	@Column("PowerType")
	private int powerType;
	
	@Comment("设备编号")
	@Column("EquipNo")
	@ColDefine(update=false, insert=true)
	private int equipNo;
	
	@Comment("设备名称")
	@Column("EquipName")
	private String equipName;
	
	@Comment("设备功率")
	@Column("Power")
	private double power;
	
	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("所在面")
	@Column("SideTag")
	private int sideTag;
	
	@Comment("x轴坐标")
	@Column("xAxis")
	private int xaxis;
	
	@Comment("y轴坐标")
	@Column("yAxis")
	private int yaxis;
	
	@Comment("z轴坐标")
	@Column("zAxis")
	private int zaxis;
	
	@Comment("模式类型")
	@Column("_Model")
	private int model;
	
	@Comment("DO1")
	@Column("DoWay")
	private int doWay = -1;
	
	@Comment("DO2")
	@Column("DoWay1")
	private int doWay1 = -1;
	
	@Comment("状态DI1")
	@Column("DiWay")
	private int diWay = -1;
	
	@Comment("模式DI位")
	@Column("DiWay1")
	private int diWay1 = -1;
	
	@Comment("故障DI位")
	@Column("DiWay2")
	private int diWay2 = -1;
	
	@Comment("状态DI2")
	@Column("DiWay3")
	private int diWay3 = -1;
	
	@Comment("模块位")
	@Column("ModelWay")
	private int modelWay;
	
	@Comment("寄存器位")
	@Column("RegisterWay")
	private int registerWay;
	
	@Comment("前置风窗IP")
	@Column("BindIp")
	private String bindIp;
	
	@Comment("前置风窗端口")
	@Column("BindPort")
	private int bindPort;
	
	@Comment("前置风窗模块位")
	@Column("BindModel")
	private int bindModel;
	
	@Comment("前置风窗寄存器位")
	@Column("BindRegister")
	private int bindRegister;
	
	@Comment("绑定窗户DO1")
	@Column("WindDo1")
	private int windDo1 = -1;
	
	@Comment("绑定窗户DO2")
	@Column("WindDo2")
	private int windDo2 = -1;
	
	@Comment("绑定窗户Di1")
	@Column("WindDi1")
	private int windDi1 = -1;
	
	@Comment("绑定窗户Di2")
	@Column("WindDi2")
	private int windDi2 = -1;
	
	@Comment("内环流绑定设备")
	@Column("CAEquipNo")
	private int caEquipNo = 0;
	
	private byte status;
	
	private int sel;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getSideTag() {
		return sideTag;
	}

	public void setSideTag(int sideTag) {
		this.sideTag = sideTag;
	}

	public int getXaxis() {
		return xaxis;
	}

	public void setXaxis(int xaxis) {
		this.xaxis = xaxis;
	}

	public int getYaxis() {
		return yaxis;
	}

	public void setYaxis(int yaxis) {
		this.yaxis = yaxis;
	}

	public int getZaxis() {
		return zaxis;
	}

	public void setZaxis(int zaxis) {
		this.zaxis = zaxis;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getDoWay() {
		return doWay;
	}

	public void setDoWay(int doWay) {
		this.doWay = doWay;
	}

	public int getDiWay() {
		return diWay;
	}

	public void setDiWay(int diWay) {
		this.diWay = diWay;
	}

	public int getDiWay1() {
		return diWay1;
	}

	public void setDiWay1(int diWay1) {
		this.diWay1 = diWay1;
	}

	public int getDiWay2() {
		return diWay2;
	}

	public void setDiWay2(int diWay2) {
		this.diWay2 = diWay2;
	}

	public int getModelWay() {
		return modelWay;
	}

	public void setModelWay(int modelWay) {
		this.modelWay = modelWay;
	}

	public int getRegisterWay() {
		return registerWay;
	}

	public void setRegisterWay(int registerWay) {
		this.registerWay = registerWay;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getClazz() {
		if(status != 0){
			if(status == CtrConstant.R1O || status == CtrConstant.R1C ||status == CtrConstant.R1S 
					|| status == CtrConstant.R2O || status == CtrConstant.R2S || status == CtrConstant.R3OWI 
					|| status == CtrConstant.R3SWI || status == CtrConstant.R4O || status == CtrConstant.R4Os
					|| status == CtrConstant.R4S){
				return CtrConstant.C_ING;
			}
			if((status & 0xFF) > 0x90){
				return CtrConstant.C_BAD;
			}
			if(status == CtrConstant.R1OA || status == CtrConstant.R2OA || status == CtrConstant.R3OWA
					|| status == CtrConstant.R3OFA || status == CtrConstant.R4OA || status == CtrConstant.R4OsA 
					|| status == CtrConstant.IOT){
				return CtrConstant.C_OPEN;
			}
			if(status == CtrConstant.R1SA){
				return CtrConstant.C_HALF;
			}
			if(status == CtrConstant.IOC || status == CtrConstant.R4SA || status == CtrConstant.R3SWA 
					|| status == CtrConstant.R2SA || status == CtrConstant.R1CA){
				return CtrConstant.C_CLOSE;
			}
		}
		return CtrConstant.C_CLOSE;
	}

	public int getSel() {
		return sel;
	}

	public void setSel(int sel) {
		this.sel = sel;
	}

	public String getBindIp() {
		return bindIp;
	}

	public void setBindIp(String bindIp) {
		this.bindIp = bindIp;
	}

	public int getBindPort() {
		return bindPort;
	}

	public void setBindPort(int bindPort) {
		this.bindPort = bindPort;
	}

	public int getBindModel() {
		return bindModel;
	}

	public void setBindModel(int bindModel) {
		this.bindModel = bindModel;
	}

	public int getBindRegister() {
		return bindRegister;
	}

	public void setBindRegister(int bindRegister) {
		this.bindRegister = bindRegister;
	}

	public int getDoWay1() {
		return doWay1;
	}

	public void setDoWay1(int doWay1) {
		this.doWay1 = doWay1;
	}

	public int getDiWay3() {
		return diWay3;
	}

	public void setDiWay3(int diWay3) {
		this.diWay3 = diWay3;
	}

	public int getWindDo1() {
		return windDo1;
	}

	public void setWindDo1(int windDo1) {
		this.windDo1 = windDo1;
	}

	public int getWindDo2() {
		return windDo2;
	}

	public void setWindDo2(int windDo2) {
		this.windDo2 = windDo2;
	}

	public int getWindDi1() {
		return windDi1;
	}

	public void setWindDi1(int windDi1) {
		this.windDi1 = windDi1;
	}

	public int getWindDi2() {
		return windDi2;
	}

	public void setWindDi2(int windDi2) {
		this.windDi2 = windDi2;
	}

	public int getCaEquipNo() {
		return caEquipNo;
	}

	public void setCaEquipNo(int caEquipNo) {
		this.caEquipNo = caEquipNo;
	}

	public int getPowerType() {
		return powerType;
	}

	public void setPowerType(int powerType) {
		this.powerType = powerType;
	}
	
}
