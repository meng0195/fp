package cn.com.bjggs.msg.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.lang.Strings;

import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.msg.domain.MsgUser;

public class MsgUtil {
	
	public static final Map<Integer, Set<String>> msgUser = new LinkedHashMap<Integer, Set<String>>();
	
	public static Dao dao;
	
	public static final void initMsgUser(final Dao d) {
		dao = d;
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("msgType");
		List<MsgUser> list = dao.query(MsgUser.class, cri);
		Set<String> s;
		if(list != null){
			for(MsgUser mu : list){
				if(msgUser.containsKey(mu.getMsgType())){
					s = msgUser.get(mu.getMsgType());
				} else {
					s = new LinkedHashSet<String>();
					msgUser.put(mu.getMsgType(), s);
				}
				s.add(mu.getUserCode());
			}
		}
	}
	
	public static final void reload(){
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("msgType");
		List<MsgUser> list = dao.query(MsgUser.class, cri);
		Set<String> s;
		if(list != null){
			msgUser.clear();
			for(MsgUser mu : list){
				if(msgUser.containsKey(mu.getMsgType())){
					s = msgUser.get(mu.getMsgType());
				} else {
					s = new LinkedHashSet<String>();
					msgUser.put(mu.getMsgType(), s);
				}
				s.add(mu.getUserCode());
			}
		}
	}
	
	@SuppressWarnings("serial")
	public static final void reload(final String userCode, int[] msgType){
		if(Strings.isNotBlank(userCode)){
			Set<String> us;
			for(Map.Entry<Integer, Set<String>> entry : msgUser.entrySet()){
				us = entry.getValue();
				us.remove(userCode);
			}
			if(msgType != null){
				for(int type : msgType){
					if(msgUser.containsKey(type)){
						msgUser.get(type).add(userCode);
					} else {
						msgUser.put(type, new LinkedHashSet<String>(){{add(userCode);}});
					}
				}
			}
		}
	}
	
	public static final void reload(int type){
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("msgType");
		cri.where().andEquals("msgType", type);
		List<MsgUser> list = dao.query(MsgUser.class, cri);
		Set<String> s;
		if(list != null){
			if(msgUser.containsKey(type)){
				s = msgUser.get(type);
			} else {
				s = new LinkedHashSet<String>();
				msgUser.put(type, s);
			}
			for(MsgUser mu : list){
				s.add(mu.getUserCode());
			}
		}
	}
	
	private static final ExecutorService eps = Executors.newFixedThreadPool(PropsUtil.getInteger("msg.send.max", 10));
	
	public synchronized static final void insertMsg(String houseNo, String msg, int msgType, int modType, String testId){
		try {
			MsgSend ms = new MsgSend(dao, houseNo, msg, msgType, modType, testId);
			eps.execute(ms);
		} catch (Exception e) {
			// TODO: 操作日志
		}
	}
	
}
