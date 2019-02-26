package cn.com.bjggs.msg.domain;

import java.util.Date;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("消息表")
@Table("MsgInfo")
public class MsgInfo extends IdEntry{
	
	@Column("Msg")
	@Comment("消息主体")
	private String msg;
	
	@Column("MsgType")
	@Comment("消息类型")
	private int msgType;
	
	@Column("ModType")
	@Comment("模块类型")
	private int modType;
	
	@Column("_Time")
	@Comment("生产时间")
	private Date time;
	
	@Column("TestCode")
	@Comment("检测ID")
	private String testId;
	
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	public MsgInfo(){}

	public MsgInfo(String msg, int msgType, int modType, String testId, String houseNo){
		this.time = new Date();
		this.msg = msg;
		this.msgType = msgType;
		this.modType = modType;
		this.testId = testId;
		this.houseNo = houseNo;
		this.setId(UUID.randomUUID().toString());
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getModType() {
		return modType;
	}

	public void setModType(int modType) {
		this.modType = modType;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}
	
}
