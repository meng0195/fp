package cn.com.bjggs.pest.domain;

import java.util.UUID;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("选通器信息")
@Table("GateInfo")
public class GateInfo extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("选通器编号")
	@Column("GateNo")
	private int gateNo;
	
	@Comment("起始点编号")
	@Column("PointStart")
	private int pointStart;
	
	@Comment("结束点编号")
	@Column("PointEnd")
	private int pointEnd;

	public GateInfo(){}
	
	public GateInfo(String houseNo, int gateNo, int pointStart, int pointEnd){
		this.houseNo = houseNo;
		this.gateNo = gateNo;
		this.pointStart = pointStart;
		this.pointEnd = pointEnd;
		this.setId(UUID.randomUUID().toString());
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getGateNo() {
		return gateNo;
	}

	public void setGateNo(int gateNo) {
		this.gateNo = gateNo;
	}

	public int getPointStart() {
		return pointStart;
	}

	public void setPointStart(int pointStart) {
		this.pointStart = pointStart;
	}

	public int getPointEnd() {
		return pointEnd;
	}

	public void setPointEnd(int pointEnd) {
		this.pointEnd = pointEnd;
	}
	
}
