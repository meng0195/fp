package cn.com.bjggs.temp.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.view.ViewHouseInfo;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.system.domain.SysUser;
import cn.com.bjggs.temp.domain.ChecksLoop;
import cn.com.bjggs.temp.domain.ChecksOne;
import cn.com.bjggs.temp.domain.LoopConf;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempBoaInfos;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.enums.TypePointLeve;
import cn.com.bjggs.temp.service.ITempService;
import cn.com.bjggs.temp.util.AreaUtil;
import cn.com.bjggs.temp.util.CheckFactory;
import cn.com.bjggs.temp.util.ChecksUtil;
import cn.com.bjggs.temp.util.JointUtil;
import cn.com.bjggs.temp.util.PointUtil;
import cn.com.bjggs.temp.util.TempChange;
import cn.com.bjggs.temp.util.TempRealUtil;
import cn.com.bjggs.temp.util.TempUtil;

@IocBean(name = "tempService", args = { "refer:dao" })
public class TempServiceImpl extends BaseServiceImpl implements ITempService{
	
	public TempServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void save(StoreHouse s, TempBoaInfos tb, TempInfo t, String houseNo, int tag){
		if(tag == 0){
			//删除温度点信息表,批量添加
			delete(PointInfo.class, Cnd.where("houseNo", "=", houseNo));
			dao.insert(PointUtil.getPoints(s, t));
		}
		//删除测温板配置表信息,批量添加
		delete(TempBoaInfo.class, Cnd.where("houseNo", "=", houseNo));
		dao.insert(tb.getTbs());
		//更新
		update(t);	//是继承的BaseServiceImpl中封装的update
		TempUtil.refresh(houseNo, t);
		TempUtil.refresh(houseNo, tb.getTbs());
	}
	
	/**
	 * 获取仓房信息和线缆点集合
	 * @author	wc
	 * @date	2017-07-09
	 */
	public Record getHouseAndPoints(String houseNo){
		Record rs = new Record();
		if(Strings.isBlank(houseNo)) throw new RuntimeException("仓房编号错误，请选择一个仓房进行查看！");
		//仓房基本信息
		rs.set("house", dao.fetch(ViewHouseInfo.class, Cnd.where("houseNo", "=", houseNo)));
		TempInfo eqment = dao.fetch(TempInfo.class, Cnd.where("houseNo", "=", houseNo));
		rs.set("eqment", eqment);
		//获取所有点
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo);
		cri.getOrderBy().asc("yaxis");
		cri.getOrderBy().asc("xaxis");
		cri.getOrderBy().asc("zaxis");
		List<PointInfo> list = dao.query(PointInfo.class, cri);
		
		List<List<List<PointInfo>>> points = new LinkedList<List<List<PointInfo>>>();
		List<List<PointInfo>> xps;
		List<PointInfo> ps;
		int[] x = {-1, -1};
		int[] y = {-1, -1};
		//拼装参数
		for(PointInfo p : list){
			if(y[1] == p.getYaxis()){
				xps = points.get(y[0]);
			} else {
				//新建子集
				xps = new LinkedList<List<PointInfo>>();
				points.add(xps);
				//重置子集参数
				x[0] = -1;
				x[1] = -1;
				y[0] = (y[0] == -1) ? 0 : (y[0] + 1);
				y[1] = p.getYaxis();
 			}
			if(x[1] == p.getXaxis()){
				ps = xps.get(x[0]);
			} else {
				ps = new LinkedList<PointInfo>();
				xps.add(ps);
				x[0] = (x[0] == -1) ? 0 : (x[0] + 1);
				x[1] = p.getXaxis();
			}
			ps.add(p);
		}
		rs.put("html", eqment == null ? "" : PointUtil.getSortMap(points, eqment));
		rs.put("points", points);
		return rs;
	}

	public String getChecksOne(SysUser user){
		//获取手动检测状态
		Criteria cri = Cnd.cri();
		cri.where().andEquals("userCode", user.getId());
		cri.getOrderBy().asc("houseNo");
		List<ChecksOne> list = dao.query(ChecksOne.class, cri);
		return JointUtil.initOneHtml(list);
	}

	public String getChecksOneConf(SysUser user){
		String[] hs = new String[HouseUtil.houseMap.keySet().size()];
		if(Lang.isEmptyArray(user.getHouses())){
			HouseUtil.houseMap.keySet().toArray(hs);
		} else {
			hs = user.getHouses();
		}
		Criteria cri = Cnd.cri();
		cri.where().andEquals("userCode", user.getId());
		cri.getOrderBy().asc("houseNo");
		List<ChecksOne> cos = dao.query(ChecksOne.class, cri);
		return HouseUtil.getChecksHtml(hs, cos);
	}
	
	public void oneSave(SysUser user, String[] houseNo){
		//修改最大检测数量
		List<ChecksOne> list = new LinkedList<ChecksOne>();
		for(String s : houseNo){
			list.add(new ChecksOne(s, user.getId()));
		}
		this.delete(ChecksOne.class, Cnd.where("userCode", "=", user.getId()));
		dao.insert(list);
	}
	
	private static final ExecutorService esOne = Executors.newFixedThreadPool(PropsUtil.getInteger("temp.max.one", 5));
	public void oneStart(SysUser user, int reportFlag){
		//根据当前用户获取相应的手动检测列表
		List<ChecksOne> list = dao.query(ChecksOne.class, Cnd.where("userCode", "=", user.getId()));
		//验证检测列表
		if(list == null || list.size() == 0) throw new RuntimeException("检测列表为空！");
		for(ChecksOne c : list){
			try { Thread.sleep(50); 
//			if(!ChecksUtil.ONES.contains(c.getHouseNo())){
				ChecksUtil.ONES.add(c.getHouseNo());
				Thread checks = CheckFactory.createChecking(TypeCheck.ONE, c.getHouseNo(), null, null, reportFlag);
				esOne.execute(checks);
			} catch (Exception e) {
				log.info(e.getMessage());
				throw new RuntimeException("加载检测列表异常！");
			}
//			}
		}
	}
	
	private static final int maxLoop = PropsUtil.getInteger("temp.max.loop", 5);
	private static final ExecutorService esLoop = Executors.newFixedThreadPool(maxLoop);
	public void loopStart(SysUser user){
		//根据当前用户获取相应的手动检测列表
		final List<ChecksLoop> list = dao.query(ChecksLoop.class, Cnd.where("userCode", "=", user.getId()));
		//验证检测列表
		if(list == null || list.size() == 0) throw new RuntimeException("检测列表为空！");
		//如果循环检测已经开始，添加仓房就可以了
		if(ChecksUtil.loopTag){
			for(ChecksLoop c : list){
				if(!ChecksUtil.LOOPINGS.contains(c.getHouseNo()) && !ChecksUtil.LOOPS.contains(c.getHouseNo())){
					ChecksUtil.LOOPS.offer(c.getHouseNo());
					ChecksUtil.SAVE_NUM.put(c.getHouseNo(), 0);
				}
			}
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					ChecksUtil.loopTag = true;
					for(final ChecksLoop c : list){
						ChecksUtil.LOOPS.offer(c.getHouseNo());
						ChecksUtil.SAVE_NUM.put(c.getHouseNo(), 0);
					}
					while(ChecksUtil.loopTag){
						if(ChecksUtil.LOOPINGS.size() < maxLoop && ChecksUtil.LOOPS.size() > 0){
							Thread checks = CheckFactory.createChecking(TypeCheck.LOOP, ChecksUtil.LOOPS.poll(), null, null, 0);
							esLoop.execute(checks);
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
					}
				}
			}).start();
		}
	}
	
	
	/**
	 * 获取循环检测配置信息
	 * @author	wc
	 * @date	2017-07-24
	 */
	public LoopConf getLoopConf(SysUser user){
		String[] hs = new String[HouseUtil.houseMap.keySet().size()];
		if(Lang.isEmptyArray(user.getHouses())){
			HouseUtil.houseMap.keySet().toArray(hs);
		} else {
			hs = user.getHouses();
		}
		Criteria cri = Cnd.cri();
		cri.where().andEquals("userCode", user.getId());
		cri.getOrderBy().asc("houseNo");
		List<ChecksLoop> los = dao.query(ChecksLoop.class, cri);
		LoopConf lc = new LoopConf();
		lc.setCheckLoops(new LinkedList<ChecksLoop>(los));
		lc.setClsStr(HouseUtil.getChecksHtml(hs, los));
		return lc;
	}
	
	/**
	 * 保存循环检测信息
	 * @author	yucy
	 * @date	2017-08-30
	 */
	public void loopSave(SysUser user, String[] houseNos){
		List<ChecksLoop> list = new LinkedList<ChecksLoop>();
		for(String hosueNo : houseNos){
			list.add(new ChecksLoop(hosueNo, user.getId()));
		}
		this.delete(ChecksLoop.class, Cnd.where("userCode", "=", user.getId()));
		dao.insert(list);
	}
	
	/**
	 * 查询信息,显示实时数据
	 * @param id
	 * @return
	 */
	public String showRealData(String houseNo){
		TempInfo ti = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		//获取带有层次结构的点位置信息
		List<List<List<PointInfo>>> list = PointUtil.getPointLevel(houseNo);
		//获取从板子上取到的温度,byte[]型
		byte[] datas = TempRealUtil.getRealTemp(houseNo);
		//将这些参数封装成html格式返回
		String html = TempRealUtil.getSheetReal(list, ti, datas);
		return html;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Record getHouseAndArea(String houseNo) {
		Record rs = new Record();
		if(Strings.isBlank(houseNo)) throw new RuntimeException("仓房编号错误，请选择一个仓房进行查看！");
		//仓房基本信息
		ViewHouseInfo sh = dao.fetch(ViewHouseInfo.class, Cnd.where("houseNo", "=", houseNo));
		rs.set("house", sh);
		TempInfo eqment = dao.fetch(TempInfo.class, Cnd.where("houseNo", "=", houseNo));
		if(!Lang.isEmpty(eqment.getLeveStrMaps())){
			rs.set("t1", ((ArrayList<String>)eqment.getLeveStrMaps().get("1")).toArray());
			rs.set("t2", ((ArrayList<String>)eqment.getLeveStrMaps().get("2")).toArray());
			rs.set("t3", ((ArrayList<String>)eqment.getLeveStrMaps().get("3")).toArray());
			rs.set("t4", ((ArrayList<String>)eqment.getLeveStrMaps().get("4")).toArray());
		}
		rs.set("eqment", eqment);
		//获取所有点
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo);
		cri.getOrderBy().asc("yaxis");
		cri.getOrderBy().asc("xaxis");
		cri.getOrderBy().asc("zaxis");
		List<PointInfo> list = dao.query(PointInfo.class, cri);
		
		List<List<List<PointInfo>>> points = new LinkedList<List<List<PointInfo>>>();
		List<List<PointInfo>> xps;
		List<PointInfo> ps;
		int[] x = {-1, -1};
		int[] y = {-1, -1};
		//拼装参数
		for(PointInfo p : list){
			if(y[1] == p.getYaxis()){
				xps = points.get(y[0]);
			} else {
				//新建子集
				xps = new LinkedList<List<PointInfo>>();
				points.add(xps);
				//重置子集参数
				x[0] = -1;
				x[1] = -1;
				y[0] = (y[0] == -1) ? 0 : (y[0] + 1);
				y[1] = p.getYaxis();
 			}
			if(x[1] == p.getXaxis()){
				ps = xps.get(x[0]);
			} else {
				ps = new LinkedList<PointInfo>();
				xps.add(ps);
				x[0] = (x[0] == -1) ? 0 : (x[0] + 1);
				x[1] = p.getXaxis();
			}
			ps.add(p);
		}
		rs.put("html", eqment == null ? "" : AreaUtil.getSortMap(points, eqment, sh.getHouseType()));
		rs.put("points", points);
		rs.put("houseType", sh.getHouseType());
		return rs;
	}

	@Override
	public void updateArea(String houseNo, String cable, String area, String[] t1, String[] t2, String[] t3, String[] t4){
		String[] strCable = cable.split(",");
		String[] strArea = area.split(",");
		if (strCable.length == strArea.length) {
			for (int i = 0; i < strCable.length; i++) {
				dao.update(PointInfo.class, Chain.make("area", strArea[i]), Cnd.where("houseNo", "=", houseNo).and("cableNo1", "=", strCable[i]));
			}
		}
		TempInfo ti = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("1", t1);
		map.put("2", t2);
		map.put("3", t3);
		map.put("4", t4);
		String leveStr = JsonUtil.toJson(map);
		ti.setLeveStr(leveStr);
		dao.update(TempInfo.class, Chain.make("leveStr", leveStr), Cnd.where("id", "=", ti.getId()));
		String[] zaxis;
		//TODO 验证数组是否为null 是都相等 有数组越界的风险
		StoreHouse h = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		if(h.getHouseType() == 1){
			zaxis = t1[0].split("\\.");
			dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.A.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis));
			zaxis = t2[0].split("\\.");
			dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.B.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis));
			zaxis = t3[0].split("\\.");
			dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.C.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis));
			zaxis = t4[0].split("\\.");
			dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.D.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis));
		} else {
			for(int i = 0; i < t1.length; i++){
				zaxis = t1[i].split("\\.");
				dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.A.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis).and("yaxis", "=", i));
				zaxis = t2[i].split("\\.");
				dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.B.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis).and("yaxis", "=", i));
				zaxis = t3[i].split("\\.");
				dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.C.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis).and("yaxis", "=", i));
				zaxis = t4[i].split("\\.");
				dao.update(PointInfo.class, Chain.make("leve", TypePointLeve.D.val()), Cnd.where("houseNo", "=", houseNo).and("zaxis", "in", zaxis).and("yaxis", "=", i));
			}
		}
	}
	
	public String getChange(String id){
		TestData td = dao.fetch(TestData.class, Cnd.where("id", "=", id));
		byte[] temps = td.getTset();
		StringBuffer sb = new StringBuffer("<table layoutH=\"35\" class=\"edit temp-sheet-change\" >");
		
		TempInfo ti = HouseUtil.get(td.getHouseNo(), TypeHouseConf.TEMP.code(), TempInfo.class);
		
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", td.getHouseNo());
		cri.getOrderBy().asc("yaxis");
		cri.getOrderBy().asc("xaxis");
		cri.getOrderBy().asc("zaxis");
		List<PointInfo> list = dao.query(PointInfo.class, cri);
		
		List<List<List<PointInfo>>> points = new LinkedList<List<List<PointInfo>>>();
		List<List<PointInfo>> pss;
		List<PointInfo> ps;
		int[] x1 = {-1, -1};
		int[] y1 = {-1, -1};
		//拼装参数
		for(PointInfo p : list){
			if(y1[1] == p.getYaxis()){
				pss = points.get(y1[0]);
			} else {
				//新建子集
				pss = new LinkedList<List<PointInfo>>();
				points.add(pss);
				//重置子集参数
				x1[0] = -1;
				x1[1] = -1;
				y1[0] = (y1[0] == -1) ? 0 : (y1[0] + 1);
				y1[1] = p.getYaxis();
 			}
			if(x1[1] == p.getXaxis()){
				ps = pss.get(x1[0]);
			} else {
				ps = new LinkedList<PointInfo>();
				pss.add(ps);
				x1[0] = (x1[0] == -1) ? 0 : (x1[0] + 1);
				x1[1] = p.getXaxis();
			}
			ps.add(p);
		}
		
		PointInfo p;
		int len = 0;
		int maxZ = ti.getMaxLNum();
		
		Map<String, StringBuffer> trs = new LinkedHashMap<String, StringBuffer>();
		StringBuffer sbi;
		double temp;
		//生成每行
		for(int x = 0; x < points.size(); x++){
			//全局变量 x 轴方向标记
			int xTag = 0;
			pss = points.get(x);
			for(int z = 0; z <= maxZ; z++){
				trs.put("x" + x + "z" + z, new StringBuffer());
			}
			for(int i = 0; i < pss.size(); i++){
				ps = pss.get(i);
				p = ps.get(0);
				//当每个点的x轴标记大于 X轴标记时 表示此行方向上有空列 添加空列
				if(p.getXaxis() > xTag){
					len = p.getXaxis() - xTag;
					for(int j = 0; j < len; j++){
						for(int k = 0; k <= maxZ; k++){
							sbi = trs.get("x" + x + "z" + k);
							if(xTag == 0){
								if(k == 0){
									sbi.append("<th width=\"60\" style=\"text-align:center\">层\\缆</th>");
								} else {
									sbi.append("<td>S")
										.append(k)
										.append("</td>");
								}
							}
							if(k == 0){
								sbi.append("<td></td>");
							} else {
								sbi.append("<td></td>");
							}
						}
						xTag += 1;
					}
				}
				//没有空列的
				//处理title
				sbi = trs.get("x" + x + "z" + 0);
				if (xTag == 0) {
					sbi.append("<th width=\"60\" style=\"text-align:center;\">层\\缆</th>");
				}
				sbi.append("<th style=\"text-align:center;width:60px;\">C").append(p.getCableNo1()).append("</th>");
				//处理其他
				//z轴方向上的标记
				int maxTag = 0;
				for(int j = 0; j < p.getZaxis(); j++){
					maxTag += 1;
					sbi = trs.get("x" + x + "z" + maxTag);
					if (xTag == 0) {
						sbi.append("<td style=\"text-align:center;\">S");
						sbi.append(j + 1);
						sbi.append("</td>");
					}
					sbi.append("<td></td>");
				}
				for(int j = 0; j < ps.size(); j++){
					maxTag += 1;
					p = ps.get(j);
					sbi = trs.get("x" + x + "z" + maxTag);
					if (xTag == 0) {
						sbi.append("<td style=\"text-align:center;\">S").append(p.getZaxis()+1).append("</td>");
					}
					sbi.append("<td style=\"position:relative;\">");
					if(temps != null && temps.length > ((p.getPoinNo1()-1) * 2 + 1)){
						if(temps[(p.getPoinNo1()-1) * 2] == 0 && temps[(p.getPoinNo1()-1) * 2 + 1] == 0){
							sbi.append("-");
						} else {
							temp = MathUtil.byte2Int(temps[(p.getPoinNo1()-1) * 2], temps[(p.getPoinNo1()-1) * 2 + 1])/10.0D;
							sbi.append("<input type=\"hidden\" name=\"_index\" value=\"" + p.getPoinNo1()+ "\" ><input name=\"_temp\" class=\"temp-change w60\" value=\" " + temp + " \" /> ");
						}
					}
					sbi.append("</td>");
				}
				for(int j = maxTag; j < maxZ; j++){
					sbi = trs.get("x" + x + "z" + (j+1));
					if (xTag == 0) {
						sbi.append("<td style=\"text-align:center;\">S").append(j + 1).append("</td>");
					}
					sbi.append("<td></td>");
				}
				xTag += 1;
			}
		}
		for(Map.Entry<String, StringBuffer> entry : trs.entrySet()){
			sb.append("<tr>").append(entry.getValue()).append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	public void saveChange(String id, double[] temps, int[] indexs){
		TempChange tc = new TempChange(id, temps, indexs);
		tc.change();
	}
}
