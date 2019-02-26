package cn.com.bjggs.basis.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import org.nutz.lang.Times;

import cn.com.bjggs.Constant;
import cn.com.bjggs.ctr.enums.TypeSmart;
import cn.com.bjggs.ctr.util.CtrConstant;

public class WarnTld {

	public static final String get1(int i) {
		String req = "";
		switch (i) {
		case Constant.W_TEMP:
			req = "测温";
			break;
		case Constant.W_GAN:
			req = "测气";
			break;
		case Constant.W_PEST:
			req = "测虫";
			break;
		case Constant.W_GRAIN:
			req = "储粮";
			break;
		case Constant.W_CTR:
			req = "控制";
			break;
		default:
			break;
		}
		return req;
	}
	
	public static final String get2(int i, int j) {
		String req = "";
		switch (i) {
		case Constant.W_TEMP:
			switch (j) {
			case Constant.W_TEMP_1:
				req = "温度高限";
				break;
			case Constant.W_TEMP_2:
				req = "温度升高率";
				break;
			case Constant.W_TEMP_3:
				req = "温度异常点";
				break;
			case Constant.W_TEMP_4:
				req = "层均温";
				break;
			case Constant.W_TEMP_5:
				req = "缺点率";
				break;
			case Constant.W_TEMP_6:
				req = "冷芯";
				break;
			case Constant.W_DO:
				req = "检测异常";
				break;
			default:
				break;
			}
			break;
		case Constant.W_GAN:
			switch (j) {
			case Constant.W_GAN_1:
				req = "磷化氢";
				break;
			case Constant.W_GAN_2:
				req = "二氧化碳";
				break;
			case Constant.W_GAN_3:
				req = "氧气";
				break;
			case Constant.W_DO:
				req = "检测异常";
				break;
			default:
				break;
			}
			break;
		case Constant.W_PEST:
			switch (j) {
			case Constant.W_PEST_1:
				req = "测虫预警";
				break;
			case Constant.W_PEST_2:
				req = "测虫报警";
				break;
			case Constant.W_DO:
				req = "检测异常";
				break;
			default:
				break;
			}
			break;
		case Constant.W_GRAIN:
			switch (j) {
			case Constant.W_GRAIN_1:
				req = "满仓";
				break;
			case Constant.W_GRAIN_2:
				req = "半仓";
				break;
			case Constant.W_DO:
				req = "检测异常";
				break;
			default:
				break;
			}
			break;
		case Constant.W_CTR:
			switch (j) {
			case Constant.W_CTR_1:
				req = "设备异常";
				break;
			case Constant.W_DO:
				req = "检测异常";
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return req;
	}
	
	public static final String getModel(int model) {
		String s = "";
		switch (model) {
		case Constant.W_TEMP : s = "温度模块"; break;
		case Constant.W_CTR: s = "控制模块"; break;
		case Constant.W_PEST: s = "测虫模块"; break;
		case Constant.W_GAN: s = "测气模块"; break;
		default:
			break;
		}
		return s;
	}
	
	public static final String getEquipStatus(int model, byte status) {
		String s = "";
		switch (model) {
		case CtrConstant.WIND:
			switch (status) {
			case CtrConstant.R1O: s = "接到开窗指令"; break;
			case CtrConstant.R1OI: s = "正在开窗"; break;
			case CtrConstant.R1OA: s = "开窗到位"; break;
			case CtrConstant.R1C: s = "接到关窗指令"; break;
			case CtrConstant.R1CI: s = "正在关窗"; break;
			case CtrConstant.R1CA: s = "关窗到位"; break;
			case CtrConstant.R1S: s = "接到停止指令"; break;
			case CtrConstant.R1SA: s = "停在中间位置"; break;
			case CtrConstant.R1F: s = "窗户故障"; break;
			case CtrConstant.R1OT: s = "开窗超时"; break;
			case CtrConstant.R1CT: s = "关窗超时"; break;
			default:break;
			}
			break;
		case CtrConstant.ONEW:
			switch (status) {
			case CtrConstant.R2O: s = "接到启动指令"; break;
			case CtrConstant.R2OA: s = "已经启动"; break;
			case CtrConstant.R2S: s = "接到停止指令"; break;
			case CtrConstant.R2SA: s = "已经停止"; break;
			case CtrConstant.R2N: s = "风机没有启动"; break;
			case CtrConstant.R2F: s = "负载超载"; break;
			default:break;
			}
			break;
		case CtrConstant.WONE:
			switch (status) {
			case CtrConstant.R3O: s = "接到开启指令"; break;
			case CtrConstant.R3OW: s = "接到开窗指令"; break;
			case CtrConstant.R3OF: s = "接到开风机指令"; break;
			case CtrConstant.R3SF: s = "接到关风机指令"; break;
			case CtrConstant.R3SW: s = "接到关窗指令"; break;
			case CtrConstant.R3S: s = "接到关闭指令"; break;
			
			case CtrConstant.R3OWI: s = "正在开窗"; break;
			case CtrConstant.R3OWA: s = "开窗到位"; break;
			case CtrConstant.R3OFA: s = "风机已经启动"; break;
			case CtrConstant.R3SFA: s = "风机已经停止"; break;
			case CtrConstant.R3SWI: s = "正在关窗"; break;
			case CtrConstant.R3SWA: s = "关窗到位"; break;
			case CtrConstant.R3SWM: s = "窗户中间位置"; break;
			
			case CtrConstant.R3WN: s = "窗户故障"; break;
			case CtrConstant.R3OWT: s = "开窗超时"; break;
			case CtrConstant.R3SWT: s = "关窗超时"; break;
			case CtrConstant.R3F: s = "负载超载"; break;
			case CtrConstant.R3FN: s = "风机没有运行"; break;
			default: break;
			}
			break;
		case CtrConstant.TWOW:
			switch (status) {
			case CtrConstant.R4O: s = "接到正向启动指令"; break;
			case CtrConstant.R4OA: s = "正向已经启动"; break;
			case CtrConstant.R4Os: s = "接到方向启动指令"; break;
			case CtrConstant.R4OsA: s = "反向已经启动"; break;
			case CtrConstant.R4S: s = "接受停止指令"; break;
			case CtrConstant.R4SA: s = "风机已经停止"; break;
			case CtrConstant.R4N: s = "正向未运行"; break;
			case CtrConstant.R4F: s = "负载超载"; break;
			case CtrConstant.R4sN: s = "反向未运行"; break;
			default: break;
			}
			break;
		case CtrConstant.DIDO:
		case CtrConstant.AIRC:
		case CtrConstant.ARMDIO:
			switch (status) {
			case CtrConstant.IOT: s = "开启"; break;
			case CtrConstant.IOC: s = "关闭"; break;
			case CtrConstant.IOF: s = "故障"; break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return s;
	}
	
	public static final String getModelMsg(boolean tag, int type) {
		switch (type) {
		case 1: return tag ? TypeSmart.JWTF.starts() : TypeSmart.JWTF.ends();
		case 2: return tag ? TypeSmart.ZRTF.starts() : TypeSmart.ZRTF.ends();
		case 3: return tag ? TypeSmart.JSTF.starts() : TypeSmart.JSTF.ends();
		case 4: return tag ? TypeSmart.BSTF.starts() : TypeSmart.BSTF.ends();
		case 5: return tag ? TypeSmart.NHL.starts() : TypeSmart.NHL.ends();
		case 6: return tag ? TypeSmart.ZNKT.starts() : TypeSmart.ZNKT.ends();
		case 7: return tag ? TypeSmart.PJRTF.starts() : TypeSmart.PJRTF.ends();
		case 8: return tag ? TypeSmart.YXBH.starts() : TypeSmart.YXBH.ends();
		default: return "";
		}
	}
	
	public static final String getOperName(int key){
		switch (key) {
		case CtrConstant.CTR_OPEN_TAG: return "开启";
		case CtrConstant.CTR_CLOSE_TAG: return "关闭";
		case CtrConstant.CTR_OPENR_TAG: return "反向开启";
		case CtrConstant.CTR_STOP_TAG: return "停止";
		default: return "";
		}
	}
	
	public static final String getFileSize(File f, int type){
		DecimalFormat df = new DecimalFormat("#0.0");
		long bys = f.length();
		switch (type) {
		case 0 : return (df.format(bys) + "B");
		case 1 : return (df.format(bys/1024.0D) + "KB");
		case 2 : return (df.format(bys/(1024 * 1024.0)) + "MB");
		default:
			return "";
		}
	}
	
	public static final String getFileTime(File f){
		return Times.format("yyyy-MM-dd HH:mm:ss", new Date(f.lastModified()));
	}
	
}
