package cn.com.bjggs.msg.view;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

@Comment("用户消息视图")
@View("VIEW_USER_MSG")
public class ViewUserMsg {

	@Comment("库存消息")
	@Column("msgs")
	private int msgs;
	
	@Comment("已读数量")
	@Column("_reads")
	private int reads;
	
	@Comment("未读数量")
	private int waits;
	
	@Comment("用户编码")
	@Column("UserCode")
	private String userCode;

	public int getMsgs() {
		return msgs;
	}

	public void setMsgs(int msgs) {
		this.msgs = msgs;
	}

	public int getWaits() {
		waits = msgs - reads;
		return waits;
	}

	public void setWaits(int waits) {
		this.waits = waits;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}
	
}
