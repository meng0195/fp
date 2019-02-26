package cn.com.bjggs.pest.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Lang;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.pest.domain.GateInfo;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestPoint;
import cn.com.bjggs.pest.domain.PestResults;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.warns.domain.AlarmNotes;

@SuppressWarnings("unchecked")
public class PestUtil {
	
	private static Dao dao;
	
	public static final void clearPestForConfs(){
		for(Map.Entry<String, Map<String, Object>> param : HouseUtil.houseConfs.entrySet()){
			if(param.getValue() != null){
				if(param.getValue().containsKey(TypeHouseConf.PEST.code())){
					param.getValue().remove(TypeHouseConf.PEST.code());
				}
				if(param.getValue().containsKey(TypeHouseConf.PPS.code())){
					param.getValue().remove(TypeHouseConf.PPS.code());
				}
//				if(param.getValue().containsKey(TypeHouseConf.GATE.code())){
//					param.getValue().remove(TypeHouseConf.GATE.code());
//				}
			}
		}
	}
	
	public static final void initLastChecks(final Dao d){
		dao = d;
		Sql sql = Sqls.create("SELECT t.* FROM testpest t INNER JOIN (SELECT MAX(StartTime) as StartTime, HouseNo FROM testpest GROUP BY HouseNo) t2 on t.HouseNo = t2.HouseNo and t.StartTime = t2.StartTime");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(TestPest.class));
		dao.execute(sql);
		List<TestPest> pests = sql.getList(TestPest.class);
		PestResults res;
		for(TestPest pest : pests){
			res = new PestResults();
			res.setTag(TypeCT.END.val());
			res.setHouseNo(pest.getHouseNo());
			res.setPest(pest);
			List<AlarmNotes> ans = dao.query(AlarmNotes.class, Cnd.where("testCode", "=", pest.getId()));
			for (AlarmNotes an : ans) {
				if(an != null){
					if(an.getType1() == Constant.W_PEST_1){
						res.setAn0(an);
					} else if(an.getType1() == Constant.W_PEST_2){
						res.setAn1(an);
					} else if(an.getType1() == Constant.W_DO){
						res.setAn(an);
					}
				}
			}
			CheckPest.lastPests.put(pest.getHouseNo(), res);
		}
	}
	
	/**
	 * 启动时更新测虫配置信息
	 * @author	wc
	 * @date	2017年7月7日
	 * @return	void
	 */
	public static final void initPestConfs(final Dao d) {
		dao = d;
		List<PestInfo> pests = dao.query(PestInfo.class, Cnd.cri().asc("houseNo"));
		Map<String, Object> param;
		if (!Lang.isEmpty(pests)) {
			for(PestInfo p : pests){
				if(HouseUtil.houseConfs.containsKey(p.getHouseNo())){
					param = HouseUtil.houseConfs.get(p.getHouseNo());
					param.put(TypeHouseConf.PEST.code(), p);
				}			
			}
		}
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("houseNo");
		cri.getOrderBy().asc("zaxis");
		cri.getOrderBy().asc("yaxis");
		cri.getOrderBy().asc("xaxis");
		List<PestPoint> pps = dao.query(PestPoint.class, cri);
		List<PestPoint> hp = null;
		if(!Lang.isEmpty(pps)){
			for(PestPoint ppt : pps){
				if(HouseUtil.houseConfs.containsKey(ppt.getHouseNo())){
					hp = null;
					param = HouseUtil.houseConfs.get(ppt.getHouseNo());
					if(param.containsKey(TypeHouseConf.PPS.code())){
						hp = (List<PestPoint>)param.get(TypeHouseConf.PPS.code());
					}
					if(null == hp) hp = new LinkedList<PestPoint>();
					param.put(TypeHouseConf.PPS.code(), hp);
					hp.add(ppt);
				}			
			}
		}
	}
	
	/**
	 * 重置测虫配置信息
	 * @author	wc
	 * @date	2017年7月7日
	 */
	public static final void refresh(String houseNo, PestInfo p) {
		if(HouseUtil.houseConfs.containsKey(houseNo)){
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if(param != null){
				param.put(TypeHouseConf.PEST.code(), p);
			}
		}
	}
	
	public static final void refresh(String houseNo, List<PestPoint> pps){
		if(HouseUtil.houseConfs.containsKey(houseNo)){
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if(param != null){
				param.put(TypeHouseConf.PPS.code(), pps);
			}
		}
	}
	
	public static final void refreshGate(String houseNo, List<GateInfo> gi){
		if(HouseUtil.houseConfs.containsKey(houseNo)){
			Map<String, Object> param = HouseUtil.houseConfs.get(houseNo);
			if(param != null){
				param.put(TypeHouseConf.GATE.code(), gi);
			}
		}
	}
	
}
