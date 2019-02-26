package cn.com.bjggs.system.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.ChecksUtil;

public class IfaceTemp {
	
	private double outT;
	
	private double outH;
	
	private double inT;
	
	private double inH;
	
	private double avgT;
	
	private double maxT;
	
	private double minT;
	
	private double[] tset;
	
	private byte[] warns;
	
	private List<IfacePoints> points = new LinkedList<IfacePoints>();
	
	public IfaceTemp(){}
	
	@SuppressWarnings("unchecked")
	public IfaceTemp(String houseNo){
		TempResults tr = ChecksUtil.lastChecks.get(houseNo);
		if(tr != null && tr.getDatas() != null){
			TestData td = tr.getDatas();
			this.outT = td.getOutT();
			this.outH = td.getOutH();
			this.inT = td.getInT();
			this.inH = td.getInH();
			this.avgT = td.getAvgT();
			this.maxT = td.getMaxT();
			this.minT = td.getMinT();
			if(td.getTset() != null && td.getTset().length > 0){
				this.tset = new double[td.getTset().length/2];
				for(int i = 0; i < td.getTset().length - 1; i += 2){
					tset[i/2] = MathUtil.byte2IntNb(td.getTset()[i], td.getTset()[i+1])/10.0;
				}
			}
			this.warns = td.getWarns();
			Map<String, PointInfo> map = (Map<String, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS.code());
			if(!Lang.isEmpty(map)){
				for(PointInfo p : map.values()){
					this.points.add(new IfacePoints(p));
				}
			}
		}
	}

	public double getOutT() {
		return outT;
	}

	public void setOutT(double outT) {
		this.outT = outT;
	}

	public double getOutH() {
		return outH;
	}

	public void setOutH(double outH) {
		this.outH = outH;
	}

	public double getInT() {
		return inT;
	}

	public void setInT(double inT) {
		this.inT = inT;
	}

	public double getInH() {
		return inH;
	}

	public void setInH(double inH) {
		this.inH = inH;
	}

	public double getAvgT() {
		return avgT;
	}

	public void setAvgT(double avgT) {
		this.avgT = avgT;
	}

	public double getMaxT() {
		return maxT;
	}

	public void setMaxT(double maxT) {
		this.maxT = maxT;
	}

	public double getMinT() {
		return minT;
	}

	public void setMinT(double minT) {
		this.minT = minT;
	}

	public double[] getTset() {
		return tset;
	}

	public void setTset(double[] tset) {
		this.tset = tset;
	}

	public byte[] getWarns() {
		return warns;
	}

	public void setWarns(byte[] warns) {
		this.warns = warns;
	}

	public List<IfacePoints> getPoints() {
		return points;
	}

	public void setPoints(List<IfacePoints> points) {
		this.points = points;
	}
	
}
