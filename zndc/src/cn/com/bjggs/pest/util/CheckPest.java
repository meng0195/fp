package cn.com.bjggs.pest.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.basis.enums.TypeTestTag;
import cn.com.bjggs.basis.util.DwrUtil;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.core.util.StringUtil;
import cn.com.bjggs.msg.enums.TypeMsg;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestPoint;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.warns.domain.AlarmTT;

public class CheckPest extends Thread{
	
	private static Dao dao;
	
	public static final void initDao(Dao d){
		dao = d;
	}
	
	private static final Log log = Logs.getLog(CheckPest.class);
	
	//标准查询指令，查询一号选通器状态
	private static final byte[] CMD = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x00, 0x03, (byte)0x9C, (byte)0xFF, 0x00, 0x01};
	//摄像头供电指令
	private static final byte[] OPEN_SXT = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x00, 0x06, (byte)0x9C, (byte)0xFF, 0x60, 0x01};
	//摄像头断电指令
	private static final byte[] CLOSE_SXT = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x00, 0x06, (byte)0x9C, (byte)0xFF, 0x60, 0x02};
	//看门狗复位
	private static final byte[] RESET = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, (byte)0x9C, 0x5E, 0x11, 0x02};
	//线程等待时间 毫秒
	private static final int WT = PropsUtil.getInteger("pest.wait.time", 3) * 1000;
	//摄像机打开等待时间
	private static final int WTS = PropsUtil.getInteger("pest.sxt.time", 120) * 1000;
	//最大请求步长
	private static final int MAX_STEPS = PropsUtil.getInteger("pest.steps.max", 60);
	/**
	 * 正在检测的所有仓房信息
	 */
	public static final Map<String, CheckPest> checks = new LinkedHashMap<String, CheckPest>();
	
	public static final Map<String, PestResults> lastPests = new LinkedHashMap<String, PestResults>();
	
	private static enum Steps {B, FW, JC, QC, E;}
	public static enum Controls {B, THIS, NPOINT, E;}
	
	/**
	 * 控制
	 */
	private Controls control = Controls.B;
	private Steps step = Steps.B;
	private int steps = 0;
	private PestResults res;
	private Date testDate;
	
	//测虫配置
	private PestInfo pestInfo;
	//测虫点集
	private List<PestPoint> points;
	//检测结果
	private TestPest pest;
	//当前检测点
	private PestPoint point;
	//剩余点数
	private int diff;
	//检测点数
	private int add;
	//指令
	private byte[] cmd = CMD.clone();
	//下位机返回值
	private byte[] result = new byte[12];
	//选通器异常集
	private int[] gates = new int[7];
	//检测异常
	private AlarmNotes ans = null;
	//预警点集
	private AlarmNotes an0 = null;
	//报警点集
	private AlarmNotes an1 = null;
	//图片路径集合
	private String[] picArrs;
	//视频路径集合
	private String[] videoArrs;
	//本次检测所有虫数用于计算平均虫数
	private int all = 0;
	private int overs = 0;
	private int[] overArr;
	private AlarmTT att;
	private boolean resetTag = false;
	
	public Controls getControl() {
		return control;
	}

	public void setControl(Controls control) {
		this.control = control;
	}
	
	public int getDiff(){
		return this.diff;
	}
	
	private boolean isGates(int gate){
		boolean tag = false;
		for(int i : gates){
			if(i == gate){
				tag = true;
				break;
			}
		}
		return tag;
	}

	private void changeControl(Controls control){
		if(!(this.control == Controls.E)){
			this.control = control;
		}
	}
	public CheckPest(){
		
	}
	
	/**
	 * 初始化当前检测类
	 * @param
	 */
	@SuppressWarnings("unchecked")
	public CheckPest(String houseNo, TypeCheck type, List<PestPoint> points, String planCode, Date testDate){
		testDate = testDate == null ? new Date() : testDate;
		ans = new AlarmNotes(Constant.W_PEST, Constant.W_DO, houseNo, testDate);
		this.pestInfo = HouseUtil.get(houseNo, TypeHouseConf.PEST.code(), PestInfo.class);
		if(type == TypeCheck.POINTS){
			this.points = points;
		} else {
			this.points = (List<PestPoint>)HouseUtil.get(houseNo, TypeHouseConf.PPS.code());
		}
		if(pestInfo == null){
			pest = new TestPest(houseNo, type.val(), testDate, planCode, 0);
			picArrs = new String[0];
			videoArrs = new String[0];
			overArr = new int[0];
			pest.setOvers(new int[0]);
			addError("该仓房检测信息未配置！");
		} else {
			pest = new TestPest(houseNo, type.val(), testDate, planCode, pestInfo.getPointNum());
			picArrs = new String[pestInfo.getPointNum()];
			videoArrs = new String[pestInfo.getPointNum()];
			overArr = new int[pestInfo.getPointNum()];
			pest.setOvers(new int[pestInfo.getPointNum()]);
		}
		res = new PestResults(houseNo, pest, ans);
		res.setTag(TypeCT.WAIT.val());
		lastPests.put(houseNo, res);
		res.setOvers(overArr);
		pest.setPicArrs(picArrs);
		pest.setVideoArrs(videoArrs);
		att = HouseUtil.get(houseNo, TypeHouseConf.WARNS.code(), AlarmTT.class);
	}
	
	public static final String getHouses(){
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		PestResults pr = null;
		for(Map.Entry<String, PestResults> entry : lastPests.entrySet()){
			pr = entry.getValue();
			if(pr == null || pr.getPest() == null || pr.getPest().getTestTag() == 0){
				map.put(entry.getKey(), 0);
			} else if(pr.getTag() == TypeCT.WAIT.val()){
				map.put(entry.getKey(), 5);
			} else {
				map.put(entry.getKey(), pr.getPest().getTestTag());
			}
		}
		//正在检测的仓房
		for(String key : checks.keySet()){
			map.put(key, 4);
		}
		return JsonUtil.toJson(map);
	}
	
	private void sendPest(){
		DwrUtil.changePest(getHouses());
	}
	
	/**
	 * 检测方法 执行检测时调用
	 * @author	wc
	 * @date	2017年5月31日
	 * @return
	 */
	public void check(){
		res.setTag(TypeCT.ING.val());
		while(Controls.E != this.control){
			//应该添加 所有参数验证的过程 这一版 先不添加了
			switch (this.control) {
			case B:
				if(!Lang.isEmpty(points)){
					point = points.get(0);
					res.setPointIng(point.getPoints());
					add = 0;
					diff = points.size();
					//放置到正在检测的仓房里面
					checks.put(pestInfo.getHouseNo(), this);
					sendPest();
					//摄像头供电
					try {
						sendCMD();
					} catch (Exception e) {
						
					}
					try {
						Thread.sleep(WTS);
					} catch (Exception e) {
						log.error("摄像头打开等待线程停止失败！" + e.getMessage());
					}
					//将控制调整为this，执行单点检测
					changeControl(Controls.THIS);
					this.checkOnePoint();
					add++;
					diff--;
				} else {//没有可检测的点将检测
					changeControl(Controls.E);
				}
				break;
			case THIS://这个状态的数据不应该跳出内层循环,如果在这里出现了 程序就写错了,强制修改为结束状态
				changeControl(Controls.E);
				break;
			case NPOINT:
				if(add < points.size()){
					point = points.get(add);
					//TODO 下一点
					if(isGates(point.getGateNo())){
						add++;
						diff--;
						break;
					}
					res.setPointIng(point.getPoints());
					//重置参数
					this.step = Steps.B;
					this.steps = 0;
					this.checkOnePoint();
					add++;
					diff--;
				} else {
					changeControl(Controls.E);
				}
				break;
			case E:
				break;
			}
		}
		//发送摄像头断电指令
		sendCMD();
		//看门狗复位
		resetTag = true;
		sendCMD();
		//检测结束将这个仓房从checks中取出来
		if(pestInfo != null){
			checks.remove(pestInfo.getHouseNo());
		}
		res.setTag(TypeCT.END.val());
		sendPest();
		//插入数据
		setDatas();
	}
	
	/**
	 * 检测一个点
	 * @author	wc
	 * @date	2017年4月26日
	 * @return
	 */
	private boolean reqTag = true;
	private final void checkOnePoint(){
		while(Steps.E != this.step){
			this.sendCMD();
			if(this.reqTag){
				this.doReq();
			}
			try { 
				Thread.sleep(WT); // 每隔3s调用一次socket
			} catch (InterruptedException e) {
				changeControl(Controls.E);
			}
		}
	}
	/**
	 * 根据选通器编号修改查询指令中选通器对应部分指令
	 * @author	wc
	 * @date	2017年4月25日
	 * @return	void
	 */
	private void changeCmd(){
		if(control == Controls.B){
			cmd = OPEN_SXT.clone();
			return;
		}
		if(control == Controls.E){
			if(resetTag){
				cmd = RESET.clone();
			} else {
				cmd = CLOSE_SXT.clone();
			}
			return;
		}
		byte[] c = CMD.clone();
		//将控制状态改为this
		changeControl(Controls.THIS);
		//确定当前选通器
		switch(point.getGateNo()){
			case 1 : c[8] = (byte)0x9C; c[9] = (byte)0xFF; break;
			case 2 : c[8] = (byte)0x9D; c[9] = 0x00; break;
			case 3 : c[8] = (byte)0x9D; c[9] = 0x01; break;
			case 4 : c[8] = (byte)0x9D; c[9] = 0x02; break;
			case 5 : c[8] = (byte)0x9D; c[9] = 0x03; break;
			case 6 : c[8] = (byte)0x9D; c[9] = 0x04; break;
			case 7 : c[8] = (byte)0x9D; c[9] = 0x05; break;
		}
		//根据当前步骤修改指令
		switch(this.step){
			//检测最初状态，发送查询指令，需要处理返回的流（默认处理）
			case B :
				break;
			//当前状态为需要fw操作状态
			case FW : 
				//当前状态第一次发送的指令为复位指令,且不需要处理返回流
				if(this.steps == 0){
					c[7] = 0x06;
					c[10] = 0x51;   	//复位事件	
					c[11] = 0x01;
				}
				break;
			case JC :
				//当前状态第一次发送的指令为选点指令,且不需要处理返回流
				if(this.steps == 0){
					c[7] = 0x06;
					c[10] = (byte)point.getPointNo(); 			//赋值检测点
					c[11] = 0x01;
				}
				break;
			case QC : 
			case E : 
				break;
		}
		this.cmd = c;
	}
	private int timeOutStep = 0;
	//流相关变量 辅助 sendCMD()方法执行;
	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	//对下位机发送指令
	private final void sendCMD(){
		try {
			timeOutStep++;
			//修改指令
			changeCmd();
			//修改指令
			socket = new Socket();
			socket.connect(new InetSocketAddress(this.pestInfo.getCtrIp(), this.pestInfo.getCtrPort()), 2500);
			//发送指令
			os = socket.getOutputStream();
			os.write(cmd);
			
			//处理返回流
			is = socket.getInputStream();        																		// 获取一个输入流，接收服务端的信息
			byte[] bytes = new byte[1];
			int i = 0;
			while (is.read(bytes) != -1) {
				if(i < 12){
					result[i] = bytes[0];
					i++;
				}
				if(is.available() == 0){
					break;
				}
			}
			this.reqTag = true;
			timeOutStep = 0;
		} catch (Exception e){
			log.info("向下位机发送指令失败：" + e.getMessage());
			this.reqTag = false;
			if(timeOutStep > 8){
				changeControl(Controls.E);
				this.step = Steps.E;
				addError("通讯失败：请检查网络是否畅通、设备是否供电！");
			}
		} finally{
			try{
				if (this.os != null) {
					this.os.flush();
					this.os.close();
				}
				if (this.is != null) {
					this.is.close();
				}
				if (this.socket != null){
					this.socket.close();
				}
			} catch (Exception ee) {
			} finally {
				this.steps++;
			}
		}
	}
	//异常判断
	private void checkE9E10(byte e9, byte e10){
		if (0x00 == e9) {//没有选通器  将控制调整为下一个点
			setBadGate();
			changeControl(Controls.NPOINT);
			this.addError(point.getGateNo() + "号选通器异常！");
			this.step = Steps.E;
		} else if ((byte)0x81 == e9 && 0x01 == e10) {//选点电机异常 将控制调整为下一个选通器
			setBadGate();
			changeControl(Controls.NPOINT);
			this.addError(point.getGateNo() + "号选点电机异常！");
			this.step = Steps.E;
		} else if ((byte)0x82 == e9 && 0x01 == e10) {//移位电机异常 将控制调整为下一个选通器
			setBadGate();
			changeControl(Controls.NPOINT);
			this.addError(point.getGateNo() + "号移位电机异常！");
			this.step = Steps.E;
		} else if ((byte)0x83 == e9 && 0x01 == e10) {//空气泵异常 将控制调整为结束
			changeControl(Controls.E);
			this.addError("空气泵异常！");
			this.step = Steps.E;
		} else if ((byte)0x84 == e9 && 0x01 == e10) {//清虫电机异常 将控制调整为结束
			changeControl(Controls.E);
			this.addError("清虫电机异常！");
			this.step = Steps.E;
		}
		if(this.steps >= MAX_STEPS){
			if(0x01 == e10){
				this.addError("选点超时！");
			} else if(0x02 == e10){
				this.addError("计数超时！");
			} else if(0x04 == e10){
				this.addError("清虫超时！");
			} else if(0x05 == e10){
				this.addError("归位超时！");
			} else if(0x51 == e9 && 0x00 == e10){
				this.addError("未执行选点！");
			} else {
				this.addError("未知错误！");
			}
			setBadGate();
			changeControl(Controls.NPOINT);
			this.step = Steps.E;
		}
	}
	//添加异常选通器
	public void setBadGate(){
		if(point.getGateNo() < 8 && point.getGateNo() > 0){
			gates[point.getGateNo()-1] = point.getGateNo();
		}
	}
	
	private int recSteps = 0;
	//处理返回流
	private final void doReq(){
		if (this.result != null && this.result.length >= 12) {
			byte e9 = this.result[9];
			byte e10 = this.result[10];
			byte e11 = this.result[11];
			switch (this.step) {
			//当前状态为开始
			case B :
				//选通器
				if (0x51 == e9) {
					//复位完成 可以执行选点操作
					if(0x00 == e10){
						this.steps = 0;//当前步数清零
						this.step = Steps.JC;
					}
				} else if(0x00 == e9){//没有选通器将
					//填写选通器异常
					setBadGate();
					//将控制调整为 下一点
					changeControl(Controls.NPOINT);
					this.addError(point.getGateNo() + "号选通器异常");
					this.step = Steps.E;
				} else {//执行复位
					this.steps = 0;//当前步数清零
					this.step = Steps.FW;
				}
				break;
			case FW :
				//选通器
				if (0x51 == e9 && 0x00 == e10) {//复位完成 可以执行选点操作
					this.steps = 0;//当前步数清零
					this.step = Steps.JC;
					this.recSteps = 0;
				} else if(this.steps == 1 && (cmd[10] != e10 || cmd[11] != e11) && this.recSteps < 5){
					this.steps = 0;//当前步数清零
					this.recSteps++;
				} else {
					this.checkE9E10(e9, e10);
				}
				break;
			case JC :
				if (0x21 <= e9 && 0x40 >= e9){//执行计数，保存方法 稍后 在写
					this.steps = 0;//当前步数清零
					if(pestInfo.getPointNum() < point.getPoints()) break;
					pest.getPset()[point.getPoints()-1] = e10;
					//获取图片和视频
					getSrc();
					pest.getOvers()[point.getPoints()-1] = 1;
					count(e10);
					if(att != null){
						doWarns(e10);
					}
					this.step = Steps.QC;
					this.recSteps = 0;
				} else if(this.steps == 1 && (cmd[10] != e10 || cmd[11] != e11) && this.recSteps < 5){
					this.steps = 0;//当前步数清零
					this.recSteps++;
				} else {
					this.checkE9E10(e9, e10);
				}
				break;
			case QC :
				//选通器
				if (0x51 == e9 && 0x00 == e10) {
					changeControl(Controls.NPOINT);
					this.step = Steps.E;
				} else if(this.steps > 55){
					this.steps = 0;//当前步数清零
					this.step = Steps.E;
					this.control = Controls.NPOINT;
				} else {//没有选通器将
					this.checkE9E10(e9, e10);
				}
				break;
			case E : //一般不会出现这个状态
				break;
			}
		}
	}
	//计算最大值最小值平均值
	private void count(int num){
		all += num;
		overs += 1;
		overArr[point.getPoints() - 1] = 1;
		pest.setMinNum(Math.min(num, pest.getMinNum()));
		pest.setMaxNum(Math.max(num, pest.getMaxNum()));
		pest.setAvgNum(all/overs);
	}
	
	private void doWarns(byte v){
		if(att.isWarns(Constant.IDX_P_0) && (v & 0xFF) > att.getThd(Constant.IDX_P_0)){
			if(an0 == null) an0 = new AlarmNotes(Constant.W_PEST, Constant.W_PEST_1, pestInfo.getHouseNo(), testDate);
			an0.setNums(an0.getNums() + 1);
			pest.getWarns()[point.getPoints()-1] += WarnType.WARN_1;
		}
		if(att.isWarns(Constant.IDX_P_1) && (v & 0xFF) > att.getThd(Constant.IDX_P_1)){
			if(an1 == null) an1 = new AlarmNotes(Constant.W_PEST, Constant.W_PEST_2, pestInfo.getHouseNo(), testDate);
			an1.setNums(an1.getNums() + 1);
			pest.getWarns()[point.getPoints()-1] += WarnType.WARN_2;
		}
	}
	
	//插入错误，会根据当前检测对象的基本数据进行插入
	private void addError(String msg){
		ans.setNums(ans.getNums() + 1);
		pest.setWarnsStr(pest.getWarnsStr() == null ? "" : pest.getWarnsStr() + msg + "|");	
	}
	
	//获取图片路径和视频路径
//	private void getSrc(){
//		try {
//			String src = HCMonitorUtil.dm.GETNVRPICIMG(pestInfo.getCamIp(), pestInfo.getCamPort(), pestInfo.getCamUser(), pestInfo.getCamPw(), HCMonitorUtil.NVR, HCMonitorUtil.WAY + pestInfo.getCamWay(), HCMonitorUtil.TIME, HCMonitorUtil.PIC_SRC);
//			String[] srcs = src.split("\\|FG\\|");
//			String v = srcs[0].substring(0, srcs[0].length() - 4) + ".swf";
//			videoArrs[point.getPoints() - 1] = v.substring(v.indexOf("monitorzd"), v.length());
//			picArrs[point.getPoints() - 1] = srcs[1].substring(srcs[1].indexOf("monitorzd"), srcs[1].length());
//		} catch (Exception e) {
//			log.error("图片/录像抓取失败：" + e.getMessage());
//			addError("图片/录像抓取失败：" + e.getMessage());
//		}
//	}
	
	private void getSrc(){
	try {
		String src = HCMonitorUtil.dm.GETNVRPICIMG("192.168.30.58", 34567, "admin", "", 1, 33, 10, "D:/monitorzd/");
		String[] srcs = src.split("\\|FG\\|");
		String v = srcs[0].substring(0, srcs[0].length() - 4) + ".swf";
		videoArrs[point.getPoints() - 1] = v.substring(v.indexOf("monitorzd"), v.length());
		picArrs[point.getPoints() - 1] = srcs[1].substring(srcs[1].indexOf("monitorzd"), srcs[1].length());
	} catch (Exception e) {
		log.error("图片/录像抓取失败：" + e.getMessage());
		addError("图片/录像抓取失败：" + e.getMessage());
	}
}
	
	public static void main(String[] args) {
		CheckPest cp = new CheckPest();
		cp.getSrc();
		
	}
	private void setDatas(){
		try {
			//插入检测记录
			pest.setVideos(StringUtil.join(videoArrs, ","));
			pest.setPics(StringUtil.join(picArrs, ","));
			pest.setEndTime(Times.now());
			pest.setTestTag(TypeTestTag.NORMAL.val());
			List<AlarmNotes> as = new LinkedList<AlarmNotes>();
			//插入检测异常
			if(ans != null && ans.getNums() > 0){
				ans.setTestCode(pest.getId());
				as.add(ans);
				pest.setTestTag(TypeTestTag.ABNORMAL.val());
				res.setAn(ans);
				MsgUtil.insertMsg(pest.getHouseNo(), "虫情检测异常", TypeMsg.WARN.val(), Constant.W_PEST, pest.getId());
			}
			if(an0 != null && an0.getNums() > 0){
				an0.setTestCode(pest.getId());
				as.add(an0);
				pest.setTestTag(TypeTestTag.ABNORMAL.val());
				res.setAn0(an0);
				MsgUtil.insertMsg(pest.getHouseNo(), "虫情预警：预警点数" + an0.getNums(), TypeMsg.WARN.val(), Constant.W_PEST, pest.getId());
			}
			if(an1 != null && an1.getNums() > 0){
				an1.setTestCode(pest.getId());
				as.add(an1);
				pest.setTestTag(TypeTestTag.ABNORMAL.val());
				res.setAn1(an1);
				MsgUtil.insertMsg(pest.getHouseNo(), "虫情报警：报警点数" + an1.getNums(), TypeMsg.WARN.val(), Constant.W_PEST, pest.getId());
			}
			dao.insert(pest);
			dao.insert(as);
		} catch (Exception e) {
			log.error("数据插入异常：" + e.getMessage());
		}
	}
	
	@Override
	public void run() {
		check();
	}
}
