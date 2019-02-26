package cn.com.bjggs.pest.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_PEST_CTR")
public enum TypePestCtr implements IBaseEnum {

	NORMAL("主控空闲", "1"),
	CHECK("正在检测", "2"),
	BUSY("主控占用", "3");

	private String text;

	private String code;

	private TypePestCtr(String text, String code) {
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
