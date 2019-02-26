package cn.com.bjggs.system.domain;

import cn.com.bjggs.basis.domain.Equipment;

public class IfaceEquip {
	
	private int type;
	
	private int equipNo;
	
	private String houseNo;
	
	private String equipName;
	
	private byte status;

	public IfaceEquip(){}
	
	public IfaceEquip(Equipment equip){
		if(equip != null){
			this.type = equip.getType();
			this.equipNo = equip.getEquipNo();
			this.houseNo = equip.getHouseNo();
			this.equipName = equip.getEquipName();
			this.status = equip.getStatus();
		}
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
}
