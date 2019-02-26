package cn.com.bjggs;

public class Constant {
	
	//从js文件中获取的字典集
	public static final int DICS = 1;
	
	//从enums中获取的字典集
	public static final int ENUMS = 2;
	
	//从数据库字典表中获取的字典集
	public static final int CODES = 4;
	
	//部门code-text对应集
	public static final int DEPTS = 8;
	
	//地域code-text对应集
	public static final int AREAS = 16;
	
	//ID_ENTRY 的默认ID值
	public static final long ID_DEFAULT = -1L;
	
	//默认的总条数
	public static final long TOTAL_DEFAULT = -1L;
	
	//当前登录用户在session中的变量名称
	public static final String SS_USER = "_SYS_USER_";
	
	//当前用户拥有权限的菜单列表在session中的变量名称
	public static final String SS_USER_MENUS = "_SYS_USER_MENUS_";
	
	//后台user验证失败提示信息
	public static final String NO_USER_MSG = "当前会话已超时，请重新登录系统！";
	
	public static final int W_TEMP = 0;//报警类别-温度
	public static final int W_TEMP_1 = 1;//温度高限
	public static final int W_TEMP_2 = 2;//温度升高率
	public static final int W_TEMP_3 = 3;//温度异常点
	public static final int W_TEMP_4 = 4;//层均温
	public static final int W_TEMP_5 = 5;//缺点率
	public static final int W_TEMP_6 = 6;//冷芯
	
	public static final int W_GRAIN = 1;//报警类别-储粮
	public static final int W_GRAIN_1 = 1;//满仓
	public static final int W_GRAIN_2 = 2;//半仓
	
	public static final int W_PEST = 2;//报警类别-测虫
	public static final int W_PEST_1 = 1;//预警
	public static final int W_PEST_2 = 2;//报警
	
	public static final int W_GAN = 3;//报警类别-测气
	public static final int W_GAN_1 = 1;//磷化氢
	public static final int W_GAN_2 = 2;//二氧化碳
	public static final int W_GAN_3 = 3;//氧气下限
	
	public static final int W_CTR = 4;//报警类别-控制
	public static final int W_CTR_1 = 1;//设备异常
	
	public static final int W_DO = 0;//检测异常
	
	public static final int PT_T = 1;//定时计划类型-温度
	public static final int PT_P = 2;//定时计划类型-虫害
	public static final int PT_G = 4;//定时计划类型-气体
	
	public static final int IDX_T_0 = 0;//温度高限
	public static final int IDX_T_1 = 1;//温度升高率
	public static final int IDX_T_2 = 2;//温度异常点
	public static final int IDX_T_3 = 3;//层均温
	public static final int IDX_T_4 = 4;//缺点率
	public static final int IDX_T_5 = 5;//冷芯
	public static final int IDX_C_0 = 6;//满仓
	public static final int IDX_C_1 = 7;//半仓
	public static final int IDX_P_0 = 8;//预警
	public static final int IDX_P_1 = 9;//报警
	public static final int IDX_G_0 = 10;//磷化氢
	public static final int IDX_G_1 = 11;//二氧化碳
	public static final int IDX_G_2 = 12;//氧气下限
	public static final int IDX_S_0 = 13;//设备异常
	
}
