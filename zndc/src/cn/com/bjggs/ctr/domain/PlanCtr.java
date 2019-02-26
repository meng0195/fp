package cn.com.bjggs.ctr.domain;

import java.util.Calendar;
import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.util.DateUtil;
import cn.com.bjggs.temp.enums.TypeTime1;

@Tag("PlanCtr")
@Comment("计划任务表")
@Table("PlanCtr")
public class PlanCtr extends IdEntry {
	
	@Column("PlanCode")
	@Comment("计划编号")
	private String planCode;
	
	@Column("PlanName")
	@Comment("计划名称")
	private String planName;
	
	@Column("TimeOne")	
	@Comment("时间参数1")
	private int timeOne;
	
	@Column("TimeTwo")
	@Comment("时间参数2")
	private String timeTwo;
	
	@Column("TimeThree")
	@Comment("时间参数3")
	private String timeThree;
	
	@Column("TimeCron")
	@Comment("时间序列码")
	private String timeCron;
	
	@Column("TimeDesc")
	@Comment("时间点描述")
	private String timeDesc;
	
	@Column("Status")
	@Comment("定时任务状态")
	private int status;
	
	@Column("OperType")
	@Comment("任务类型")
	private int type;
	
	@Column("UserCode")
	@Comment("用户ID")
	private String userCode;
	
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public int getTimeOne() {
		return timeOne;
	}

	public void setTimeOne(int timeOne) {
		this.timeOne = timeOne;
	}

	public String getTimeTwo() {
		return timeTwo;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}

	public String getTimeThree() {
		return timeThree;
	}

	public void setTimeThree(String timeThree) {
		this.timeThree = timeThree;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getTimeCron() {
		return timeCron;
	}

	public void setTimeCron(String timeCron) {
		this.timeCron = timeCron;
	}

	public String getTimeDesc() {
		return timeDesc;
	}

	public void setTimeDesc(String timeDesc) {
		this.timeDesc = timeDesc;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private StringBuffer getWeeks(){
		String[] weeks =  timeTwo.split(",");
		StringBuffer ws = new StringBuffer();
		for(String k : weeks){
			if("1".equals(k)){
				ws.append(",星期日");
			}else if("2".equals(k)){
				ws.append(",星期一");
			}else if("3".equals(k)){
				ws.append(",星期二");
			}else if("4".equals(k)){
				ws.append(",星期三");
			}else if("5".equals(k)){
				ws.append(",星期四");
			}else if("6".equals(k)){
				ws.append(",星期五");
			}else if("7".equals(k)){
				ws.append(",星期六");
			}
		}
		return ws;
	}
	
	private StringBuffer getMonths(){
		String[] month =  timeTwo.split(",");
		StringBuffer ws = new StringBuffer();
		for(String m : month){
			ws.append(",").append(m).append("号");
		}
		return ws;
	}
	private static Calendar calendar;
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.HOUR_OF_DAY);
	}
	 
	/**
	 * 功能描述：返回分
	 *
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
	    calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.MINUTE);
	}
	 
	/**
	 * 返回秒钟
	 *
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
	    calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.SECOND);
	}

	
	public void initCronAndDesc(){
		StringBuffer sb = new StringBuffer();
		//默认每月1号 00:00:00执行
		StringBuffer sb1 = new StringBuffer();
		sb.append(Enums.get("TYPE_TIME1", String.valueOf(timeOne)));
		Date time = DateUtil.parse("HH:mm:ss", timeThree);
		sb1.append(getSecond(time))
			.append(" ")
			.append(getMinute(time))
			.append(" ")
			.append(getHour(time))
			.append(" ");
		if(timeOne == TypeTime1.D.val()){
			sb.append(timeThree);
			sb1.append("* * ?");
		}else if(timeOne == TypeTime1.W.val()){
			sb.append(getWeeks()).append(timeThree);
			sb1.append("? * ").append(timeTwo);
		}else if(timeOne == TypeTime1.M.val()){
			sb.append(getMonths()).append(timeThree);
			sb1.append(timeTwo).append(" * ?");
		}
		timeDesc = sb.toString();
		timeCron = sb1.toString();
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	
}
