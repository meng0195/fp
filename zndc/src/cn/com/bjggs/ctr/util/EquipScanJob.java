package cn.com.bjggs.ctr.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.com.bjggs.basis.domain.EquipIps;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.power.enums.TypePowerBoard;

public class EquipScanJob implements Job {

	public void execute(JobExecutionContext jec) throws JobExecutionException {
		ExecutorService eScan = Executors.newFixedThreadPool(PropsUtil.getInteger("ctr.scan.max", 5));
		for(final String houseNo : HouseUtil.houseMap.keySet()){
			eScan.execute(new Runnable() {
				public void run() {
					//如果设备板类型为ARM，启动扫描ARM的方法，否则启动扫描模块板的方法
					EquipIps ips = HouseUtil.get(houseNo, TypeHouseConf.EIPS.code(), EquipIps.class);
					if (ips != null) {
						if (ips.getBoardType() == TypePowerBoard.ARM.val()) {
							ScanARMUtil.refreshSmartStatus(houseNo);
						} else {
							CtrUtil.refreshEquipStatus(houseNo);
						}
					}
					
				}
			});
		}
	}
	
}

