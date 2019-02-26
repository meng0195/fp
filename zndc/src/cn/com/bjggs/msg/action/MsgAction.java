package cn.com.bjggs.msg.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.msg.domain.MsgUser;
import cn.com.bjggs.msg.service.IMsgService;
import cn.com.bjggs.msg.view.ViewMsgInfo;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/admin/msg"})
public class MsgAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IMsgService msgService;
	
	@At({"/list"})
	@Ok("jsp:/WEB-INF/admin/msg/msg-list.jsp")
	public void list(HttpServletRequest req){
		Page<ViewMsgInfo> page = getPage("page.", "status", TypeOrder.ASC);
		page.addOrder("time", TypeOrder.DESC);
		Map<String, String[]> params = getParams4Admin();
		params.put("ft_EQ_S_userCode", new String[]{getSessionUid()});
		page = msgService.findPage(ViewMsgInfo.class, page, params);
		req.setAttribute("page", page);
	}
	
	@At({"/user/?"})
	@Ok("jsp:/WEB-INF/admin/msg/power-user.jsp")
	public void powerHouse(String uid, HttpServletRequest req) {
		req.setAttribute("uid", uid);
		req.setAttribute("ms", msgService.query(MsgUser.class, Cnd.where("userCode", "=", uid)));
	}
	
	@At({"/user/save"})
	public Object save(HttpServletRequest req, int[] types, String uid, String tid) {
		try {
			if(Strings.isBlank(uid)){
				throw new RuntimeException("请至少选择一个用户！");
			}
			msgService.saveMsgUser(types, uid);
			return DwzUtil.reloadTabAndCloseCurrent("消息权限修改成功！", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("消息权限限修改失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("消息权限修改成功失败：" + e.getMessage());
		}
	}
	
	@At({"/read/?"})
	public Object read(String msgCode, String tid){
		try {
			msgService.read(msgCode, getSessionUid());
			return DwzUtil.reloadCurrPage("操作成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("消息标记已读失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("消息标记已读失败: "+ e.getMessage());
		}
	}
	
	@At({"/reads"})
	public Object reads(HttpServletRequest req, String[] uids, String tid){
		try {
			if (Lang.isEmptyArray(uids)) {
				throw new RuntimeException("请至少选择一条消息");
			}
			msgService.read(uids, getSessionUid());
			//重新加载字典表
			return DwzUtil.reloadCurrPage("操作成功", tid);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("消息标记已读失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("消息标记已读失败: "+ e.getMessage());
		}
	}
}
