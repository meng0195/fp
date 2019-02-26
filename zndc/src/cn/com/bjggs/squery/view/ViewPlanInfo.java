package cn.com.bjggs.squery.view;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

@Comment("定时检测执行情况")
@View("View_Plan_Info")
public class ViewPlanInfo {
	
	@Column("houseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	@Column("planCode")
	@Comment("计划编号")
	private String planCode;
	
	@Column("planName")
	@Comment("计划编号")
	private String planName;

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

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}
	
}
