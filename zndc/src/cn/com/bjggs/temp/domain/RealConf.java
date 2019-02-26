package cn.com.bjggs.temp.domain;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.core.util.PropsUtil;

@Comment("实时数据配置")
public class RealConf {
	
	public static final RealConf realConf = new RealConf();
	
	@Comment("最大并发检测数量")
	private int maxCheckNum;
	
	@Comment("刷新频率(秒 )")
	private int intervalTimes;
	
	@Comment("自动关闭时间(分钟)")
	private int closeTimes;
	
	@Comment("检测列表")
	private List<ChecksReal> checkReals = new LinkedList<ChecksReal>();
	
	public RealConf(){}

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

	public int getCloseTimes() {
		return closeTimes;
	}

	public void setCloseTimes(int closeTimes) {
		this.closeTimes = closeTimes;
	}

	public List<ChecksReal> getCheckReals() {
		return checkReals;
	}

	public void setCheckReals(List<ChecksReal> checkReals) {
		this.checkReals = checkReals;
	}

	public void setReal(RealConf rc){
		realConf.setCloseTimes(rc.getCloseTimes());
		PropsUtil.writeProperties("temp.close.real", String.valueOf(rc.getCloseTimes()));
		realConf.setIntervalTimes(rc.getIntervalTimes());
		PropsUtil.writeProperties("temp.interval.real", String.valueOf(rc.getIntervalTimes()));
		realConf.setMaxCheckNum(rc.getMaxCheckNum());
		PropsUtil.writeProperties("temp.max.real", String.valueOf(rc.getMaxCheckNum()));
	}
	
	public void setReal(){
		realConf.setCloseTimes(PropsUtil.getInteger("temp.close.real"));
		realConf.setIntervalTimes(PropsUtil.getInteger("temp.interval.real"));
		realConf.setMaxCheckNum(PropsUtil.getInteger("temp.max.real"));
	}
}
