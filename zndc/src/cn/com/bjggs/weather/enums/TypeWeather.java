package cn.com.bjggs.weather.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;

@Enum("TYPE_WEATHER")
public enum TypeWeather implements IBaseEnum {

	NET("网口", "1"),
	COM("串口", "2"),
	ONET("老网口", "3");

	private String text;

	private String code;

	private TypeWeather(String text, String code) {
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
