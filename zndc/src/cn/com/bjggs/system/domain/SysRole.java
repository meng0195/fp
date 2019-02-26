package cn.com.bjggs.system.domain;

import java.util.Collection;
import java.util.LinkedList;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.TypeStatus;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.View;

@Comment("角色信息")
@Table("SYS_ROLE")
@View("VIEW_SYS_ROLE")
public class SysRole extends IdEntry {

	@Column("ROLE_CODE")
	@Comment("角色编码")
	private String code;

	@Column("ROLE_NAME")
	@Comment("角色名称")
	private String name;

	@Column("ROLE_DESC")
	@Comment("角色描述")
	private String desc;

	@Column("STATUS")
	@Comment("是否有效：1-有效， 0-无效")
	private int status = TypeStatus.TRUE.val();

	@One(target=SysDept.class, field="id")
	private SysDept dept;

	@Column("DEPT_ID")
	@Comment("部门ID")
	private String deptId;

	@Readonly
	@Column("DEPT_CODE")
	@Comment("部门代码")
	private String deptCode;

	@Readonly
	@Column("DEPT_NAME")
	@Comment("部门名称")
	private String deptName;

	@ManyMany(target=SysUser.class, relation="SYS_USER_ROLE", from="rid", to="uid")
	private Collection<SysUser> users = new LinkedList<SysUser>();

	@ManyMany(target=SysMenu.class, relation="SYS_ROLE_MENU", from="rid", to="mid")
	private Collection<SysMenu> menus = new LinkedList<SysMenu>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public SysDept getDept() {
		return dept;
	}

	public void setDept(SysDept dept) {
		this.dept = dept;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Collection<SysUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<SysUser> users) {
		this.users = users;
	}

	public Collection<SysMenu> getMenus() {
		return menus;
	}

	public void setMenus(Collection<SysMenu> menus) {
		this.menus = menus;
	}
	
}
