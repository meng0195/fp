package cn.com.bjggs.warns.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.view.ViewHouseInfo;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.warns.domain.Alarm;
import cn.com.bjggs.warns.domain.AlarmTT;
import cn.com.bjggs.warns.domain.Alarms;
import cn.com.bjggs.warns.service.IWarnsService;

@IocBean
@At({"/conf/warn"})
public class WarnsAction extends SysAction{
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IWarnsService warnsService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/warns/warns-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewHouseInfo> page = getPage("page.", "houseNo", TypeOrder.ASC);
		page = warnsService.findPage(ViewHouseInfo.class, page, getParams4Admin());
		//同步报警列表
		List<ViewHouseInfo> list = page.getResult();
		for(ViewHouseInfo vsh : list){
			AlarmTT ws = HouseUtil.get(vsh.getHouseNo(), TypeHouseConf.WARNS.code(), AlarmTT.class);
			if(ws != null){
				int[] w = new int[5];
				if(ws.isWarns(0) || ws.isWarns(1) || ws.isWarns(2) || ws.isWarns(3) || ws.isWarns(4) || ws.isWarns(5)) w[0] = 1;
				if(ws.isWarns(6) || ws.isWarns(7)) w[1] = 1;
				if(ws.isWarns(8) || ws.isWarns(9)) w[2] = 1;
				if(ws.isWarns(10) || ws.isWarns(11) || ws.isWarns(12)) w[3] = 1;
				if(ws.isWarns(13)) w[4] = 1;
				vsh.setWarnTag(w);
			}
		}
		req.setAttribute("page", page);
	}

	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/warns/warns-edit.jsp")
	public void edit(String houseNo, HttpServletRequest req){
		req.setAttribute("houseNo", houseNo);
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo);
		cri.getOrderBy().asc("type").asc("type1");
		req.setAttribute("list", warnsService.query(Alarm.class, cri));
	}
	
	@At({"/save"})
	public Object save(@Param("::a")Alarms as, HttpServletRequest req, String tid){
		try {
			if(as.getType() != null){
				int[] tags = new int[as.getType().length];
				for(int i = 0; i < as.getType().length; i++){
					tags[i] = ParseUtil.toInt(req.getParameter("a.alarmTag" + i), 0);
				}
				as.setAlarmTag(tags);
			}
			//实现修改,不存在新增,存在修改
			warnsService.save(as);
			return DwzUtil.reloadTabAndCloseCurrent("仓房预警信息保存成功!", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("仓房预警信息保存失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("仓房预警信息保存失败: " + e.getMessage());
		}
	}
}