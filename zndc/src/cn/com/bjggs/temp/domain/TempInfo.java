package cn.com.bjggs.temp.domain;

import java.util.Map;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.util.JsonUtil;

@Comment("测温信息")
@Table("TempInfo")
public class TempInfo extends IdEntry {

	@Comment("仓房编号")
	@Column("HouseNo")
	private String houseNo;
	
	@Comment("起始电缆编号")
	@Column("BeginNum")
	private int beginNum;

	@Comment("列数")
	@Column("VNum")
	private int vnum;
	
	@Comment("行数")
	@Column("HNum")
	private String hnum;
	private String[] hnums;
	
	@Comment("层数")
	@Column("LNum")
	private String lnum;
	private String[] lnums;
	
	@Comment("最大行数")
	@Column("MaxHNum")
	private int maxHNum;
	
	@Comment("最大层数")
	@Column("MaxLNum")
	private int maxLNum;

	@Comment("起始位置")
	@Column("Begins")
	private int begins;

	@Comment("线缆排布方式")
	@Column("SortOri")
	private int sortOri;

	@Comment("线缆排布方向")
	@Column("SortRule")
	private int sortRule;

	@Comment("传感器排布方式")
	@Column("PointRule")
	private int pointRule;

	@Comment("传感器数量")
	@Column("PointNum")
	private int pointNum;
	
	@Comment("冷心起始列")
	@Column("MinY")
	private int miny;
	
	@Comment("冷心结束列")
	@Column("MaxY")
	private int maxy;
	
	@Comment("冷心起始行")
	@Column("MinX")
	private int minx;
	
	@Comment("冷心结束行")
	@Column("MaxX")
	private int maxx;
	
	@Comment("冷心起始层")
	@Column("MinZ")
	private int minz;
	
	@Comment("冷心结束层")
	@Column("MaxZ")
	private int maxz;
	
	@Comment("层分布")
	@Column("LeveStr")
	@ColDefine(update=false, insert=true)
	private String leveStr;
	private Map<String, Object> leveStrMaps;
	
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public int getBeginNum() {
		return beginNum;
	}

	public void setBeginNum(int beginNum) {
		this.beginNum = beginNum;
	}

	public int getVnum() {
		return vnum;
	}

	public void setVnum(int vnum) {
		this.vnum = vnum;
	}

	public String getHnum() {
		return hnum;
	}

	public void setHnum(String hnum) {
		this.hnum = hnum;
		if(Strings.isNotBlank(hnum)){
			hnums = hnum.split(",");
		}
	}

	public String[] getHnums() {
		return hnums;
	}

	public void setHnums(String[] hnums) {
		this.hnums = hnums;
	}

	public String getLnum() {
		return lnum;
	}

	public void setLnum(String lnum) {
		this.lnum = lnum;
		if(Strings.isNotBlank(lnum)){
			lnums = lnum.split(",");
		}
	}

	public String[] getLnums() {
		return lnums;
	}

	public void setLnums(String[] lnums) {
		this.lnums = lnums;
	}

	public int getMaxHNum() {
		return maxHNum;
	}
	
	public void setMaxHNum(int maxHNum) {
		this.maxHNum = maxHNum;
	}

	public int getMaxLNum() {
		return maxLNum;
	}

	public void setMaxLNum(int maxLNum) {
		this.maxLNum = maxLNum;
	}

	public int getBegins() {
		return begins;
	}

	public void setBegins(int begins) {
		this.begins = begins;
	}

	public int getSortOri() {
		return sortOri;
	}

	public void setSortOri(int sortOri) {
		this.sortOri = sortOri;
	}

	public int getSortRule() {
		return sortRule;
	}

	public void setSortRule(int sortRule) {
		this.sortRule = sortRule;
	}

	public int getPointRule() {
		return pointRule;
	}

	public void setPointRule(int pointRule) {
		this.pointRule = pointRule;
	}

	public int getPointNum() {
		return pointNum;
	}

	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}

	public int getMiny() {
		return miny;
	}

	public void setMiny(int miny) {
		this.miny = miny;
	}

	public int getMaxy() {
		return maxy;
	}

	public void setMaxy(int maxy) {
		this.maxy = maxy;
	}

	public int getMinx() {
		return minx;
	}

	public void setMinx(int minx) {
		this.minx = minx;
	}

	public int getMaxx() {
		return maxx;
	}

	public void setMaxx(int maxx) {
		this.maxx = maxx;
	}

	public int getMinz() {
		return minz;
	}

	public void setMinz(int minz) {
		this.minz = minz;
	}

	public int getMaxz() {
		return maxz;
	}

	public void setMaxz(int maxz) {
		this.maxz = maxz;
	}

	public String getLeveStr() {
		return leveStr;
	}

	public void setLeveStr(String leveStr) {
		this.leveStr = leveStr;
		if(Strings.isNotBlank(leveStr)){
			leveStrMaps = JsonUtil.json2Map(leveStr);
		}
	}

	public Map<String, Object> getLeveStrMaps() {
		return leveStrMaps;
	}

	public void setLeveStrMaps(Map<String, Object> leveStrMaps) {
		this.leveStrMaps = leveStrMaps;
	}

}
