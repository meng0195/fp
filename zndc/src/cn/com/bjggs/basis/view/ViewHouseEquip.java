package cn.com.bjggs.basis.view;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

@Comment("仓房设备汇总")
@View("VIEW_HOUSE_EQUIP1")
public class ViewHouseEquip{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("设备数量")
	@Column("nums")
	private int nums;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}
	
}
