package cn.com.bjggs.temp.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.temp.util.ChecksUtil;

@Comment("循环检测配置")
public class LoopConf {
	
	@Comment("最大并发检测数量")
	private int maxCheckNum;
	
	@Comment("插入数据的间隔时间(小时 )")
	private int intervalTimes;
	
	@Comment("已执行时间")
	private double runTimes;
	
	@Comment("检测列表")
	private List<ChecksLoop> checkLoops = new LinkedList<ChecksLoop>();
	
	@Comment("检测列表Str")
	private String clsStr;
	
	@Comment("循环检测状态")
	private int loopTag;

	public LoopConf(){
		this.intervalTimes = ChecksUtil.TEMP_DATA_SAVE;
		this.runTimes = Math.round(((new Date()).getTime() - ChecksUtil.LAST_BEGIN_TIME.getTime())/360000D)/10.0D;
	}
	
	public int getMaxCheckNum() {
		return maxCheckNum;
	}

	public void setMaxCheckNum(int maxCheckNum) {
		this.maxCheckNum = maxCheckNum;
	}

	public int getIntervalTimes() {
		return intervalTimes;
	}

	public void setIntervalTimes(int intervalTimes) {
		this.intervalTimes = intervalTimes;
	}

	public double getRunTimes() {
		return runTimes;
	}

	public void setRunTimes(double runTimes) {
		this.runTimes = runTimes;
	}

	public List<ChecksLoop> getCheckLoops() {
		return checkLoops;
	}

	public void setCheckLoops(List<ChecksLoop> checkLoops) {
		this.checkLoops = checkLoops;
	}

	public int getLoopTag() {
		return loopTag;
	}

	public void setLoopTag(int loopTag) {
		this.loopTag = loopTag;
	}

	public String getClsStr() {
		return clsStr;
	}

	public void setClsStr(String clsStr) {
		this.clsStr = clsStr;
	}

}
