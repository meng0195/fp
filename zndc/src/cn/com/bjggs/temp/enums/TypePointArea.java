package cn.com.bjggs.temp.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_POINT_AREA")
public enum TypePointArea implements IBaseEnum {

	A("西北", "1"),
	B("东北", "2"),
	C("中央", "3"),
	D("西南", "4"),
	E("东南", "5");

	private String text;

	private String code;

	private TypePointArea(String text, String code) {
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
