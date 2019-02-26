package cn.com.bjggs.temp.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("测温板配置信息")
@Table("TempBoaInfo")
public class TempBoaInfo extends IdEntry {

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("测温信息类型")
	@Column("TempType")
	private int tempType;
	
	@Comment("ip")
	@Column("Ip")
	private String ip;

	@Comment("端口号")
	@Column("Port")
	private int port;
	
	@Comment("板子类型")
	@Column("BoardType")
	private int boardType;
	
	@Comment("通道号")
	@Column("WayNo")
	private int wayNo;

	@Comment("电缆号")
	@Column("CableNo")
	private String cableNo;

	public TempBoaInfo(){}
	
	public TempBoaInfo(String houseNo, int tempType, String ip, int port, int boardType, int wayNo, String cableNo){
		this.houseNo = houseNo;
		this.tempType = tempType;
		this.ip = ip;
		this.port = port;
		this.boardType = boardType;
		this.wayNo = wayNo;
		this.cableNo = cableNo;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getTempType() {
		return tempType;
	}

	public void setTempType(int tempType) {
		this.tempType = tempType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public int getWayNo() {
		return wayNo;
	}

	public void setWayNo(int wayNo) {
		this.wayNo = wayNo;
	}

	public String getCableNo() {
		return cableNo;
	}

	public void setCableNo(String cableNo) {
		this.cableNo = cableNo;
	}
	
}
