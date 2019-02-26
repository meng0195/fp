package cn.com.bjggs.warns.domain;

import cn.com.bjggs.Constant;


public class AlarmTT {
	//1温度高限, 2温度升高率, 3温度异常点, 4层均温, 5缺点率,	6冷芯报警, 7满仓, 8半仓, 9预警, 10报警, 11磷化氢, 12二氧化碳, 13氧气下限, 14设备异常
	//报警状态集合
	private boolean[] tags = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false};
	//报警阀值集合
	private double[] vs = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	public boolean isWarns(int index){
		if(-1 < index && index < 14)
			return tags[index];
		else 
			return false;
	}
	
	public double getThd(int index){
		if(-1 < index && index < 14)
			return vs[index];
		else 
			return 0;
	}
	
	public void setArrs(Alarm a){
		switch (a.getType()) {
		case Constant.W_TEMP:
			switch (a.getType1()) {
			case Constant.W_TEMP_1:
				tags[0] = a.getAlarmTag() == 1 ? true : false;
				vs[0] = a.getThreshold();
				break;
			case Constant.W_TEMP_2:
				tags[1] = a.getAlarmTag() == 1 ? true : false;
				vs[1] = a.getThreshold();
				break;
			case Constant.W_TEMP_3:
				tags[2] = a.getAlarmTag() == 1 ? true : false;
				vs[2] = a.getThreshold();
				break;
			case Constant.W_TEMP_4:
				tags[3] = a.getAlarmTag() == 1 ? true : false;
				vs[3] = a.getThreshold();
				break;
			case Constant.W_TEMP_5:
				tags[4] = a.getAlarmTag() == 1 ? true : false;
				vs[4] = a.getThreshold();
				break;
			case Constant.W_TEMP_6:
				tags[5] = a.getAlarmTag() == 1 ? true : false;
				vs[5] = a.getThreshold();
				break;
			default:break;
			}
			break;
		case Constant.W_GRAIN:
			switch (a.getType1()) {
			case Constant.W_GRAIN_1:
				tags[6] = a.getAlarmTag() == 1 ? true : false;
				vs[6] = a.getThreshold();
				break;
			case Constant.W_GRAIN_2:
				tags[7] = a.getAlarmTag() == 1 ? true : false;
				vs[7] = a.getThreshold();
				break;
			default:break;
			}
			break;
		case Constant.W_PEST:
			switch (a.getType1()) {
			case Constant.W_PEST_1:
				tags[8] = a.getAlarmTag() == 1 ? true : false;
				vs[8] = a.getThreshold();
				break;
			case Constant.W_PEST_2:
				tags[9] = a.getAlarmTag() == 1 ? true : false;
				vs[9] = a.getThreshold();
				break;
			default:break;
			}
			break;
		case Constant.W_GAN:
			switch (a.getType1()) {
			case Constant.W_GAN_1:
				tags[10] = a.getAlarmTag() == 1 ? true : false;
				vs[10] = a.getThreshold();
				break;
			case Constant.W_GAN_2:
				tags[11] = a.getAlarmTag() == 1 ? true : false;
				vs[11] = a.getThreshold();
				break;
			case Constant.W_GAN_3:
				tags[12] = a.getAlarmTag() == 1 ? true : false;
				vs[12] = a.getThreshold();
				break;
			default:break;
			}
			break;
		case Constant.W_CTR:
			switch (a.getType1()) {
			case Constant.W_CTR_1:
				tags[13] = a.getAlarmTag() == 1 ? true : false;
				vs[13] = a.getThreshold();
				break;
			default:break;
			}
			break;
		default:
			break;
		}
	}
}
