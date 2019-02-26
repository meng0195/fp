package cn.com.bjggs.ctr.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.Enums;

@Comment("仓房模式配置")
@Table("HouseSmarts")
public class HouseSmarts extends IdEntry{
	
	@Comment("摄像头名称")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("粮食品种")
	@Column("GrainCode")
	private int grainCode;
	
	@Comment("入仓时间")
	@Column("DateOfIn")
	private Date dateOfIn;
	
	@Comment("已启动模式")
	@Column("Opens")
	private String opens;
	private String opensName;
	
	@Comment("正在运行模式")
	@Column("Ings")
	private String ings;
	private String ingsName;
	
	//开始模式配置集
	private Map<Integer, SmartConf> scs = new LinkedHashMap<Integer, SmartConf>();  
	
	public HouseSmarts(){}
	
	public HouseSmarts(String houseNo, int grainCode, Date dateOfIn){
		this.setId(UUID.randomUUID().toString());
		this.houseNo = houseNo;
		this.grainCode = grainCode;
		this.dateOfIn = dateOfIn;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(int grainCode) {
		this.grainCode = grainCode;
	}

	public Date getDateOfIn() {
		return dateOfIn;
	}

	public void setDateOfIn(Date dateOfIn) {
		this.dateOfIn = dateOfIn;
	}

	public String getOpens() {
		return opens;
	}

	public void setOpens(String opens) {
		this.opens = opens;
		if(Strings.isNotBlank(opens)){
			opensName = "";
			String[] openss = opens.split(",");
			for(String code : openss){
				opensName += Enums.get("TYPE_SMART", code) + ",";
			}
			opensName = opensName.substring(0, opensName.length() - 1);
		}
	}

	public String getOpensName() {
		return opensName;
	}

	public void setOpensName(String opensName) {
		this.opensName = opensName;
	}

	public String getIngs() {
		return ings;
	}

	public void setIngs(String ings) {
		this.ings = ings;
		if(Strings.isNotBlank(ings)) {
			String[] ingss = ings.split(",");
			ingsName = "";
			for(String code : ingss){
				ingsName += Enums.get("TYPE_SMART", code) + ",";
			}
			ingsName = ingsName.substring(0, ingsName.length() - 1);
		}
	}

	public String getIngsName() {
		return ingsName;
	}

	public void setIngsName(String ingsName) {
		this.ingsName = ingsName;
	}

	public Map<Integer, SmartConf> getScs() {
		return scs;
	}

	public void setScs(Map<Integer, SmartConf> scs) {
		this.scs = scs;
	}
	
}
