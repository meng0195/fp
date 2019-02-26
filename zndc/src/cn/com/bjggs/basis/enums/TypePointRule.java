package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_POINT_RULE")
public enum TypePointRule implements IBaseEnum {

	ASC("从上到下", "1"),
	DESC("从下到上", "2");

	private String text;

	private String code;

	private TypePointRule(String text, String code) {
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
