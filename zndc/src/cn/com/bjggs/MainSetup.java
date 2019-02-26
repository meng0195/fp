package cn.com.bjggs;

import javax.servlet.ServletContext;

import org.nutz.dao.Dao;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import cn.com.bjggs.basis.util.EquipUtil;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.QuartzUtil;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.util.DicsUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.core.util.TagsUtil;
import cn.com.bjggs.ctr.util.CtrJob;
import cn.com.bjggs.ctr.util.CtrOne;
import cn.com.bjggs.ctr.util.CtrPlan;
import cn.com.bjggs.ctr.util.CtrUtil;
import cn.com.bjggs.ctr.util.ScanARMUtil;
import cn.com.bjggs.ctr.util.SmartScan;
import cn.com.bjggs.ctr.util.SmartUtil;
import cn.com.bjggs.gas.util.GasJob;
import cn.com.bjggs.gas.util.GasOne;
import cn.com.bjggs.gas.util.GasTime;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.msg.util.MsgUtil;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.pest.util.HCMonitorUtil;
import cn.com.bjggs.pest.util.PestJob;
import cn.com.bjggs.pest.util.PestUtil;
import cn.com.bjggs.power.util.PowerUtil;
import cn.com.bjggs.squery.util.QueryUtil;
import cn.com.bjggs.temp.util.ChecksUtil;
import cn.com.bjggs.temp.util.PointUtil;
import cn.com.bjggs.temp.util.TempChange;
import cn.com.bjggs.temp.util.TempJob;
import cn.com.bjggs.temp.util.TempLoop;
import cn.com.bjggs.temp.util.TempOne;
import cn.com.bjggs.temp.util.TempTime;
import cn.com.bjggs.temp.util.TempUtil;
import cn.com.bjggs.warns.util.WarnsUtil;
import cn.com.bjggs.weather.util.WeatherUtil;


public class MainSetup implements Setup {

	@Override
	public void init(NutConfig nc) {

		ServletContext sc = nc.getServletContext();
		sc.setAttribute("CTX_PATH", sc.getContextPath());
		final Dao dao = nc.getIoc().get(Dao.class);
		
		// 数据导入导出实体检测
		TagsUtil.scan(sc);
		// 枚举码表实体检测
		Enums.scan(sc);
		// 全局回话域中的变量为：dics
		DicsUtil.loadDirectory(sc);
		// 字典表检测
		Codes.getSysCodesMap(sc, dao);
		// 更新仓房编号字典
		HouseUtil.initHouseNo(sc, dao);
		// 更新仓房信息
		HouseUtil.initHouseConfs(dao);
		// 初始化测虫配置信息
		//PestUtil.initPestConfs(dao);
		// 初始化预警配置信息
		WarnsUtil.initWarnsConfs(dao);
		// 初始化测温配置信息
		TempUtil.initTempConfs(dao);
		// 初始化测气配置信息
		//GasUtil.initGasConfs(dao);
		// 初始化模式配置信息
		EquipUtil.initEips(dao);
		//初始化最后一次检测
		PestUtil.initLastChecks(dao);
		//将测温信息存入内存
		ChecksUtil.initLastChecks(dao);
		initDao(dao);
		//初始化测虫
//		HCMonitorUtil.initMonitorZd();
		//初始化气象站信息
		//WeatherUtil.initWea(dao);
		//初始化电表信息
		PowerUtil.initWoper(dao);
		//初始化当前设备状态
		//CtrUtil.initCtrLast(dao);
		//初始化ARM相关状态及开启扫描
		ScanARMUtil.initSmartLast(dao);
		//初始化温度点集
		PointUtil.initPoints(dao);
		//初始化最后一次气体检测
		GasUtil.initLastChecks(dao);
		//初始化消息权限
		MsgUtil.initMsgUser(dao);
		//初始化智能模式配置
		SmartUtil.initCons();
		SmartUtil.initModels(dao);
		SmartUtil.initPlans(dao);
		SmartUtil.initOpens(dao);
		//初始化主页对象
		QueryUtil.initQueryMain(dao);
		if(PropsUtil.getInteger("smart.timer.tag", 0) == 1){
			SmartUtil.initTimer();
		}
		//初始化定时任务
		QuartzUtil.initAll(dao);
	}

	@Override
	public void destroy(NutConfig nc) {
		
	}
	
	private void initDao(Dao dao){
		CheckPest.initDao(dao);
		TempJob.initDao(dao);
		CtrOne.initDao(dao);
		TempOne.initDao(dao);
		TempLoop.initDao(dao);
		TempTime.initDao(dao);
		GasOne.initDao(dao);
		GasTime.initDao(dao);
		GasJob.initDao(dao);
		PestJob.initDao(dao);
		CtrJob.initDao(dao);
		CtrPlan.initDao(dao);
		SmartScan.initDao(dao);
		TempChange.initDao(dao);
	}
}
