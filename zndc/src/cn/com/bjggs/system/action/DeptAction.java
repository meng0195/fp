package cn.com.bjggs.system.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.annotation.ActionDesc;
import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.domain.SysDept;
import cn.com.bjggs.system.service.ISystemService;
import cn.com.bjggs.system.util.CodesUtil;

@IocBean
@At({ "/admin/system/dept" })
@ActionDesc("部门信息")
public class DeptAction extends SysAction {
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ISystemService systemService;

	@At({ "/list" })
	@Ok("jsp:/WEB-INF/admin/system/dept-list.jsp")
	public Object list(HttpServletRequest req) {
		String jsonString = this.systemService.getTreeJsonString(SysDept.class);
		req.setAttribute("jsonString", jsonString);
		return jsonString;
	}

	@At({ "/add/?" })
	@Ok("jsp:/WEB-INF/admin/system/dept-edit.jsp")
	public Object add(int level, HttpServletRequest req) {
		SysDept d = (SysDept) this.systemService.genSysEntryCode(SysDept.class,
				"", level);
		req.setAttribute("d", d);
		return d;
	}

	@At({ "/edit/?" })
	@Ok("jsp:/WEB-INF/admin/system/dept-edit.jsp")
	public Object edit(String id, HttpServletRequest req) {
		SysDept d = (SysDept) this.systemService.fetch(SysDept.class, id, true);
		req.setAttribute("d", d);
		return d;
	}

	@At({ "/save" })
	public Object save(@Param("::d.") SysDept d, String tid) {
		try {
			if (d == null) {
				throw new Exception("未找到对应的部门信息!");
			}
			if (this.systemService.checkUnique(SysDept.class, "code",
					d.getCode(), d.getId())) {
				throw new Exception("编码已存在!".intern());
			}
			d = this.systemService.update(d);
			CodesUtil.update("sys_dept", d.getCode(), d.getName());

			return DwzUtil.reloadCurrPage("部门信息保存成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("部门信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("部门信息保存失败: " + e.getMessage());
		}
	}

	@At({ "/save/next/before/?/?" })
	@Ok("jsp:/WEB-INF/admin/system/dept-edit.jsp")
	public Object saveChildBefore(String pid, int level, String tid,
			HttpServletRequest req) {
		SysDept d = (SysDept) this.systemService.genSysEntryCode(SysDept.class,
				pid, level);
		d.setPid(pid);
		d.setLevel(level + 1);

		req.setAttribute("d", d);
		return d;
	}

	@At({ "/save/next" })
	public Object saveChild(@Param("::d.") SysDept d, String tid) {
		try {
			this.systemService.saveNextLevel(SysDept.class, d);
			CodesUtil.update("sys_dept", d.getCode(), d.getName());

			return DwzUtil.reloadCurrPage("下级部门信息保存成功", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.error("下级部门信息保存失败：" + e.getMessage());
			}
			return DwzUtil.stopPageError("下级部门信息保存失败: " + e.getMessage());
		}
	}

	@At({ "/del/?" })
	public Object del(String id, String tid) {
		try {
			if (id == null) {
				throw new Exception("未找到对应的部门信息!");
			}
			SysDept d = (SysDept) this.systemService.fetch(SysDept.class, id,
					false);
			if (d == null) {
				throw new Exception("未找到对应的部门信息!");
			}
			this.systemService.deleteDept(id);

			return DwzUtil.reloadCurrPage("部门信息删除成功", tid, id);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("部门信息删除失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("部门信息删除失败: " + e.getMessage());
		}
	}

	@At({ "/view/?" })
	@Ok("jsp:/WEB-INF/admin/system/dept-view.jsp")
	public Object view(String id, HttpServletRequest req) {
		try {
			if (id == null) {
				throw new Exception("未找到对应的部门信息!");
			}
			SysDept d = (SysDept) this.systemService.fetch(SysDept.class, id,
					false);
			if (d == null) {
				throw new Exception("未找到对应的部门信息!");
			}
			req.setAttribute("d", d);

			return d;
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("部门信息查看失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("部门信息查看失败: " + e.getMessage());
		}
	}
}
