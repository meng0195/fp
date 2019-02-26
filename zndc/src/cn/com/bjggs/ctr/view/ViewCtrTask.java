package cn.com.bjggs.ctr.view;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

@Comment("设备控制任务执行情况（定时/智能）")
@View("VIEW_CTR_TASK")
public class ViewCtrTask{

	@Comment("操作设备总数")
	@Column("Totals")
	private int totals;
	
	@Comment("故障数量")
	@Column("Warns")
	private int warns;
	
	@Comment("正常数量")
	@Column("Normals")
	private int normals;
	
	@Comment("执行时间")
	@Column("Times")
	private Date times;
	
	@Comment("任务编码")
	@Column("TaskCode")
	private String taskCode;
	
	@Comment("模式编码")
	@Column("modelType")
	private int modelType;

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

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public int getModelType() {
		return modelType;
	}

	public void setModelType(int modelType) {
		this.modelType = modelType;
	}
}
