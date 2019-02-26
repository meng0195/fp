package cn.com.bjggs.temp.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_POINT_SQUAT")
public enum TypePointSquat implements IBaseEnum {

	A("内圈", "1"),
	B("中圈", "2"),
	C("外圈", "3");

	private String text;

	private String code;

	private TypePointSquat(String text, String code) {
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
