package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_EQUIP")
public enum TypeEquip implements IBaseEnum {

	TFC("自然通风窗", "1"),
	ZLFJ("有窗单向风机", "2"),
	HLFJ("混流风机", "4"),
	ZM("照明", "6"),
	KT("空调", "7"),
	NHL("内环流", "8"),
	PJRFJ("无窗单向风机", "9"),
	TFK("通风口", "10");

	private String text;

	private String code;

	private TypeEquip(String text, String code) {
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
