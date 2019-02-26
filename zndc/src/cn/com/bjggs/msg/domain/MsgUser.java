package cn.com.bjggs.msg.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("消息用户关权限表")
@Table("MsgUser")
@PK({"userCode", "msgType"})
public class MsgUser {
	
	@Column("UserCode")
	@Comment("用户编码")
	private String userCode;
	
	@Column("MsgType")
	@Comment("消息类型")
	private int msgType;

	public MsgUser(){}
	
	public MsgUser(String userCode, int msgType){
		this.userCode = userCode;
		this.msgType = msgType;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
}
