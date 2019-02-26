package cn.com.bjggs.system.domain;

import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.annotation.TreeDefine;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.enums.TypeStatus;
import cn.com.bjggs.system.enums.TypePageOpen;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.View;

@Tag("SysMenu")
@Comment("菜单信息")
@Table("SYS_MENU")
@View("VIEW_SYS_MENU")
@TreeDefine(value="code", text="name", url="link", render="render", valid="status", seqno="seqno", leve="level")
public class SysMenu extends IdEntry {

	@Column("PID")
	@Comment("上级菜单ID")
	private String pid;

	@Column("MENU_CODE")
	@Comment("菜单编码")
	private String code;
	  
	@Column("MENU_NAME")
	@Comment("菜单名称")
	private String name;

	@Column("MENU_DESC")
	@Comment("菜单描述")
	private String desc;

	@Column("MENU_LINK")
	private String link;

	@Column("LEVE")
	@Comment("层级（level为关键字）")
	private int level = 1;
	
	@Column("SEQNO")
	@Comment("排序号")
	private int seqno = 1;

	@Column("OPEN_TYPE")
	@Comment("页面渲染方式")
	private int render = TypePageOpen.NORMAL.val();

	@Column("STATUS")
	@Comment("是否有效：1-有效， 0-无效")
	private int status = TypeStatus.TRUE.val();

	@Readonly
	@Column("FLAG")
	@Comment("是否是菜单：1-是，0-否")
	private int flag = TypeFlag.YES.val();

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

	public int getRender() {
		return render;
	}

	public void setRender(int render) {
		this.render = render;
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
	
}
