package cn.com.bjggs.msg.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.msg.domain.MsgStatus;
import cn.com.bjggs.msg.domain.MsgUser;
import cn.com.bjggs.msg.enums.TypeMsgRead;
import cn.com.bjggs.msg.service.IMsgService;
import cn.com.bjggs.msg.util.MsgUtil;

@IocBean(name = "msgService", args = { "refer:dao" })
public class MsgServiceImpl extends BaseServiceImpl implements IMsgService{

	
	public MsgServiceImpl(Dao dao){
		this.dao = dao;
	}
	
	public void saveMsgUser(int[] types, String uid){
		this.delete(MsgUser.class, Cnd.where("userCode", "=", uid));
		if(Strings.isNotBlank(uid) && types != null){
			List<MsgUser> list = new LinkedList<MsgUser>();
			for(int type : types){
				list.add(new MsgUser(uid, type));
			}
			dao.insert(list);
		}
		MsgUtil.reload(uid, types);
	}

	public void read(String msgCode, String uid){
		dao.update(MsgStatus.class, Chain.make("status", TypeMsgRead.READ.val()), Cnd.where("msgCode", "=", msgCode).and("userCode", "=", uid));
	}
	
	public void read(String[] msgCodes, String uid){
		Criteria cri = Cnd.cri();
		cri.where().andIn("msgCode", msgCodes).andEquals("userCode", uid);
		dao.update(MsgStatus.class, Chain.make("status", TypeMsgRead.READ.val()), cri);
	}
}
