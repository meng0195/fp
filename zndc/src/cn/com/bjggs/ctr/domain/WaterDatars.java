package cn.com.bjggs.ctr.domain;

import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("水分查定表")
@Table("WaterDatars")
public class WaterDatars extends IdEntry {

	@Comment("粮食品种")
	@Column("GrainCode")
	private int grainCode;
	
	@Comment("空气湿度")
	@Column("AirHumidity")
	private int airHumidity;
	
	@Comment("空气温度")
	@Column("AirTemp")
	private int airTemp;
	
	@Comment("吸附水分")
	@Column("InWater")
	private double inWater;
	
	@Comment("解吸水分")
	@Column("OutWater")
	private double outWater;

	public WaterDatars(){}
	
	public WaterDatars(int grainCode, int airHumidity, int airTemp, double inWater, double outWater){
		setId(UUID.randomUUID().toString());
		this.grainCode = grainCode;
		this.airHumidity = airHumidity;
		this.airTemp = airTemp;
		this.inWater = inWater;
		this.outWater = outWater;
	}
	
	public int getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(int grainCode) {
		this.grainCode = grainCode;
	}

	public int getAirHumidity() {
		return airHumidity;
	}

	public void setAirHumidity(int airHumidity) {
		this.airHumidity = airHumidity;
	}

	public int getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(int airTemp) {
		this.airTemp = airTemp;
	}

	public double getInWater() {
		return inWater;
	}

	public void setInWater(double inWater) {
		this.inWater = inWater;
	}

	public double getOutWater() {
		return outWater;
	}

	public void setOutWater(double outWater) {
		this.outWater = outWater;
	}

}
