/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HCNetSDK.java
 *
 * Created on 2009-9-14, 19:31:34
 */

/**
 *
 * @author Xubinfeng
 */

package cn.com.bjggs.camera.util;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.win32.StdCallLibrary;

//SDK接口说明,HCNetSDK.dll
public interface HCNetSDK extends StdCallLibrary {

    HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary("D:\\HKlib\\HCNetSDK",
            HCNetSDK.class);
    /***宏定义***/
    //常量
    public static final int SERIALNO_LEN = 48;   //序列号长度
    int  NET_DVR_GetLastError();
    
    
    //初始化SDK
	 boolean  NET_DVR_Init();
	 //释放SDK资源
	 boolean  NET_DVR_Cleanup();
	 
	 boolean  NET_DVR_SetDVRMessage(int nMessage,int hWnd);
	 //用户注册设备
	 NativeLong  NET_DVR_Login_V30(String sDVRIP, short wDVRPort, String sUserName, String sPassword, NET_DVR_DEVICEINFO_V30 lpDeviceInfo);
	 //用户销毁设备
	 boolean NET_DVR_Logout(NativeLong l);
	 //启动预览(NET_DVR_Login_V30的返回值,预览参数,码流数据回调函数,用户数据)
//	 NativeLong NET_DVR_RealPlay_V40(NativeLong lUserID, LPNET_DVR_PREVIEWINFO lpPreviewInfo);//TODO 有问题,参数对不上
	 NativeLong  NET_DVR_RealPlay_V30(NativeLong lUserID, NET_DVR_CLIENTINFO lpClientInfo, FRealDataCallBack_V30 fRealDataCallBack_V30, Pointer pUser , boolean bBlocked );
	 //停止预览
	 NativeLong NET_DVR_StopRealPlay (NativeLong lRealHandle);
	 //捕获数据并保存到指定路径
	 boolean NET_DVR_SaveRealData(NativeLong lRealHandle,String sFileName);
	 //停止捕获
	 boolean NET_DVR_StopSaveRealData(NativeLong lRealHandle);
	 //抓图到指定路径
	 boolean  NET_DVR_CaptureJPEGPicture(NativeLong lUserID, NativeLong lChannel, NET_DVR_JPEGPARA lpJpegPara, String sPicFileName);
	 
//NET_DVR_Login_V30()参数结构
public static class NET_DVR_DEVICEINFO_V30 extends Structure
{
  public  byte[] sSerialNumber = new byte[SERIALNO_LEN];  //序列号
  public  byte byAlarmInPortNum;		        //报警输入个数
  public  byte byAlarmOutPortNum;		        //报警输出个数
  public  byte byDiskNum;				    //硬盘个数
  public  byte byDVRType;				    //设备类型, 1:DVR 2:ATM DVR 3:DVS ......
  public  byte byChanNum;				    //模拟通道个数
  public  byte byStartChan;			        //起始通道号,例如DVS-1,DVR - 1
  public  byte byAudioChanNum;                //语音通道数
  public  byte byIPChanNum;					//最大数字通道个数
  public  byte[] byRes1 = new byte[24];					//保留
}


public static class LPNET_DVR_PREVIEWINFO extends Structure{
	public byte hPlayWnd;//需要SDK 解码时句柄设为有效值，仅取流不解码时可设为空
	public byte lChannel = 1;//预览通道号
	public byte dwStreamType = 0; //0-主码流，1-子码流，2-码流3，3-码流4，以此类推
	public byte dwLinkMode = 0; //0- TCP 方式，1- UDP 方式，2- 多播方式，3- RTP 方式，4-RTP/RTSP，5-RSTP/HTTP
	public byte bBlocked = 1; //0- 非阻塞取流，1- 阻塞取流
}


public static class NET_DVR_CLIENTINFO extends Structure {
    public NativeLong lChannel;
    public NativeLong lLinkMode;
//    public HWND hPlayWnd;
    public String sMultiCastIP;
}

/***API函数声明,详细说明见API手册***/
public static interface FRealDataCallBack_V30 extends StdCallCallback {
     public void invoke(NativeLong lRealHandle, int dwDataType,
             ByteByReference pBuffer, int dwBufSize, Pointer pUser);
 }

//图片质量
public static class NET_DVR_JPEGPARA extends Structure {
/*注意：当图像压缩分辨率为VGA时，支持0=CIF, 1=QCIF, 2=D1抓图，
当分辨率为3=UXGA(1600x1200), 4=SVGA(800x600), 5=HD720p(1280x720),6=VGA,7=XVGA, 8=HD900p
仅支持当前分辨率的抓图*/
public short wPicSize;				/* 0=CIF, 1=QCIF, 2=D1 3=UXGA(1600x1200), 4=SVGA(800x600), 5=HD720p(1280x720),6=VGA*/
public short wPicQuality;			/* 图片质量系数 0-最好 1-较好 2-一般 */
}
}
