package cn.com.bjggs.basis.service.impl;

import java.util.List;
import java.util.UUID;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import cn.com.bjggs.basis.domain.CopyGrainInfo;
import cn.com.bjggs.basis.domain.CopyStoreHouse;
import cn.com.bjggs.basis.domain.Equipment;
import cn.com.bjggs.basis.domain.GlobalConf;
import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.NoToNb;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.service.IBasisService;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.gas.domain.GasInfo;
import cn.com.bjggs.pest.domain.PestInfo;
import cn.com.bjggs.pest.domain.PestPoint;
import cn.com.bjggs.power.domain.PowerInfo;
import cn.com.bjggs.squery.util.QueryUtil;
import cn.com.bjggs.system.util.PassUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempInfo;

@IocBean(name = "basisService", args = { "refer:dao" })
public class BasisServiceImpl extends BaseServiceImpl implements IBasisService{

	private static int maxEquipNo = 0;
	
	public BasisServiceImpl(Dao dao){
		this.dao = dao;
		getMaxNo();
	}
	
	/**
	 * 同步修改或新增storehouse和GrainInfo两张表,事务
	 * @author	yucy
	 * @date	2017-08-24
	 */
	public void update(StoreHouse s,GrainInfo g){
		//更新仓房信息
		this.update(s);
		//根据仓房编号删除储粮信息表中的数据,保证唯一性
		this.delete(GrainInfo.class, "houseNo", s.getHouseNo());
		//更新倉房位置信息的類型
		//dao.update(HousePosition.class, Chain.make("typeCode", h.getTypeCode()), Cnd.where("houseNo", "=", h.getHouseNo()));
		g.setHouseNo(s.getHouseNo());
		this.update(g);
		//更新仓房名称字典
		HouseUtil.refresh(s);
		//更新配置
		HouseUtil.refresh(s.getHouseNo(), TypeHouseConf.HOUSE.code(), s);
		HouseUtil.refresh(s.getHouseNo(), TypeHouseConf.GRAIN.code(), g);
		QueryUtil.refreshHouse(s.getHouseNo());
	}
	
	/**
	 * 创建sql,查询仓房编号使用到哪位,若前面有空余,取出.若没有,依次往后取
	 * @author	yucy
	 * @date	2017-08-24
	 */
	public String getNextHouseNo(){
		Sql sql = Sqls.create("SELECT houseNo as HOUSENO FROM StoreHouse ORDER BY houseNo ASC");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> houseNos = sql.getList(Record.class);
		int number = 0;
		int houseNo = 0;
		boolean tag = true;
		for(Record r : houseNos){
			number = number + 1;
			houseNo = ParseUtil.toInt(r.get("HOUSENO"), 0);
			if(number != houseNo){
				tag = false;
				break;
			}
		}
		if(tag){
			houseNo = number + 1;
		} else {
			houseNo = number;
		}
		
		String str;
		if(0 < houseNo && houseNo < 10){
			str = "00" + houseNo;
		}else if(9 < houseNo && houseNo < 100){
			str = "0" + houseNo;
		}else { 
			str = "" + houseNo;
		}
		return str;
	}
	
	
	/**
	 * 删除某个仓房,并将其关联的配置信息删除
	 * @author	yucy
	 * @date	2017-08-24
	 */
	public int deleteAll(String id){
		StoreHouse sh = dao.fetch(StoreHouse.class, id);
		int num = dao.delete(StoreHouse.class, id);
		if(Strings.isNotBlank(sh.getHouseNo())){
			//删除仓房信息
			this.delete(GrainInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			//删除设备
			this.delete(Equipment.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			//删除测温信息
			this.delete(TempInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			this.delete(TempBoaInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			this.delete(PointInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			//删除测虫信息
			this.delete(PestInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			this.delete(PestPoint.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			//删除测气信息
			this.delete(GasInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			//删除电表信息
			this.delete(PowerInfo.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
			//删除点排布信息
			
//			//删除仓房位置
//			this.delete(HousePosition.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			//删除设备
			
//			//删除报警配置
//			this.delete(HouseWarn.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			//删除详细信息
//			this.delete(HouseWarns.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			//删除
			
//			//删除
//			this.delete(ChecksLoop.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			//删除
//			this.delete(ChecksOne.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			//删除
//			this.delete(ChecksTime.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			this.delete(PointInfor.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			this.delete(TestData.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			this.delete(Warns.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			this.delete(HouseWarn.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
//			this.delete(HouseWarns.class, Cnd.where("houseNo", "=", sh.getHouseNo()));
		}
		return num;
		
	}
	
	/**
	 * 删除多个仓房,并将其关联的配置信息删除
	 * @author	yucy
	 * @date	2017-08-24
	 */
	public int deleteAll(String[] ids){
		int num = 0;
		for(String id : ids){
			num += deleteAll(id);
		}
		return num;
	}
	
	/**
	 * 更新设备信息
	 */
	public void updateInitNo(Equipment equip){
		if(equip.getEquipNo() == 0){
			//TODO 自动生成设备编号
			maxEquipNo += 1;
			equip.setEquipNo(maxEquipNo);
		}
		this.update(equip);
	}
	
	private void getMaxNo(){
		Sql sql = Sqls.create("SELECT max(equipNo) as EQUIPNO FROM Equipment");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(Record.class));
		dao.execute(sql);
		List<Record> men = sql.getList(Record.class);
		if(!Lang.isEmpty(men)){
			Record r = men.get(0);
			maxEquipNo =  ParseUtil.toInt(r.get("EQUIPNO"), 0);
		}
	}
	
	/**
	 * 设备排布保存
	 */
	public void saveRank(int[] xaxis, int[] yaxis, String houseNo){
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("equipNo");
		cri.where().and("houseNo", "=", houseNo);
		List<Equipment> es = dao.query(Equipment.class, cri);
		if(es == null || xaxis == null || yaxis == null || es.size() != xaxis.length || es.size() != yaxis.length){
			throw new RuntimeException("设备排布参数不正确，请检查是否有新的设备录入！");
		} else {
			int index = 0;
			for(Equipment equip : es){
				equip.setXaxis(xaxis[index]);
				equip.setYaxis(yaxis[index]);
				index++;
			}
			this.delete(Equipment.class, Cnd.where("houseNo", "=", houseNo));
			dao.insert(es);
		}
	}

	@Override
	public void updateGlobal(GlobalConf g) {
		PropsUtil.writeProperties("base.name.md5", g.getIdentCode());
		PropsUtil.writeProperties("base.name.real", g.getIdentName());
		PassUtil._CODE = g.getIdentCode();
		PassUtil._NAME = g.getIdentName();
		//dao.update(g);
	}
	
	public void ifaceSave(CopyStoreHouse s){
		if(Strings.isBlank(s.getOtherNo())){
			throw new RuntimeException("无关联主键！");
		}
		NoToNb nn = dao.fetch(NoToNb.class, Cnd.where("otherNb", "=", s.getOtherNo()));
		//更新
		if(null != nn){
			StoreHouse h = dao.fetch(StoreHouse.class, Cnd.where("houseNo", "=", nn.getHouseNo()));
			if(h == null){
				throw new RuntimeException("没有找到对应仓房！");
			}
			s.setId(h.getId());
			s.setHouseNo(h.getHouseNo());
			dao.update(s);
		}else{//插入
			s.setHouseNo(getNextHouseNo());
			s.setId(UUID.randomUUID().toString());
			dao.insert(s);
			NoToNb nns = new NoToNb(s.getHouseNo(), s.getOtherNo());
			dao.insert(nns);
		}
	}
	
	public void ifaceGrainSave(CopyGrainInfo g){
		if(Strings.isBlank(g.getHouseNo())){
			throw new RuntimeException("无关联主键！");
		}
		NoToNb nn = dao.fetch(NoToNb.class, Cnd.where("otherNb", "=", g.getHouseNo()));
		if(null != nn){
			GrainInfo h = dao.fetch(GrainInfo.class, Cnd.where("houseNo", "=", nn.getHouseNo()));
			if(h != null){
				g.setId(h.getId());
				g.setHouseNo(h.getHouseNo());
				dao.update(g);
			}else{
				g.setId(UUID.randomUUID().toString());
				g.setHouseNo(nn.getHouseNo());
				dao.insert(g);
			}
		}
	}
}
