package cn.com.bjggs.report.action;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.Constant;
import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.enums.TypeHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.query.Page;
import cn.com.bjggs.core.query.TypeOrder;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.report.domain.ReportAll;
import cn.com.bjggs.report.domain.ReportModel;
import cn.com.bjggs.report.service.IReportService;
import cn.com.bjggs.report.util.ReportUtil;
import cn.com.bjggs.squery.domain.QueryTemp;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.system.util.PassUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.PointUtil;
import cn.com.bjggs.warns.domain.AlarmNotes;
import cn.com.bjggs.weather.domain.TestWeather;
import cn.com.bjggs.weather.util.WeatherUtil;

@IocBean
@At({"/report"})
public class ReportAction extends SysAction{
	
	private final Log log = Logs.getLog(getClass());

	@Inject
	private IReportService reportService;
	
	@At({"/all/main"})
	@Ok("jsp:/WEB-INF/admin/report/all-main.jsp")
	public void allMain(HttpServletRequest req){
		QueryTemp t = new QueryTemp();
		req.setAttribute("t", t);
	}
	
	@At({"/all/sheet"})
	@Ok("jsp:/WEB-INF/admin/report/all-sheet.jsp")
	public void allSheet(@Param("::t")QueryTemp t, HttpServletRequest req){
		Map<String, Map<String,Object>> maps = new LinkedHashMap<String, Map<String,Object>>();
		for (String s : HouseUtil.houseMap.keySet()) {
			Map<String,Object> map = reportService.getReportAll(t, s);
			maps.put(s, map);
		}
		String title = PassUtil._NAME;
		req.setAttribute("shs", HouseUtil.houseMap);
		req.setAttribute("maps", maps);
		req.setAttribute("title", title);
	}
	
	@At({"/detail/main"})
	@Ok("jsp:/WEB-INF/admin/report/detail-main.jsp")
	public void detailMain(HttpServletRequest req){
		
	}
	
	@At({"/detail/sheet"})
	@Ok("jsp:/WEB-INF/admin/report/detail-sheet.jsp")
	public void detailSheet(String houseNo, Date time, HttpServletRequest req){
		StoreHouse house = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		GrainInfo grain = HouseUtil.get(houseNo, TypeHouseConf.GRAIN.code(), GrainInfo.class);
		TestData temp = reportService.fetch(TestData.class, Cnd.where("houseNo", "=", houseNo).and("testDate", "=", time));
		Criteria cri = Cnd.cri();
		cri.where().and("houseNo", "=", houseNo).and("startTime", "<", time);
		cri.getOrderBy().desc("startTime");
		TestPest pest;
		TestGas gas;
		pest = reportService.fetch(TestPest.class, cri);
		if (pest == null) {
			pest = new TestPest();
		}
		gas = reportService.fetch(TestGas.class, cri);
		if (gas == null) {
			gas = new TestGas();
		}
		//可以从报警的an 中取nums
		int warn1s = 0;
		int warn2s = 0;
		//处理温度高限点数和温度升高率点数-wc-2017-10-03
		if(temp != null){
			//获取配置中的最大层数
			TempInfo ti = HouseUtil.get(houseNo, TypeHouseConf.TEMP.code(), TempInfo.class);
			//获取点集
			Map<String, PointInfo> points = PointUtil.getPoints1(houseNo);
			if(ti != null){
				int maxL = ti.getMaxLNum();
				int[] warn1 = new int[maxL];
				int[] warn2 = new int[maxL];
				int index = 0;
				PointInfo pi;
				for(byte b : temp.getWarns()){
					//温度高限
					if(WarnType.isWarn1(b)){
						pi = points.get(String.valueOf(index));
						if(pi != null){
							warn1[pi.getZaxis()] += 1;
							warn1s += 1;
						}
					}
					//温度升高率
					if(WarnType.isWarn2(b)){
						pi = points.get(String.valueOf(index));
						if(pi != null){
							warn2[pi.getZaxis()] += 1;
							warn2s += 1;
						}
					}
					index++;
				}
				req.setAttribute("warn1", warn1);
				req.setAttribute("warn2", warn2);
			}
			//获取缺点率
			AlarmNotes an5 = reportService.fetch(AlarmNotes.class, Cnd.where("type1", "=", Constant.W_TEMP_5).and("testCode", "=", temp.getId()));
			if(an5 != null && an5.getNums() > 0){
				req.setAttribute("warn3s", an5.getNums());
			}
		}
		req.setAttribute("warn1s", warn1s);
		req.setAttribute("warn2s", warn2s);
		TestWeather weather = WeatherUtil.testWeather;
		String title = PassUtil._NAME;
		req.setAttribute("title", title);
		req.setAttribute("house", house);
		req.setAttribute("g", grain);
		req.setAttribute("temp", temp);
		req.setAttribute("pest", pest);
		req.setAttribute("gas", gas);
		req.setAttribute("weather", weather);
	}
	
	@At({"/shunde"})
	@Ok("jsp:/WEB-INF/admin/report/shunde-list.jsp")
	public void shunde(HttpServletRequest req){
		Page<TestData> page = getPage("page.", "HouseNo", TypeOrder.ASC);
		Map<String, String[]> params = getParams4Admin();	
		params.put("ft_EQ_I_reportFlag", new String[]{"1"});
		if(params.containsKey("ft_LIKEL_S_testDate")){
			req.setAttribute("ft_LIKEL_S_testDate", params.get("ft_LIKEL_S_testDate")[0]);
		} else {
			params.put("ft_LIKEL_S_testDate", new String[]{Times.format("yyyy-MM-dd", new Date())});
			req.setAttribute("ft_LIKEL_S_testDate", Times.format("yyyy-MM-dd", new Date()));
		}
		page = reportService.findPage(TestData.class, page, params);
		List<ReportAll> list = new LinkedList<ReportAll>();
		if(page.getResult() != null){
			//将上报的数据放入内存，用于导出报表
			ReportUtil.reportDataList.clear();
			for(TestData td : page.getResult()){
				list.add(new ReportAll(td));
				ReportUtil.reportDataList.add(td);
			}
		}
		req.setAttribute("page", page);
		req.setAttribute("list", list);
	}
	
	@At({"/shunde/area"})
	@Ok("jsp:/WEB-INF/admin/report/shunde-area-list-${a.htype}.jsp")
	public void shundeArea(HttpServletRequest req){
		Page<TestData> page = getPage("page.", "testDate", TypeOrder.ASC);	//获取前端传来的以page开头的分页信息,并以houseNo升序排序存放进page中
		Map<String, String[]> params = getParams4Admin();	//封装管理后台查询参数(ft_查询参量_类型_变量)
		params.put("ft_EQ_I_reportFlag", new String[]{"1"});
		String date;
		if(params.containsKey("ft_LE_S_testDate")){
			date = params.get("ft_LE_S_testDate")[0];
			req.setAttribute("ft_LE_S_testDate", date);
			params.put("ft_GE_S_testDate", new String[]{date.substring(0, 5) + "01-01"});
		} else {
			date = Times.format("yyyy-MM-dd", new Date());
			params.put("ft_LE_S_testDate", new String[]{date});
			params.put("ft_GE_S_testDate", new String[]{date.substring(0, 5) + "01-01"});
			req.setAttribute("ft_LE_S_testDate", date);
		}
		if(params.containsKey("ft_EQ_S_houseNo")){
			req.setAttribute("ft_EQ_S_houseNo", params.get("ft_EQ_S_houseNo")[0]);
		} else {
			params.put("ft_EQ_S_houseNo", new String[]{"001"});
			req.setAttribute("ft_EQ_S_houseNo", "001");
		}
		page = reportService.findPage(TestData.class, page, params);	//执行分页查询,参数(执行的实体类,分页条件,前台传来的条件)
		req.setAttribute("page", page);
		String houseNo = params.get("ft_EQ_S_houseNo")[0];
		StoreHouse sh = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
		req.setAttribute("htype", sh.getHouseType() == 1 ? 1 : 0);
		req.setAttribute("year", date.substring(0, 4));
		List<ReportModel> reports = new LinkedList<ReportModel>();
		if(!Lang.isEmpty(page.getResult())){
			for(TestData td : page.getResult()){
				reports.add(new ReportModel(td, sh.getHouseType()));
			}
		}
		req.setAttribute("reports", reports);
	}
	
	@At({"/xls"})
	@Ok("raw:stream")
	public Object xls(String testDate, HttpServletRequest req){
		try {
			String downFileDir = Mvcs.getServletContext().getRealPath("/export");
			String downFileName = reportService.xlsAll(downFileDir, testDate);
			File file = new File(downFileName);
			try {
				String fileName = file.getName();
				String agent = Strings.sBlank(getRequest().getHeader("USER-AGENT"));  
		        if(agent.toLowerCase().indexOf("firefox") > 0) {
		        	fileName = new String(fileName.getBytes(Encoding.UTF8), Encoding.ISO_8859_1);
		        } else {
		        	fileName = URLEncoder.encode(fileName, Encoding.UTF8);
		        }
		        getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			} catch(Exception e) {
			}
			return file;
		} catch (Exception e) {
		}
		return null;
	}
	
	@At({"/xls1"})
	@Ok("raw:stream")
	public Object xls1(String testDate, String houseNo, HttpServletRequest req){
		try {
			String downFileDir = Mvcs.getServletContext().getRealPath("/export");
			StoreHouse sh = HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class);
			String downFileName;
			if(sh.getHouseType() == TypeHouse.WARE.val()){
				downFileName = reportService.xlsAll1(downFileDir, testDate, houseNo);
			} else {
				downFileName = reportService.xlsAll2(downFileDir, testDate, houseNo);
			}
			File file = new File(downFileName);
			try {
				String fileName = file.getName();
				String agent = Strings.sBlank(getRequest().getHeader("USER-AGENT"));  
		        if(agent.toLowerCase().indexOf("firefox") > 0) {
		        	fileName = new String(fileName.getBytes(Encoding.UTF8), Encoding.ISO_8859_1);
		        } else {
		        	fileName = URLEncoder.encode(fileName, Encoding.UTF8);
		        }
		        getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			} catch(Exception e) {
			}
			return file;
		} catch (Exception e) {
		}
		return null;
	}
	
	@At({"/prints"})
	@Ok("jsp:/WEB-INF/admin/report/print-list.jsp")
	public void prints(String testDate, HttpServletRequest req){
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("HouseNo");
		cri.where().andEquals("reportFlag", TypeFlag.YES.val());
		if(Strings.isBlank(testDate)){
			testDate = Times.format("yyyy-MM-dd", new Date());
		}
		cri.where().andLikeL("testDate", testDate);
		req.setAttribute("testDate", testDate);
		List<TestData> tds = reportService.query(TestData.class, cri);
		List<ReportAll> list = new LinkedList<ReportAll>();
		if(tds != null){
			for(TestData td : tds){
				list.add(new ReportAll(td));
			}
		}
		req.setAttribute("list", list);
	}
	
	@At({"/print/all"})
	@Ok("jsp:/WEB-INF/admin/report/prints-all.jsp")
	public void printAll(String testDate, HttpServletRequest req) {
		try {
			req.setAttribute("html", reportService.getPrints(testDate));
		} catch (Exception e) {
			req.setAttribute("html", "<div>请核对输入的信息</div>");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param houseNo
	 * @param tid
	 * @return
	 * 导出温度表格
	 * @author yucy
	 * @date 2018-05-22
	 */
	@At({"/xls/save"})
	@Ok("raw")
	public Object save(String houseNo, String tid) {
		try {
			String downFileDir = Mvcs.getServletContext().getRealPath("/export");
			String filePath = reportService.xlsDataExp(downFileDir);
			return new File(filePath);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.errorf("数据导出失败：%s", new Object[] { e.getMessage() });
			}
		}
		return null;
	}
	
	
	
	
}
