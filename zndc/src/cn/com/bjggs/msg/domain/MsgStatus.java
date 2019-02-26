package cn.com.bjggs.msg.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("消息表状态表")
@Table("MsgStatus")
@PK({"msgCode", "userCode"})
public class MsgStatus {
	
	@Column("MsgCode")
	@Comment("消息编码")
	private String msgCode;
	
	@Column("UserCode")
	@Comment("用户编码")
	private String userCode;
	
	@Column("Status")
	@Comment("消息类型")
	private int status = 0;

	public MsgStatus(){}
	
	public MsgStatus(String userCode, String msgCode){
		this.userCode = userCode;
		this.msgCode = msgCode;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
