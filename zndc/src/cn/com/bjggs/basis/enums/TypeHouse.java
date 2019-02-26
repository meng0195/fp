package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_HOUSE")
public enum TypeHouse implements IBaseEnum {

	WARE("平房仓", "1"),
	SILO("浅圆仓", "2"),
	SQUAT("筒仓", "3"),
	STAR("星仓", "4");

	private String text;

	private String code;

	private TypeHouse(String text, String code) {
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
