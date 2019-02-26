package cn.com.bjggs.system.domain;

import java.util.Collection;
import java.util.LinkedList;

import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.annotation.TreeDefine;
import cn.com.bjggs.core.domain.IdEntry;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.View;

@Tag("SysDept")
@Comment("部门信息")
@Table("SYS_DEPT")
@View("VIEW_SYS_DEPT")
@TreeDefine(text="name", value="code", valid="status", seqno="seqno", leve="level")
public class SysDept extends IdEntry {

	@Column("PID")
	@Comment("上级部门ID")
	private String pid;
	
	@Column("DEPT_CODE")
	@Comment("部门编码")
	private String code;
	
	@Column("DEPT_NAME")
	@Comment("部门名称")
	private String name;
	
	@Column("DEPT_DESC")
	@Comment("部门描述")
	private String desc;
	  
	@Column("REMARK1")
	@Comment("预留字段1")
	private String remark1;

	@Column("REMARK2")
	@Comment("预留字段2")
	private String remark2;
	  
	@Column("LEVE")
	@Comment("层级（level为关键字）")
	private int level = 1;
	
	@Column("SEQNO")
	@Comment("排序号")
	private int seqno = 1;
	
	@Column("STATUS")
	@Comment("是否有效：1-有效， 0-无效")
	private int status = 1;

	@Readonly
	@Column("FLAG")
	@Comment("是否是部门：1-是，0-否")
	private int flag = 1;
	
	@Many(target=SysRole.class, field="id")
	private Collection<SysRole> roles = new LinkedList<SysRole>();

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

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

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Collection<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<SysRole> roles) {
		this.roles = roles;
	}
	
}
