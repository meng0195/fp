package cn.com.bjggs.pest.view;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房与配置信息")
@View("VIEW_HOUSE_PEST")
public class ViewHousePest extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("控制板IP")
	@Column("CtrIp")
	private String ctrIp;
	
	@Comment("控制板端口")
	@Column("CtrPort")
	private int ctrPort;
	
	@Comment("摄像头IP")
	@Column("CamIp")
	private String camIp;
	
	@Comment("摄像头端口")
	@Column("CamPort")
	private int camPort;
	
	@Comment("摄像头用户")
	@Column("CamUser")
	private String camUser;
	
	@Comment("摄像头密码")
	@Column("CamPw")
	private String camPw;
	
	@Comment("摄像头通道号")
	@Column("CamWay")
	private int camWay;
	
	@Comment("层数")
	@Column("Layers")
	private int layers;
	
	@Comment("仓房名称")
	@Column("HouseName")
	private String houseName;

	@Comment("仓房类型")
	@Column("HouseType")
	private int houseType;
	
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

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}
	
}
