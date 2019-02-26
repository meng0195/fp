package cn.com.bjggs.temp.util;

import java.util.Date;

import cn.com.bjggs.basis.enums.TypeCheck;

public class CheckFactory {

	public static final Thread createChecking(TypeCheck tc, String houseNo, Date testDate, String planCode, int tag){
		if (tc == TypeCheck.ONE) {
			return new TempOne(houseNo, tag);
		} else if (tc == TypeCheck.TIME) {
			return new TempTime(houseNo, testDate, planCode, tag);
		} else if (tc == TypeCheck.LOOP){
			return new TempLoop(houseNo);
		} else {
			return null;
		}
	}
}
