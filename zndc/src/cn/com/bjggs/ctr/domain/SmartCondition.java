package cn.com.bjggs.ctr.domain;

import org.nutz.dao.entity.annotation.Comment;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.ctr.util.SmartUtil;

@Comment("智能模式界限属性")
public class SmartCondition extends IdEntry{
	
	private int grainCode;
	
	private double inT;
	
	private double outT;
	
	private double inH;
	
	private double outH;
	
	private double avgT;
	
	private double archT;//拱温
	
	private double coreT;//冷芯

	public double getInT() {
		return inT;
	}

	public void setInT(double inT) {
		this.inT = inT;
	}

	public double getOutT() {
		return outT;
	}

	public void setOutT(double outT) {
		this.outT = outT;
	}

	public double getInH() {
		return inH;
	}

	public void setInH(double inH) {
		this.inH = inH;
	}

	public double getOutH() {
		return outH;
	}

	public void setOutH(double outH) {
		this.outH = outH;
	}

	public double getAvgT() {
		return avgT;
	}

	public void setAvgT(double avgT) {
		this.avgT = avgT;
	}

	public double getArchT() {
		return archT;
	}

	public void setArchT(double archT) {
		this.archT = archT;
	}

	public double getCoreT() {
		return coreT;
	}

	public void setCoreT(double coreT) {
		this.coreT = coreT;
	}

	public boolean isOpenJWTF(double target){
		if(avgT - outT >= SmartUtil.JWS && avgT > target){
			return true;
		}
		return false;
	}
	
	public boolean isCloseJWTF(double target){
		if(avgT - outT <= SmartUtil.JWE || avgT < target){
			return true;
		}
		return false;
	}
	
	public boolean isOpenZRTF(){
		if(inT - outT >= SmartUtil.ZRS){
			return true;
		}
		return false;
	}
	
	public boolean isCloseZRTF(){
		if(inT - outT <= SmartUtil.ZRE){
			return true;
		}
		return false;
	}
	
	public boolean isOpenJSTF(double target){
		WaterDatars wd = CtrUtil.getWaterDatars((int)outH, (int)outT, grainCode);
		//验证水分是否达到 解吸的界限
		if(wd.getOutWater() < target - 1){
			return true;
		}
		return false;
	}
	
	public boolean isCloseJSTF(double target){
		WaterDatars wd = CtrUtil.getWaterDatars((int)outH, (int)outT, grainCode);
		if(wd.getOutWater() > target){
			return true;
		}
		return false;
	}
	
	public boolean isOpenBSTF(double target, double target2){
		WaterDatars wd = CtrUtil.getWaterDatars((int)outH, (int)outT, grainCode);
		if(avgT - outT > SmartUtil.BSS && wd.getOutWater() > target + 1 && avgT > target2){
			return true;
		}
		return false;
	}
	
	public boolean isCloseBSTF(double target, double target2){
		WaterDatars wd = CtrUtil.getWaterDatars((int)outH, (int)outT, grainCode);
		if(avgT - outT < SmartUtil.BSE || wd.getOutWater() < target || avgT < target2){
			return true;
		}
		return false;
	}
	
	public boolean isOpenNHL(double target){
		if(inT - coreT > SmartUtil.NHLS && coreT < SmartUtil.NHLC && inT > target){
			return true;
		}
		return false;
	}
	
	public boolean isCloseNHL(double target){
		if(inT - coreT < SmartUtil.NHLE || coreT > SmartUtil.NHLC || inT <= target){
			return true;
		}
		return false;
	}
	
	public boolean isOpenZNKT(double target){
		if(inT > target){
			return true;
		}
		return false;
	}
	
	public boolean isCloseZNKT(double target){
		if(inT < target){
			return true;
		}
		return false;
	}
	
	public boolean isOpenPJRTF(boolean tag){
		if(tag){
			if(archT - outT >= SmartUtil.PJRS){
				return true;
			}
		} else {
			if(inT - outT >= SmartUtil.PJRS){
				return true;
			}
		}
		return false;
	}
	
	public boolean isClosePJRTF(boolean tag){
		if(tag){
			if(archT - outT < SmartUtil.PJRE){
				return true;
			}
		} else {
			if(inT - outT < SmartUtil.PJRE){
				return true;
			}
		}
		return false;
	}
	
	public boolean isYX(){
		//从气象站中获取晴/雨/雪状态
		//順德 没有气象站
		return false;
	}

	public int getGrainCode() {
		return grainCode;
	}

	public void setGrainCode(int grainCode) {
		this.grainCode = grainCode;
	}
	
}
