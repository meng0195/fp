package cn.com.bjggs.weather.domain;

import java.util.Date;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.domain.IdEntry;

@Comment("气象站信息表")
@Table("TestWeather")
public class TestWeather extends IdEntry{

	@Comment("检测时间")
	@Column("TestDate")
	private Date testDate;
	
	@Comment("温度")
	@Column("Temp")
	private double temp;
	
	@Comment("风向")
	@Column("WindDirec")
	private double windDirec;
	@SuppressWarnings("unused")
	private String direcStr;
	
	@Comment("风速")
	@Column("WindSpeed")
	private double windSpeed;

	@Comment("湿度")
	@Column("Humidity")
	private double humidity;
	
	@Comment("大气压强")
	@Column("kPa")
	private double kpa;
	
	@Comment("雨雪")
	@Column("Sleet")
	private double sleet;

	@Comment("光照度")
	@Column("Illumina")
	private double illumina;
	
	@Comment("CO2浓度")
	@Column("CO2")
	private double co2;

	public TestWeather(){}
	
	public TestWeather(Date testDate, byte[] datas){
		if(datas != null && datas.length == 22){
			this.temp = MathUtil.byte2IntNb(datas[0], datas[1])/10.0D;
			this.humidity = MathUtil.byte2IntNb(datas[2], datas[3])/10.0D;
			this.illumina = MathUtil.byte2IntNb(datas[4], datas[5]);//光照度,0-20KLUX, 0x0000-0x4E20(0-20000);
			this.co2 = MathUtil.byte2IntNb(datas[6], datas[7]);//co2,0-5000ppm, 0x0000-0x1388(0-5000)
			this.kpa = MathUtil.byte2IntNb(datas[12], datas[13])/10.0D;//大气压强 0- 120Kpa, 03DB = 987hpa = 0.987Kpa
			this.windSpeed = MathUtil.byte2IntNb(datas[14], datas[15])/10.0D;//风速, 007D表示12.5m/s
			this.windDirec = MathUtil.byte2IntNb(datas[16], datas[17])/10.0D;//风向,如0020表示3.2度
			this.sleet = MathUtil.byte2IntNb(datas[18], datas[19]);//雨雪, 0000=无雨雪  AAAA=有雨雪
		}
		this.testDate = testDate;
		this.setId(UUID.randomUUID().toString());
	}
	
	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getWindDirec() {
		return windDirec;
	}

	public void setWindDirec(double windDirec) {
		this.windDirec = windDirec;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getSleet() {
		return sleet;
	}

	public void setSleet(double sleet) {
		this.sleet = sleet;
	}

	public double getIllumina() {
		return illumina;
	}

	public void setIllumina(double illumina) {
		this.illumina = illumina;
	}

	public double getKpa() {
		return kpa;
	}

	public void setKpa(double kpa) {
		this.kpa = kpa;
	}

	public double getCo2() {
		return co2;
	}

	public void setCo2(double co2) {
		this.co2 = co2;
	}
	
	public String getDirecStr() {
		return getWindAngle(windDirec);
	}

	public void setDirecStr(String direcStr) {
		this.direcStr = direcStr;
	}

	public static String getWindAngle(double angle){
		String wind = "北";
		if(angle>=348.76 || angle<=11.25){
			wind = "北";
		}else if(angle>=11.26 || angle<=33.75){
			wind = "东北偏北";
		}else if(angle>=33.76 || angle<=56.25){
			wind = "东北";
		}else if(angle>=56.26 || angle<=78.75){
			wind = "东北偏东";
		}else if(angle>=78.76 || angle<=101.25){
			wind = "东";
		}else if(angle>=101.26 || angle<=123.75){
			wind = "东南偏东";
		}else if(angle>=123.76 || angle<=146.25){
			wind = "东南";
		}else if(angle>=146.26 || angle<=168.75){
			wind = "东南偏南";
		}else if(angle>=167.76 || angle<=191.25){
			wind = "南";
		}else if(angle>=191.26 || angle<=213.75){
			wind = "西南偏南";
		}else if(angle>=213.76 || angle<=236.25){
			wind = "西南";
		}else if(angle>=236.26 || angle<=258.75){
			wind = "西南偏西";
		}else if(angle>=258.76 || angle<=281.25){
			wind = "西";
		}else if(angle>=281.26 || angle<=303.75){
			wind = "西北偏西";
		}else if(angle>=303.76 || angle<=326.25){
			wind = "西北";
		}else if(angle>=326.26 || angle<=348.75){
			wind = "北西北";
		}
		return wind;	
	}
	
	
}
