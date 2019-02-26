package cn.com.bjggs.basis.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("设备管理表")
@Table("EquipIps")
public class EquipIps extends IdEntry {
	
	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("模块类型")
	@Column("BoardType")
	private int boardType;
	
	@Comment("开放模式Ip")
	@Column("DioIp")
	private String dioIp;
	
	@Comment("开放模式PORT")
	@Column("DioPort")
	private int dioPort = 503;
	
	@Comment("通风窗模式IP1")
	@Column("WindIp1")
	private String windIp1;
	
	@Comment("通风窗模式IP2")
	@Column("WindIp2")
	private String windIp2;
	
	@Comment("通风窗模式IP3")
	@Column("WindIp3")
	private String windIp3;
	
	@Comment("通风窗模式端口1")
	@Column("WindPort1")
	private int windPort1 = 503;
	
	@Comment("通风窗模式端口2")
	@Column("WindPort2")
	private int windPort2 = 503;
	
	@Comment("通风窗模式端口3")
	@Column("WindPort3")
	private int windPort3 = 503;
	
	@Comment("单项风机模式IP")
	@Column("OnewIp")
	private String onewIp;
	
	@Comment("单项风机模式端口")
	@Column("OnewPort")
	private int onewPort = 503;
	
	@Comment("窗+单项风机模式IP")
	@Column("WoneIp")
	private String woneIp;
	
	@Comment("窗+单项风机模式端口")
	@Column("WonePort")
	private int wonePort = 503;
	
	@Comment("双向风机模式IP")
	@Column("TwowIp")
	private String twowIp;
	
	@Comment("双向风机模式端口")
	@Column("TwowPort")
	private int twowPort = 503;
	
	@Comment("ARM开放模式Ip")
	@Column("ArmDioIp")
	private String armDioIp;
	
	@Comment("ARM开放模式PORT")
	@Column("ArmDioPort")
	private int armDioPort = 503;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getDioIp() {
		return dioIp;
	}

	public void setDioIp(String dioIp) {
		this.dioIp = dioIp;
	}

	public int getDioPort() {
		return dioPort;
	}

	public void setDioPort(int dioPort) {
		this.dioPort = dioPort;
	}

	public String getWindIp1() {
		return windIp1;
	}

	public void setWindIp1(String windIp1) {
		this.windIp1 = windIp1;
	}

	public String getWindIp2() {
		return windIp2;
	}

	public void setWindIp2(String windIp2) {
		this.windIp2 = windIp2;
	}

	public int getWindPort1() {
		return windPort1;
	}

	public void setWindPort1(int windPort1) {
		this.windPort1 = windPort1;
	}

	public int getWindPort2() {
		return windPort2;
	}

	public void setWindPort2(int windPort2) {
		this.windPort2 = windPort2;
	}

	public String getOnewIp() {
		return onewIp;
	}

	public void setOnewIp(String onewIp) {
		this.onewIp = onewIp;
	}

	public int getOnewPort() {
		return onewPort;
	}

	public void setOnewPort(int onewPort) {
		this.onewPort = onewPort;
	}

	public String getWoneIp() {
		return woneIp;
	}

	public void setWoneIp(String woneIp) {
		this.woneIp = woneIp;
	}

	public int getWonePort() {
		return wonePort;
	}

	public void setWonePort(int wonePort) {
		this.wonePort = wonePort;
	}

	public String getTwowIp() {
		return twowIp;
	}

	public void setTwowIp(String twowIp) {
		this.twowIp = twowIp;
	}

	public int getTwowPort() {
		return twowPort;
	}

	public void setTwowPort(int twowPort) {
		this.twowPort = twowPort;
	}

	public String getWindIp3() {
		return windIp3;
	}

	public void setWindIp3(String windIp3) {
		this.windIp3 = windIp3;
	}

	public int getWindPort3() {
		return windPort3;
	}

	public void setWindPort3(int windPort3) {
		this.windPort3 = windPort3;
	}

	public String getArmDioIp() {
		return armDioIp;
	}

	public void setArmDioIp(String armDioIp) {
		this.armDioIp = armDioIp;
	}

	public int getArmDioPort() {
		return armDioPort;
	}

	public void setArmDioPort(int armDioPort) {
		this.armDioPort = armDioPort;
	}

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}
	
}
