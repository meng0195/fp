package cn.com.bjggs.gas.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("气体检测列表")
@Table("GasChecks")
@PK({"houseNo", "userCode"})
public class GasList {

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("用户ID")
	@Column("userCode")
	private String userCode;
	
	public GasList(){}
	
	public GasList(String s, String uid){
		this.houseNo = s;
		this.userCode = uid;
	}
	
//	public GasList(String s){
//		this.houseNo = s;
//	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	
}
