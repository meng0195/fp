package cn.com.bjggs.temp.domain;

import java.util.List;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.Constant;
import cn.com.bjggs.warns.domain.AlarmNotes;

@Comment("测温检测结果")
public class TempResults {

	private String houseNo;
	
	private int tag;
	
	private TestData datas;
	
	private AlarmNotes an;
	//温度高限
	private AlarmNotes an0;
	//温度升高率
	private AlarmNotes an1;
	//温度异常点
	private AlarmNotes an2;
	//层均温
	private AlarmNotes an3;
	//缺点率
	private AlarmNotes an4;
	//冷芯
	private AlarmNotes an5;
	
	public TempResults(String houseNo, TestData datas, AlarmNotes an){
		this.houseNo = houseNo;
		this.datas = datas;
		this.an = an;
	}
	
	public TempResults(){}

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

	public TestData getDatas() {
		return datas;
	}

	public void setDatas(TestData datas) {
		this.datas = datas;
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

	public AlarmNotes getAn2() {
		return an2;
	}

	public void setAn2(AlarmNotes an2) {
		this.an2 = an2;
	}

	public AlarmNotes getAn3() {
		return an3;
	}

	public void setAn3(AlarmNotes an3) {
		this.an3 = an3;
	}

	public AlarmNotes getAn4() {
		return an4;
	}

	public void setAn4(AlarmNotes an4) {
		this.an4 = an4;
	}

	public AlarmNotes getAn5() {
		return an5;
	}

	public void setAn5(AlarmNotes an5) {
		this.an5 = an5;
	}
	
	public AlarmNotes getAnByType(int type){
		switch (type) {
		case Constant.W_TEMP_1 : return an0;
		case Constant.W_TEMP_2 : return an1;
		case Constant.W_TEMP_3 : return an2;
		case Constant.W_TEMP_4 : return an3;
		case Constant.W_TEMP_5 : return an4;
		case Constant.W_TEMP_6 : return an5;
		case Constant.W_DO : return an;
		default:
			return an;
		}
	}
	
	public void setAns(List<AlarmNotes> ans){
		if(ans != null){
			for(AlarmNotes an : ans){
				switch (an.getType1()) {
				case Constant.W_TEMP_1 : this.an0 = an; break;
				case Constant.W_TEMP_2 : this.an1 = an; break;
				case Constant.W_TEMP_3 : this.an2 = an; break;
				case Constant.W_TEMP_4 : this.an3 = an; break;
				case Constant.W_TEMP_5 : this.an4 = an; break;
				case Constant.W_TEMP_6 : this.an5 = an; break;
				case Constant.W_DO : this.an = an; break;
				default:
					break;
				}
			}
		}
	}
	
}
