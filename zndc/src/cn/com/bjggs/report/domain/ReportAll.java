package cn.com.bjggs.report.domain;

import java.util.List;

import org.nutz.lang.Times;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.temp.domain.TestData;

public class ReportAll extends IdEntry {
	
	private String houseName;
	
	private String grainName;
	
	private double grainCount;
	
	private int year;
	
	private String inDate;
	
	private double water = -1204;
	
	private double inT = -1204;
	
	private double inH = -1204;
	
	private double avgT1 = -1204;
	
	private double avgT2 = -1204;
	
	private double avgT3 = -1204;
	
	private double avgT4 = -1204;
	
	private double maxT = -1204;
	
	private double minT = -1204;
	
	private double avgT = -1204;
	
	private int reportFlag;
	
	private int mbTag;
	
	private int xzTag;
	
	private int jccTag;
	
	public ReportAll(){}
	
	@SuppressWarnings("unchecked")
	public ReportAll(TestData td){
		if(td == null) return;
		setId(td.getId());
		reportFlag = td.getReportFlag();
		StoreHouse sh = HouseUtil.get(td.getHouseNo(), TypeHouseConf.HOUSE.code(), StoreHouse.class);
		GrainInfo gi = HouseUtil.get(td.getHouseNo(), TypeHouseConf.GRAIN.code(), GrainInfo.class);
		houseName = sh.getHouseName();
		grainName = Codes.get("TYPE_GRAIN", String.valueOf(gi.getGrainCode()));
		grainCount = gi.getGrainCount();
		year = gi.getGainYear();
		inDate = Times.format("yyyy-MM", gi.getDateOfIn());
		water = gi.getGrainWater();
		inT = td.getInT();
		inH = td.getInH();
		if(td.getAreaTsMap() != null && td.getAreaTsMap().containsKey("0")){
			List<Object> areas = (List<Object>)td.getAreaTsMap().get("0");
			if(areas.size() > 0) avgT1 = (Double)areas.get(0);
			if(areas.size() > 1) avgT2 = (Double)areas.get(1);
			if(areas.size() > 2) avgT3 = (Double)areas.get(2);
			if(areas.size() > 3) avgT4 = (Double)areas.get(3);
		}
		maxT = td.getMaxT();
		minT = td.getMinT();
		avgT = td.getAvgT();
		this.mbTag = td.getMbTag();
		this.xzTag = td.getXzTag();
		this.jccTag = td.getJccTag();
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getGrainName() {
		return grainName;
	}

	public void setGrainName(String grainName) {
		this.grainName = grainName;
	}

	public double getGrainCount() {
		return grainCount;
	}

	public void setGrainCount(double grainCount) {
		this.grainCount = grainCount;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public double getWater() {
		return water;
	}

	public void setWater(double water) {
		this.water = water;
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

	public double getAvgT1() {
		return avgT1;
	}

	public void setAvgT1(double avgT1) {
		this.avgT1 = avgT1;
	}

	public double getAvgT2() {
		return avgT2;
	}

	public void setAvgT2(double avgT2) {
		this.avgT2 = avgT2;
	}

	public double getAvgT3() {
		return avgT3;
	}

	public void setAvgT3(double avgT3) {
		this.avgT3 = avgT3;
	}

	public double getAvgT4() {
		return avgT4;
	}

	public void setAvgT4(double avgT4) {
		this.avgT4 = avgT4;
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

	public double getAvgT() {
		return avgT;
	}

	public void setAvgT(double avgT) {
		this.avgT = avgT;
	}

	public int getReportFlag() {
		return reportFlag;
	}

	public void setReportFlag(int reportFlag) {
		this.reportFlag = reportFlag;
	}

	public int getMbTag() {
		return mbTag;
	}

	public void setMbTag(int mbTag) {
		this.mbTag = mbTag;
	}

	public int getXzTag() {
		return xzTag;
	}

	public void setXzTag(int xzTag) {
		this.xzTag = xzTag;
	}

	public int getJccTag() {
		return jccTag;
	}

	public void setJccTag(int jccTag) {
		this.jccTag = jccTag;
	}
	
}
