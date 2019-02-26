package cn.com.bjggs.weather.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("气象站配置表")
@Table("WeatherInfo")
public class WeatherInfo extends IdEntry{

	@Comment("气象站类型")
	@Column("Type")
	private int type;
	
	@Comment("气象站IP")
	@Column("IP")
	private String ip;
	
	@Comment("气象站端口号")
	@Column("Port")
	private int port;
	
	@Comment("刷新频率")
	@Column("RefreTime")
	private int refreTime;
	
	@Comment("保存频率")
	@Column("SaveTime")
	private int saveTime;
	
	@Comment("气象站位置")
	@Column("Address")
	private String address;

	@Comment("气温气湿获取类型")
	@Column("OutType")
	private int outType;
	
	@Comment("ARM模式IP")
	@Column("OutIp")
	private String outIp;
	
	@Comment("ARM模式Port")
	@Column("OutPort")
	private int outPort;
	
	@Comment("ARM模式通道号")
	@Column("OutWay")
	private int outWay;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getRefreTime() {
		return refreTime;
	}

	public void setRefreTime(int refreTime) {
		this.refreTime = refreTime;
	}

	public int getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(int saveTime) {
		this.saveTime = saveTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String initCron(){
		StringBuffer sb = new StringBuffer();
		sb.append("0")
		  .append(" ");
		if (refreTime % 60 == 0) {
			sb.append("0 ")
			  .append("0/" + String.valueOf(refreTime / 60));
		} else {
			sb.append("0/" + String.valueOf(refreTime % 60))
			  .append(" ");
			  if (refreTime < 60) {
				  sb.append("*");
			} else {
				 sb.append("0/" + String.valueOf(refreTime / 60));
			}
			 
		}
		sb.append(" * * ?");
		return sb.toString();
	}

	public int getOutType() {
		return outType;
	}

	public void setOutType(int outType) {
		this.outType = outType;
	}

	public String getOutIp() {
		return outIp;
	}

	public void setOutIp(String outIp) {
		this.outIp = outIp;
	}

	public int getOutPort() {
		return outPort;
	}

	public void setOutPort(int outPort) {
		this.outPort = outPort;
	}

	public int getOutWay() {
		return outWay;
	}

	public void setOutWay(int outWay) {
		this.outWay = outWay;
	}
	
}
