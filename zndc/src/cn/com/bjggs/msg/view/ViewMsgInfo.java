package cn.com.bjggs.msg.view;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

@Comment("用户消息视图")
@View("VIEW_MSG_INFO")
public class ViewMsgInfo {

	@Comment("消息code")
	@Column("MsgCode")
	private String msgCode;
	
	@Comment("消息阅读状态")
	@Column("Status")
	private int status;
	
	@Comment("用户编码")
	@Column("UserCode")
	private String userCode;
	
	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("消息")
	@Column("Msg")
	private String msg;
	
	@Comment("归属模块")
	@Column("ModType")
	private int modType;
	
	@Comment("消息类型")
	@Column("MsgType")
	private int msgType;
	
	@Comment("检测ID")
	@Column("TestCode")
	private String testCode;
	
	@Comment("消息产生时间")
	@Column("_Time")
	private Date time;
	
	@Column("TestCode")
	@Comment("检测ID")
	private String testId;

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getModType() {
		return modType;
	}

	public void setModType(int modType) {
		this.modType = modType;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
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
