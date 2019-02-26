package cn.com.bjggs.pest.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("测虫配置信息")
@Table("PestInfo")
public class PestInfo extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("控制板Ip")
	@Column("CtrIp")
	private String ctrIp;

	@Comment("控制板端口")
	@Column("CtrPort")
	private int ctrPort = 502;

	@Comment("控制板摄像头Ip")
	@Column("CamIp")
	private String camIp;
	
	@Comment("摄像头端口")
	@Column("CamPort")
	private int camPort = 8000;
	
	@Comment("摄像头用户名称")
	@Column("CamUser")
	private String camUser;
	
	@Comment("摄像头密码")
	@Column("CamPw")
	private String camPw;
	
	@Comment("摄像头通道号")
	@Column("CamWay")
	private int camWay;
	
	@Comment("排布层数")
	@Column("Layers")
	private int layers = 3;
	
	@Comment("点抽气时间")
	@Column("Times")
	private int times = 10;
	
	@Comment("总点数")
	@Column("PointNum")
	private int pointNum = 32;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getCtrIp() {
		return ctrIp;
	}

	public void setCtrIp(String ctrIp) {
		this.ctrIp = ctrIp;
	}

	public int getCtrPort() {
		return ctrPort;
	}

	public void setCtrPort(int ctrPort) {
		this.ctrPort = ctrPort;
	}

	public String getCamIp() {
		return camIp;
	}

	public void setCamIp(String camIp) {
		this.camIp = camIp;
	}

	public int getCamPort() {
		return camPort;
	}

	public void setCamPort(int camPort) {
		this.camPort = camPort;
	}

	public String getCamUser() {
		return camUser;
	}

	public void setCamUser(String camUser) {
		this.camUser = camUser;
	}

	public String getCamPw() {
		return camPw;
	}

	public void setCamPw(String camPw) {
		this.camPw = camPw;
	}

	public int getCamWay() {
		return camWay;
	}

	public void setCamWay(int camWay) {
		this.camWay = camWay;
	}

	public int getLayers() {
		return layers;
	}

	public void setLayers(int layers) {
		this.layers = layers;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getPointNum() {
		return pointNum;
	}

	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}
	
}
