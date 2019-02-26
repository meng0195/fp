package cn.com.bjggs.temp.domain;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.export.annotation.ExportDic;
import cn.com.bjggs.export.annotation.ExportFmt;
import cn.com.bjggs.export.format.DateFmt;
import cn.com.bjggs.export.format.NumberFmt;
import cn.com.bjggs.system.util.PassUtil;

@Tag("TestData")
@Comment("粮情检测数据")
@Table("TestData")
public class TestData extends IdEntry {
	
	@Column("HouseNo")
	@Comment("仓房编号")
	@ExportDic(sc=Constant.CODES, type="houses")
	private String houseNo;
	
	@Column("TestDate")
	@Comment("检测时间")
	@ExportFmt(formator=DateFmt.class, pattern="yyyy-MM-dd HH:mm:ss")
	private Date testDate;
	
	@Column("TestType")
	@Comment("检测类型")
	private int testType;
	
	@Column("PlanCode")
	@Comment("计划编号")
	private String planCode;
	
	@Column("_InT")
	@Comment("仓房内温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double inT;
	
	@Column("OutT")
	@Comment("仓房外温")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double outT;
	
	@Column("InH")
	@Comment("仓房内湿")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double inH;
	
	@Column("OutH")
	@Comment("仓房外湿")
	@ExportFmt(formator=NumberFmt.class, pattern="#0.0")
	private double outH;
	
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
	
	@Column("TSet")
	@Comment("粮食温度值集合")
	private byte[] tset;
	
	@Column("Warns")
	@Comment("报警标记集合")
	private byte[] warns;
	
	@Column("LayerTs")
	@Comment("层温集合")
	private String layerTs;
	
	@Column("TestWarns")
	@Comment("检测异常集合")
	private String testWarns;
	
	@Column("TestTag")
	@Comment("报警状态")
	private int testTag;
	
	@Column("LayT")
	@Comment("夹层温度")
	private double layT;
	
	@Column("VentT")
	@Comment("通风口温度")
	private double ventT;
	
	@Column("TopT")
	@Comment("吊顶温度")
	private double topT;
	
	@Column("CcT")
	@Comment("冷芯温度")
	private double ccT;
	
	@Column("ReportFlag")
	@Comment("报表标记")
	private int reportFlag;
	
	@Column("CurveFlag")
	@Comment("曲线标记")
	private int curveFlag;
	
	@Column("AreaTs")
	@Comment("区温集合")
	private String areaTs;
	
	private String[] tws;
	
	private Map<String, Object> layerTsMap;
	
	private Map<String, Object> areaTsMap;
	
	@Column("MbTag")
	@Comment("密闭与否")
	private int mbTag;
	
	@Column("XzTag")
	@Comment("熏蒸与否")
	private int xzTag;
	
	@Column("JccTag")
	@Comment("进出仓状态")
	private int jccTag;
	
	@Column("WeaTag")
	@Comment("天气情况")
	private int weaTag = PassUtil._WEATAG;

	public TestData(){	
	}
	
	public TestData(int testType, Date testDate, String houseNo, String planCode){
		setId(UUID.randomUUID().toString());
		this.testType = testType;
		this.testDate = testDate == null ? new Date() : testDate;
		this.houseNo = houseNo;
		this.planCode = planCode;
		StoreHouse h = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		this.jccTag = h.getJccTag();
		this.xzTag = h.getXzTag();
		this.mbTag = h.getMbTag();
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public Date getTestDate() {
		return testDate;
	}
	
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public int getTestType() {
		return testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public double getInT() {
		if(inT == -128.9){
			return 88;
		}
		return inT;
	}

	public void setInT(double inT) {
		this.inT = inT;
	}

	public double getOutT() {
		if(outT == -128.9){
			return 88;
		}
		return outT;
	}

	public void setOutT(double outT) {
		this.outT = outT;
	}

	public double getInH() {
		return inH;
	}

	public void setInH(double inH) {
		this.inH = inH;
	}

	public double getOutH() {
		return outH;
	}

	public void setOutH(double outH) {
		this.outH = outH;
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

	public byte[] getTset() {
		return tset;
	}

	public void setTset(byte[] tset) {
		this.tset = tset;
	}

	public String getLayerTs() {
		return layerTs;
	}

	public void setLayerTs(String layerTs) {
		this.layerTs = layerTs;
		if(Strings.isNotBlank(layerTs)){
			this.layerTsMap = JsonUtil.json2Map(layerTs);
		}
	}

	public Map<String, Object> getLayerTsMap() {
		return layerTsMap;
	}

	public void setLayerTsMap(Map<String, Object> layerTsMap) {
		this.layerTsMap = layerTsMap;
	}

	public byte[] getWarns() {
		return warns;
	}

	public void setWarns(byte[] warns) {
		this.warns = warns;
	}

	public String getTestWarns() {
		return testWarns;
	}

	public void setTestWarns(String testWarns) {
		this.testWarns = testWarns;
		if(!Strings.isBlank(testWarns)){
			tws = testWarns.split("\\|");
		}
	}

	public String[] getTws() {
		return tws;
	}

	public void setTws(String[] tws) {
		this.tws = tws;
	}

	public int getTestTag() {
		return testTag;
	}

	public void setTestTag(int testTag) {
		this.testTag = testTag;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public double getLayT() {
		return layT;
	}

	public void setLayT(double layT) {
		this.layT = layT;
	}

	public double getVentT() {
		return ventT;
	}

	public void setVentT(double ventT) {
		this.ventT = ventT;
	}

	public double getTopT() {
		return topT;
	}

	public void setTopT(double topT) {
		this.topT = topT;
	}

	public double getCcT() {
		return ccT;
	}

	public void setCcT(double ccT) {
		this.ccT = ccT;
	}

	public int getReportFlag() {
		return reportFlag;
	}

	public void setReportFlag(int reportFlag) {
		this.reportFlag = reportFlag;
	}

	public int getCurveFlag() {
		return curveFlag;
	}

	public void setCurveFlag(int curveFlag) {
		this.curveFlag = curveFlag;
	}

	public String getAreaTs() {
		return areaTs;
	}

	public void setAreaTs(String areaTs) {
		this.areaTs = areaTs;
		if(Strings.isNotBlank(areaTs)){
			this.areaTsMap = JsonUtil.json2Map(areaTs);
		}
	}

	public Map<String, Object> getAreaTsMap() {
		return areaTsMap;
	}

	public void setAreaTsMap(Map<String, Object> areaTsMap) {
		this.areaTsMap = areaTsMap;
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

	public int getWeaTag() {
		return weaTag;
	}

	public void setWeaTag(int weaTag) {
		this.weaTag = weaTag;
	}
	
}
