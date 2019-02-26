package temp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.nutz.dao.entity.Record;

import cn.com.bjggs.core.util.JsonUtil;
import cn.com.bjggs.system.util.PassUtil;

public class TestSocket {

	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	
	public byte[] sendMsg(byte[] msg, String ip, int port, boolean tag, int len){
		byte[] req = new byte[len];
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 5000);
			//发送指令
			os = socket.getOutputStream();
			os.write(msg);
			//处理返回流
			if(tag){
				is = socket.getInputStream();        																		// 获取一个输入流，接收服务端的信息
				is.read(req);
			}
			//尝试销毁所有流对象
			this.destroyAll();
			return req;
		} catch (Exception e){
			//尝试销毁所有流对象
			this.destroyAll();
			return null;
		}
	}
	
	private void destroyAll(){
		try{
			if (this.os != null) {
				this.os.flush();
				this.os.close();
			}
			if (this.is != null) {
				this.is.close();
			}
			if (this.socket != null){
				this.socket.close();
			}
		} catch (Exception e) {
		}
	} 
	
	//@Test
	public void testSocket(){
		int[][] xyz = {{16}, {8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8}, {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5}};
		List<Record> rs = new LinkedList<Record>();
		Record r;
  		for(int i = 0; i < xyz[0][0]; i++){
			for(int j = 0; j < xyz[1][i]; j++){
				for(int k = 0; k < xyz[2][i]; k++){
					r = new Record();
					rs.add(r);
					r.put("temp", String.format("%.1f", 50 * Math.random()));
					r.put("temp1", String.format("%.1f", 50 * Math.random()));
					r.put("xaxis", j);
					r.put("yaxis", i);
					r.put("zaxis", k);
					r.put("tag", k%3);
				}
			}
		}
  		System.out.print(JsonUtil.toJson(rs));
	}
	
	@Test
	public void testMd5(){
		String name = PassUtil.getMD5Name("北京良安科技有限公司");
		System.out.println(name);
	}
}
