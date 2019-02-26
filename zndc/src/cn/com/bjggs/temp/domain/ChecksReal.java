package cn.com.bjggs.temp.domain;

import java.util.Date;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.lang.Times;

@Comment("手动检测列表")
public class ChecksReal {
	
	@Comment("仓房编号")
	private String houseNo;
	
	@Comment("开始时间")
	private Date begin = new Date();

	public ChecksReal(String houseNo){
		this.houseNo = houseNo;
	}
	
	public ChecksReal(String houseNo, Date begin){
		this.houseNo = houseNo;
		this.begin = begin;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
	/**
	 * 已执行多少秒
	 * @return
	 */
	public long getRunTimes(){
		return (Times.now().getTime() - begin.getTime())/Times.T_1S;
	}
}
