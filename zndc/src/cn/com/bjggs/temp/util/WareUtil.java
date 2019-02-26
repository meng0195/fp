package cn.com.bjggs.temp.util;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypePointRule;
import cn.com.bjggs.basis.enums.TypeSortRule;
import cn.com.bjggs.basis.enums.TypeSortWare;
import cn.com.bjggs.basis.enums.TypeStartWare;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;



public class WareUtil {
	
	private static final double step = ParseUtil.toDouble(PropsUtil.getString("house.step"), 0.5D);
	
	private double stepX;
	
	private int numX;
	
	private double stepY;
	
	private int[] numY;
	
	private double stepZ;
	
	private int[] numZ;
	
	private TempInfo epment;
	
	private List<PointInfo> points;
	
	private int[][] cables;
	
	private int maxY;
	
	private int maxZ;
	
	private boolean sortH = true;
	
	public List<PointInfo> getPoints(){
		return points;
	}
	
	public int getPointNum(){
		return points.size();
	}
	
	private double getStep(int index, double s){
		return Math.round((step + index * s)*10)/10.0;
	}
	
	public WareUtil(StoreHouse s, TempInfo e){
		//验证仓房基本长宽高设定
		if(s.getDim1() - 2 * step < 0 || s.getDim2() - 2 * step < 0 || s.getDim3() - 2 * step < 0){
			throw new RuntimeException("仓房基础参数：长-宽-高设置不正确！");
		}
		int x = e.getVnum();
		//长度方向上线缆设定是否小于0
		if(x < 1) throw new RuntimeException("长度方向上线缆排布不正确！");
		//计算长度方向上点间距的step
		this.stepX = (s.getDim1() - 2 * step)/Math.max((x - 1), 1);
		this.numX = x;
		//计算高度 和宽度方向上的参数
		if(x != e.getHnums().length) throw new RuntimeException("宽度方向上线缆排布不正确！");
		if(x != e.getLnums().length) throw new RuntimeException("高度方向上的先来排布不正确！");
		this.numY = new int[x];
		this.numZ = new int[x];
		int y = 0;
		int z = 0;
		int maxY = 0;
		int maxZ = 0;
		for(int i = 0; i < x; i++ ){
			y = ParseUtil.toInt(e.getHnums()[i], 1);
			z = ParseUtil.toInt(e.getLnums()[i], 1);
			if(y < 1) throw new RuntimeException("宽度方向上线缆排布不正确！");
			if(z < 1) throw new RuntimeException("高度方向上的先来排布不正确！");
			this.numY[i] = y;
			this.numZ[i] = z;
			maxY = Math.max(y, maxY);
			maxZ = Math.max(z, maxZ);
		}
		this.maxY = maxY;
		this.maxZ = maxZ;
		//计算宽度和高度方向上的步长
		this.stepY = (s.getDim2() - 2 * step)/Math.max((maxY - 1), 1);
		this.stepZ = (s.getDim3() - 2 * step)/Math.max((maxZ - 1), 1);
		this.epment = e;
		this.points = new LinkedList<PointInfo>();
		if(e.getSortOri() == TypeSortWare.H.val()){
			this.cables = new int[maxY][];
		} else {
			this.sortH = false;
			this.cables = new int[x][];
		}
		this.initPoints();
		
		//将最大行数和层数赋值
		e.setMaxHNum(maxY);
		e.setMaxLNum(maxZ);
	}
	
	//根据配置获取points
	private void initPoints(){
		this.initCables();
		String houseNo = epment.getHouseNo();
		int[] arr;
		int index = 0;
		int cs;//高度循环补偿
		boolean sb = epment.getPointRule() == TypePointRule.ASC.val();//顺序排布;
		int sensorNo = 0;
		int cable = 0;
		if(sortH){
			for(int i = 0; i < cables.length; i++){
				arr = cables[i];
				for(int j = 0; j < arr.length; j++){
					if(cables[i][j] != 0){
						cable = cables[i][j] + (epment.getBeginNum() - 1);
						cs = maxZ - numZ[j];
						sensorNo = sb ? 0 : (numZ[j] + 1);
						for(int z = cs; z < maxZ; z++){
							index += 1;
							sensorNo = sb ? (sensorNo + 1) : (sensorNo - 1);
							points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, getStep(j, stepX), getStep(i, stepY), getStep(z, stepZ), index, cable, sensorNo, j, i, z));
						}
					}
				}
			}
		} else {
			for(int i = 0; i < cables.length; i++){
				arr = cables[i];
				for(int j = 0; j < arr.length; j++){
					if(cables[i][j] != 0){
						cable = cables[i][j] + (epment.getBeginNum() - 1);
						cs = maxZ - numZ[i];
						sensorNo = sb ? 0 : (numZ[i] + 1);
						for(int z = cs; z < maxZ; z++){
							index += 1;
							sensorNo = sb ? (sensorNo + 1) : (sensorNo - 1);
							points.add(new PointInfo(UUID.randomUUID().toString(), houseNo, getStep(i, stepX), getStep(j, stepY), getStep(z, stepZ), index, cable, sensorNo, i, j, z));
						}
					}
				}
			}
		}
	}
	
	//初始化cables
	private void initCables(){
		int begins = epment.getBegins();
		int sortRule = epment.getSortRule();
		//横向排序
		if (sortH) {
			if (TypeStartWare.S1.val() == begins) {
				int[] arr;
				int index = 0;
				for(int i = 0; i < maxY; i++){
					arr = new int[numX];
					if (TypeSortRule.E.val() == sortRule || i%2 == 0) {
						for(int j = 0; j < numX; j++){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					} else {
						for(int j = numX-1; j >= 0; j--){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					}
					cables[i] = arr;
				}
			} else if(TypeStartWare.S2.val() == begins){
				int[] arr;
				int index = 0;
				for(int i = 0; i < maxY; i++){
					arr = new int[numX];
					if (TypeSortRule.E.val() == sortRule || i%2 == 0) {
						for(int j = numX-1; j >= 0; j--){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					} else {
						for(int j = 0; j < numX; j++){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					}
					cables[i] = arr;
				}
			} else if(TypeStartWare.S3.val() == begins) {
				int[] arr;
				int index = 0;
				int tag = (maxY-1)%2;
				for(int i = maxY - 1; i >= 0; i--){
					arr = new int[numX];
					if (TypeSortRule.E.val() == sortRule || i%2 == tag) {
						for(int j = 0; j < numX; j++){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					} else {
						for(int j = numX-1; j >= 0; j--){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					}
					cables[i] = arr;
				}
			} else if(TypeStartWare.S4.val() == begins){
				int[] arr;
				int index = 0;
				int tag = (maxY-1)%2;
				for(int i = maxY - 1; i >= 0; i--){
					arr = new int[numX];
					if (TypeSortRule.E.val() == sortRule || i%2 == tag) {
						for(int j = numX-1; j >= 0; j--){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					} else {
						for(int j = 0; j < numX; j++){
							if (i < numY[j]) {
								index += 1;
								arr[j] = index;
							} else {
								arr[j] = 0;
							}
						}
					}
					cables[i] = arr;
				}
			}
		} else {
			if (TypeStartWare.S1.val() == begins) {
				int[] arr;
				int index = 0;
				int ny = 0;
				for(int i = 0; i < numX; i++){
					ny = numY[i];
					arr = new int[ny];
					if(TypeSortRule.E.val() == sortRule || i%2 == 0){
						for(int j = 0; j < ny; j++){
							index += 1;
							arr[j] = index;
						}
					} else {
						for(int j = ny - 1; j >= 0; j--){
							index += 1;
							arr[j] = index;
						}
					}
					cables[i] = arr;
				}
			} else if (TypeStartWare.S2.val() == begins) {
				int[] arr;
				int index = 0;
				int ny = 0;
				int tag = (numX-1)%2;
				for(int i = numX - 1; i >= 0; i--){
					ny = numY[i];
					arr = new int[ny];
					if(TypeSortRule.E.val() == sortRule || i%2 == tag){
						for(int j = 0; j < ny; j++){
							index += 1;
							arr[j] = index;
						}
					} else {
						for(int j = ny - 1; j >= 0; j--){
							index += 1;
							arr[j] = index;
						}
					}
					cables[i] = arr;
				}
			} else if (TypeStartWare.S3.val() == begins) {
				int[] arr;
				int index = 0;
				int ny = 0;
				for(int i = 0; i < numX; i++){
					ny = numY[i];
					arr = new int[ny];
					if(TypeSortRule.E.val() == sortRule || i%2 == 0){
						for(int j = ny - 1; j >= 0; j--){
							index += 1;
							arr[j] = index;
						}
					} else {
						for(int j = 0; j < ny; j++){
							index += 1;
							arr[j] = index;
						}
					}
					cables[i] = arr;
				}
			} else if (TypeStartWare.S4.val() == begins) {
				int[] arr;
				int index = 0;
				int ny = 0;
				int tag = (numX-1)%2;
				for(int i = numX - 1; i >= 0; i--){
					ny = numY[i];
					arr = new int[ny];
					if(TypeSortRule.E.val() == sortRule || i%2 == tag){
						for(int j = ny - 1; j >= 0; j--){
							index += 1;
							arr[j] = index;
						}
					} else {
						for(int j = 0; j < ny; j++){
							index += 1;
							arr[j] = index;
						}
					}
					cables[i] = arr;
				}
			}
		}
	}
}
