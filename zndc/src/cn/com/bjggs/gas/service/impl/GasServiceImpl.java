package cn.com.bjggs.gas.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.basis.enums.TypeCheck;
import cn.com.bjggs.basis.enums.TypeTestTag;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.StringUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.gas.domain.GasList;
import cn.com.bjggs.gas.domain.GasResults;
import cn.com.bjggs.gas.service.IGasService;
import cn.com.bjggs.gas.util.GasCheckFactory;
import cn.com.bjggs.gas.util.GasUtil;
import cn.com.bjggs.system.domain.SysUser;

@IocBean(name = "gasService", args = { "refer:dao" })
public class GasServiceImpl extends BaseServiceImpl implements IGasService{

	private static final ExecutorService TGs = Executors.newFixedThreadPool(GasUtil.MAX_CHECK_NUM);
	
	public GasServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void updateRanks(Integer[] xaxis, Integer[] yaxis, String houseNo){
		dao.update(GasInfo.class, Chain.make("wayXaxis", StringUtil.join(xaxis, ",")).add("wayYaxis", StringUtil.join(yaxis, ",")), Cnd.where("houseNo", "=", houseNo));
	}
	
	public String getGasConf(SysUser user){
		String[] hs = new String[HouseUtil.houseMap.keySet().size()];
		if(Lang.isEmptyArray(user.getHouses())){
			HouseUtil.houseMap.keySet().toArray(hs);
		} else {
			hs = user.getHouses();
		}
		Criteria cri = Cnd.cri();
		cri.where().andEquals("userCode", user.getId());
		cri.getOrderBy().asc("houseNo");
		List<GasList> cos = dao.query(GasList.class, cri);
		return HouseUtil.getChecksHtml(hs, cos);
	}
	
	public void saveConf(SysUser user, String[] houseNos){
		List<GasList> list = new LinkedList<GasList>();
		for(String s : houseNos){
			list.add(new GasList(s, user.getId()));
		}
		this.delete(GasList.class, Cnd.where("userCode", "=", user.getId()));
		dao.insert(list);
	}
	
	public void start(int[] ways, String houseNo){
		Thread check = GasCheckFactory.createChecking(TypeCheck.ONE, houseNo, null, ways, null);
		TGs.execute(check);
	}
	
	public void starts(String uid){
		Criteria cri = Cnd.cri();
		cri.where().andEquals("userCode", uid);
		cri.getOrderBy().asc("houseNo");
		List<GasList> cos = dao.query(GasList.class, cri);
		for(GasList gc : cos){
			Thread check = GasCheckFactory.createChecking(TypeCheck.ONE, gc.getHouseNo(), null, null, null);
			TGs.execute(check);
		}
	}
	
	public void stops(){
		GasResults gr;
		for(Map.Entry<String, GasResults> entry : GasUtil.lastChecks.entrySet()){
			gr = entry.getValue();
			if(gr.getTag() != TypeCT.END.val()){
				gr.setTag(TypeCT.TOEND.val());
			}
		}
	}
	
	public String getMains(SysUser user){
		List<GasList> list = dao.query(GasList.class, Cnd.where("userCode", "=", user.getId()));
		StringBuffer sb = new StringBuffer();
		GasResults res;
		String key;
		boolean tag = false;
		for(Map.Entry<String, String> entry : HouseUtil.houseMap.entrySet()){
			res = GasUtil.lastChecks.get(entry.getKey());
			key = entry.getKey();
			sb.append("<a houseNo=\"")
				.append(key)
				.append("\" target=\"dialog\" rel=\"dialog-gas-check-detail\" width=\"1200\" height=\"600\" mask=\"true\" ")
				.append(" class=\"houseTest");
			
			tag = true;
			for(GasList c : list){
				if(key.equals(c.getHouseNo())){
					if(res != null && GasUtil.checking.contains(res.getHouseNo())){
						sb.append(" checking ");
					}
					if(res != null && res.getGas().getTestTag() == TypeTestTag.ABNORMAL.val()){
						sb.append(" red\"");
					} else {
						sb.append(" green\"");
					}
					if(res != null && GasUtil.checking.contains(res.getHouseNo())){
						sb.append(" href=\"/zndc/check/gas/detail/")
						.append(key)
						.append("\">")
						.append(entry.getValue());
					} else {
						sb.append(" href=\"/zndc/squery/show/")
						.append(key)
						.append("/0/4\">")
						.append(entry.getValue());
					}
					
					tag = false;
					break;
				}
			}
			if(tag){
				sb.append(" gray\" href=\"/zndc/squery/show/")
					.append(key)
					.append("/0/4\">")
					.append(entry.getValue());
			}
			sb.append("</a>");
		}
		return sb.toString();
	}
}
