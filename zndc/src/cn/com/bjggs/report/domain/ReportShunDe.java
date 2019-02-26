package cn.com.bjggs.report.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;
import org.nutz.lang.Strings;

import cn.com.bjggs.Constant;
import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.export.annotation.ExportDic;
import cn.com.bjggs.export.annotation.ExportFmt;
import cn.com.bjggs.export.format.DateFmt;
import cn.com.bjggs.export.format.NumberFmt;

@Tag("ReportShunDe")
@View("VIEW_REPORT_N")
@Comment("粮情报表")
public class ReportShunDe extends IdEntry{
	
	@Column("HouseName")
	@Comment("仓号")
	private String houseName;
	
	@Column("GrainCode")
	@Comment("粮食品种")
	@ExportDic(sc=Constant.CODES, type="TYPE_GRAIN")
	private String grainCode;
	
	@Column("GrainCount")
	@Comment("现存数量")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.000")
	private double grainCount;
	
	@Column("GainYear")
	@Comment("收获年度")
	private int gainYear;
	
	@Column("dateOfIn")
	@Comment("入仓时间")
	@ExportFmt(formator=DateFmt.class, pattern="yyyy-MM")
	private Date dateOfIn;
	
	@Comment("当前水分(%)")
	@Column("GrainWater")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double grainWater;
	
	@Column("_InT")
	@Comment("仓温(℃)")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double inT;
	
	@Column("InH")
	@Comment("仓湿(RH%)")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double inH;
	
	@Column("LayerTs")
	@Comment("层温集合")
	private String layerTs;
	
	@Comment("上层粮温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double layer1;
	
	@Comment("中上层粮温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double layer2;
	
	@Comment("中下层粮温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double layer3;
	
	@Comment("下层粮温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double layer4;
	
	@Column("AreaTs")
	@Comment("层温集合")
	private String areaTs;
	
	@Comment("西北区平均温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area1AvgT;
	
	@Comment("西北区最高温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area1MaxT;
	
	@Comment("西北区最低温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area1MinT;
	
	@Comment("东北区平均温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area2AvgT;
	
	@Comment("东北区最高温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area2MaxT;
	
	@Comment("东北区最低温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area2MinT;
	
	@Comment("中央区平均温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area3AvgT;
	
	@Comment("中央区最高温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area3MaxT;
	
	@Comment("中央区最低温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area3MinT;
	
	@Comment("西南区平均温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area4AvgT;
	
	@Comment("西南区最高温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area4MaxT;
	
	@Comment("西南区最低温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area4MinT;
	
	@Comment("东南区平均温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area5AvgT;
	
	@Comment("东南区最高温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area5MaxT;
	
	@Comment("东南区最低温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double area5MinT;

	@Column("AvgT")
	@Comment("粮堆均温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double avgT;
	
	@Column("minT")
	@Comment("粮堆最低温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double minT;
	
	@Column("maxT")
	@Comment("粮堆最高温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double maxT;
	
	@Column("testDate")
	@Comment("检测时间")
	private String testDate;

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(String grainCode) {
		this.grainCode = grainCode;
	}

	public double getGrainCount() {
		return grainCount;
	}

	public void setGrainCount(double grainCount) {
		this.grainCount = grainCount;
	}

	public int getGainYear() {
		return gainYear;
	}

	public void setGainYear(int gainYear) {
		this.gainYear = gainYear;
	}

	public Date getDateOfIn() {
		return dateOfIn;
	}

	public void setDateOfIn(Date dateOfIn) {
		this.dateOfIn = dateOfIn;
	}

	public double getGrainWater() {
		return grainWater;
	}

	public void setGrainWater(double grainWater) {
		this.grainWater = grainWater;
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

	public String getLayerTs() {
		return layerTs;
	}

	@SuppressWarnings("unchecked")
	public void setLayerTs(String layerTs) {
		this.layerTs = layerTs;
		if(Strings.isNotBlank(layerTs)){
			Map<String, Object> map = JsonUtil.json2Map(layerTs);
			double layer = 0;
			for(Map.Entry<String, Object> entry : map.entrySet()){
				try {
					layer = ((List<Double>)entry.getValue()).get(0);
					if(Strings.equals(entry.getKey(), "0")){
						this.layer1 = layer;
					} else if(Strings.equals(entry.getKey(), "1")){
						this.layer2 = layer;
					} else if(Strings.equals(entry.getKey(), "2")){
						this.layer3 = layer;
					} else if(Strings.equals(entry.getKey(), "3")){
						this.layer4 = layer;
					}
				} catch (Exception e) {
					
				}
			}
		}
	}

	public double getLayer1() {
		return layer1;
	}

	public void setLayer1(double layer1) {
		this.layer1 = layer1;
	}

	public double getLayer2() {
		return layer2;
	}

	public void setLayer2(double layer2) {
		this.layer2 = layer2;
	}

	public double getLayer3() {
		return layer3;
	}

	public void setLayer3(double layer3) {
		this.layer3 = layer3;
	}

	public double getLayer4() {
		return layer4;
	}

	public void setLayer4(double layer4) {
		this.layer4 = layer4;
	}

	public String getAreaTs() {
		return areaTs;
	}
	
	@SuppressWarnings("unchecked")
	public void setAreaTs(String areaTs) {
		this.areaTs = areaTs;
		if(Strings.isNotBlank(areaTs)){
			Map<String, Object> map = JsonUtil.json2Map(areaTs);
			double areaAvg = 0, areaMax = 0, areaMin = 0;
			for(Map.Entry<String, Object> entry : map.entrySet()){
				try {
					areaAvg = ((List<Double>)entry.getValue()).get(0);
					areaMax = ((List<Double>)entry.getValue()).get(1);
					areaMin = ((List<Double>)entry.getValue()).get(2);
					if(Strings.equals(entry.getKey(), "0")){
						this.area1AvgT = areaAvg;
						this.area1MaxT = areaMax;
						this.area1MinT = areaMin;
					} else if(Strings.equals(entry.getKey(), "1")){
						this.area2AvgT = areaAvg;
						this.area2MaxT = areaMax;
						this.area2MinT = areaMin;
					} else if(Strings.equals(entry.getKey(), "2")){
						this.area3AvgT = areaAvg;
						this.area3MaxT = areaMax;
						this.area3MinT = areaMin;
					} else if(Strings.equals(entry.getKey(), "3")){
						this.area4AvgT = areaAvg;
						this.area4MaxT = areaMax;
						this.area4MinT = areaMin;
					} else if(Strings.equals(entry.getKey(), "4")){
						this.area5AvgT = areaAvg;
						this.area5MaxT = areaMax;
						this.area5MinT = areaMin;
					}
				} catch (Exception e) {
					
				}
			}
		}
	}
	
	
	
	public double getArea1AvgT() {
		return area1AvgT;
	}

	public void setArea1AvgT(double area1AvgT) {
		this.area1AvgT = area1AvgT;
	}

	public double getArea1MaxT() {
		return area1MaxT;
	}

	public void setArea1MaxT(double area1MaxT) {
		this.area1MaxT = area1MaxT;
	}

	public double getArea1MinT() {
		return area1MinT;
	}

	public void setArea1MinT(double area1MinT) {
		this.area1MinT = area1MinT;
	}

	public double getArea2AvgT() {
		return area2AvgT;
	}

	public void setArea2AvgT(double area2AvgT) {
		this.area2AvgT = area2AvgT;
	}

	public double getArea2MaxT() {
		return area2MaxT;
	}

	public void setArea2MaxT(double area2MaxT) {
		this.area2MaxT = area2MaxT;
	}

	public double getArea2MinT() {
		return area2MinT;
	}

	public void setArea2MinT(double area2MinT) {
		this.area2MinT = area2MinT;
	}

	public double getArea3AvgT() {
		return area3AvgT;
	}

	public void setArea3AvgT(double area3AvgT) {
		this.area3AvgT = area3AvgT;
	}

	public double getArea3MaxT() {
		return area3MaxT;
	}

	public void setArea3MaxT(double area3MaxT) {
		this.area3MaxT = area3MaxT;
	}

	public double getArea3MinT() {
		return area3MinT;
	}

	public void setArea3MinT(double area3MinT) {
		this.area3MinT = area3MinT;
	}

	public double getArea4AvgT() {
		return area4AvgT;
	}

	public void setArea4AvgT(double area4AvgT) {
		this.area4AvgT = area4AvgT;
	}

	public double getArea4MaxT() {
		return area4MaxT;
	}

	public void setArea4MaxT(double area4MaxT) {
		this.area4MaxT = area4MaxT;
	}

	public double getArea4MinT() {
		return area4MinT;
	}

	public void setArea4MinT(double area4MinT) {
		this.area4MinT = area4MinT;
	}

	public double getArea5AvgT() {
		return area5AvgT;
	}

	public void setArea5AvgT(double area5AvgT) {
		this.area5AvgT = area5AvgT;
	}

	public double getArea5MaxT() {
		return area5MaxT;
	}

	public void setArea5MaxT(double area5MaxT) {
		this.area5MaxT = area5MaxT;
	}

	public double getArea5MinT() {
		return area5MinT;
	}

	public void setArea5MinT(double area5MinT) {
		this.area5MinT = area5MinT;
	}

	public double getAvgT() {
		return avgT;
	}

	public void setAvgT(double avgT) {
		this.avgT = avgT;
	}

	public double getMinT() {
		return minT;
	}

	public void setMinT(double minT) {
		this.minT = minT;
	}

	public double getMaxT() {
		return maxT;
	}

	public void setMaxT(double maxT) {
		this.maxT = maxT;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	
}
