package cn.com.bjggs.pest.util;

import cn.com.bjggs.core.util.PropsUtil;

public class HCMonitorUtil {
	//测试方法 
	public native void sayHello(); 
	//加载海康SDK
	public native int InitHCSdk();
	//加载大华SDK
	public native int InitDHSdk();
	//加载日志   日志路径+文件名和文件类型
	public native int InitLog(String path);
	//开始诊断    设备IP,设备端口,设备用户名,设备密码,厂商类型(海康1;大华2),设备类型(球机1,枪机2),诊断项目(1诊断,0不诊断),文件路径(诊断截图)
	public native int Start(String ip,int port,String username,String password,int type,int ptztype,String zdvalue,String path);
	//获取诊断结果
	public native String GetValue();
	//设备停止  厂商类型
	public native int Stop(int type);
	//清理海康SDK
	public native int CleanHCSdk();
	//清理大华SDK
	public native int CleanDHSdk();
	//清理日志
	public native int CleanLog();
	//设备登录测试
    public native int LOGINTEST(String ip,int port,String username,String password,int type);
    //通过NVR获取IPC信息
	public native String NVRGETIP(String ip,int port,String username,String password,int type);
	//通过DVR，获取通道号启用信息
	public native String GETDVRTDH(String ip,int port,String username,String password,int type);
	//得到本地文件选择框
	public native String GetLocalPath();
	//获取设备信息
	public native String GETIPC(String ip,int port,String username,String password,int type);
	//根据DVR通道号截图
	public native String GETDVRJPG(String ip,int port,String username,String password,int type,int tdh,String path);
	//获取球机PTZ坐标
	public native String GETPTZ(String ip,int port,String username,String password,int type,String path);
	
	//通过nvr/dvr 通道号，对对应通道进行摄像头抓图和录像
	//ip,port,username,pwd,tpe:1海康2大华，tdh:海康nvr33开始大话1开始，lxtime:录像时间秒，path：录像抓图路径
	public native String GETNVRPICIMG(String ip, int port, String username, String password, int type, int tdh, int lxtime, String path);
	
	public static HCMonitorUtil dm = null; 
	public static void initMonitorZd(){
		if(dm == null){
			System.loadLibrary("MonitorZD");
			dm = new HCMonitorUtil();
			//加载信息
			dm.InitHCSdk();
		}
	}
	
	public static String PIC_SRC = PropsUtil.getString("pest.monitor.src");
	public static int NVR = PropsUtil.getInteger("pest.monitor.nvr", 1);
	public static int WAY = PropsUtil.getInteger("pest.monitor.way", 32);
	public static int TIME = PropsUtil.getInteger("pest.monitor.time", 10);
	
	/*
    public static void main(String[] args) { 
		System.out.println(System.getProperty("java.library.path"));
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
	    TimeZone t = sdf.getTimeZone();
		t.setRawOffset(0);
		sdf.setTimeZone(t);
		//开始时间
	    Long startTime = System.currentTimeMillis();
	    //诊断对象
    	HCMonitorUtil dm = null; 
	    String path="E:/monitorzd/",zdvalue="111111111111111";
	    System.loadLibrary("MonitorZD");  
	    dm = new HCMonitorUtil(); 
	    //加载信息
    	dm.InitLog("E:/monitor_zd.log");
    	dm.InitHCSdk();
    	dm.InitDHSdk();  
    	GETNVRPICIMG(dm,"D:/MyInstall/tomcat_inatall/apache-tomcat-7-eclipse/webapps/monitorzd/");
	    //结束日期
	    Long endTime = System.currentTimeMillis();
	    System.out.println("用时:" + sdf.format(new Date(endTime - startTime)));
   }  
   
    public static void ZD(HCMonitorUtil dm,String zdvalue,String path){
    	 //dm.sayHello();
       //int err=dm.Start("172.16.10.150", 7000, "admin", "12345", 1,1,zdvalue,path);
       int err=dm.Start("192.168.60.130", 8000, "admin", "12345", 1,2,zdvalue,path);
         //int err=dm.Start("192.168.1.1", 8000, "admin", "12345", 1,2,zdvalue,path);
         if(0==err||"0".equals(err)){
         	 System.out.println("启动诊断成功");
         }else{
         	 System.out.println(""+err);
         }
         String ZDValue="";
         ZDValue=dm.GetValue();
         boolean isvalue=true;
         while(isvalue){
         	if(null==ZDValue||"".equals(ZDValue)){
         		ZDValue=dm.GetValue();
         	}else{
         		isvalue=false;
         		//dm.Stop(1);
         	}
         }
         String[] value=ZDValue.split("\\|F-G\\|");
         for(int i=0;i<value.length;i++){
         	System.out.println("诊断结果:"+value[i]);
         }
         System.out.println("图像冻结:"+value[0]);
         System.out.println("图像过亮:"+value[1]);
         System.out.println("图像过暗:"+value[2]);
         System.out.println("图像对比度:"+value[3]);
         System.out.println("图像遮挡:"+value[4]);
         System.out.println("图像失色:"+value[5]);
         System.out.println("图像偏色:"+value[6]);
         System.out.println("信号丢失:"+value[7]);
         System.out.println("视频剧变:"+value[8]);
         System.out.println("清晰度:"+value[9]);
         System.out.println("噪点检测:"+value[10]);
         System.out.println("场景变更:"+value[11]);
         System.out.println("视频抖动:"+value[12]);
         System.out.println("条纹干扰:"+value[13]);
         System.out.println("云台失控:"+value[14]);
         System.out.println("截图路径:"+value[15]);
    }
    
    //获取NVR上IPC信息
    public static void NVRGETIP(HCMonitorUtil dm){
    	//String IPC=dm.NVRGETIP("172.16.10.160", 7000, "admin", "12345", 1);
    	String IPC=dm.NVRGETIP("192.168.60.140", 8000, "admin", "12345", 1);
    	//String IPC=dm.NVRGETIP("192.168.1.1", 8000, "admin", "12345", 1);
    	String[] value=IPC.split("IPC");
		for(int i=0;i<value.length;i++){
			String[] MYIPC=value[i].split("\\|F-G\\|");
			if("0.0.0.0"==MYIPC[0]||"0.0.0.0".equals(MYIPC[0])){
					 System.out.println("1");  
			}else{
				System.out.println("设备IP:"+MYIPC[0]+";通道号:"+MYIPC[1]);
			}
		}
    }
    
    //通过DVR/nvr，获取通道号启用状态
    public static void GETDVRTDH(HCMonitorUtil dm){
    	String IPC=dm.GETDVRTDH("192.168.50.227", 8000, "admin", "ycc12345", 1);
    	String[] a={"2"};//根据NVRID，查询已经存在表的通道号
    	String[] TDH=IPC.split(",");
    	for(int i=0;i<TDH.length;i++){
    		if(!useList(a,TDH[i])&&(TDH[i].equals("1")||TDH[i]=="1")){
    			System.out.println("通道："+(i+1)+"启用,随机生成名称：摄像机"+(i+1));
    		}
    	}
    }
    
    public static boolean useList(String[] arr, String targetValue) {    	
    	    return Arrays.asList(arr).contains(targetValue);    	
    }
    
    //获取单个设备信息
	public static void GETIPC(HCMonitorUtil dm){
		//String IPC=dm.GETIPC("172.16.10.150", 7000, "admin", "12345", 1);
		//String IPC=dm.GETIPC("10.103.241.191", 8000, "admin", "632911632", 1);
		//String IPC=dm.GETIPC("10.103.251.12", 37777, "admin", "admin", 2);
		//String IPC=dm.GETIPC("192.168.50.150", 7000, "admin", "12345", 1);
		String IPC=dm.GETIPC("192.168.50.190", 7000, "admin", "admin", 2);
		System.out.println(IPC);
		//String[] value=IPC.split("\\|FG\\|");
		//System.out.println("设备型号:"+value[0]+";设备序列号"+value[1]);
	}
   
   public static void GETDVRJPG(HCMonitorUtil dm,String path){
	   String IPC=dm.GETDVRJPG("192.168.60.140", 8000, "admin", "12345", 1,33,path);
	   System.out.println("抓图路径:"+IPC);
   }
   //获取球机坐标
   public static void GETPTZ(HCMonitorUtil dm,String path){
	   //String IPC=dm.GETPTZ("192.168.1.13", 7000, "admin", "12345", 1,path);
	   //String IPC=dm.GETPTZ("172.16.10.150", 7000, "admin", "12345", 1,path);
	   String IPC=dm.GETPTZ("172.16.10.190", 7000, "admin", "admin", 2,path);
	   String[] value=IPC.split("\\|FG\\|");
	   System.out.println("图片路径:"+value[0]+";P坐标:"+value[1]+";T坐标:"+value[2]+";Z坐标:"+value[3]);
   }


   public static void GETNVRPICIMG(HCMonitorUtil dm,String path){
	   //nvrIp,nvr端口，nvr账号，nvr密码，nvr类型（1海康，2大华），通道号（新版海康nvr是33开始，大华nvr从1开始），录像时间，保存路径
	   String IPC=dm.GETNVRPICIMG("192.168.60.140", 8000, "admin", "12345", 1,33,3,path);
	   System.out.println(IPC);
   }*/
}
