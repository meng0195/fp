package cn.com.bjggs.msg.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_MSG_READ")
public enum TypeMsgRead implements IBaseEnum {

	WAIT("未读", "0"),
	READ("已读", "1");

	private String text;

	private String code;

	private TypeMsgRead(String text, String code) {
		this.text = text;
		this.code = code;
	}

	public String text() {
		return this.text;
	}

	public String code() {
		return this.code;
	}

	public int val() {
		return Integer.parseInt(this.code);
	}
}
