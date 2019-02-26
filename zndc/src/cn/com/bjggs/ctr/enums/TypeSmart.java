package cn.com.bjggs.ctr.enums;

import cn.com.bjggs.core.annotation.Enum;
import cn.com.bjggs.core.enums.IBaseEnum;
import cn.com.bjggs.ctr.util.SmartUtil;

@Enum("TYPE_SMART")
public enum TypeSmart implements IBaseEnum {

	JWTF("降温通风", "1", "平均粮温 - 外温 ≥ " + SmartUtil.JWS + "℃ 且 平均粮温 > 目标值", "平均粮温 - 外温 ≤ " + SmartUtil.JWE + "℃ 或 粮均温 < 目标值"),
	ZRTF("自然通风", "2", "仓温 - 气温 ≥ " + SmartUtil.ZRS + "℃", "仓温 - 气温 ≤ " + SmartUtil.ZRE + "℃"),
	JSTF("降水通风", "3", "外界绝对湿度 < 目标水分 - 1%", "外界绝对湿度 > 目标水分"),
	BSTF("保水通风", "4", "平均粮温 - 外温 ≥ " + SmartUtil.BSS + "℃ 且 外界绝对湿度 > 当前粮食水分 + 1% 且 平均粮温 > 目标值", "平均粮温 - 外温 < " + SmartUtil.BSE + "℃ 或 外界绝对湿度 < 当前粮食水分 或 平均粮温 < 目标值"),
	NHL("内环流", "5", "仓温 - 冷芯温度 ≥ " + SmartUtil.NHLS + "℃ 且 冷芯温度 ≤ " + SmartUtil.NHLC + "℃ 且 仓温 > 目标值", "仓温 - 冷芯温度 < " + SmartUtil.NHLE + "℃ 或 冷芯温度 > " + SmartUtil.NHLC + "℃ 或 仓温 <= 目标值"),
	ZNKT("智能空调", "6", "仓温 => 开启值", "仓温 <= 停止值"),
	PJRTF("排积热通风", "7", "拱温/仓温 - 气温 ≥ " + SmartUtil.PJRS + "℃", "拱温/仓温 - 气温 ＜ " + SmartUtil.PJRE + "℃"),
	YXBH("雨雪保护", "8", "气象站监测到有降雨或降雪", "无");

	private String text;

	private String code;
	
	private String starts;
	
	private String ends;

	private TypeSmart(String text, String code, String starts, String ends) {
		this.text = text;
		this.code = code;
		this.starts = starts;
		this.ends = ends;
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
	
	public String starts(){
		return this.starts;
	}
	
	public String ends(){
		return this.ends;
	}
}
