package cn.com.bjggs.squery.domain;

import java.util.Date;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.ctr.domain.CtrResults;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.util.ChecksUtil;


public class QueryMain {
	
	private String houseNo;
	
	private String houseName;
	
	private String typeName;
	
	private double storeCount;
	
	private int builtYear;
	
	private String grainName;
	
	private String natureName;
	
	private double grainCount;
	
	private String gradeName;
	
	private int gainYear;
	
	private String origin;
	
	private Date dateOfIn;
	
	private String proof3;
	
	private String keeperName;
	
	private Date tempTime;
	
	private int tempTag;
	
	private Date pestTime;
	
	private int pestTag;
	
	private Date gasTime;
	
	private int gasTag;
	
	private int equipTag;
	
	private int tag;

	public QueryMain(String houseNo){
		this.houseNo = houseNo;
		//同步仓房信息
		StoreHouse s = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		this.houseName = s.getHouseName();
		this.typeName = Enums.get("TYPE_HOUSE","" + s.getHouseType());
		this.storeCount = s.getStoreCount();
		this.builtYear = s.getBuiltYear();
		//同步储粮信息
		GrainInfo g = HouseUtil.get(houseNo, TypeHouseConf.GRAIN.code(), GrainInfo.class);
		this.grainName = Codes.get("TYPE_GRAIN", "" + g.getGrainCode());
		this.natureName = Codes.get("TYPE_NATURE", "" + g.getNature());
		this.grainCount = g.getGrainCount();
		this.gradeName = Codes.get("TYPE_GRADE", "" + g.getGrade());
		this.gainYear = g.getGainYear();
		this.origin = g.getOrigin();
		this.dateOfIn = g.getDateOfIn();
		this.proof3 = g.getProof3();
		this.keeperName = g.getKeeperName();
		refreshTemp();
		refreshPest();
		refreshGas();
		refreshEquip();
	}
	
	public void refreshTemp(){
		TempResults tr = ChecksUtil.lastChecks.get(houseNo);
		if(tr != null){
			this.tempTime = tr.getDatas() == null ? null : tr.getDatas().getTestDate();
			this.tempTag = tr.getDatas() == null ? 0 : tr.getDatas().getTestTag();
		}	
	}
	
	public void refreshPest(){
		PestResults pr = CheckPest.lastPests.get(houseNo);
		if(pr != null){
			this.pestTime = pr.getPest() == null ? null : pr.getPest().getStartTime();
			this.pestTag = pr.getPest() == null ? 0 : pr.getPest().getTestTag();
		}
	}
	
	public void refreshGas(){
		GasResults gr = GasUtil.lastChecks.get(houseNo);
		if(gr != null){
			this.gasTime = gr.getGas() == null ? null : gr.getGas().getStartTime();
			this.gasTag = gr.getGas() == null ? 0 : gr.getGas().getTestTag();
		}
	}
	
	public void refreshEquip(){
		CtrResults res = CtrUtil.lasts.get(houseNo);
		if(res != null){
			this.equipTag = res.getWarn();
		}
	}
	
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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public double getStoreCount() {
		return storeCount;
	}

	public void setStoreCount(double storeCount) {
		this.storeCount = storeCount;
	}

	public int getBuiltYear() {
		return builtYear;
	}

	public void setBuiltYear(int builtYear) {
		this.builtYear = builtYear;
	}

	public String getGrainName() {
		return grainName;
	}

	public void setGrainName(String grainName) {
		this.grainName = grainName;
	}

	public String getNatureName() {
		return natureName;
	}

	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}

	public double getGrainCount() {
		return grainCount;
	}

	public void setGrainCount(double grainCount) {
		this.grainCount = grainCount;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public int getGainYear() {
		return gainYear;
	}

	public void setGainYear(int gainYear) {
		this.gainYear = gainYear;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Date getDateOfIn() {
		return dateOfIn;
	}

	public void setDateOfIn(Date dateOfIn) {
		this.dateOfIn = dateOfIn;
	}

	public String getProof3() {
		return proof3;
	}

	public void setProof3(String proof3) {
		this.proof3 = proof3;
	}

	public String getKeeperName() {
		return keeperName;
	}

	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}

	public Date getTempTime() {
		return tempTime;
	}

	public void setTempTime(Date tempTime) {
		this.tempTime = tempTime;
	}

	public int getTempTag() {
		return tempTag;
	}

	public void setTempTag(int tempTag) {
		this.tempTag = tempTag;
	}

	public Date getPestTime() {
		return pestTime;
	}

	public void setPestTime(Date pestTime) {
		this.pestTime = pestTime;
	}

	public int getPestTag() {
		return pestTag;
	}

	public void setPestTag(int pestTag) {
		this.pestTag = pestTag;
	}

	public Date getGasTime() {
		return gasTime;
	}

	public void setGasTime(Date gasTime) {
		this.gasTime = gasTime;
	}

	public int getGasTag() {
		return gasTag;
	}

	public void setGasTag(int gasTag) {
		this.gasTag = gasTag;
	}

	public int getTag() {
		if(equipTag == 1 || gasTag == 1 || tempTag == 1 || pestTag == 1){
			tag = 1;
		} else {
			tag = 0;
		}
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getEquipTag() {
		return equipTag;
	}

	public void setEquipTag(int equipTag) {
		this.equipTag = equipTag;
	}
	
}
