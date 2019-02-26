package cn.com.bjggs.pest.domain;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.warns.domain.AlarmNotes;

@Comment("虫害检测结果")
public class PestResults {

	private String houseNo;
	
	private int tag;
	
	private TestPest pest;
	
	private AlarmNotes an;
	
	private AlarmNotes an0;
	
	private AlarmNotes an1;
	
	private int pointIng;
	
	private int[] overs;
	
	public PestResults(String houseNo, TestPest pest, AlarmNotes an){
		this.houseNo = houseNo;
		this.pest = pest;
		this.an = an;
	}
	
	public PestResults(){}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public TestPest getPest() {
		return pest;
	}

	public void setPest(TestPest pest) {
		this.pest = pest;
	}

	public AlarmNotes getAn() {
		return an;
	}

	public void setAn(AlarmNotes an) {
		this.an = an;
	}

	public AlarmNotes getAn0() {
		return an0;
	}

	public void setAn0(AlarmNotes an0) {
		this.an0 = an0;
	}

	public AlarmNotes getAn1() {
		return an1;
	}

	public void setAn1(AlarmNotes an1) {
		this.an1 = an1;
	}

	public int getPointIng() {
		return pointIng;
	}

	public void setPointIng(int pointIng) {
		this.pointIng = pointIng;
	}

	public int[] getOvers() {
		return overs;
	}

	public void setOvers(int[] overs) {
		this.overs = overs;
	}
	
}
