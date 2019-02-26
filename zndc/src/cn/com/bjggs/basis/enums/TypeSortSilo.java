package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_SORT_SILO")
public enum TypeSortSilo implements IBaseEnum {

	CW("顺时针", "1"),
	AW("逆时针", "2");

	private String text;

	private String code;

	private TypeSortSilo(String text, String code) {
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
