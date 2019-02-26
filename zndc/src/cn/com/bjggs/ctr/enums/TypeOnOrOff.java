package cn.com.bjggs.ctr.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_ON_OR_OFF")
public enum TypeOnOrOff implements IBaseEnum {

	ON("开启", "1"),
	OFF("关闭", "0");

	private String text;

	private String code;

	private TypeOnOrOff(String text, String code) {
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
