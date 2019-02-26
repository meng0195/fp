package cn.com.bjggs.system.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.ChecksUtil;

public class IfaceAll {
	
	private int grainCode;
	
	private double grainCount;
	
	private double avgO2;
	
	private double avgCO2;
	
	private double avgPH3;
	
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
	
	public IfaceAll(){}
	
	@SuppressWarnings("unchecked")
	public IfaceAll(String houseNo){
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
		if(Strings.isNotBlank(houseNo)){
			GrainInfo gi = HouseUtil.get(houseNo, TypeHouseConf.GRAIN.code(), GrainInfo.class);
			grainCode = gi.getGrainCode();
			grainCount = gi.getGrainCount();
		}
		GasResults gr = GasUtil.lastChecks.get(houseNo);
		if(gr != null && gr.getGas() != null){
			this.avgO2 = gr.getGas().getAvgO2();
			this.avgCO2 = gr.getGas().getAvgCO2();
			this.avgPH3 = gr.getGas().getAvgPH3();
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

	public int getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(int grainCode) {
		this.grainCode = grainCode;
	}

	public double getGrainCount() {
		return grainCount;
	}

	public void setGrainCount(double grainCount) {
		this.grainCount = grainCount;
	}

	public double getAvgO2() {
		return avgO2;
	}

	public void setAvgO2(double avgO2) {
		this.avgO2 = avgO2;
	}

	public double getAvgCO2() {
		return avgCO2;
	}

	public void setAvgCO2(double avgCO2) {
		this.avgCO2 = avgCO2;
	}

	public double getAvgPH3() {
		return avgPH3;
	}

	public void setAvgPH3(double avgPH3) {
		this.avgPH3 = avgPH3;
	}
	
}
