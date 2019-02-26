package cn.com.bjggs.ctr.domain;

import java.util.Map;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.basis.domain.Equipment;

@Comment("设备状态")
public class CtrResults {

	private String houseNo;
	
	private int tag = 0;
	
	private int narmal = 0;
	
	private int warn = 0;
	
	private int none = 0;
	
	private Map<Integer, Equipment> equips;
	
	public CtrResults(){};
	
	public CtrResults(String houseNo){
		this.houseNo = houseNo;
	};

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getNarmal() {
		return narmal;
	}

	public void setNarmal(int narmal) {
		this.narmal = narmal;
	}

	public int getWarn() {
		return warn;
	}

	public void setWarn(int warn) {
		this.warn = warn;
	}

	public Map<Integer, Equipment> getEquips() {
		return equips;
	}

	public void setEquips(Map<Integer, Equipment> equips) {
		this.equips = equips;
	}

	public int getNone() {
		return none;
	}

	public void setNone(int none) {
		this.none = none;
	}

}
