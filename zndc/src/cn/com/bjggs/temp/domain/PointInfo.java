package cn.com.bjggs.temp.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("温度点信息表")
@Table("PointInfo")
public class PointInfo extends IdEntry {

	@Comment("仓房编号")
	@Column("HouseNo")
	public String houseNo;

	@Comment("参数1")
	@Column("Length")
	public double length;
	
	@Comment("参数2")
	@Column("Width")
	public double width;
	
	@Comment("参数3")
	@Column("Height")
	public double height;
	
	@Comment("温度点编号")
	@Column("PoinNo1")
	public int poinNo1;
	
	@Comment("电缆编号")
	@Column("CableNo1")
	public int cableNo1;
	
	@Comment("传感器编号")
	@Column("SensorNo1")
	public int sensorNo1;

	@Comment("坐标1")
	@Column("xAxis")
	public int xaxis;

	@Comment("坐标2")
	@Column("yAxis")
	public int yaxis;

	@Comment("坐标3")
	@Column("zAxis")
	public int zaxis;
	
	@Comment("所属区域")
	@Column("Area")
	public int area = 0;
	
	@Comment("是否是有效点")
	@Column("Valid")
	public int valid = 0;
	
	@Comment("层标记")
	@Column("Leve")
	public int leve;
	
	public PointInfo(){
		
	}

	public PointInfo(String id, String houseNo, double length, double width, double height, int poinNo1, int cableNo1, int sensorNo1, int xaxis, int yaxis, int zaxis){
		this.setId(id);
		this.houseNo = houseNo;
		this.length = length;
		this.width = width;
		this.height = height;
		this.poinNo1 = poinNo1;
		this.cableNo1 = cableNo1;
		this.sensorNo1 = sensorNo1;
		this.xaxis = xaxis;
		this.yaxis = yaxis;
		this.zaxis = zaxis;
	}
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getPoinNo1() {
		return poinNo1;
	}

	public void setPoinNo1(int poinNo1) {
		this.poinNo1 = poinNo1;
	}

	public int getCableNo1() {
		return cableNo1;
	}

	public void setCableNo1(int cableNo1) {
		this.cableNo1 = cableNo1;
	}

	public int getSensorNo1() {
		return sensorNo1;
	}

	public void setSensorNo1(int sersorNo1) {
		this.sensorNo1 = sersorNo1;
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

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getLeve() {
		return leve;
	}

	public void setLeve(int leve) {
		this.leve = leve;
	}
}
