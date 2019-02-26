package cn.com.bjggs.pest.domain;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("点位排布信息s")
public class PestPoints extends IdEntry{

	private String houseNo;
	
	private int[] pointNo;
	
	private int[] gateNo;
	
	private int[] xaxis;
	
	private int[] yaxis;
	
	private int[] zaxis;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int[] getPointNo() {
		return pointNo;
	}

	public void setPointNo(int[] pointNo) {
		this.pointNo = pointNo;
	}

	public int[] getGateNo() {
		return gateNo;
	}

	public void setGateNo(int[] gateNo) {
		this.gateNo = gateNo;
	}

	public int[] getXaxis() {
		return xaxis;
	}

	public void setXaxis(int[] xaxis) {
		this.xaxis = xaxis;
	}

	public int[] getYaxis() {
		return yaxis;
	}

	public void setYaxis(int[] yaxis) {
		this.yaxis = yaxis;
	}

	public int[] getZaxis() {
		return zaxis;
	}

	public void setZaxis(int[] zaxis) {
		this.zaxis = zaxis;
	}
	
	public List<PestPoint> getPestPoints(){
		if(gateNo != null && pointNo != null && xaxis != null && null != yaxis && null != zaxis
				&& gateNo.length == pointNo.length && xaxis.length == gateNo.length && yaxis.length == gateNo.length
				&& gateNo.length == zaxis.length){
			List<PestPoint> list = new LinkedList<PestPoint>();
			int point = 1; 
			for(int i = 0; i < gateNo.length; i++){
				if(0 == pointNo[i] || gateNo[i] == 0){
					continue;
				}
				list.add(new PestPoint(houseNo, point, pointNo[i], gateNo[i], xaxis[i], yaxis[i], zaxis[i]));
				point++;
			}
			return list;
		}
		return null;
	}
}
