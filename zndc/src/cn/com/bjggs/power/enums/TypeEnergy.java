package cn.com.bjggs.power.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_POWER")
public enum TypeEnergy implements IBaseEnum {

	T("通风", "1"),
	Z("照明", "2"),
	K("空调", "3"),
	N("内环流", "4"),
	P("排积热", "5"),
	W("外接设备", "6");

	private String text;

	private String code;

	private TypeEnergy(String text, String code) {
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
