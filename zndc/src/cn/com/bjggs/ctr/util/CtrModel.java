package cn.com.bjggs.ctr.util;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.warns.domain.AlarmNotes;

public interface CtrModel {
	
	int ctr(Equipment equip, AlarmNotes an0);
	
	void ctrs(String houseNo, int[] equipNo);
	
}
