package cn.com.bjggs.msg.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_MSG")
public enum TypeMsg implements IBaseEnum {

	WARN("系统报警", "1"),
	THING("操作事件", "2");

	private String text;

	private String code;

	private TypeMsg(String text, String code) {
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
