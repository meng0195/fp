package cn.com.bjggs.basis.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_CTR_MODEL")
public enum TypeCtrModel implements IBaseEnum {

	WIND("通风窗模式", "1"),
	ONEW("单向风机模式", "2"),
	WONE("窗+风机模式", "3"),
	TWOW("双向风机模式", "4"),
	DIDO("开放模式", "5"),
	AIRC("空调模式", "6"),
	ARMDIO("ARM开放模式","7"),
	ARMWIND("ARM通风窗模式","8"),
	ARMONEW("ARM风机模式","9"),
	ARMNHL("ARM内环流模式","10");

	private String text;

	private String code;

	private TypeCtrModel(String text, String code) {
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
