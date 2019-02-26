package cn.com.bjggs.gas.view;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.View;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.domain.IdEntry;

@Comment("仓房与气体配置信息")
@View("VIEW_HOUSE_GAS")
public class ViewHouseGas extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("仓房名称")
	@Column("HouseName")
	private String houseName;
	
	@Comment("仓房类型")
	@Column("HouseType")
	private int houseType;
	
	@Comment("控制板IP")
	@Column("CtrIp")
	private String ctrIp;
	
	@Comment("控制板端口")
	@Column("CtrPort")
	private int ctrPort;
	
	@Comment("控制板类型")
	@Column("CtrType")
	private int ctrType;
	
	@Comment("清空时间")
	@Column("DrainTime")
	private int drainTime;
	
	@Comment("风路数")
	@Column("WayNumb")
	private int wayNumb;
	
	@Comment("各风路抽气时间")
	@Column("WayTime")
	private byte[] wayTime;

	private int[] times;
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public int getHouseType() {
		return houseType;
	}

	public void setHouseType(int houseType) {
		this.houseType = houseType;
	}

	public String getCtrIp() {
		return ctrIp;
	}

	public void setCtrIp(String ctrIp) {
		this.ctrIp = ctrIp;
	}

	public int getCtrPort() {
		return ctrPort;
	}

	public void setCtrPort(int ctrPort) {
		this.ctrPort = ctrPort;
	}

	public int getCtrType() {
		return ctrType;
	}

	public void setCtrType(int ctrType) {
		this.ctrType = ctrType;
	}

	public int getDrainTime() {
		return drainTime;
	}

	public void setDrainTime(int drainTime) {
		this.drainTime = drainTime;
	}

	public int getWayNumb() {
		return wayNumb;
	}

	public void setWayNumb(int wayNumb) {
		this.wayNumb = wayNumb;
	}

	public byte[] getWayTime() {
		return wayTime;
	}

	public void setWayTime(byte[] wayTime) {
		this.wayTime = wayTime;
		if(wayTime != null){
			this.times = new int[wayTime.length/2];
			for(int i = 0; i < wayTime.length; i += 2){
				this.times[i/2] = MathUtil.byte2Int(wayTime[i], wayTime[i + 1]);
			}
		}
	}

	public int[] getTimes() {
		return times;
	}

	public void setTimes(int[] times) {
		this.times = times;
	}
	
}
