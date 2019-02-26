package cn.com.bjggs.ctr.domain;

import org.nutz.dao.entity.annotation.Comment;

@Comment("设备控制同步讯息类")
public class CtrMsg {

	private String houseNo;
	
	private int model;
	
	private int equipNo;
	
	private String clazz;
	
	private int status;
	
	private String msg;

	public CtrMsg(String houseNo, int equipNo, String clazz, int model, int status, String msg){
		this.houseNo = houseNo;
		this.equipNo = equipNo;
		this.clazz = clazz;
		this.model = model;
		this.status = status;
		this.msg = msg;
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}
	
}
