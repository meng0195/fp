package cn.com.bjggs.power.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_BOARD")
public enum TypePowerBoard implements IBaseEnum {

	NTCP("ARM17", "1"),
	ARM("ARM", "2");

	private String text;

	private String code;

	private TypePowerBoard(String text, String code) {
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
