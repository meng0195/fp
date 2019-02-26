package cn.com.bjggs.basis.util;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

public class DwrUtil {
	
	private static class Runs extends Thread{
		
		private String json;
		
		private String func;
		
		public Runs(final String func, final String json){
			this.json = json;
			this.func = func;
		}
		public void run() {
			try {
				// 设置要调用的 js及参数
				ScriptBuffer script = new ScriptBuffer();
				script.appendCall(func, json);
				// 得到所有ScriptSession
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				// 遍历每一个ScriptSession
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			} catch (Exception e) {}
		}
	}
	
	public static synchronized void changePest(final String json) {
		try{
			final Runs run = new Runs("changeTest", json);
			Browser.withAllSessions(run);
		}catch (Exception e){}
	}
	
	/**
	 * 气体检测同步状态
	 * @author	wc
	 * @date	2017年9月14日
	 * @return	void
	 */
	public static synchronized void sendGas(final String json) {
		try{
			final Runs run = new Runs("changeGasCheck", json);
			Browser.withAllSessions(run);
		}catch (Exception e){}
	}
	public static synchronized void sendWay(final String str) {
		try{
			final Runs run = new Runs("changeWayCheck", str);
			Browser.withAllSessions(run);
		}catch (Exception e){}
	}
	
	
	public static synchronized void sendOne(final String json) {
		try{
			final Runs run = new Runs("showCheckOne", json);
			Browser.withAllSessions(run);
		}catch (Exception e){}
	}

	public static synchronized void sendLoop(final String json) {
		try{
			final Runs run = new Runs("showCheckLoop", json);
			Browser.withAllSessions(run);
		}catch (Exception e){}
	}
	
	public static synchronized void sendCtr(final String json) {
		try{
			final Runs run = new Runs("changeEquip", json);
			Browser.withAllSessions(run);
		}catch (Exception e){}
	}
}
