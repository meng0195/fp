package cn.com.bjggs.ctr.util;

import cn.com.bjggs.core.util.PropsUtil;

public class CtrConstant {
	
	//通风窗模式-指令
	public static final byte M1O = 0x11;	//开窗
	public static final byte M1C = 0x14;	//关窗
	public static final byte M1S = 0x17;	//停止

	//通风窗模式-回码
	public static final byte R1O = 0x11;	//接到开窗指令
	public static final byte R1OI = 0x12;	//正在开窗
	public static final byte R1OA = 0x13;	//开窗到位
	public static final byte R1C = 0x14;	//接到关窗指令
	public static final byte R1CI = 0x15;	//正在关窗
	public static final byte R1CA = 0x16;	//关窗到位
	public static final byte R1S = 0x17;	//接到停止指令
	public static final byte R1SA = 0x18;	//中间位置
	public static final byte R1F = (byte)0x94;	//窗户故障
	public static final byte R1OT = (byte)0x95;	//开窗超时
	public static final byte R1CT = (byte)0x96;	//关窗超时
	
	//单向风机模式
	public static final byte M2O = 0x21;	//风机启动
	public static final byte M2S = 0x23;	//风机停止

	//单项风机回码
	public static final byte R2O = 0x21;	//接到启动指令
	public static final byte R2OA = 0x22;	//已经启动
	public static final byte R2S = 0x23;	//接到停止指令
	public static final byte R2SA = 0x24;	//已经停止
	public static final byte R2OW = 0x25;	//正在开窗
	public static final byte R2CW = 0x26;	//正在关窗
	public static final byte R2C = 0x27;	//窗户已经关闭
	public static final byte R2N = (byte)0x97;	//风机没有启动
	public static final byte R2F = (byte)0x98;	//负载超载
	
	//窗+风机模式
	public static final byte M3O = 0x31;	//启动
	public static final byte M3OW = 0x32;	//开窗
	public static final byte M3OF = 0x33;	//开风机
	public static final byte M3SF = 0x34;	//关风机
	public static final byte M3SW = 0x35;	//关窗
	public static final byte M3S = 0x36;	//关闭
	
	public static final byte R3O = 0x31;	//接到开启指令
	public static final byte R3OW = 0x32;	//接到开窗指令
	public static final byte R3OF = 0x33;	//接到开风机指令
	public static final byte R3SF = 0x34;	//接到关风机指令
	public static final byte R3SW = 0x35;	//接到关窗指令
	public static final byte R3S = 0x36;	//接到关闭指令
	
	public static final byte R3OWI = 0x37;	//正在开窗
	public static final byte R3OWA = 0x38;	//开窗到位
	public static final byte R3OFA = 0x39;	//风机已经启动
	public static final byte R3SFA = 0x3A;	//风机已经停止
	public static final byte R3SWI = 0x3B;	//正在关窗
	public static final byte R3SWA = 0x3C;	//关窗到位
	public static final byte R3SWM = 0x3D;	//窗户中间位置
	public static final byte R3WN = (byte)0x94;	//窗户故障
	public static final byte R3OWT = (byte)0x95;	//开窗超时
	public static final byte R3SWT = (byte)0x96;	//关窗超时
	public static final byte R3F = (byte)0x98;	//负载超载
	public static final byte R3FN = (byte)0x99;	//风机没有运行
	
	//双向风机模式
	public static final byte M4O = 0x41;	//正向启动
	public static final byte M4Os = 0x43;	//反向启动
	public static final byte M4S = 0x45;	//停止
	
	public static final byte R4O = 0x41;	//接到正向启动指令
	public static final byte R4OA = 0x42;	//正向已经启动
	public static final byte R4Os = 0x43;	//接到方向启动指令
	public static final byte R4OsA = 0x44;	//反向已经启动
	public static final byte R4S = 0x45;	//接受停止指令
	public static final byte R4SA = 0x46;	//风机已经停止
	public static final byte R4N = (byte)0x99;	//风机没有运行
	public static final byte R4F = (byte)0x98;	//负载超载
	public static final byte R4sN = (byte)0x9A;	//反向未运行
	
	//开放模式
	public static final byte IOT = 0x01;	//IO开启状态
	public static final byte IOC = 0x00;	//IO关闭状态
	public static final byte IOI = 0x02;
	public static final byte IOF = (byte)0x91; //故障
	
	//模式代码
	public static final int WIND = 1;
	public static final int ONEW = 2;
	public static final int WONE = 3;
	public static final int TWOW = 4;
	public static final int DIDO = 5;
	public static final int AIRC = 6;
	public static final int ARMDIO = 7;
	public static final int ARMWIND = 8;
	public static final int ARMONEW = 9;
	public static final int ARMNHL = 10;
	
	//通讯模式代码
	public static final int LINE = 1;
	public static final int ONE = 2;
	
	//类
	public static final String C_OPEN = "open";
	public static final String C_ING = "ing";
	public static final String C_ING1 = "ing1";
	public static final String C_HALF = "half";
	public static final String C_CLOSE = "close";
	public static final String C_BAD = "bad";
	public static final String C_BAD1 = "bad1";
	
	public static final int DIO_STATUS = PropsUtil.getInteger("smart.dio.status", 1);
	public static final int DIO_FAULT = PropsUtil.getInteger("smart.dio.fault", 1);
	
	public static final int CTR_OPEN_TAG = 1;
	public static final int CTR_CLOSE_TAG = 2;
	public static final int CTR_STOP_TAG = 3;
	public static final int CTR_OPENR_TAG = 4;
	
	public static final int CTR_READ_LEN = 9;
}
