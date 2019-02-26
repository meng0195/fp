package cn.com.bjggs.basis.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.com.bjggs.core.domain.IdEntry;

@Comment("全局信息配置表")
@Table("GlobalConf")
public class GlobalConf extends IdEntry{

	@Comment("库标识码")
	@Column("IdentCode")
	private String identCode;
	
	@Comment("库标识码")
	@Column("IdentName")
	private String identName;
	
	@Comment("通风窗最大开启时间")
	@Column("Max_TF")
	private String max_TF;
	
	@Comment("轴流风机最大开启时间")
	@Column("Max_ZL")
	private String max_ZL;
	
	@Comment("轴流窗最大开启时间")
	@Column("Max_ZLC")
	private String max_ZLC;
	
	@Comment("混流风机最大开启时间")
	@Column("Max_HL")
	private String max_HL;
	
	@Comment("混流窗最大开启时间")
	@Column("Max_HLC")
	private String max_HLC;
	
	@Comment("照明最大开启时间")
	@Column("Max_ZM")
	private String max_ZM;

	@Comment("空调最大开启时间")
	@Column("Max_KT")
	private String max_KT;
	
	@Comment("内环流最大开启时间")
	@Column("Max_NHL")
	private String max_NHL;

	@Comment("排积热风机最大开启时间")
	@Column("Max_PJR")
	private String max_PJR;
	
	@Comment("排积热风机窗最大开启时间")
	@Column("Max_PJRC")
	private String max_PJRC;
	
	private int weaTag;

	public String getIdentCode() {
		return identCode;
	}

	public void setIdentCode(String identCode) {
		this.identCode = identCode;
	}

	public String getMax_TF() {
		return max_TF;
	}

	public void setMax_TF(String max_TF) {
		this.max_TF = max_TF;
	}

	public String getMax_ZL() {
		return max_ZL;
	}

	public void setMax_ZL(String max_ZL) {
		this.max_ZL = max_ZL;
	}

	public String getMax_ZLC() {
		return max_ZLC;
	}

	public void setMax_ZLC(String max_ZLC) {
		this.max_ZLC = max_ZLC;
	}

	public String getMax_HL() {
		return max_HL;
	}

	public void setMax_HL(String max_HL) {
		this.max_HL = max_HL;
	}

	public String getMax_HLC() {
		return max_HLC;
	}

	public void setMax_HLC(String max_HLC) {
		this.max_HLC = max_HLC;
	}

	public String getMax_ZM() {
		return max_ZM;
	}

	public void setMax_ZM(String max_ZM) {
		this.max_ZM = max_ZM;
	}

	public String getMax_KT() {
		return max_KT;
	}

	public void setMax_KT(String max_KT) {
		this.max_KT = max_KT;
	}

	public String getMax_NHL() {
		return max_NHL;
	}

	public void setMax_NHL(String max_NHL) {
		this.max_NHL = max_NHL;
	}

	public String getMax_PJR() {
		return max_PJR;
	}

	public void setMax_PJR(String max_PJR) {
		this.max_PJR = max_PJR;
	}

	public String getMax_PJRC() {
		return max_PJRC;
	}

	public void setMax_PJRC(String max_PJRC) {
		this.max_PJRC = max_PJRC;
	}

	public String getIdentName() {
		return identName;
	}

	public void setIdentName(String identName) {
		this.identName = identName;
	}

	public int getWeaTag() {
		return weaTag;
	}

	public void setWeaTag(int weaTag) {
		this.weaTag = weaTag;
	}
	
}
