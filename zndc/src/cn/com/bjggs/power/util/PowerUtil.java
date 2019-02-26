package cn.com.bjggs.power.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.ctr.util.GainArmPower;
import cn.com.bjggs.power.domain.CtrStatus;
import cn.com.bjggs.power.domain.PowerInfo;
import cn.com.bjggs.power.domain.PowerNotes;
import cn.com.bjggs.power.enums.TypePowerBoard;

public class PowerUtil {
	
	private static Dao dao;
	
	public static final Map<String, CtrStatus> last = new LinkedHashMap<String, CtrStatus>();
	
	public static final Map<String, PowerInfo> powerInfo = new LinkedHashMap<String, PowerInfo>();
	
	public static final Map<String, PowerNotes> powerNotes = new LinkedHashMap<String, PowerNotes>();
	
	private static final Log log = Logs.getLog(PowerUtil.class); 
	/**
	 * 初始化电表信息，添加定时任务
	 * @author	yucy
	 * @date	2017年10月30日
	 * @return	void
	 */
	public static final void initWoper(final Dao d) {
		dao = d;
		//将配置信息放入内存
		List<PowerInfo> powers = dao.query(PowerInfo.class, Cnd.cri().asc("houseNo"));
		if (!Lang.isEmpty(powers)) {
			for (PowerInfo p : powers) {
				powerInfo.put(p.getHouseNo(), p);
			}
		}
		
		//将能耗记录信息放入内存
		for (String houseNo : HouseUtil.houseMap.keySet()) {
			Criteria cri = Cnd.cri();
			cri.where().andEquals("houseNo", houseNo);
			cri.getOrderBy().desc("checkTime");
			PowerNotes pn = dao.fetch(PowerNotes.class, cri);
			powerNotes.put(houseNo, pn);
		}
		
		
		//添加定时任务，定时扫描总能耗
		String timer = "0 0 0/" + PropsUtil.getInteger("power.scan.time", 12) + " * * ?";
//		String timer = "0 0/" + PropsUtil.getInteger("power.scan.time", 12) + " * * * ?";
		QuartzUtil.addJob("PowerJob", PowerJob.class, timer);
	}
	
	
	/**
	 * 重置电表配置信息
	 * @author	yucy
	 * @date	2017年10月30日
	 */
	public static final void refresh(PowerInfo p) {
		if (p != null) {
			powerInfo.put(p.getHouseNo(), p);
		}
	}
	
	public static final void savePower(){
		
		for (String houseNo : HouseUtil.houseMap.keySet()) {
			//定点查询所有仓的总能耗
			PowerInfo p = PowerUtil.powerInfo.get(houseNo);
			PowerNotes beforePns = PowerUtil.powerNotes.get(houseNo);
			PowerNotes laterPns = null;
			
			if (p != null) {
				double power = 0;
				try {
					if (p.getBoardType() == TypePowerBoard.ARM.val()) {//如果板子类型是ARM
						power = GainArmPower.getArmDate(p.getPowerIp());
					} else {
						PowerTCP pt = new PowerTCP(p.getPowerIp(), p.getPowerPort(), 0);
						byte[] ps = pt.getDatas();
						power = (((ps[22] & 0xFF) << 24) + ((ps[23] & 0xFF) << 16) + ((ps[24] & 0xFF) << 8) + (ps[25] & 0xFF))/1000.000D;
					}
					if (beforePns == null) {
						laterPns = new PowerNotes(p, power, 0);
					} else {
						laterPns = new PowerNotes(p, beforePns.getCheckTime(), power, beforePns.getCheckPower());
					}
					dao.insert(laterPns);
					powerNotes.put(houseNo, laterPns);
				} catch (Exception e) {
					log.info(e.getMessage());
				}
			}
		}
		
	}
	
	//获取后一天
	public static String getLastDay(String date1){
		String dateStr = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();  
			c.setTime(sdf.parse(date1));  
			c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
			Date tomorrow = c.getTime();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			dateStr = sdf1.format(tomorrow);
		} catch (ParseException e) {
			log.info("日期转换失败");
		}
		return dateStr;//这就是往后一天的时间
	}
	
}
