package cn.com.bjggs.gas.domain;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.warns.domain.AlarmNotes;

@Comment("气体检测结果")
public class GasResults {

	private String houseNo;
	
	private int tag;
	
	private int checkSum;//正在检测风路几
	
	private int surTime;//剩余检测时间
	
	private TestGas gas;
	
	private AlarmNotes an;
	
	private AlarmNotes anCO2;
	
	private AlarmNotes anPH3;
	
	private AlarmNotes anO2;
	
	public GasResults(String houseNo, TestGas gas, AlarmNotes an){
		this.houseNo = houseNo;
		this.gas = gas;
		this.an = an;
	}
	
	public GasResults(){}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public TestGas getGas() {
		return gas;
	}

	public void setGas(TestGas gas) {
		this.gas = gas;
	}

	public AlarmNotes getAn() {
		return an;
	}

	public void setAn(AlarmNotes an) {
		this.an = an;
	}

	public AlarmNotes getAnCO2() {
		return anCO2;
	}

	public void setAnCO2(AlarmNotes anCO2) {
		this.anCO2 = anCO2;
	}

	public AlarmNotes getAnPH3() {
		return anPH3;
	}

	public void setAnPH3(AlarmNotes anPH3) {
		this.anPH3 = anPH3;
	}

	public AlarmNotes getAnO2() {
		return anO2;
	}

	public void setAnO2(AlarmNotes anO2) {
		this.anO2 = anO2;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(int checkSum) {
		this.checkSum = checkSum;
	}

	public int getSurTime() {
		return surTime;
	}

	public void setSurTime(int surTime) {
		this.surTime = surTime;
	}
	
}
