package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_TEST_TAG")
public enum TypeTestTag implements IBaseEnum {

	NORMAL("正常", "0"),
	ABNORMAL("异常", "1");

	private String text;

	private String code;

	private TypeTestTag(String text, String code) {
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
