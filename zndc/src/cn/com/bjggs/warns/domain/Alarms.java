package cn.com.bjggs.warns.domain;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房报警信息")
public class Alarms extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("报警类型")
	@Column("Type")
	private int[] type;
	
	@Comment("报警类型1")
	@Column("Type1")
	private int[] type1;
	
	@Comment("报警阀值")
	@Column("Threshold")
	private double[] threshold;
	
	@Comment("是否报警")
	@Column("AlarmTag")
	private int[] alarmTag;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int[] getType() {
		return type;
	}

	public void setType(int[] type) {
		this.type = type;
	}

	public int[] getType1() {
		return type1;
	}

	public void setType1(int[] type1) {
		this.type1 = type1;
	}

	public double[] getThreshold() {
		return threshold;
	}

	public void setThreshold(double[] threshold) {
		this.threshold = threshold;
	}

	public int[] getAlarmTag() {
		return alarmTag;
	}

	public void setAlarmTag(int[] alarmTag) {
		this.alarmTag = alarmTag;
	}
	
	public List<Alarm> getAlarms(){
		if(type1 != null && type != null && threshold != null && alarmTag != null 
				&& type1.length == type.length && type1.length == threshold.length 
				&& type1.length == alarmTag.length && Strings.isNotBlank(houseNo)){
			List<Alarm> reqs = new LinkedList<Alarm>();
			for(int i = 0; i < type1.length; i++){
				reqs.add(new Alarm(houseNo, type1[i], type[i], threshold[i], alarmTag[i]));
			}
			return reqs;
		}
		return null;
	}
	
}
