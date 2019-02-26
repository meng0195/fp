package cn.com.bjggs.power.domain;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.enums.TypeCtrModel;
import cn.com.bjggs.basis.enums.TypeEquip;
import cn.com.bjggs.ctr.util.CtrConstant;
import cn.com.bjggs.power.enums.TypeEnergy;

@Comment("设备能耗状态记录")
public class CtrStatus{
	
	private boolean ventTag = false;
	
	private boolean airTag = false;
	
	private boolean inLoopTag = false;
	
	private boolean heatTag = false;
	
	private boolean nvcTag = false;
	
	private List<Boolean> ventList;
	
	private List<Boolean> airList;
	
	private List<Boolean> inLoopList;
	
	private List<Boolean> heatList;
	
	private List<Boolean> nvcList;

	public void initLists(){
		this.ventList = new LinkedList<Boolean>();
		this.airList = new LinkedList<Boolean>();
		this.inLoopList = new LinkedList<Boolean>();
		this.heatList = new LinkedList<Boolean>();
		this.nvcList = new LinkedList<Boolean>();
	}
	
	public boolean isVentTag() {
		return ventTag;
	}

	public void setVentTag(boolean ventTag) {
		this.ventTag = ventTag;
	}

	public boolean isAirTag() {
		return airTag;
	}

	public void setAirTag(boolean airTag) {
		this.airTag = airTag;
	}

	public boolean isInLoopTag() {
		return inLoopTag;
	}

	public void setInLoopTag(boolean inLoopTag) {
		this.inLoopTag = inLoopTag;
	}

	public boolean isHeatTag() {
		return heatTag;
	}

	public void setHeatTag(boolean heatTag) {
		this.heatTag = heatTag;
	}

	public List<Boolean> getVentList() {
		return ventList;
	}

	public void setVentList(List<Boolean> ventList) {
		this.ventList = ventList;
	}

	public List<Boolean> getAirList() {
		return airList;
	}

	public void setAirList(List<Boolean> airList) {
		this.airList = airList;
	}

	public List<Boolean> getInLoopList() {
		return inLoopList;
	}

	public void setInLoopList(List<Boolean> inLoopList) {
		this.inLoopList = inLoopList;
	}

	public List<Boolean> getHeatList() {
		return heatList;
	}

	public void setHeatList(List<Boolean> heatList) {
		this.heatList = heatList;
	}
	
	public boolean isNvcTag() {
		return nvcTag;
	}

	public void setNvcTag(boolean nvcTag) {
		this.nvcTag = nvcTag;
	}

	public List<Boolean> getNvcList() {
		return nvcList;
	}

	public void setNvcList(List<Boolean> nvcList) {
		this.nvcList = nvcList;
	}

	public boolean isOpen(List<Boolean> list){
		for (Boolean b : list) {
			if (b == true) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOpen(int tag){
		if(tag == TypeEnergy.T.val()){
			return isOpen(ventList);
		} else {
			return false;
		}
	}
	
	public boolean isClose(int tag){
		if(tag == TypeEnergy.T.val()){
			return !isOpen(ventList);
		} else {
			return false;
		}
	}

	public boolean isClose(List<Boolean> list){
		return !isOpen(list);
	}
	
	public void changeList(Equipment e){
		List<Boolean> tagList = null;
		if(e.getType() <= 5 ){ //通风
			tagList = ventList;	//将引用赋给tagList
		} else if (e.getType() == TypeEquip.KT.val()){ //空调
			tagList = airList;
		} else if (e.getType() == TypeEquip.NHL.val()){ //内环流
			tagList = inLoopList;
		} else if (e.getType() == TypeEquip.ZM.val()){ //照明
			tagList = nvcList;
		} else { //排积热
			tagList = heatList;
		}
		//先判断设备的模式,在根据模式分别获取故障和关闭状态对应的byte
		if (e.getModel() == TypeCtrModel.WIND.val()) {//通风窗模式
			if (e.getStatus() == CtrConstant.R1CA || (e.getStatus() & 0xFF) > 0x90) {
				tagList.add(false);
			} else {
				tagList.add(true);
			}
		} else if (e.getModel() == TypeCtrModel.ONEW.val()) {//单项风机模式
			if (e.getStatus() == CtrConstant.R2SA || (e.getStatus() & 0xFF) > 0x90) {
				tagList.add(false);
			} else {
				tagList.add(true);
			}
		} else if (e.getModel() == TypeCtrModel.WONE.val()) {//窗+风机模式
			if (e.getStatus() == CtrConstant.R3SWA || (e.getStatus() & 0xFF) > 0x90) {
				tagList.add(false);
			} else {
				tagList.add(true);
			}
		} else if (e.getModel() == TypeCtrModel.TWOW.val()) {//双向风机模式
			if (e.getStatus() == CtrConstant.R4SA || (e.getStatus() & 0xFF) > 0x90) {
				tagList.add(false);
			} else {
				tagList.add(true);
			}
		} else if (e.getModel() == TypeCtrModel.DIDO.val()) {//开放模式
			if (e.getStatus() == CtrConstant.IOC || (e.getStatus() & 0xFF) > 0x90) {
				tagList.add(false);
			} else {
				tagList.add(true);
			}
		} else if (e.getModel() == TypeCtrModel.AIRC.val()){
			if (e.getStatus() == CtrConstant.IOC || (e.getStatus() & 0xFF) > 0x90) {
				tagList.add(false);
			} else {
				tagList.add(true);
			}
		}
	}
	
}
