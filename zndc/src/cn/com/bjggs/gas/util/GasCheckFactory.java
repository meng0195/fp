package cn.com.bjggs.gas.util;

import java.util.Date;

import cn.com.bjggs.basis.enums.TypeCheck;

public class GasCheckFactory {

	public static final Thread createChecking(TypeCheck tc, String houseNo, String planCode, int[] ways, Date testDate){
		if (tc == TypeCheck.ONE) {
			return new GasOne(houseNo, testDate, ways);
		} else {
			return new GasTime(houseNo, testDate, planCode);
		} 
	}
}
