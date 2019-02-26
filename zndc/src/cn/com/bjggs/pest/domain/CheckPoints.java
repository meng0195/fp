package cn.com.bjggs.pest.domain;

import java.util.LinkedList;
import java.util.List;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

public class CheckPoints {
	
	private String houseNo;
	
	private String[] points0;
	
	private String[] points1;
	
	private String[] points2;
	
	private String[] points3;
	
	private String[] points4;

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String[] getPoints0() {
		return points0;
	}

	public void setPoints0(String[] points0) {
		this.points0 = points0;
	}

	public String[] getPoints1() {
		return points1;
	}

	public void setPoints1(String[] points1) {
		this.points1 = points1;
	}

	public String[] getPoints2() {
		return points2;
	}

	public void setPoints2(String[] points2) {
		this.points2 = points2;
	}

	public String[] getPoints3() {
		return points3;
	}

	public void setPoints3(String[] points3) {
		this.points3 = points3;
	}

	public String[] getPoints4() {
		return points4;
	}

	public void setPoints4(String[] points4) {
		this.points4 = points4;
	}
	
	public List<PestPoint> getPoints(){
		if(Strings.isNotBlank(houseNo)){
			List<PestPoint> ps = new LinkedList<PestPoint>();
			if(!Lang.isEmpty(points0)){
				for(String s : points0){
					ps.add(new PestPoint(houseNo, s));
				}
			}
			if(!Lang.isEmpty(points1)){
				for(String s : points1){
					ps.add(new PestPoint(houseNo, s));
				}
			}
			if(!Lang.isEmpty(points2)){
				for(String s : points2){
					ps.add(new PestPoint(houseNo, s));
				}
			}
			if(!Lang.isEmpty(points3)){
				for(String s : points3){
					ps.add(new PestPoint(houseNo, s));
				}
			}
			if(!Lang.isEmpty(points4)){
				for(String s : points4){
					ps.add(new PestPoint(houseNo, s));
				}
			}
			return ps;
		}
		return null;
	}
	
}
