package cn.com.bjggs.camera.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.camera.domain.CameraInfo;
import cn.com.bjggs.camera.service.ICamService;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/cam"})
public class CameraAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ICamService camService;
	
	@At({"/main"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-main.jsp")
	public void main(HttpServletRequest req){
		List<CameraInfo> list = camService.query(CameraInfo.class, Cnd.cri());
		req.setAttribute("list", list);
	}
	
	@At({"/out/?"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-out.jsp")
	public void out(String id, HttpServletRequest req){
		CameraInfo cam = camService.fetch(CameraInfo.class, Cnd.where("ID", "=", id));
		req.setAttribute("cam", cam);
	}
	
	@At({"/in/?"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-in.jsp")
	public void showCam(String id, HttpServletRequest req){
		CameraInfo e = camService.fetch(CameraInfo.class, Cnd.where("ID", "=", id));
		req.setAttribute("e", e);
	}
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-list.jsp")
	public void list(HttpServletRequest req){
		Page<CameraInfo> page = getPage("page.");
		Map<String, String[]> params = getParams4Admin();
		page = camService.findPage(CameraInfo.class, page, params);	
		req.setAttribute("page", page);
	}
	
	@At({"/detail/?"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-detail.jsp")
	public void detail(String id,HttpServletRequest req){
		CameraInfo c = camService.fetch(CameraInfo.class, id, true);
		req.setAttribute("c", c);
	}
	
	@At({"/edit/?"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-edit.jsp")
	public void edit(String id,HttpServletRequest req){
		CameraInfo c = camService.fetch(CameraInfo.class, id, true);
		req.setAttribute("c", c);
	}
	
	@At({"/save"})
	public Object save(@Param("::c")CameraInfo c, String tid){
		try {
			if (camService.checkUnique(CameraInfo.class, "camName", c.getCamName(), c.getId())) {
				throw new RuntimeException("摄像头名称已存在!");
			}
			camService.update(c);
			return DwzUtil.reloadTabAndCloseCurrent("摄像头信息保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("摄像头信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("摄像头信息保存失败: " + e.getMessage());
		}
	}
	
	@At({"/status/?/?"})
	public Object status(String id, int status, String tid){
		try {
			camService.updateStatus(id, status);
			return DwzUtil.reloadCurrPage("状态修改成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("状态修改失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("状态修改失败: " + e.getMessage());
		}
	}
	
	@At({"/del/?"})
	public Object del(String id, String tid){
		try {
			camService.delete(id);
			return DwzUtil.reloadCurrPage("摄像头删除成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("摄像头删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("摄像头删除失败: " + e.getMessage());
		}
	}
	
	@At({"/del"})
	public Object deleteAll(HttpServletRequest req, String[] uids, String tid){
		try {
			if (Lang.isEmptyArray(uids)) {
				throw new RuntimeException("请至少选择一个设备!");
			}
			int num = camService.deleteAll(uids);
			return DwzUtil.reloadCurrPage("删除成功"+num+"条数据", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("摄像头删除失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("摄像头删除失败: "+ e.getMessage());
		}
	}
	
	
	@At({"/rank"})
	@Ok("jsp:/WEB-INF/admin/camera/camera-rank.jsp")
	public void rank(HttpServletRequest req){
		List<CameraInfo> list = camService.query(CameraInfo.class,  Cnd.cri());
		req.setAttribute("list", list);
	}
	
	@At({"/rank/save"})
	public Object save(double[] xaxis, double[] yaxis, String tid){
		try {
			camService.saveRank(xaxis, yaxis);
			return DwzUtil.reloadTabAndCloseCurrent("排布保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("排布保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("排布保存失败: " + e.getMessage());
		}
	}
	
}
