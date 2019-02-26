package cn.com.bjggs.system.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_PAGE_OPEN")
public enum TypePageOpen implements IBaseEnum {

	NORMAL("navTab", "1"),
	FRAME("iframe", "2"),
	BLANK("_blank", "3");

	private String text;

	private String code;

	private TypePageOpen(String text, String code) {
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
