package cn.com.bjggs.ctr.util;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.warns.domain.AlarmNotes;

public interface SmartModel {
	
	int open(Equipment equip, AlarmNotes an0);
	
	int openr(Equipment equip, AlarmNotes an0);
	
	int close(Equipment equip, AlarmNotes an0);
	
	int stop(Equipment equip, AlarmNotes an0);
}
