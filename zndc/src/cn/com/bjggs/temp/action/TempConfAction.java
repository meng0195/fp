package cn.com.bjggs.temp.action;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempBoaInfo;
import cn.com.bjggs.temp.domain.TempBoaInfos;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.service.ITempService;
import cn.com.bjggs.temp.util.PointUtil;
import cn.com.bjggs.temp.view.ViewHouseTempInfo;

@IocBean
@At({"/conf/temp"})
public class TempConfAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ITempService tempService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-conf-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewHouseTempInfo> page = getPage("page.", "houseNo", TypeOrder.ASC);
		Map<String,String[]> param = getParams4Admin();
		page = tempService.findPage(ViewHouseTempInfo.class, page, param);
		req.setAttribute("page", page);
	}
	
	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-conf-edit.jsp")
	public void edit(String houseNo, HttpServletRequest req){
		StoreHouse s = tempService.fetch(StoreHouse.class, "houseNo", houseNo);
		TempInfo t = tempService.fetch(TempInfo.class, "houseNo", houseNo);
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("tempType");
		cri.where().andEquals("houseNo", houseNo);
		List<TempBoaInfo> tb = tempService.query(TempBoaInfo.class, cri);
		if(tb == null || tb.size() < 6){
			tb = new LinkedList<TempBoaInfo>();
			for(int i =0; i < 6; i++){
				tb.add(new TempBoaInfo());
			}
		}
		req.setAttribute("s", s);
		req.setAttribute("t", t);
		req.setAttribute("tb", tb);
	}
	
	@At({"/save/?"})
	public Object save(int tag, @Param("::tb")TempBoaInfos tb, @Param("::t")TempInfo t, String houseNo, String tid){
		try {
			StoreHouse s = tempService.fetch(StoreHouse.class, "houseNo", houseNo);
			tempService.save(s, tb, t, houseNo, tag);	//同时保存测温信息,测温板信息,测温点位置信息
			return DwzUtil.reloadTabAndCloseCurrent("测温信息保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("测温信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("测温信息保存失败: " + e.getMessage());
		}
	}

	@At({"/rank/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-conf-rank.jsp")
	public void rank(String houseNo, HttpServletRequest req) {
		req.setAttribute("rs", tempService.getHouseAndPoints(houseNo));
	}
	
	@At({"/edit/point/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-point-edit.jsp")
	public void edit(String id, String divId, boolean sTag, HttpServletRequest req) {
		req.setAttribute("p", tempService.fetch(PointInfo.class, id, true));
		req.setAttribute("divId", divId);
		req.setAttribute("sTag", sTag);
    }
	
	@At({"/save/point"})
	public Object savePoint(@Param("::p.")PointInfo p, String divId, boolean sTag) {
		try {
			tempService.update(p);
			String html = PointUtil.getHtmls(p, sTag);
			//刷新单个点信息
			PointUtil.refreshSinglePoint(p);
			return DwzUtil.reloadDivAndCloseCurrent("保存温度点信息成功！", divId, html);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("温度点信息保存失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("温度点信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/area/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-conf-area.jsp")
	public void area(String houseNo, HttpServletRequest req) {
		Record aaa = tempService.getHouseAndArea(houseNo);
		req.setAttribute("rs", tempService.getHouseAndArea(houseNo));
	}
	
	@At({"/area/save"})
	public Object areaSave(String houseNo, String cable, String area, String tid, String[] t1, String[] t2, String[] t3, String[] t4, HttpServletRequest req) {
		try {
			//TODO 未将保存的分区放入内存,后期修改
			tempService.updateArea(houseNo, cable, area, t1, t2, t3, t4);
			return DwzUtil.nothing("电缆位置保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("电缆位置保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("电缆位置保存失败: " + e.getMessage());
		}
		
	}
	
	@At({"/area/leve/?/?/?/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-Leve.jsp")
	public void areaLeve(String houseNo, int code, String sel, String name, HttpServletRequest req) {
		if(Strings.isNotBlank(houseNo)){
			TempInfo ti = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
			String[] lnums = ti.getLnums();
			int maxLeve = ParseUtil.toInt(lnums[code-1], 0);
			String[] sels = sel.split("\\.");
			int[] leves = new int[maxLeve];
			if(!"-1".equals(sel)){
				for(int i = 0; i < sels.length; i++){
					leves[ParseUtil.toInt(sels[i], 0)] = 1;
				}
			}
			req.setAttribute("leves", leves);
			req.setAttribute("id", name);
		}
	}
}
