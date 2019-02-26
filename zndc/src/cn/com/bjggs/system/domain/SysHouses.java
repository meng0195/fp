package cn.com.bjggs.system.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.annotation.Tag;

@Tag("sys_houses")
@Comment("仓房权限表")
@Table("SYS_HOUSES")
@PK({ "uid", "houseNo"})
public class SysHouses {
	
	@Column("U_ID")
	@Comment("用户ID")
	private String uid;
	
	@Column("HOUSENO")
	@Comment("仓房编号")
	private String houseNo;

	public SysHouses(){}
	
	public SysHouses(String houseNo, String uid){
		this.uid = uid;
		this.houseNo = houseNo;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	
}
