package cn.com.bjggs.pest.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.util.JsonUtil;

@Comment("检测记录")
@Table("TestPest")
public class TestPest extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("检测类型")
	@Column("TestType")
	private int testType;
	
	@Comment("平均数量")
	@Column("AvgNum")
	private int avgNum;
	
	@Comment("最小数量")
	@Column("MinNum")
	private int minNum;
	
	@Comment("最大数量")
	@Column("MaxNum")
	private int maxNum;
	
	@Comment("虫点数集合")
	@Column("PSet")
	private byte[] pset;
	
	@Comment("点状态集合")
	@Column("Warns")
	private byte[] warns;
	
	@Comment("层统计")
	@Column("LayerPs")
	private String layerPs;
	private Map<String, int[]> layerMap = new LinkedHashMap<String,  int[]>();
	
	@Comment("检测异常信息集合")
	@Column("WarnsStr")
	private String warnsStr;
	private String[] warnArrs;
	
	@Comment("检测状态")
	@Column("TestTag")
	private int testTag;
	
	@Comment("检测开始时间")
	@Column("StartTime")
	private Date startTime;
	
	@Comment("检测结束时间")
	@Column("EndTime")
	private Date endTime;
	
	@Comment("定时计划编号")
	@Column("PlanCode")
	private String planCode;
	
	@Comment("图片集")
	@Column("Pics")
	private String pics;
	private String[] picArrs;
	
	@Comment("图片集")
	@Column("Videos")
	private String videos;
	private String[] videoArrs;
	
	@Column("CurveFlag")
	@Comment("曲线标记")
	private int curveFlag;
	
	private int[] overs;
	
	public TestPest(){}
	
	public TestPest(String houseNo, int type, Date testDate, String planCode, int pointNum){
		this.setId(UUID.randomUUID().toString());
		this.houseNo = houseNo;
		this.planCode = planCode;
		this.testType = type;
		this.startTime = testDate == null ? new Date() : testDate;
		this.pset = new byte[pointNum];
		this.warns = new byte[pointNum];
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getTestType() {
		return testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public int getAvgNum() {
		return avgNum;
	}

	public void setAvgNum(int avgNum) {
		this.avgNum = avgNum;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public byte[] getPset() {
		return pset;
	}

	public void setPset(byte[] pset) {
		this.pset = pset;
	}

	public byte[] getWarns() {
		return warns;
	}

	public void setWarns(byte[] warns) {
		this.warns = warns;
	}

	public String getLayerPs() {
		return layerPs;
	}

	public void setLayerPs(String layerPs) {
		this.layerPs = layerPs;
		if(Strings.isNotBlank(layerPs)){
			layerMap = JsonUtil.json2Map(int[].class, layerPs);
		}
	}

	public Map<String, int[]> getLayerMap() {
		return layerMap;
	}

	public void setLayerMap(Map<String, int[]> layerMap) {
		this.layerMap = layerMap;
	}

	public String getWarnsStr() {
		return warnsStr;
	}

	public void setWarnsStr(String warnsStr) {
		this.warnsStr = warnsStr;
	}

	public String[] getWarnArrs() {
		if(Strings.isNotBlank(warnsStr)){
			return warnsStr.split("\\|");
		}
		return warnArrs;
	}

	public void setWarnArrs(String[] warnArrs) {
		this.warnArrs = warnArrs;
	}

	public int getTestTag() {
		return testTag;
	}

	public void setTestTag(int testTag) {
		this.testTag = testTag;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
		if(Strings.isNotBlank(pics))
			this.picArrs = pics.split(",");
	}

	public String getVideos() {
		return videos;
	}

	public void setVideos(String videos) {
		this.videos = videos;
		if(Strings.isNotBlank(videos))
			this.videoArrs = videos.split(",");
	}

	public String[] getPicArrs() {
		return picArrs;
	}

	public void setPicArrs(String[] picArrs) {
		this.picArrs = picArrs;
	}

	public String[] getVideoArrs() {
		return videoArrs;
	}

	public void setVideoArrs(String[] videoArrs) {
		this.videoArrs = videoArrs;
	}

	public int[] getOvers() {
		return overs;
	}

	public void setOvers(int[] overs) {
		this.overs = overs;
	}

	public int getCurveFlag() {
		return curveFlag;
	}

	public void setCurveFlag(int curveFlag) {
		this.curveFlag = curveFlag;
	}
	
	
}
