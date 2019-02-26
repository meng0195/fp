package cn.com.bjggs.pest.domain;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("选通器信息s")
public class GateInfos extends IdEntry{

	private String houseNo;
	
	private int[] gateNo;
	
	private int[] pointStart;
	
	private int[] pointEnd;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int[] getGateNo() {
		return gateNo;
	}

	public void setGateNo(int[] gateNo) {
		this.gateNo = gateNo;
	}

	public int[] getPointStart() {
		return pointStart;
	}

	public void setPointStart(int[] pointStart) {
		this.pointStart = pointStart;
	}

	public int[] getPointEnd() {
		return pointEnd;
	}

	public void setPointEnd(int[] pointEnd) {
		this.pointEnd = pointEnd;
	}

	public List<GateInfo> getGateInfos(){
		if(pointEnd != null && pointStart != null 
				&& gateNo != null && pointEnd.length == gateNo.length && gateNo.length == pointStart.length
				&& Strings.isNotBlank(houseNo)){
			List<GateInfo> reqs = new LinkedList<GateInfo>();
			for(int i = 0; i < gateNo.length; i++){
				reqs.add(new GateInfo(houseNo, gateNo[i], pointStart[i], pointEnd[i]));
			}
			return reqs;
		}
		return null;
	}
}
