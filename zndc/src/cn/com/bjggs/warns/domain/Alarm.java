package cn.com.bjggs.warns.domain;

import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房报警信息")
@Table("Alarm")
public class Alarm extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("报警类型")
	@Column("Type")
	private int type;
	
	@Comment("报警类型1")
	@Column("Type1")
	private int type1;
	
	@Comment("报警阀值")
	@Column("Threshold")
	private double threshold;
	
	@Comment("是否报警")
	@Column("AlarmTag")
	private int alarmTag;
	
	@Comment("页面提醒标识")
	@Column("WebTag")
	private int webTag = 1;
	
	@Comment("短信提醒标识")
	@Column("SmsTag")
	private int smsTag = 0;
	
	@Comment("邮件提醒标识")
	@Column("MailTag")
	private int mailTag = 0;

	public Alarm(){}
	
	public Alarm(String houseNo, int type1, int type, double threshold, int alarmTag){
		this.setId(UUID.randomUUID().toString());
		this.houseNo = houseNo;
		this.type1 = type1;
		this.type = type;
		this.threshold = threshold;
		this.alarmTag = alarmTag;
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType1() {
		return type1;
	}

	public void setType1(int type1) {
		this.type1 = type1;
	}

	public int getAlarmTag() {
		return alarmTag;
	}

	public void setAlarmTag(int alarmTag) {
		this.alarmTag = alarmTag;
	}

	public int getWebTag() {
		return webTag;
	}

	public void setWebTag(int webTag) {
		this.webTag = webTag;
	}

	public int getMailTag() {
		return mailTag;
	}

	public void setMailTag(int mailTag) {
		this.mailTag = mailTag;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public int getSmsTag() {
		return smsTag;
	}

	public void setSmsTag(int smsTag) {
		this.smsTag = smsTag;
	}
	
}
