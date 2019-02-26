package cn.com.bjggs.camera.util.test;

import cn.com.bjggs.camera.util.HCNetSDK;
import cn.com.bjggs.camera.util.HCNetSDK.NET_DVR_JPEGPARA;
import cn.com.bjggs.camera.util.VideoToVideo;

import com.sun.jna.NativeLong;

public class HCSDKTest {
	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	public static void main(String[] args) {
		boolean initSuc = hCNetSDK.NET_DVR_Init();
		if (initSuc != true) {
			System.out.println("初始化失败");
		} else {
			System.out.println("初始化成功");
			HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;// 设备信息
			HCNetSDK.NET_DVR_JPEGPARA lpJpegPara;//图片质量
			
			NativeLong lUserID;// 用户句柄
			NativeLong openPlay;//开启预览句柄
			NativeLong endPlay;//结束预览句柄
			
			HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;//用户参数
			m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
			lpJpegPara = new NET_DVR_JPEGPARA();
			
			//初始化成功 登陆
			lUserID = hCNetSDK.NET_DVR_Login_V30("192.168.30.170", (short) 8000,
					"admin", "a1b2c3d4e5", m_strDeviceInfo);
//			lUserID = hCNetSDK.NET_DVR_Login_V40("192.168.30.173", (short) 8000,
//					"admin", "1234qwer", m_strDeviceInfo);
			long userID = lUserID.longValue();
			if (userID == -1) {
				System.out.println("登陆失败");
			}else{
				System.out.println("登陆成功");
				System.out.println("设备类型:"+m_strDeviceInfo.byDVRType+",模拟通道个数:"+m_strDeviceInfo.byChanNum+";起始通道号"+m_strDeviceInfo.byStartChan);
				
				m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
	            m_strClientInfo.lChannel = new NativeLong(33);
				
				//登陆成功 开启预览
				openPlay = hCNetSDK.NET_DVR_RealPlay_V30(lUserID,m_strClientInfo, null, null, true);
//				openPlay = hCNetSDK.NET_DVR_RealPlay_V30(lUserID, l_previewInfo);
				if (openPlay.longValue() == -1) {
					System.out.println("预览启动失败:" + hCNetSDK.NET_DVR_GetLastError());
				} else {
					//开始录像
					hCNetSDK.NET_DVR_SaveRealData(lUserID, "D:/video/video.mp4");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//结束录像
					boolean stopReal = false;
					while(!stopReal){
						stopReal = hCNetSDK.NET_DVR_StopSaveRealData(lUserID);
					}
					//开始抓图
					hCNetSDK.NET_DVR_CaptureJPEGPicture(lUserID, m_strClientInfo.lChannel, lpJpegPara, "D:/video/img.png");
					//关闭预览
					endPlay = hCNetSDK.NET_DVR_StopRealPlay(lUserID);
					if (endPlay.longValue() == -1) {
						System.out.println("关闭预览失败:" + hCNetSDK.NET_DVR_GetLastError());
					}
				}
				
				//退出登陆
				hCNetSDK.NET_DVR_Logout(lUserID);
			}
			
			System.out.println("错误代码:" + hCNetSDK.NET_DVR_GetLastError());
			
			hCNetSDK.NET_DVR_Cleanup();
			//视频转码
			VideoToVideo.videoToVideo("D:/video/video.mp4",
					"D:/video/ffmpeg.exe",
					"D:/video/video.swf");
		}
	}

}
