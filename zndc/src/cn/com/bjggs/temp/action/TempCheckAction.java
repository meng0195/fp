package cn.com.bjggs.temp.action;

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

import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.temp.domain.LoopConf;
import cn.com.bjggs.temp.domain.RealConf;
import cn.com.bjggs.temp.service.ITempService;
import cn.com.bjggs.temp.util.ChecksUtil;
import cn.com.bjggs.temp.util.JointUtil;

@IocBean
@At({"/check/temp"})
public class TempCheckAction extends SysAction {

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ITempService tempService;
	
	/**
	 * 加载初始页面
	 * @param req
	 */
	@At({"/main"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-main.jsp")
	public void list(HttpServletRequest req){}
	
	@At({"/oneMain"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-oneMain.jsp")
	public void oneMain(HttpServletRequest req) {
		req.setAttribute("r", tempService.getChecksOne(getSessionUser()));
	}
	
	@At({"/tempClear"})
	public Object tempClear(String tid) {
		try {
			ChecksUtil.ONES.clear();
			return DwzUtil.nothing("异常清除成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("异常清除成功：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("异常清除成功: " + e.getMessage());
		}
	}
	
	
	/**
	 * 手动检测配置
	 * @param req
	 */
	@At({"/oneConf"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-oneConf.jsp")
	public void oneConf(HttpServletRequest req) {
		req.setAttribute("r", tempService.getChecksOneConf(getSessionUser()));
	}
	
	/**
	 * 保存手动配置信息
	 * @param maxNum
	 * @param houseNo
	 * @param tid
	 * @return
	 */
	@At({"/oneSave"})
	public Object oneSave(int maxNum, String[] houseNo, String tid) {
		try {
			tempService.oneSave(getSessionUser(), houseNo);
			return DwzUtil.reloadTabAndCloseCurrent("手动检测配置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("手动检测配置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("手动检测配置失败: " + e.getMessage());
		}
	}
	
	@At({"/oneStart/?"})
	public Object oneStart(int reportTag, HttpServletRequest req, String tid) {
		try {
			tempService.oneStart(getSessionUser(), reportTag);
			return DwzUtil.nothing("操作成功，手动检测已经开始！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("手动检测开启失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("手动检测开启失败: " + e.getMessage());
		}
	}
	
	/**
	 * 循环检测首页
	 * @param req
	 */
	@At({"/loopMain"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-loopMain.jsp")
	public void loopMain(HttpServletRequest req) {
		LoopConf lc = tempService.getLoopConf(getSessionUser());
		req.setAttribute("rightContent", JointUtil.initLoopHtml(lc.getCheckLoops()));
	}
	
	/**
	 * 循环检测配置
	 * @param req
	 */
	@At({"/loopConf"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-loopConf.jsp")
	public void loopConf(HttpServletRequest req) {
		req.setAttribute("c", tempService.getLoopConf(getSessionUser()));
	}
	
	@At({"/loopSave"})
	public Object loopSave(String[] houseNo, String tid) {
		try {
			tempService.loopSave(getSessionUser(), houseNo);
			return DwzUtil.reloadTabAndCloseCurrent("循环检测配置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("循环检测配置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("循环检测配置失败: " + e.getMessage());
		}
	}
	
	@At({"/loopStart"})
	public Object loopStart(HttpServletRequest req, String tid) {
		try {
			tempService.loopStart(getSessionUser());
			return DwzUtil.nothing("操作成功，循环检测已经开始！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("循环检测检测开启失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("循环检测检测开启失败: " + e.getMessage());
		}
	}
	
	@At({"/loopStop"})
	public Object loopStop(HttpServletRequest req, String tid) {
		try {
			ChecksUtil.loopTag = false;
			return DwzUtil.nothing("操作成功！当前仓房检测结束后停止循环检测", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("循环检测停止失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("循环检测停止失败: " + e.getMessage());
		}
	}
	
	/**
	 * 实时数据首页
	 * @param req
	 */
	@At({"/realMain"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-realMain.jsp")
	public void realMain(HttpServletRequest req) {
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("houseNo");
		List<StoreHouse> houses = tempService.query(StoreHouse.class, cri);
		req.setAttribute("houses", houses);
	}
	
	/**
	 * 循环检测配置
	 * @param req
	 */
	@At({"/realConf"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-check-realConf.jsp")
	public void realConf(HttpServletRequest req) {
		RealConf.realConf.setReal();
		req.setAttribute("c", RealConf.realConf);
	}
	
	@At({"/realSave"})
	public Object realSave(@Param("::c.")RealConf c, String[] houseNo, String tid) {
		try {
			RealConf.realConf.setReal(c);
			return DwzUtil.reloadCurrPage("配置成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("配置失败：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("配置失败: " + e.getMessage());
		}
	}
	
	/**
	 * 显示实时数据
	 * @author	yucy
	 * @param	req
	 */
	@At({"/showReal/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-real-detail.jsp")
	public void showReal(String houseNo, HttpServletRequest req) {
		req.setAttribute("houseNo", houseNo);
	}

	@At({"/showRealDetail/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-real-detail1.jsp")
	public void showRealDetail(String houseNo, HttpServletRequest req) {
		String html = tempService.showRealData(houseNo);
		req.setAttribute("html", html);
	}
	
	@At({"/to/change/?"})
	@Ok("jsp:/WEB-INF/admin/temp/temp-change.jsp")
	public void toChange(String id, HttpServletRequest req) {
		req.setAttribute("html", tempService.getChange(id));
		req.setAttribute("id", id);
	}
	
	@At({"/change/save"})
	public Object changeSave(String id, double[] _temp, int[] _index, String tid) {
		try {
			tempService.saveChange(id, _temp, _index);
			return DwzUtil.reloadTabAndCloseCurrent("修改成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("修改成功：%s", e.getMessage());
			}
			return DwzUtil.stopPageError("配置失败: " + e.getMessage());
		}
	}

}
