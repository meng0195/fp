package cn.com.bjggs.msg.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;

import cn.com.bjggs.msg.domain.MsgInfo;
import cn.com.bjggs.msg.domain.MsgStatus;
import cn.com.bjggs.system.domain.SysHouses;
import cn.com.bjggs.system.domain.SysUser;

public class MsgSend extends Thread{
	
	private String houseNo;
	private Dao dao;
	private String msg;
	private int msgType;
	private int modType;
	private String testId;
	
	public MsgSend(Dao dao, String houseNo, String msg, int msgType, int modType, String testId){
		this.msgType = msgType;
		this.houseNo = houseNo;
		this.dao = dao;
		this.msg = msg;
		this.modType = modType;
		this.testId = testId;
	}

	private void send(){
		try {
		//获取具备当前仓房权限的所有用户
		List<SysHouses> us1 = dao.query(SysHouses.class, Cnd.where("houseNo", "=", houseNo));
		//获取全部仓房权限的用户
		List<SysUser> us2 = dao.query(SysUser.class, Cnd.where("allStatus", "=", 1));
		//获取当前消息类型的用户权限
		Set<String> us = MsgUtil.msgUser.get(msgType);
		//初始化消息主体
		MsgInfo mi = new MsgInfo(msg, msgType, modType, testId, houseNo);
		//要发送的所有消息集合
		List<MsgStatus> mss = new LinkedList<MsgStatus>();
		boolean tag = false;
		for(String s : us){
			tag = false;
			for(SysUser u : us2){
				if(Strings.equals(s, u.getId())){
					tag = true;
					mss.add(new MsgStatus(s, mi.getId()));
					break;
				}
			}
			if(tag) continue;
			for(SysHouses sh : us1){
				if(Strings.equals(s, sh.getUid())){
					mss.add(new MsgStatus(s, mi.getId()));
					break;
				}
			}
		}
		if(mss.size() > 0){
			dao.insert(mi);
			dao.insert(mss);
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void run() {
		send();
	}
}
