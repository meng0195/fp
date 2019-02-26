package cn.com.bjggs.report.domain;

import java.util.List;
import java.util.Map;

import org.nutz.lang.Times;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.util.InvokeUtil;
import cn.com.bjggs.temp.domain.TestData;

public class ReportModel extends IdEntry {
	
	private String month;
	
	private String day;
	
	private String weather;
	
	private double outT = -1024;
	
	private double outH = -1024;
	
	private double inT = -1024;
	
	private double inH = -1024;
	
	private double a11 = -1024;
	
	private double a12 = -1024;
	
	private double a13 = -1024;
	
	private double a14 = -1024;
	
	private double a21 = -1024;
	
	private double a22 = -1024;
	
	private double a23 = -1024;
	
	private double a24 = -1024;
	
	private double a31 = -1024;
	
	private double a32 = -1024;
	
	private double a33 = -1024;
	
	private double a34 = -1024;
	
	private double a41 = -1024;
	
	private double a42 = -1024;
	
	private double a43 = -1024;
	
	private double a44 = -1024;
	
	private double a51 = -1024;
	
	private double a52 = -1024;
	
	private double a53 = -1024;
	
	private double a54 = -1024;
	
	private double max1 = -1024;
	
	private double max2 = -1024;
	
	private double max3 = -1024;
	
	private double max4 = -1024;
	
	private double avgT = -1024;
	
	private int reportFlag;
	
	private int weaTag;
	
	public ReportModel(){}
	
	@SuppressWarnings("unchecked")
	public ReportModel(TestData td, int htype){
		if(td == null) return;
		setId(td.getId());
		reportFlag = td.getReportFlag();
		String testDate = Times.format("yyyy-MM-dd", td.getTestDate());
		this.month = testDate.substring(5, 7);
		this.day = testDate.substring(8, 10);
		this.outT = td.getOutT();
		this.outH = td.getOutH();
		this.inT = td.getInT();
		this.inH = td.getInH();
		this.avgT = td.getAvgT();
		if(htype == 1){
			for(int i = 1; i < 6; i++){
				initAxy(i, getArea(td.getAreaTsMap(), i));
			}
		} else {
			for(int i = 1; i < 4; i++){
				initAxy(i, (List<Object>)td.getAreaTsMap().get("" + i));
			}
		}
		this.weaTag = td.getWeaTag();
	}

	@SuppressWarnings("unchecked")
	private List<Object> getArea(Map<String, Object> map, int index){
		switch (index) {
		case 1: return (List<Object>)map.get("3");
		case 2: return (List<Object>)map.get("5");
		case 3: return (List<Object>)map.get("4");
		case 4: return (List<Object>)map.get("1");
		case 5: return (List<Object>)map.get("2");
		default: return null;
		}
	}
	
	private void initAxy(int x, List<Object> areas){
		if(areas!=null){
			double _1 = -1024, _2 = -1024, _3 = -1024, _4 = -1024;
			if(areas.size() > 0) _1 = (Double)areas.get(0);
			if(areas.size() > 1) _2 = (Double)areas.get(1);
			if(areas.size() > 2) _3 = (Double)areas.get(2);
			if(areas.size() > 3) _4 = (Double)areas.get(3);
			max1 = Math.max(max1, _1);
			max2 = Math.max(max2, _2);
			max3 = Math.max(max3, _3);
			max4 = Math.max(max4, _4);
			InvokeUtil.setValue(this, "a" + x + 1, _1);
			InvokeUtil.setValue(this, "a" + x + 2, _2);
			InvokeUtil.setValue(this, "a" + x + 3, _3);
			InvokeUtil.setValue(this, "a" + x + 4, _4);
		}
	}
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
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

	public double getA11() {
		return a11;
	}

	public void setA11(double a11) {
		this.a11 = a11;
	}

	public double getA12() {
		return a12;
	}

	public void setA12(double a12) {
		this.a12 = a12;
	}

	public double getA13() {
		return a13;
	}

	public void setA13(double a13) {
		this.a13 = a13;
	}

	public double getA14() {
		return a14;
	}

	public void setA14(double a14) {
		this.a14 = a14;
	}

	public double getA21() {
		return a21;
	}

	public void setA21(double a21) {
		this.a21 = a21;
	}

	public double getA22() {
		return a22;
	}

	public void setA22(double a22) {
		this.a22 = a22;
	}

	public double getA23() {
		return a23;
	}

	public void setA23(double a23) {
		this.a23 = a23;
	}

	public double getA24() {
		return a24;
	}

	public void setA24(double a24) {
		this.a24 = a24;
	}

	public double getA31() {
		return a31;
	}

	public void setA31(double a31) {
		this.a31 = a31;
	}

	public double getA32() {
		return a32;
	}

	public void setA32(double a32) {
		this.a32 = a32;
	}

	public double getA33() {
		return a33;
	}

	public void setA33(double a33) {
		this.a33 = a33;
	}

	public double getA34() {
		return a34;
	}

	public void setA34(double a34) {
		this.a34 = a34;
	}

	public double getA41() {
		return a41;
	}

	public void setA41(double a41) {
		this.a41 = a41;
	}

	public double getA42() {
		return a42;
	}

	public void setA42(double a42) {
		this.a42 = a42;
	}

	public double getA43() {
		return a43;
	}

	public void setA43(double a43) {
		this.a43 = a43;
	}

	public double getA44() {
		return a44;
	}

	public void setA44(double a44) {
		this.a44 = a44;
	}

	public double getA51() {
		return a51;
	}

	public void setA51(double a51) {
		this.a51 = a51;
	}

	public double getA52() {
		return a52;
	}

	public void setA52(double a52) {
		this.a52 = a52;
	}

	public double getA53() {
		return a53;
	}

	public void setA53(double a53) {
		this.a53 = a53;
	}

	public double getA54() {
		return a54;
	}

	public void setA54(double a54) {
		this.a54 = a54;
	}

	public double getMax1() {
		return max1;
	}

	public void setMax1(double max1) {
		this.max1 = max1;
	}

	public double getMax2() {
		return max2;
	}

	public void setMax2(double max2) {
		this.max2 = max2;
	}

	public double getMax3() {
		return max3;
	}

	public void setMax3(double max3) {
		this.max3 = max3;
	}

	public double getMax4() {
		return max4;
	}

	public void setMax4(double max4) {
		this.max4 = max4;
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

	public int getWeaTag() {
		return weaTag;
	}

	public void setWeaTag(int weaTag) {
		this.weaTag = weaTag;
	}
	
}
