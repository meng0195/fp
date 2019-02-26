package cn.com.bjggs.system.domain;

import java.util.Date;

import cn.com.bjggs.core.annotation.Tag;
import cn.com.bjggs.core.domain.IdEntry;
import cn.com.bjggs.core.enums.TypeDataSource;
import cn.com.bjggs.core.enums.TypeLogLv;
import cn.com.bjggs.core.enums.TypeStatus;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Tag("SysLog")
@Comment("系统日志")
@Table("SYS_LOG_${YM}")
public class SysLog extends IdEntry {

	@Column("SOURCE")
	@ColDefine(type=ColType.CHAR, width=1)
	@Comment("日志来源(ADMIN,WEB,MICRO,ANDROID,IOS,OTHER)")
	private String source = TypeDataSource.OTHER.code();

	@Column("MODULE")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment("所属模块")
	private String module;

	@Column("CLAZZ")
	@ColDefine(type=ColType.VARCHAR, width=30)
	@Comment("所属类")
	private String clazz;

	@Column("METHOD")
	@ColDefine(type=ColType.VARCHAR, width=30)
	@Comment("所属方法")
	private String method;

	@Column("LEVE")
	@ColDefine(type=ColType.VARCHAR, width=10)
	@Comment("日志级别")
	private String level = TypeLogLv.DEBUG.code();

	@Column("CONTENT")
	@ColDefine(type=ColType.VARCHAR, width=2000)
	@Comment("日志内容")
	private String content;
	
	@Column("STATUS")
	@ColDefine(type=ColType.INT, width=1)
	@Comment("日志状态(1:有效,0:无效)")
	private int status = TypeStatus.TRUE.val();

	@Column("CREATE_TIME")
	@ColDefine(type=ColType.DATETIME)
	@Comment("日志记录时间")
	private Date createTime;

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getClazz() {
		return this.clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
