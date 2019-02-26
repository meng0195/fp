package cn.com.bjggs.pest.domain;

import java.util.UUID;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.util.ParseUtil;

@Comment("点位排布信息")
@Table("PestPoints")
public class PestPoint extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("测虫点号")
	@Column("Points")
	private int points;
	
	@Comment("点号")
	@Column("PointNo")
	private int pointNo;
	
	@Comment("选通器编号")
	@Column("GateNo")
	private int gateNo;
	
	@Comment("列数")
	@Column("XAxis")
	private int xaxis;
	
	@Comment("行数")
	@Column("YAxis")
	private int yaxis;
	
	@Comment("层数")
	@Column("ZAxis")
	private int zaxis;
	
	@Comment("点位信息描述")
	@Column("Address")
	private String address;
	
	public PestPoint(){}
	
	public PestPoint(String houseNo, String param){
		this.houseNo = houseNo;
		if(Strings.isNotBlank(param)){
			String[] arr = param.split("_");
			this.points = ParseUtil.toInt(arr[2], 0);
			this.pointNo = ParseUtil.toInt(arr[1], 0);
			this.gateNo = ParseUtil.toInt(arr[0], 0);
		}
	}
	
	public PestPoint(String houseNo, int points, int pointNo, int gateNo, int xaxis, int yaxis, int zaxis){
		this.setId(UUID.randomUUID().toString());
		this.houseNo = houseNo;
		this.points = points;
		this.pointNo = pointNo;
		this.gateNo = gateNo;
		this.xaxis = xaxis;
		this.yaxis = yaxis;
		this.zaxis = zaxis;
		this.address = xaxis + "列" + yaxis + "行" + zaxis + "层";
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getPointNo() {
		return pointNo;
	}

	public void setPointNo(int pointNo) {
		this.pointNo = pointNo;
	}

	public int getGateNo() {
		return gateNo;
	}

	public void setGateNo(int gateNo) {
		this.gateNo = gateNo;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
}
