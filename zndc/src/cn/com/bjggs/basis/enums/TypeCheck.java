package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_CHECK")
public enum TypeCheck implements IBaseEnum {

	ONE("手动检测", "1"),
	LOOP("循环检测", "2"),
	TIME("定时检测", "3"),
	HOUSES("多仓检测", "4"),
	POINTS("多点检测", "5"),
	ZNSM("智能扫描", "6");

	private String text;

	private String code;

	private TypeCheck(String text, String code) {
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
