package cn.com.bjggs.basis.view;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

@Comment("定时检测执行情况")
@View("View_Task_Exes")
public class ViewTaskExes {
	
	@Column("planCode")
	@Comment("计划编号")
	private String planCode;
	
	@Column("times")
	@Comment("执行日期")
	private Date times;
	
	@Column("totals")
	@Comment("检测仓房总数")
	private int totals;
	
	@Column("warns")
	@Comment("报警仓房总数")
	private int warns;
	
	@Column("normals")
	@Comment("正常仓房总数")
	private int normals;

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public int getTotals() {
		return totals;
	}

	public void setTotals(int totals) {
		this.totals = totals;
	}

	public int getWarns() {
		return warns;
	}

	public void setWarns(int warns) {
		this.warns = warns;
	}

	public int getNormals() {
		return normals;
	}

	public void setNormals(int normals) {
		this.normals = normals;
	}

	public Date getTimes() {
		return times;
	}

	public void setTimes(Date times) {
		this.times = times;
	}
	
}
