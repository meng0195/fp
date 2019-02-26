package cn.com.bjggs.camera.service.impl;

import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.com.bjggs.camera.domain.CameraInfo;
import cn.com.bjggs.camera.service.ICamService;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;

@IocBean(name = "camService", args = { "refer:dao" })
public class CamServiceImpl extends BaseServiceImpl implements ICamService{

	public CamServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void updateStatus(String id, int status){
		dao.update(CameraInfo.class, Chain.make("status", status == TypeFlag.YES.val() ? TypeFlag.YES.val() : TypeFlag.NO.val()), Cnd.where("ID", "=", id));
	}

	@Override
	public int delete(String id) {
		int num = dao.delete(CameraInfo.class, id);
		return num;
	}
	
	@Override
	public int deleteAll(String[] ids) {
		int num = 0;
		for(String id : ids){
			num += delete(id);
		}
		return num;
	}
	
		/**
		 * 设备排布保存
		 */
		public void saveRank(double[] xaxis, double[] yaxis){
			Criteria cri = Cnd.cri();
			cri.getOrderBy().asc("ID");
			List<CameraInfo> cam = dao.query(CameraInfo.class, cri);
			if(cam == null || xaxis == null || yaxis == null || cam.size() != xaxis.length || cam.size() != yaxis.length){
				throw new RuntimeException("设备排布参数不正确，请检查是否有新的设备录入！");
			} else {
				int index = 0;
				for(CameraInfo c : cam){
					c.setXaxis(xaxis[index]);
					c.setYaxis(yaxis[index]);
					index++;
				}
				this.delete(CameraInfo.class, Cnd.cri());
				dao.insert(cam);
			}
		}
}
