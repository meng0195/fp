package cn.com.bjggs.temp.util;


import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.ctr.domain.SmartCondition;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempInfo;

/**
 * 智能模式 扫描温度工具类
 * @author	wc
 * @date	2017-07-18
 */
public class TempSmart extends Thread{
	
	private static final Log log = Logs.getLog(TempSmart.class);
	
	private TempInfo eqment;
	private List<TempBoaInfo> tbis;
	private Map<String, PointInfo> points;
	
	private int allPoints = 0;
	
	private int avgT;
	
	//冷芯数量 冷芯温度
	private int ccN = 0;
	private int ccT = 0;
	
	/**
	 * 所有检测温度集合
	 */
	private byte[][] allTemps;
	
	private String[][] cableRanges = new String[4][];
	
	private SmartCondition sc;
	
	public TempSmart(){}
	
	@SuppressWarnings("unchecked")
	public TempSmart(String houseNo, SmartCondition sc){
		this.sc = sc;
		this.eqment = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		this.tbis = (List<TempBoaInfo>) HouseUtil.get(houseNo, TypeHouseConf.BOARD.code(), TempBoaInfo.class);
		if(eqment == null && (tbis == null || tbis.size() == 0)) {
			//TODO 添加错误
		}
		this.points = (Map<String, PointInfo>)HouseUtil.get(houseNo, TypeHouseConf.TPS.code());
		//将测温板信息依次取出,放入cableRanges
		TempBoaInfo tb;
		for (int i = 0; i < 4; i++) {
			if(tbis == null || tbis.size() <= i) break;
			tb = tbis.get(i);
			if (Strings.isNotBlank(tb.getCableNo())) {
				cableRanges[i] = tb.getCableNo().split(",");
			}
		}
	}
	
	private void check(){
		if(points == null || points.size() == 0){
			//TODO 添加错误处理;
		} else {
			//获取外温外湿
			//getOutTH();
			//获取内温内湿
			getInTH();
			//获取吊顶温度
			getTopT();
			//获取温度
			getTemps();
			//处理获取的温度
			initDatas();
		}
	}
	
//	private void getOutTH(){
//		try{
//			TempBoaInfo tb = tbis.get(5);
//			Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
//			//TODO 从气象站获取的外温外湿需要重新编写计算规则类 wc
//			Temps tUtil = TempFactory.createCheck(tb.getBoardType());
//			byte[] outTH = check.getTH(tb.getWayNo());
//			sc.setOutT(tUtil.getT(outTH, 0, 1));
//			sc.setOutH(tUtil.getH(outTH, 2, 3));
//		} catch (Exception e){
//			//TODO 异常处理
//		}
//	}
	
	private void getInTH(){
		try{
			TempBoaInfo tb = tbis.get(4);
			if(tb != null && Strings.isNotBlank(tb.getIp())){
				synchronized (tb.getIp()) {
					Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
					Temps tUtil = TempFactory.createCheck(tb.getBoardType());
					byte[] inTH = check.getTH(tb.getWayNo());
					sc.setInT(tUtil.getT(inTH, 0, 1));
					sc.setInH(tUtil.getH(inTH, 2, 3));
				}
			}
		} catch (Exception e){
			log.info("智能模式扫描外温错误！");
		}
	}
	
	private void getTopT(){
		try{
			TempBoaInfo tb = tbis.get(8);
			if(null != tb && Strings.isNotBlank(tb.getIp())){
				synchronized (tb.getIp()) {
					Checks check =ModbusFactory.createCheck(tb.getBoardType(), tb.getIp(), tb.getPort());
					Temps tUtil = TempFactory.createCheck(tb.getBoardType());
					byte[] topT = check.getTH(tb.getWayNo());
					sc.setArchT(tUtil.getT(topT, 0, 1));
				}
			}
		} catch (Exception e){
			log.info("智能模式扫描吊顶错误");
		}
	}
	
	private void getTemp(byte[][] bss,int i, int type, String ip, int port){
		try{
			synchronized (ip) {
				Checks check =ModbusFactory.createCheck(type, ip, port);
				bss[i] = check.getTemps();
			}
			//有温度获取成功,需要进行缺点率校验
		} catch(Exception e) {
			log.info("智能模式获取粮温");
		}
	}
	
	private void getTemps(){
		allTemps = new byte[4][];
		for (int i = 0; i < 4; i++) {
			TempBoaInfo tb = tbis.get(i);
			if (tb != null && Strings.isNotBlank(tb.getIp())) {
				getTemp(allTemps, 0, tb.getBoardType(), tb.getIp(), tb.getPort());
			}
		}
	}
	
	private boolean isComplie(byte cable, int index){
		String[] cs; 
		String[] ss;
		int c = 0;
		try{
			cs = cableRanges[index];
			if(!Lang.isEmpty(cs)){
				for(String s : cs){
					ss = s.split("-");
					c = cable & 0xFF;
					if(ParseUtil.toInt(ss[0], 0) <= c && ParseUtil.toInt(ss[1], 0) >= c){
						return true;
					}
				}
			} else {
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
	
	private void initDatas(){
		try {
		StringBuffer key = new StringBuffer("C");
		PointInfo p;
		int temp;
		byte[] ds = new byte[eqment.getPointNum() * 2];
		byte[] bs;
		Temps tUtil;
		int i = 0;
		int len = 0;
		TempBoaInfo tb;
		for(int j = 0; j < 4; j++){
			bs = allTemps[j];
			tb = tbis.get(j);
			tUtil = TempFactory.createCheck(tb.getBoardType());
			i = 0;
			if(bs != null){
				len = bs.length - 3;
				while(len > i){
					key = new StringBuffer("C");
					key.append(MathUtil.byte2int(bs[i])).append("S").append(MathUtil.byte2int(bs[i+1]));
					if(points.containsKey(key.toString()) && (isComplie(bs[i], j))){
						p = points.get(key.toString());
						temp = tUtil.getT10(bs, i+2, i+3);
						if(temp == 0) temp = 1;
						setCores(p, temp);
						if((p.getPoinNo1() - 1) * 2 <= ds.length - 2){
							System.arraycopy(MathUtil.int2Bytes(temp), 0, ds, (p.getPoinNo1() - 1) * 2, 2);
						}
						avgT = avgT + temp;
					}
					i = i + 4;
				}
			}
		}
		setAvgAndCore();
		} catch (Exception e) {
			log.info("智能模式加载最后一次检测结果异常");
		}
	}
	
	private void setAvgAndCore(){
		sc.setAvgT((avgT/(allPoints == 0 ? 1 : allPoints))/10.0D);
		sc.setCoreT(getCcT()/10.0D);
	}
	
	
	/**
	 * 计算层均温
	 * @author	wc
	 * @date	2017年7月19日
	 * @return	void
	 */
	private void setCores(PointInfo p, int temp){
		//实际所有点数+1
		allPoints = allPoints + 1;
		//层点数+1
		if(p.getXaxis() >= eqment.getMinx() && p.getXaxis() <= eqment.getMaxx()
				&& p.getYaxis() >= eqment.getMiny() && p.getYaxis() <= eqment.getMaxy()
				&& p.getZaxis() >= eqment.getMinz() && p.getZaxis() <= eqment.getMaxz()){
			ccN += 1;
			ccT += temp;
		}
	}
	
	private int getCcT(){
		return ccT/(ccN == 0 ? 1 : ccN);
	}
	
	@Override
	public void run() {
		check();
	}
	
}
