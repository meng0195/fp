package cn.com.bjggs.system.domain;

import java.util.Collection;
import java.util.LinkedList;

import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.TypeStatus;
import cn.com.bjggs.system.enums.TypeUser;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.ManyMany;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.View;

@Comment("用户信息")
@Table("SYS_USER")
@View("VIEW_SYS_USER")
public class SysUser extends IdEntry {
	
	@Column("LOGIN_NAME")
	@Comment("登录名")
	private String loginName;

	@Column("LOGIN_PASS")
	@Comment("登录密码")
	private String loginPass;

	@Column("USER_NAME")
	@Comment("真实姓名")
	private String name;
	
	@Column("IDCARD")
	@Comment("身份证号码")
	private String idcard;
	
	@Column("TYPE")
	@Comment("用户类型（1：系统管理员，2：部门管理员，3：普通用户）")
	private int type = TypeUser.NOR.val();
	
	@Column("MOBILE_PHONE")
	@Comment("联系电话")
	private String phone;
	
	@Column("EMAIL")
	@Comment("电子邮箱")
	private String email;

	@Column("STATUS")
	@Comment("状态：(1：有效，0：无效)")
	private int status = TypeStatus.TRUE.val();

	@Column("DEPT_ID")
	@Comment("部门代码")
	private String deptId;

	@Readonly
	@Column("DEPT_CODE")
	@Comment("部门代码")
	private String deptCode;

	@Readonly
	@Column("DEPT_NAME")
	@Comment("部门名称")
	private String deptName;

	@Readonly
	@Column("DEPT_STATUS")
	@Comment("部门状态")
	private int deptStatus;
	
	@Column("ALL_STATUS")
	@Comment("全部状态标识")
	private int allStatus;
	
	private String[] houses;

	@ManyMany(target=SysRole.class, relation="SYS_USER_ROLE", from="uid", to="rid")
	private Collection<SysRole> roles = new LinkedList<SysRole>();

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPass() {
		return loginPass;
	}

	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getDeptStatus() {
		return deptStatus;
	}

	public void setDeptStatus(int deptStatus) {
		this.deptStatus = deptStatus;
	}

	public Collection<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<SysRole> roles) {
		this.roles = roles;
	}

	public String[] getHouses() {
		return houses;
	}

	public void setHouses(String[] houses) {
		this.houses = houses;
	}

	public int getAllStatus() {
		return allStatus;
	}

	public void setAllStatus(int allStatus) {
		this.allStatus = allStatus;
	}
	
}
