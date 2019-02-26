package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_SIZE")
public enum TypeSize implements IBaseEnum {

	BIG("大", "1"),
	NORMAL("中", "2"),
	SMALL("小", "3");

	private String text;

	private String code;

	private TypeSize(String text, String code) {
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
