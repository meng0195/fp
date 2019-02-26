package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_EQUIP_SIDE")
public enum TypeEquipSide implements IBaseEnum {

	Z("正面", "0"),
	F("反面", "1");

	private String text;

	private String code;

	private TypeEquipSide(String text, String code) {
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
