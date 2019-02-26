package cn.com.bjggs.ctr.domain;

import org.nutz.dao.entity.annotation.Comment;

@Comment("仓房模式配置")
public class SmartStatus {
	
	private int status_1;
	private int status_2;
	private int status_3;
	private int status_4;
	private int status_5;
	private int status_6;
	private int status_7;
	private int status_8;
	
	private String equip_1;
	private String equip_2;
	private String equip_3;
	private String equip_4;
	private String equip_5;
	private String equip_6;
	private String equip_7;
	
	private String equipName_1;
	private String equipName_2;
	private String equipName_3;
	private String equipName_4;
	private String equipName_5;
	private String equipName_6;
	private String equipName_7;
	
	private String houseNo;
	
	private double[] param1;
	
	private double[] param2;
	
	public int getStatus_1() {
		return status_1;
	}
	public void setStatus_1(int status_1) {
		this.status_1 = status_1;
	}
	public int getStatus_2() {
		return status_2;
	}
	public void setStatus_2(int status_2) {
		this.status_2 = status_2;
	}
	public int getStatus_3() {
		return status_3;
	}
	public void setStatus_3(int status_3) {
		this.status_3 = status_3;
	}
	public int getStatus_4() {
		return status_4;
	}
	public void setStatus_4(int status_4) {
		this.status_4 = status_4;
	}
	public int getStatus_5() {
		return status_5;
	}
	public void setStatus_5(int status_5) {
		this.status_5 = status_5;
	}
	public int getStatus_6() {
		return status_6;
	}
	public void setStatus_6(int status_6) {
		this.status_6 = status_6;
	}
	public int getStatus_7() {
		return status_7;
	}
	public void setStatus_7(int status_7) {
		this.status_7 = status_7;
	}
	public int getStatus_8() {
		return status_8;
	}
	public void setStatus_8(int status_8) {
		this.status_8 = status_8;
	}
	public double[] getParam1() {
		return param1;
	}
	public void setParam1(double[] param1) {
		this.param1 = param1;
	}
	public double[] getParam2() {
		return param2;
	}
	public void setParam2(double[] param2) {
		this.param2 = param2;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getEquip_1() {
		return equip_1;
	}
	public void setEquip_1(String equip_1) {
		this.equip_1 = equip_1;
	}
	public String getEquip_2() {
		return equip_2;
	}
	public void setEquip_2(String equip_2) {
		this.equip_2 = equip_2;
	}
	public String getEquip_3() {
		return equip_3;
	}
	public void setEquip_3(String equip_3) {
		this.equip_3 = equip_3;
	}
	public String getEquip_4() {
		return equip_4;
	}
	public void setEquip_4(String equip_4) {
		this.equip_4 = equip_4;
	}
	public String getEquip_5() {
		return equip_5;
	}
	public void setEquip_5(String equip_5) {
		this.equip_5 = equip_5;
	}
	public String getEquip_6() {
		return equip_6;
	}
	public void setEquip_6(String equip_6) {
		this.equip_6 = equip_6;
	}
	public String getEquip_7() {
		return equip_7;
	}
	public void setEquip_7(String equip_7) {
		this.equip_7 = equip_7;
	}
	public String getEquipName_1() {
		return equipName_1;
	}
	public void setEquipName_1(String equipName_1) {
		this.equipName_1 = equipName_1;
	}
	public String getEquipName_2() {
		return equipName_2;
	}
	public void setEquipName_2(String equipName_2) {
		this.equipName_2 = equipName_2;
	}
	public String getEquipName_3() {
		return equipName_3;
	}
	public void setEquipName_3(String equipName_3) {
		this.equipName_3 = equipName_3;
	}
	public String getEquipName_4() {
		return equipName_4;
	}
	public void setEquipName_4(String equipName_4) {
		this.equipName_4 = equipName_4;
	}
	public String getEquipName_5() {
		return equipName_5;
	}
	public void setEquipName_5(String equipName_5) {
		this.equipName_5 = equipName_5;
	}
	public String getEquipName_6() {
		return equipName_6;
	}
	public void setEquipName_6(String equipName_6) {
		this.equipName_6 = equipName_6;
	}
	public String getEquipName_7() {
		return equipName_7;
	}
	public void setEquipName_7(String equipName_7) {
		this.equipName_7 = equipName_7;
	}
	
	public int getStatus(int i){
		switch (i) {
		case 0: return this.status_1;
		case 1: return this.status_2;
		case 2: return this.status_3;
		case 3: return this.status_4;
		case 4: return this.status_5;
		case 5: return this.status_6;
		case 6: return this.status_7;
		case 7: return this.status_7;
		default: return 0;
		}
	}
	
	public String getEquips(int i){
		switch (i) {
		case 0: return this.equip_1;
		case 1: return this.equip_2;
		case 2: return this.equip_3;
		case 3: return this.equip_4;
		case 4: return this.equip_5;
		case 5: return this.equip_6;
		case 6: return this.equip_7;
		default: return "";
		}
	}
	
	public String getEquipNames(int i){
		switch (i) {
		case 0: return this.equipName_1;
		case 1: return this.equipName_2;
		case 2: return this.equipName_3;
		case 3: return this.equipName_4;
		case 4: return this.equipName_5;
		case 5: return this.equipName_6;
		case 6: return this.equipName_7;
		default: return "";
		}
	}
}
