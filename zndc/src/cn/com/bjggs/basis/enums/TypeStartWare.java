package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_START_WARE")
public enum TypeStartWare implements IBaseEnum {

	S1("左上", "1"),
	S2("右上", "2"),
	S3("左下", "3"),
	S4("右下", "4");

	private String text;

	private String code;

	private TypeStartWare(String text, String code) {
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
