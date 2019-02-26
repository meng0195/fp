package cn.com.bjggs.basis.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("定时检测列表")
@Table("PlanHouse")
@PK({"houseNo", "planCode"})
public class ChecksTime {
	
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	@Column("planCode")
	@Comment("仓房编号")
	private String planCode;

	public ChecksTime(){}
	
	public ChecksTime(String houseNo, String planCode){
		this.houseNo = houseNo;
		this.planCode = planCode;
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	
}
