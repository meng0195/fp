package cn.com.bjggs.temp.util;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypeHouse;
import cn.com.bjggs.basis.enums.TypePointRule;
import cn.com.bjggs.basis.enums.TypeSortRule;
import cn.com.bjggs.basis.enums.TypeSortSilo;
import cn.com.bjggs.basis.enums.TypeStartSilo;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;



public class SiloUtil {
	
	private static final double step = ParseUtil.toDouble(PropsUtil.getString("house.step"), 0.5D);
	
	private double stepX;
	
	private int numX;
	
	private double[] stepY;
	
	private int[] numY;
	
	private double stepZ;
	
	private int maxZ;
	
	private int[] numZ;
	
	private TempInfo epment;
	
	private List<PointInfo> points;
	
	private int[][] cables;
	
	private boolean onlyOneTag = false;
	
	private boolean upop = false;
	
	public int getPointNum(){
		return points.size();
	}
	
	public List<PointInfo> getPoints(){
		return points;
	}
	
	private double getStep(double index, double s, boolean tag){
		if(tag) return Math.round((step + index * s)*10)/10.0;
		return Math.round((index * s)*10)/10.0;
	}
	
	public SiloUtil(StoreHouse s, TempInfo e){
		//验证仓房基本长宽高设定
		if(s.getDim2() - 2 * step < 0 || s.getDim3() - 2 * step < 0 || (s.getDim3() - s.getDim2()) < 0){
			throw new RuntimeException("仓房基础参数：长-宽-高设置不正确！");
		}
		
		int x = e.getVnum();
		//圈数
		if(x < 1) throw new RuntimeException("圈数设定不正确！");
		this.numX = x;
		//计算角度和高度方向上的各个间距
		if(x != e.getHnums().length) throw new RuntimeException("每圈个数设定不正确！");
		if(x != e.getLnums().length) throw new RuntimeException("每圈层数设定不正确！");
		this.stepY = new double[x];
		this.numY = new int[x];
		this.numZ = new int[x];
		int y = 0;
		int z = 0;
		int maxZ = 1;
		int maxY = 1;
		for(int i = 0; i < x; i++ ){
			y = ParseUtil.toInt(e.getHnums()[i], 1);
			z = ParseUtil.toInt(e.getLnums()[i], 1);
			if(y < 1) throw new RuntimeException("每圈个数设定不正确！");
			if(z < 1) throw new RuntimeException("每圈层数设定不正确！");
			this.stepY[i] = 360/y;
			this.numY[i] = y;
			this.numZ[i] = z;
			maxZ = Math.max(z, maxZ);
			maxY = Math.max(y, maxY);
			if(i == 0 && y == 1){
				this.onlyOneTag = true;
			}
		}
		e.setMaxHNum(maxY);
		e.setMaxLNum(maxZ);
		this.maxZ = maxZ;
		//设定X轴步长
		if(this.onlyOneTag){//中心只有一根电缆
			stepX = s.getDim1()/(x > 1 ? (x - 1) : 1); 
		} else {//第一圈是多个
			stepX = s.getDim1()/(x > 1 ? (x - 0.5) : 1);
		}
		//设定Z轴步长
		stepZ = (s.getDim3() - 2 * step)/(maxZ > 1 ? maxZ - 1 : 1);
		//z轴长度不同时基准点位上还是下
		upop = TypeHouse.SILO.code().equals(s.getHouseType());
		this.epment = e;
		this.points = new LinkedList<PointInfo>();
		this.cables = new int[x][];
		this.initPoints();
	}
	
	//根据配置获取points
	private void initPoints(){
		this.initCables();
		String houseNo = epment.getHouseNo();
		int[] arr;
		int index = 0;
		int cable = 0;
		double y = 0;
		boolean ps = TypePointRule.ASC.val() == epment.getPointRule();
		int sensorNo = 0;
		int cs = 0;
		boolean sort = false;
		//处理第一圈电缆
		if(onlyOneTag){
			cable = cables[0][0] + (epment.getBeginNum() - 1);
			cs = maxZ - numZ[0];
			sensorNo = ps ? 0 : (numZ[0] + 1);
			if(upop){
				for(int i = cs; i < maxZ; i++){
					index += 1;
					sensorNo = ps ? (sensorNo + 1) : (sensorNo - 1);
					points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, 0D, 0D, getStep(i, stepZ, true), index, cable, sensorNo, 0, 0, i));
				}
			} else {
				for(int i = 0; i < numZ[0]; i++){
					index += 1;
					sensorNo = ps ? (sensorNo + 1) : (sensorNo - 1);
					points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, 0D, 0D, getStep(i, stepZ, true), index, cable, sensorNo, 0, 0, i));
				}
			}
		} else {
			sort = cables[0][0] > cables[0][1];
			cs = maxZ - numZ[0];
			for(int i = 0; i < numY[0]; i++){
				cable = cables[0][i] + (epment.getBeginNum() - 1);
				sensorNo = ps ? 0 : (numZ[0] + 1);
				if(sort){
					y = (i == (numY[0] - 1)) ? 0 : (360 - getStep(i + 1, stepY[0], false));
				} else {
					y = getStep(i, stepY[0], false);
				}
				if(upop){
					for(int j = cs; j < maxZ; j++){
						index += 1;
						sensorNo = ps ? (sensorNo + 1) : (sensorNo - 1);
						points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, getStep(0.5, stepX, false), y, getStep(j, stepZ, true), index, cable, sensorNo, i, 0, j));
					}
				} else {
					for(int j = 0; j < numZ[0]; j++){
						index += 1;
						sensorNo = ps ? (sensorNo + 1) : (sensorNo - 1);
						points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, getStep(0.5, stepX, false), y, getStep(j, stepZ, true), index, cable, sensorNo, i, 0, j));
					}
				}
			}
		}
		//处理其他
		for(int i = 1; i < numX; i++){
			arr = cables[i];
			sort = (arr.length != 1 && arr[0] > arr[1]);
			cs = maxZ - numZ[i];
			for(int j = 0; j < numY[i]; j++){
				cable = arr[j] + (epment.getBeginNum() - 1);
				sensorNo = ps ? 0 : (numZ[0] + 1);
				if(sort){
					y = (j == (numY[i] - 1)) ? 0 : (360 - getStep(j + 1, stepY[i], false));
				} else {
					y = getStep(j, stepY[i], false);
				}
				if(upop){
					for(int k = cs; k < maxZ; k++){
						index += 1;
						sensorNo = ps ? (sensorNo + 1) : (sensorNo - 1);
						points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, getStep(i, stepX, false), y, getStep(k, stepZ, true), index, cable, sensorNo, j, i, k));
					}
				} else {
					for(int k = 0; k < numZ[i]; k++){
						index += 1;
						sensorNo = ps ? (sensorNo + 1) : (sensorNo - 1);
						points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, getStep(i, stepX, false), y, getStep(k, stepZ, true), index, cable, sensorNo, j, i, k));
					}
				}
			}
		}
	}
	
	//初始化cables
	private void initCables(){
		int sortRule = epment.getSortRule();
		int sort = epment.getSortOri();
		int number = 1;
		int[] arr;
		int sTag = 0;
		int[] numXs = new int[numX];
		if(TypeStartSilo.IN.val() == epment.getBegins()){
			for(int i = 0; i < numX; i++){
				numXs[i] = i;
			}
		} else {
			int numXm = numX - 1;
			for(int i = 0; i < numX; i++){
				numXs[i] = numXm - i;
			}
			sTag = (numX-1)%2;
		}
		for(int i : numXs){
			arr = new int[numY[i]];
			cables[i] = arr;
			if (TypeSortSilo.CW.val() == sort) {//顺时针
				if (TypeSortRule.E.val() == sortRule || i%2 == sTag) {
					for(int j = 0; j < numY[i]; j++){
						arr[j] = number;
						number += 1;
					}
				} else {
					for(int j = numY[i] - 1; j >= 0; j--){
						arr[j] = number;
						number += 1;
					}
				}
			} else {//逆时针
				if (TypeSortRule.E.val() == sortRule || i%2 == sTag) {
					for(int j = numY[i] - 1; j >= 0; j--){
						arr[j] = number;
						number += 1;
					}
				} else {
					for(int j = 0; j < numY[i]; j++){
						arr[j] = number;
						number += 1;
					}
				}
			}
		}
	}
}
