package cn.com.bjggs.system.domain;

import cn.com.bjggs.temp.domain.PointInfo;


public class IfacePoints {
	
	private int poinNo1;
	
	private int xaxis;
	
	private int yaxis;
	
	private int zAxis;
	
	private int valid;
	
	public IfacePoints(){}
	
	public IfacePoints(PointInfo p){
		if(p != null){
			this.poinNo1 = p.getPoinNo1();
			this.xaxis = p.getXaxis();
			this.yaxis = p.getYaxis();
			this.zAxis = p.getZaxis();
			this.valid = p.getValid();
		}
	}

	public int getPoinNo1() {
		return poinNo1;
	}

	public void setPoinNo1(int poinNo1) {
		this.poinNo1 = poinNo1;
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

	public int getzAxis() {
		return zAxis;
	}

	public void setzAxis(int zAxis) {
		this.zAxis = zAxis;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}
}
