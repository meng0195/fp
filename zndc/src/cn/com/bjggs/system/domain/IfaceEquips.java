package cn.com.bjggs.system.domain;

import java.util.LinkedList;
import java.util.List;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.util.CtrUtil;

public class IfaceEquips {
	
	private int equipNum = 0;
	
	private List<IfaceEquip> equips = new LinkedList<IfaceEquip>();
	
	public IfaceEquips(){}
	
	public IfaceEquips(String houseNo){
		CtrResults cr = CtrUtil.lasts.get(houseNo);
		if(cr != null && cr.getEquips() != null){
			for(Equipment eq : cr.getEquips().values()){
				equips.add(new IfaceEquip(eq));
			}
		}
	}

	public int getEquipNum() {
		return equipNum;
	}

	public void setEquipNum(int equipNum) {
		this.equipNum = equipNum;
	}

	public List<IfaceEquip> getEquips() {
		return equips;
	}

	public void setEquips(List<IfaceEquip> equips) {
		this.equips = equips;
	}
	
}
