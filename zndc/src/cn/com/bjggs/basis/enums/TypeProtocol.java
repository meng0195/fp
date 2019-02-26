package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_PROTOCOL")
public enum TypeProtocol implements IBaseEnum {

	NTCP("ARM17", "1"),
	RTU("RTU", "2"),
	ARM("ARM", "3"),
	WEA("气象站", "4");

	private String text;

	private String code;

	private TypeProtocol(String text, String code) {
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
