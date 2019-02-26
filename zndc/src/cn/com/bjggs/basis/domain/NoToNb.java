package cn.com.bjggs.basis.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Comment("仓房信息")
@Table("NoToNb")
@PK({"houseNo", "otherNb"})
public class NoToNb {

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("仓房名称")
	@Column("OtherNb")
	@ColDefine(update=false, insert=true)
	private String otherNb;
	
	public NoToNb(){}
	
	public NoToNb(String houseNo, String otherNb){
		this.houseNo = houseNo;
		this.otherNb = otherNb;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getOtherNb() {
		return otherNb;
	}

	public void setOtherNb(String otherNb) {
		this.otherNb = otherNb;
	}
	
}
