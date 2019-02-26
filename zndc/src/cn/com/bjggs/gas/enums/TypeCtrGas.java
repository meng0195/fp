package cn.com.bjggs.gas.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_CTR_GAS")
public enum TypeCtrGas implements IBaseEnum {

	TCP("控制板", "1"),
	ARM("ARM板", "2");

	private String text;

	private String code;

	private TypeCtrGas(String text, String code) {
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
