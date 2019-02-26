package cn.com.bjggs.system.domain;

import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.ChecksUtil;

public class IfaceGas {
	
	private int grainCode;
	
	private double grainCount;
	
	private double outT;
	
	private double outH;
	
	private double inT;
	
	private double inH;
	
	private double avgT;
	
	private double maxT;
	
	private double minT;
	
	private double minO2;
	
	private double maxO2;
	
	private double avgO2;
	
	private double minCO2;
	
	private double maxCO2;
	
	private double avgCO2;
	
	private double minPH3;
	
	private double maxPH3;
	
	private double avgPH3;
	
	private int ways;
	
	private int testTag;
	
	private double[] gasSet;
	
	private byte[] warnSet;
	
	public IfaceGas(){}
	
	public IfaceGas(String houseNo){
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
			this.minCO2 = gr.getGas().getMinCO2();
			this.minO2 = gr.getGas().getMinO2();
			this.minPH3 = gr.getGas().getMinPH3();
			this.maxCO2 = gr.getGas().getMaxCO2();
			this.maxO2 = gr.getGas().getMaxO2();
			this.maxPH3 = gr.getGas().getMaxPH3();
			this.testTag = gr.getGas().getTestTag();
			byte[] gs = gr.getGas().getGasSet();
			int index = 0;
			this.gasSet = new double[gs.length/2];
			if(gr.getGas().getGasSet() != null && gs.length > 0){
				for(int i = 0; i < gs.length; i += 6){
					this.gasSet[index] = MathUtil.byte2IntNb(gs[i], gs[i+1])/10.0D;
					index += 1;
					this.gasSet[index] = MathUtil.byte2IntNb(gs[i+2], gs[i+3]);
					index += 1;
					this.gasSet[index] = MathUtil.byte2IntNb(gs[i+4], gs[i+5]);
					index += 1;
				}
			}
			this.warnSet = gr.getGas().getWarnSet();
		}
		GasInfo gi = HouseUtil.get(houseNo, TypeHouseConf.GAS.code(), GasInfo.class);
		if(gi != null){
			this.ways = gi.getWayNumb();
		}
		
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

	public double getMinO2() {
		return minO2;
	}

	public void setMinO2(double minO2) {
		this.minO2 = minO2;
	}

	public double getMaxO2() {
		return maxO2;
	}

	public void setMaxO2(double maxO2) {
		this.maxO2 = maxO2;
	}

	public double getAvgO2() {
		return avgO2;
	}

	public void setAvgO2(double avgO2) {
		this.avgO2 = avgO2;
	}

	public double getMinCO2() {
		return minCO2;
	}

	public void setMinCO2(double minCO2) {
		this.minCO2 = minCO2;
	}

	public double getMaxCO2() {
		return maxCO2;
	}

	public void setMaxCO2(double maxCO2) {
		this.maxCO2 = maxCO2;
	}

	public double getAvgCO2() {
		return avgCO2;
	}

	public void setAvgCO2(double avgCO2) {
		this.avgCO2 = avgCO2;
	}

	public double getMinPH3() {
		return minPH3;
	}

	public void setMinPH3(double minPH3) {
		this.minPH3 = minPH3;
	}

	public double getMaxPH3() {
		return maxPH3;
	}

	public void setMaxPH3(double maxPH3) {
		this.maxPH3 = maxPH3;
	}

	public double getAvgPH3() {
		return avgPH3;
	}

	public void setAvgPH3(double avgPH3) {
		this.avgPH3 = avgPH3;
	}

	public int getWays() {
		return ways;
	}

	public void setWays(int ways) {
		this.ways = ways;
	}

	public int getTestTag() {
		return testTag;
	}

	public void setTestTag(int testTag) {
		this.testTag = testTag;
	}

	public double[] getGasSet() {
		return gasSet;
	}

	public void setGasSet(double[] gasSet) {
		this.gasSet = gasSet;
	}

	public byte[] getWarnSet() {
		return warnSet;
	}

	public void setWarnSet(byte[] warnSet) {
		this.warnSet = warnSet;
	}
	
}
