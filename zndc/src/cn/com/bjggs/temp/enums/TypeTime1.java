package cn.com.bjggs.temp.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_TIME1")
public enum TypeTime1 implements IBaseEnum {

	H("每小时", "5"),
	D("每天", "1"),
	W("每周", "2"),
	M("每月", "3"),
	C("自定义", "4");

	private String text;

	private String code;

	private TypeTime1(String text, String code) {
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
