package cn.com.bjggs.temp.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("手动检测列表")
@Table("TempOne")
@PK({"houseNo", "userCode"})
public class ChecksOne {
	
	@Name
	@Column("HouseNo")
	@Comment("仓房编号")
	private String houseNo;
	
	@Column("UserCode")
	@Comment("用户ID")
	private String userCode;

	public ChecksOne(){}
	
	public ChecksOne(String houseNo, String userCode){
		this.houseNo = houseNo;
		this.userCode = userCode;
	}
	
	public ChecksOne(String houseNo){
		this.houseNo = houseNo;
	}
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

}
