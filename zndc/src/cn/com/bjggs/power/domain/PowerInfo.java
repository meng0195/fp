package cn.com.bjggs.power.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("电表配置表")
@Table("PowerInfo")
public class PowerInfo extends IdEntry{
	
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	@Column("BoardType")
	@Comment("模式类型")
	private int boardType;
	
	@Column("PowerIp")
	@Comment("能耗板子IP")
	private String powerIp;
	
	@Column("PowerPort")
	@Comment("能耗板子端口")
	private int powerPort;
	
	@Column("RegAdd")
	@Comment("全设备寄存器位")
	private int regAdd;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public String getPowerIp() {
		return powerIp;
	}

	public void setPowerIp(String powerIp) {
		this.powerIp = powerIp;
	}

	public int getPowerPort() {
		return powerPort;
	}

	public void setPowerPort(int powerPort) {
		this.powerPort = powerPort;
	}

	public int getRegAdd() {
		return regAdd;
	}

	public void setRegAdd(int regAdd) {
		this.regAdd = regAdd;
	}

}
