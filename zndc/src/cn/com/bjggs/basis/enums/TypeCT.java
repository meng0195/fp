package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_C_T")
public enum TypeCT implements IBaseEnum {

	WAIT("排队等待", "0"),
	ING("正在检测", "1"),
	TOEND("强制结束", "2"),
	END("检测完成", "3");

	private String text;

	private String code;

	private TypeCT(String text, String code) {
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
