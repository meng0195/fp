package cn.com.bjggs.squery.domain;

import java.util.Date;

import cn.com.bjggs.temp.domain.TestData;

public class QueryTTwo {
	
	private String houseNo;
	
	private Date time1;
	
	private Date time2;
	
	private TestData result1 = new TestData();
	
	private TestData result2 = new TestData();
	
	private TestData diff = new TestData();

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public Date getTime1() {
		return time1;
	}

	public void setTime1(Date time1) {
		this.time1 = time1;
	}

	public Date getTime2() {
		return time2;
	}

	public void setTime2(Date time2) {
		this.time2 = time2;
	}

	public TestData getResult1() {
		return result1;
	}

	public void setResult1(TestData result1) {
		if(result1 != null) this.result1 = result1;
		if(result1 != null && result2 != null){
			setDiff();
		}
	}

	public TestData getResult2() {
		return result2;
	}

	public void setResult2(TestData result2) {
		if(result2 != null) this.result2 = result2;
		if(result1 != null && result2 != null){
			setDiff();
		}
	}

	public TestData getDiff() {
		return diff;
	}

	public void setDiff(TestData diff) {
		this.diff = diff;
	}
	private void setDiff(){
		diff.setAvgT(result1.getAvgT() - result2.getAvgT());
		diff.setInH(result1.getInH()-result2.getInH());
		diff.setInT(result1.getInT()-result2.getInT());
		diff.setOutH(result1.getOutH()-result2.getOutH());
		diff.setOutT(result1.getOutT()-result2.getOutT());
		diff.setMaxT(result1.getMaxT()-result2.getMaxT());
		diff.setMinT(result1.getMinT()-result2.getMinT());
	}

}
