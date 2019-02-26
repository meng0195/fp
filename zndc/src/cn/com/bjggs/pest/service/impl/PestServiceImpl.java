package cn.com.bjggs.pest.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.pest.domain.CheckPoints;
import cn.com.bjggs.pest.domain.GateInfos;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestPoint;
import cn.com.bjggs.pest.domain.PestPoints;
import cn.com.bjggs.pest.service.IPestService;
import cn.com.bjggs.pest.util.CheckPest;
import cn.com.bjggs.pest.util.PestUtil;
import cn.com.bjggs.pest.util.SetPestTime;

@IocBean(name = "pestService", args = { "refer:dao" })
public class PestServiceImpl extends BaseServiceImpl implements IPestService{

	public PestServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	private static final ExecutorService TPs = Executors.newFixedThreadPool(PropsUtil.getInteger("pest.check.tps", 5));
	
	/**
	 * 测虫配置更新
	 * @author	wc
	 * @date	2017-08-28
	 */
	public void updateConf(PestInfo p, GateInfos gis){
//		//清空原有选通器数据
//		this.delete(GateInfo.class, Cnd.where("houseNo", "=", p.getHouseNo()));
//		List<GateInfo> gs = gis.getGateInfos();
//		//插入新的选通器数据
//		dao.insert(gs);
		//更新测虫配置
		this.update(p);
//		//更新选通器配置缓存
//		PestUtil.refreshGate(p.getHouseNo(), gs);
		//更新测虫配置缓存
		PestUtil.refresh(p.getHouseNo(), p);
		//更新抽气时间
		SetPestTime.setTime(p);
	}
	
	/**
	 * 更新点排布
	 * @author	wc
	 * @date	2017-08-28
	 */
	public void updatePoint(PestPoints pis){
		//删除原有检测点配置数据
		this.delete(PestPoint.class, Cnd.where("houseNo", "=", pis.getHouseNo()));
		//插入新的检测点数据
		List<PestPoint> ps = pis.getPestPoints();
		dao.insert(ps);
		//更新检测点缓存
		PestUtil.refresh(pis.getHouseNo(), ps);
	}
	
	/**
	 * 开启手动检测
	 * @author	wc
	 * @date	2017-08-28
	 */
	public void checkPoints(CheckPoints cps){
		List<PestPoint> ps = cps.getPoints();
		if(Strings.isBlank(cps.getHouseNo()) || Lang.isEmpty(ps)){
			throw new RuntimeException("请至少选择一个点！");
		}
		//正在检测的仓房不再添加
		if(!CheckPest.checks.containsKey(cps.getHouseNo())){
			CheckPest check = new CheckPest(cps.getHouseNo(), TypeCheck.POINTS, ps, null, null);
			TPs.execute(check);
		}
	}
	
	/**
	 * 多仓
	 */
	public void checkHouses(String[] houses){
		if(houses == null || houses.length == 0){
			throw new RuntimeException("请至少选择一个仓房");
		}
		for(String houseNo : houses){
			//正在检测的仓房不再添加
			if(!CheckPest.checks.containsKey(houseNo)){
				CheckPest check = new CheckPest(houseNo, TypeCheck.HOUSES, null, null, null);
				TPs.execute(check);
			}
		}
	}
}
