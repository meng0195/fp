package cn.com.bjggs.temp.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_POINT_LEVE")
public enum TypePointLeve implements IBaseEnum {

	A("上层", "1"),
	B("中上层", "2"),
	C("中下层", "3"),
	D("下层", "4");

	private String text;

	private String code;

	private TypePointLeve(String text, String code) {
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
