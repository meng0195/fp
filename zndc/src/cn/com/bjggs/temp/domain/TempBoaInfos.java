package cn.com.bjggs.temp.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

@Comment("测温板配置信息")
public class TempBoaInfos {

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("测温信息类型")
	@Column("TempType")
	private int[] tempType;

	
	@Comment("ip")
	@Column("Ip")
	private String[] ip;

	
	@Comment("端口号")
	@Column("Port")
	private int[] port;

	
	@Comment("板子类型")
	@Column("BoardType")
	private int[] boardType;

	
	@Comment("通道号")
	@Column("WayNo")
	private int[] wayNo;

	
	@Comment("电缆号")
	@Column("CableNo")
	private String[] cableNo;


	public String getHouseNo() {
		return houseNo;
	}


	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}


	public int[] getTempType() {
		return tempType;
	}


	public void setTempType(int[] tempType) {
		this.tempType = tempType;
	}


	public String[] getIp() {
		return ip;
	}


	public void setIp(String[] ip) {
		this.ip = ip;
	}


	public int[] getBoardType() {
		return boardType;
	}


	public void setBoardType(int[] boardType) {
		this.boardType = boardType;
	}


	public int[] getWayNo() {
		return wayNo;
	}


	public void setWayNo(int[] wayNo) {
		this.wayNo = wayNo;
	}


	public void setPort(int[] port) {
		this.port = port;
	}


	public String[] getCableNo() {
		return cableNo;
	}


	public void setCableNo(String[] cableNo) {
		this.cableNo = cableNo;
	}
	
	public List<TempBoaInfo> getTbs(){
		List<TempBoaInfo> reqs = new LinkedList<TempBoaInfo>();
		TempBoaInfo tb;
		if(!Lang.isEmpty(tempType) && Strings.isNotBlank(houseNo)){
			int len = tempType.length;
			for(int i = 0; i < len; i++){
				tb = new TempBoaInfo(houseNo, tempType[i], ip[i], port[i], boardType[i], wayNo[i], cableNo[i]);
				tb.setId(UUID.randomUUID().toString());
				reqs.add(tb);
			}
		}
		return reqs;
	}
	
}
