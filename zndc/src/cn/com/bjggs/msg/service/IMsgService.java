package cn.com.bjggs.msg.service;

import cn.com.bjggs.core.service.IBaseService;

public interface IMsgService extends IBaseService{

	public void saveMsgUser(int[] types, String uid);
	
	public void read(String msgCode, String uid);
	
	public void read(String[] msgCodes, String uid);
}
