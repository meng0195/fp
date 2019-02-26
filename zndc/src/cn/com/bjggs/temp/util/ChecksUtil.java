package cn.com.bjggs.temp.util;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.enums.TypeCT;
import cn.com.bjggs.core.util.PropsUtil;
import cn.com.bjggs.temp.domain.ChecksLoop;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.warns.domain.AlarmNotes;

/**
 * 检测列表的
 * @author	wc
 * @date	2017-07-12
 */
public class ChecksUtil {
	
	public static final void initLastChecks(final Dao dao){
		Sql sql = Sqls.create("select t1.* from (select max(testDate) as testDate, HouseNo from TestData GROUP BY HouseNo) t left join TestData t1 on t.testDate = t1.testDate and t.HouseNo = t1.HouseNo");
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(TestData.class));
		dao.execute(sql);
		List<TestData> tds = sql.getList(TestData.class);
		TempResults res;
		for(TestData td : tds){
			res = new TempResults();
			res.setHouseNo(td.getHouseNo());
			res.setDatas(td);
			res.setTag(TypeCT.END.val());
			List<AlarmNotes> ws = dao.query(AlarmNotes.class, Cnd.where("testCode", "=", td.getId()));
			for(AlarmNotes w : ws){
				switch (w.getType1()) {
				case Constant.W_TEMP_1: res.setAn0(w); break;
				case Constant.W_TEMP_2: res.setAn1(w); break;
				case Constant.W_TEMP_3: res.setAn2(w); break;
				case Constant.W_TEMP_4: res.setAn3(w); break;
				case Constant.W_TEMP_5: res.setAn4(w); break;
				case Constant.W_TEMP_6: res.setAn5(w); break;
				case Constant.W_DO: res.setAn(w); break;
				default: break;
				}
			}
			lastChecks.put(td.getHouseNo(), res);
		}
	}
	
	public static final Set<String> TIMEINGS = new LinkedHashSet<String>();
	public static final Set<String> LOOPINGS = new LinkedHashSet<String>();
	public static final Queue<String> LOOPS = new LinkedList<String>();
	public static final Set<String> ONEINGS = new LinkedHashSet<String>();
	public static final Set<String> ONES = new LinkedHashSet<String>();
	public static final Set<String> WAITS = new LinkedHashSet<String>();
	
	public static final Map<String, Integer> SAVE_NUM = new LinkedHashMap<String, Integer>();
	
	public static final boolean isSaveLoop(String houseNo){
		boolean tag = false;
		if(SAVE_NUM.containsKey(houseNo)){
			tag = (((new Date()).getTime() - LAST_BEGIN_TIME.getTime())/(TEMP_DATA_SAVE * 60 * 1000)) >= SAVE_NUM.get(houseNo);
		}
		return tag;
	}
	
	/**
	 * 初始化循环检测列表
	 */
	public static final void initLoops(List<ChecksLoop> list){
		LOOPS.clear();
		for(ChecksLoop cl : list){
			LOOPS.offer(cl.getHouseNo());
			SAVE_NUM.put(cl.getHouseNo(), 0);
		}
	}
	
	public static boolean loopTag = false;
	/**
	 * 当前正在检测的仓房集合
	 */
	public static final Map<String, TempResults> lastChecks = new LinkedHashMap<String, TempResults>();
	/**
	 * 最后一次循环检测开始时间
	 */
	public static Date LAST_BEGIN_TIME = new Date();
	/**
	 * 数据插入间隔(分钟)
	 */
	public static int TEMP_DATA_SAVE = PropsUtil.getInteger("temp.data.save", 30);
	
}
