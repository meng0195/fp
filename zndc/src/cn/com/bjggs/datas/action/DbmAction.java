package cn.com.bjggs.datas.action;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.ctr.service.ICtrService;
import cn.com.bjggs.datas.util.DB4MySQL;
import cn.com.bjggs.system.action.SysAction;

@IocBean
@At({"/dbm"})
public class DbmAction extends SysAction{

	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private ICtrService ctrService;
	
	@At({"/to/do"})
	@Ok("jsp:/WEB-INF/admin/dbm/db-bak.jsp")
	public void toBak(HttpServletRequest req){
		 File file = new File(DB4MySQL.path);
		 File[] files = file.listFiles();
		 Map<String, String> map = new LinkedHashMap<String, String>();
		 if(!Lang.isEmpty(files)){
			 for(File f : files){
				 if(f.isFile()){
					 map.put(f.getPath(), f.getName());
				 }
			 }
		 }
		 req.setAttribute("map", map);
	}
	
	@At({"/bak"})
	public Object bak(String tid, HttpServletRequest req){
		try {
			DB4MySQL.backup();
			return DwzUtil.reloadCurrPage("备份成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("操作失败: " + e.getMessage());
		}
	}
	
	@At({"/rollbak"})
	public Object rollbak(String fileName, String tid, HttpServletRequest req){
		try {
			if(Strings.isBlank(fileName)){
				throw new RuntimeException("请选择备份文件");
			}
			DB4MySQL.restore(fileName);
			return DwzUtil.reloadCurrPage("备份成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("操作失败: " + e.getMessage());
		}
	}
	
	@At({"/histery/list"})
	@Ok("jsp:/WEB-INF/admin/dbm/db-histery-list.jsp")
	public void histerys(HttpServletRequest req){
		 File file = new File(DB4MySQL.path);
		 File[] files = file.listFiles();
		 req.setAttribute("files", files);
	}
	
	@At({"/del/?"})
	public Object del(String name, String tid, HttpServletRequest req){
		try {
			File file = new File(DB4MySQL.path + name + ".sql");
			if (file.exists() && file.isFile()) {
				file.delete();
			}
			return DwzUtil.reloadCurrPage("删除成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("操作失败: " + e.getMessage());
		}
	}
	
	@At({"/dels"})
	public Object dels(String[] names, String tid, HttpServletRequest req){
		try {
			if(Lang.isEmpty(names)) throw new RuntimeException("请选择至少一条数据！");
			for(String name : names){
				File file = new File(DB4MySQL.path + name);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
			return DwzUtil.reloadCurrPage("删除成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("操作失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("操作失败: " + e.getMessage());
		}
	}
	
}
