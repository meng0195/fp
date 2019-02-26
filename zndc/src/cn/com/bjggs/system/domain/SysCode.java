package cn.com.bjggs.system.domain;

import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.TypeStatus;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Tag("code")
@Comment("系统编码表信息")
@Table("SYS_DIC_CODE")
public class SysCode extends IdEntry {
	
	@Column("TYPE")
	@Comment("码表所属类别")
	private String type;
	
	@Column("NAME")
	@Comment("代码")
	private String name;
	
	@Column("CODE")
	@Comment("代码")
	private String code;
	
	@Column("STATUS")
	@Comment("状态 1：有效，0：无效")
	private int status = TypeStatus.FALSE.val();
	
	@Column("SEQNO")
	@Comment("排序号")
	private int seqno = 1;
	  
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
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
	
	public int getSeqno() {
		return this.seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
