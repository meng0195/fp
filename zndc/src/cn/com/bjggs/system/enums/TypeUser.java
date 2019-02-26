package cn.com.bjggs.system.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_USER")
public enum TypeUser implements IBaseEnum {

	SYS("系统管理员", "1"),
	DEPT("部门管理员", "2"),
	NOR("普通用户", "3");

	private String text;

	private String code;

	private TypeUser(String text, String code) {
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
