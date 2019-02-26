package cn.com.bjggs.squery.domain;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.temp.domain.PointInfo;

public class QueryPointTemp {
	
	private double temp;
	
	private double temp1;
	
	private int xaxis;
	
	private int yaxis;
	
	private int zaxis;
	
	private int tag;
	
	public QueryPointTemp(){}
	
	public QueryPointTemp(byte[] test, byte[] test1, PointInfo p, byte[] warns, int index){
		this.temp = MathUtil.byte2IntNb(test[index], test[index + 1])/10.0D;
		this.temp1 = MathUtil.byte2IntNb(test1[index], test1[index + 1])/10.0D;
		this.xaxis = p.getXaxis();
		this.yaxis = p.getYaxis();
		this.zaxis = p.getZaxis();
		this.tag = WarnType.isWarn1(warns[index/2]) ? 2 : 0;
		if(tag == 0 && temp > 23.5) tag = 1;
	}
	
	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getTemp1() {
		return temp1;
	}

	public void setTemp1(double temp1) {
		this.temp1 = temp1;
	}

	public int getXaxis() {
		return xaxis;
	}
	public void setXaxis(int xaxis) {
		this.xaxis = xaxis;
	}
	public int getYaxis() {
		return yaxis;
	}
	public void setYaxis(int yaxis) {
		this.yaxis = yaxis;
	}
	public int getZaxis() {
		return zaxis;
	}
	public void setZaxis(int zaxis) {
		this.zaxis = zaxis;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	
}
