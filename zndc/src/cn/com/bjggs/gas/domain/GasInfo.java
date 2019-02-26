package cn.com.bjggs.gas.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.domain.IdEntry;

@Comment("测气配置信息")
@Table("GasInfo")
public class GasInfo extends IdEntry{

	@Comment("仓房编号")
	@Column("HouseNo")
	@ColDefine(update=false, insert=true)
	private String houseNo;
	
	@Comment("控制板类型")
	@Column("CtrType")
	private int ctrType;
	
	@Comment("控制板IP")
	@Column("CtrIp")
	private String ctrIp;
	
	@Comment("控制板端口")
	@Column("CtrPort")
	private int ctrPort;
	
	@Comment("模式板类型")
	@Column("ModelType")
	private int modelType;
	
	@Comment("模式板IP")
	@Column("ModelIp")
	private String modelIp;
	
	@Comment("模式板端口")
	@Column("ModelPort")
	private int modelPort;
	
	@Comment("时间同步板类型")
	@Column("TimesType")
	private int timesType;
	
	@Comment("时间同步版IP")
	@Column("TimesIp")
	private String timesIp;
	
	@Comment("时间同步板端口")
	@Column("TimesPort")
	private int timesPort;
	
	@Comment("风路数量")
	@Column("WayNumb")
	private int wayNumb;
	
	@Comment("各风路抽气时间")
	@Column("WayTime")
	private byte[] wayTime;
	
	@Comment("排空时间")
	@Column("DrainTime")
	private int drainTime;
	
	@Comment("排布坐标x")
	@Column("WayXaxis")
	private String wayXaxis;
	
	@Comment("排布坐标y")
	@Column("WayYaxis")
	private String wayYaxis;
	
	@Comment("通道直径径")
	@Column("WayDiameter")
	private int wayDiameter;

	@Comment("通道长度集")
	@Column("WayLengths")
	private byte[] wayLengths;
	
	private int[] lens = new int[0];
	
	private int[] times = new int[0];
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getCtrType() {
		return ctrType;
	}

	public void setCtrType(int ctrType) {
		this.ctrType = ctrType;
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

	public int getDrainTime() {
		return drainTime;
	}

	public void setDrainTime(int drainTime) {
		this.drainTime = drainTime;
	}

	public String getWayXaxis() {
		return wayXaxis;
	}

	public void setWayXaxis(String wayXaxis) {
		this.wayXaxis = wayXaxis;
	}

	public String getWayYaxis() {
		return wayYaxis;
	}

	public void setWayYaxis(String wayYaxis) {
		this.wayYaxis = wayYaxis;
	}

	public int getModelType() {
		return modelType;
	}

	public void setModelType(int modelType) {
		this.modelType = modelType;
	}

	public String getModelIp() {
		return modelIp;
	}

	public void setModelIp(String modelIp) {
		this.modelIp = modelIp;
	}

	public int getModelPort() {
		return modelPort;
	}

	public void setModelPort(int modelPort) {
		this.modelPort = modelPort;
	}

	public int getTimesType() {
		return timesType;
	}

	public void setTimesType(int timesType) {
		this.timesType = timesType;
	}

	public String getTimesIp() {
		return timesIp;
	}

	public void setTimesIp(String timesIp) {
		this.timesIp = timesIp;
	}

	public int getTimesPort() {
		return timesPort;
	}

	public void setTimesPort(int timesPort) {
		this.timesPort = timesPort;
	}

	public int getWayDiameter() {
		return wayDiameter;
	}

	public void setWayDiameter(int wayDiameter) {
		this.wayDiameter = wayDiameter;
	}

	public byte[] getWayLengths() {
		return wayLengths;
	}

	public void setWayLengths(byte[] wayLengths) {
		this.wayLengths = wayLengths;
		if(wayLengths != null){
			this.lens = new int[wayLengths.length/2];
			for(int i = 0; i < wayLengths.length; i += 2){
				this.lens[i/2] = MathUtil.byte2Int(wayLengths[i], wayLengths[i + 1]); 
			}
		}
	}

	public int[] getLens() {
		return lens;
	}

	public void setLens(int[] lens) {
		this.lens = lens;
	}

	public int[] getTimes() {
		return times;
	}

	public void setTimes(int[] times) {
		this.times = times;
	}
	
}
