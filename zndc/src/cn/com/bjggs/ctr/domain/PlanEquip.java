package cn.com.bjggs.ctr.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("定时检测列表")
@Table("PlanEquip")
@PK({"equipNo", "planCode"})
public class PlanEquip {
	
	@Column("EquipNo")
	@Comment("设备编号")
	private int equipNo;
	
	@Column("planCode")
	@Comment("仓房编号")
	private String planCode;
	
	public PlanEquip(){}
	
	public PlanEquip(int equipNo, String planCode){
		this.equipNo = equipNo;
		this.planCode = planCode;
	}

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

}
