package cn.com.bjggs.ctr.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_CTR_TAG")
public enum TypeCtrTag implements IBaseEnum {

	SD("手动模式", "0"),
	DS("定时模式", "1"),
	ZN("智能模式", "2");

	private String text;

	private String code;

	private TypeCtrTag(String text, String code) {
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
