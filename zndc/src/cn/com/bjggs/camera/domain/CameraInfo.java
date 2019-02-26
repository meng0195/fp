package cn.com.bjggs.camera.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("摄像头配置表")
@Table("CameraInfo")
public class CameraInfo extends IdEntry{
	
	@Comment("摄像头名称")
	@Column("Camera_Name")
	private String camName;
	
	@Comment("摄像头IP")
	@Column("Camera_IP")
	private String camIP;
	
	@Comment("摄像头端口号")
	@Column("Camera_Port")
	private int camPort;
	
	@Comment("摄像头通道号")
	@Column("ChannelsNum")
	private int channelsNum;
	
	@Comment("登录名")
	@Column("UserName")
	private String userName;
	
	@Comment("登录密码")
	@Column("Password")
	private String password;
	
	@Comment("类型")
	@Column("Type")
	private int type;
	
	@Comment("厂商")
	@Column("Vendor")
	private int vendor;
	
	@Comment("x轴坐标")
	@Column("xAxis")
	private double xaxis;
	
	@Comment("y轴坐标")
	@Column("yAxis")
	private double yaxis;
	
	@Comment("是否启用")
	@Column("Status")
	private int status;

	public String getCamName() {
		return camName;
	}

	public void setCamName(String camName) {
		this.camName = camName;
	}

	public String getCamIP() {
		return camIP;
	}

	public void setCamIP(String camIP) {
		this.camIP = camIP;
	}

	public int getCamPort() {
		return camPort;
	}

	public void setCamPort(int camPort) {
		this.camPort = camPort;
	}

	public int getChannelsNum() {
		return channelsNum;
	}

	public void setChannelsNum(int channelsNum) {
		this.channelsNum = channelsNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getVendor() {
		return vendor;
	}

	public void setVendor(int vendor) {
		this.vendor = vendor;
	}
	
	public double getXaxis() {
		return xaxis;
	}

	public void setXaxis(double xaxis) {
		this.xaxis = xaxis;
	}

	public double getYaxis() {
		return yaxis;
	}

	public void setYaxis(double yaxis) {
		this.yaxis = yaxis;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
