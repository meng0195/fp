package cn.com.bjggs.report.service.impl;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;

import cn.com.bjggs.basis.domain.GrainInfo;
import cn.com.bjggs.basis.domain.StoreHouse;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.basis.util.HouseUtil.TypeHouseConf;
import cn.com.bjggs.basis.util.MathUtil;
import cn.com.bjggs.basis.util.WarnType;
import cn.com.bjggs.core.enums.Codes;
import cn.com.bjggs.core.enums.Enums;
import cn.com.bjggs.core.enums.TypeFlag;
import cn.com.bjggs.core.service.impl.BaseServiceImpl;
import cn.com.bjggs.core.util.ParseUtil;
import cn.com.bjggs.export.util.XlsUtil;
import cn.com.bjggs.gas.domain.TestGas;
import cn.com.bjggs.pest.domain.TestPest;
import cn.com.bjggs.report.service.IReportService;
import cn.com.bjggs.report.util.ExpUtil;
import cn.com.bjggs.report.util.ReportUtil;
import cn.com.bjggs.report.util.XlsTools;
import cn.com.bjggs.squery.domain.QueryTemp;
import cn.com.bjggs.system.util.PassUtil;
import cn.com.bjggs.temp.domain.PointInfo;
import cn.com.bjggs.temp.domain.TempInfo;
import cn.com.bjggs.temp.domain.TestData;
import cn.com.bjggs.temp.util.PointUtil;

@IocBean(name = "reportService", args = { "refer:dao" })
public class ReportServiceImpl extends BaseServiceImpl implements IReportService{

	
	public ReportServiceImpl(Dao dao){
		this.dao = dao;
	}

	/**
	 * 仅获取到测温的信息,不符,舍弃
	 */
	@Override
	public List<TestData> getReportTemp(QueryTemp t) {
		String sqlState = "SELECT * FROM testdata t, (SELECT MAX(testdate) testdate, houseNo FROM testdata "
							+"WHERE BatchNo > "+ t.getStartTime().getTime() +" AND BatchNo < "+ t.getEndTime().getTime() +" GROUP BY HouseNo) b "
							+"WHERE t.testdate = b.testdate AND t.houseNo = b.houseNo ORDER BY t.HouseNo ASC";
		Sql sql = Sqls.create(sqlState);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(TestData.class));
		dao.execute(sql);
		List<TestData> temps = sql.getList(TestData.class);
		return temps;
	}

	/**
	 * 感觉效率不行
	 */
	@Override
	public Map<String, Object> getReportAll(QueryTemp t, String houseNo) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("s", HouseUtil.get(houseNo, TypeHouseConf.HOUSE.code(), StoreHouse.class));
		map.put("g", HouseUtil.get(houseNo, TypeHouseConf.GRAIN.code(), GrainInfo.class));
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo).andBetween("testDate", t.getStartTime(), t.getEndTime());
		cri.getOrderBy().desc("testDate");
		TestData temp = dao.fetch(TestData.class, cri);
		map.put("temp", temp);
		if(temp != null && temp.getTestDate() != null){
			cri = Cnd.cri();
			cri.where().andEquals("houseNo", houseNo).and("startTime", "<", temp.getTestDate());
			cri.getOrderBy().desc("startTime");
			TestPest pest = dao.fetch(TestPest.class, cri);
			TestGas gas = dao.fetch(TestGas.class, cri);
			map.put("pest", pest);
			map.put("gas", gas);
			int w_t_1 = 0;
			int w_t_2 = 1;
			if(temp.getWarns() != null){
				for(byte b :temp.getWarns()){
					if(WarnType.isWarn1(b)) w_t_1 += 1;
					if(WarnType.isWarn2(b)) w_t_2 += 1;
				}
			}
			map.put("w_t_1", w_t_1);
			map.put("w_t_2", w_t_2);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public String xlsAll(String path, String testDate) {
		String tmpFilePath = path + "/" + Times.format("yyyyMMdd", Times.now());
		Files.createDirIfNoExists(path);
		Files.createDirIfNoExists(tmpFilePath);
		String filePath = tmpFilePath
				+ "/上报报表" + Times.format("yyyyMMddHHmmss", Times.now()) + ".xls";
		Criteria cri = Cnd.cri();
		cri.where().andEquals("reportFlag", 1);
		cri.where().andLikeL("testDate", testDate);
		cri.getOrderBy().asc("houseNo");
		List<TestData> ts = dao.query(TestData.class, cri);
		GrainInfo gi;
		WritableWorkbook book = null;
		try {
			XlsUtil xls = new XlsUtil();
			book = Workbook.createWorkbook(new File(filePath));
			WritableSheet sheet = xls.createSheet(book, "粮情检测周报表");
			sheet.setRowView(0, 615);
			sheet.setRowView(1, 615);
			sheet.setRowView(2, 530);
			sheet.setRowView(3, 915);
			sheet.setColumnView(0, 6);
			sheet.setColumnView(1, 7);
			sheet.setColumnView(2, 13);
			sheet.setColumnView(3, 7);
			sheet.setColumnView(4, 13);
			sheet.setColumnView(5, 6);
			sheet.setColumnView(6, 6);
			sheet.setColumnView(7, 6);
			sheet.setColumnView(8, 7);
			sheet.setColumnView(9, 9);
			sheet.setColumnView(10, 9);
			sheet.setColumnView(11, 7);
			sheet.setColumnView(12, 6);
			sheet.setColumnView(13, 6);
			sheet.setColumnView(14, 6);
			sheet.setColumnView(15, 9);
			sheet.setColumnView(16, 10);
			sheet.setColumnView(17, 9);
			sheet.mergeCells(0, 0, 17, 0);
			sheet.mergeCells(0, 1, 1, 1);
			Label label;
			label = new Label(0, 0, "省级储备粮粮情监测周报表", XlsTools.getTitleStyle());
			sheet.addCell(label);
			label = new Label(0, 1, "承储单位名称:", XlsTools.getCeneterStyle());
			sheet.addCell(label);
			sheet.mergeCells(2, 1, 5, 1);
			label = new Label(2, 1, PassUtil._NAME, XlsTools.getCeneterStyle());
			sheet.addCell(label);
			sheet.mergeCells(6, 1, 7, 1);
			label = new Label(6, 1, "气温(℃):", XlsTools.getCeneterStyle());
			sheet.addCell(label);
			sheet.mergeCells(9, 1, 10, 1);
			label = new Label(9, 1, "气湿(%):", XlsTools.getCeneterStyle());
			sheet.addCell(label);
			sheet.mergeCells(14, 1, 15, 1);
			sheet.mergeCells(16, 1, 17, 1);
			label = new Label(14, 1, "检测日期:", XlsTools.getCeneterStyle());
			sheet.addCell(label);
			label = new Label(16, 1, testDate, XlsTools.getCeneterStyle());
			sheet.addCell(label);
			sheet.mergeCells(0, 2, 0, 3);
			label = new Label(0, 2, "仓号", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(1, 2, 1, 3);
			label = new Label(1, 2, "粮食\n品种", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(2, 2, 2, 3);
			label = new Label(2, 2, "库存数量\n(吨)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(3, 2, 3, 3);
			label = new Label(3, 2, "收获\n年度", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(4, 2, 4, 3);
			label = new Label(4, 2, "入库年月", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(5, 2, 5, 3);
			label = new Label(5, 2, "粮食\n水分\n(%)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(6, 2, 6, 3);
			label = new Label(6, 2, "仓温\n(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(7, 2, 7, 3);
			label = new Label(7, 2, "仓湿\n(%)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(8, 2, 14, 2);
			label = new Label(8, 2, "粮温(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			label = new Label(8, 3, "上层平\n均粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			label = new Label(9, 3, "中上层平\n均粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			label = new Label(10, 3, "中下层平\n均粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			label = new Label(11, 3, "下层平\n均粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			label = new Label(12, 3, "最高\n粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			label = new Label(13, 3, "最低\n粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			label = new Label(14, 3, "平均\n粮温", XlsTools.getNolmalStyle10());
			sheet.addCell(label);
			sheet.mergeCells(15, 2, 15, 3);
			label = new Label(15, 2, "密闭\n与否", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(16, 2, 16, 3);
			label = new Label(16, 2, "熏蒸\n(富氮)\n与否", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			sheet.mergeCells(17, 2, 17, 3);
			label = new Label(17, 2, "进出仓\n状态", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			int y = 4;
			if(!Lang.isEmpty(ts)){
				for(TestData t : ts){
					if(y==4){
						label = new Label(8, 1, String.format("%.1f", t.getOutT()), XlsTools.getCeneterStyle());
						sheet.addCell(label);
						label = new Label(11, 1, String.format("%.1f", t.getOutH()), XlsTools.getCeneterStyle());
						sheet.addCell(label);
					}
					gi = HouseUtil.get(t.getHouseNo(), TypeHouseConf.GRAIN.code(), GrainInfo.class);
					label = new Label(0, y, HouseUtil.houseMap.get(gi.getHouseNo()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(1, y, Codes.get("TYPE_GRAIN", "" + gi.getGrainCode()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(2, y, String.format("%.3f", gi.getGrainCount()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(3, y, "" + gi.getGainYear(), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(4, y, XlsTools.changeYear(Times.format("yyyy-MM", gi.getDateOfIn())), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(5, y, String.format("%.1f", gi.getGrainWater()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(6, y, String.format("%.1f", t.getInT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(7, y, String.format("%.1f", t.getInH()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(8, y, String.format("%.1f", ((List<Object>)(t.getLayerTsMap().get("0"))).get(0)), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(9, y, String.format("%.1f", ((List<Object>)(t.getLayerTsMap().get("1"))).get(0)), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(10, y, String.format("%.1f", ((List<Object>)(t.getLayerTsMap().get("2"))).get(0)), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(11, y, String.format("%.1f", ((List<Object>)(t.getLayerTsMap().get("3"))).get(0)), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(12, y, String.format("%.1f", t.getMaxT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(13, y, String.format("%.1f", t.getMinT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(14, y, String.format("%.1f", t.getAvgT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(15, y, Codes.get("TYPE_MB", ""+t.getMbTag()), XlsTools.getNolmalStyle10());
					sheet.addCell(label);
					label = new Label(16, y, Codes.get("TYPE_XZ", ""+t.getXzTag()), XlsTools.getNolmalStyle10());
					sheet.addCell(label);
					label = new Label(17, y, Codes.get("TYPE_JCC", ""+t.getJccTag()), XlsTools.getNolmalStyle10());
					sheet.addCell(label);
					y += 1;
				}
				sheet.mergeCells(0, y, 17, y+1);
				label = new Label(0, y, "", XlsTools.getNolmalStyle());
				sheet.addCell(label);
				y += 3;
				label = new Label(1, y, "负责人：");
				sheet.addCell(label);
				label = new Label(5, y, "审核人：");
				sheet.addCell(label);
				label = new Label(9, y, "填写人：");
				sheet.addCell(label);
				label = new Label(13, y, "填写日期：");
				sheet.addCell(label);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (book != null) {
				try {
					book.write();
					book.close();
					book = null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return filePath;
	}
	
	@SuppressWarnings("unchecked")
	public String xlsAll1(String path, String testDate, String houseNo){
		String tmpFilePath = path + "/" + Times.format("yyyyMMdd", Times.now());
		Files.createDirIfNoExists(path);
		Files.createDirIfNoExists(tmpFilePath);
		String filePath = tmpFilePath
				+ "/房式仓粮情记录" + Times.format("yyyyMMddHHmmss", Times.now()) + ".xls";
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("testDate");
		cri.where().andEquals("reportFlag", 1);
		cri.where().and("testDate", "<=", testDate);
		cri.where().andEquals("houseNo", houseNo);
		cri.where().and("testDate", ">=", testDate.substring(0, 5) + "01-01");
		List<TestData> ts = dao.query(TestData.class, cri);
		WritableWorkbook book = null;
		try {
			XlsUtil xls = new XlsUtil();
			book = Workbook.createWorkbook(new File(filePath));
			WritableSheet sheet = xls.createSheet(book, "粮情检查记录表_房式仓");
			for(int i = 0; i < 37; i++){
				sheet.setColumnView(i, 8);
			}
			sheet.mergeCells(0, 1, 36, 1);
			Label label;
			label = new Label(0, 1, "粮情检查记录表", XlsTools.getTitleStyle());
			sheet.addCell(label);
			label = new Label(0, 2, "仓号：", XlsTools.getCeneterStyle());
			sheet.addCell(label);
			label = new Label(1, 2, Codes.get("houses", houseNo), XlsTools.getCeneterStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(0, 3, 1, 3);
			label = new Label(0, 3, "日期", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(0, 4, 1, 4);
			label = new Label(0, 4, testDate.substring(0, 4), XlsTools.getNolmalStyle());
			sheet.addCell(label);
			label = new Label(0, 5, "月", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			label = new Label(1, 5, "日", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(2, 3, 2, 5);
			label = new Label(2, 3, "天气", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(3, 3, 3, 5);
			label = new Label(3, 3, "大气温度(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(4, 3, 4, 5);
			label = new Label(4, 3, "大气湿度(%)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(5, 3, 5, 5);
			label = new Label(5, 3, "仓温(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(6, 3, 6, 5);
			label = new Label(6, 3, "仓湿(%)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(7, 3, 31, 3);
			label = new Label(7, 3, "粮温情况(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(7, 4, 10, 4);
			label = new Label(7, 4, "中央", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			for(int i = 7; i < 31; i++){
				switch ((i - 7)%4) {
				case 0:
					label = new Label(i, 5, "上层", XlsTools.getNolmalStyle());
					break;
				case 1:
					label = new Label(i, 5, "中上层", XlsTools.getNolmalStyle());
					break;
				case 2:
					label = new Label(i, 5, "中下层", XlsTools.getNolmalStyle());
					break;
				case 3:
					label = new Label(i, 5, "下层", XlsTools.getNolmalStyle());
					break;
				default:
					break;
				}
				sheet.addCell(label);
			}
			
			sheet.mergeCells(11, 4, 14, 4);
			label = new Label(11, 4, "东南角", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(15, 4, 18, 4);
			label = new Label(15, 4, "西南角", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(19, 4, 22, 4);
			label = new Label(19, 4, "西北角", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(23, 4, 26, 4);
			label = new Label(23, 4, "东北角", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(27, 4, 30, 4);
			label = new Label(27, 4, "最高粮温", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(31, 4, 31, 5);
			label = new Label(31, 4, "平均粮温", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(32, 3, 34, 3);
			label = new Label(32, 3, "虫害情况", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(32, 4, 32, 5);
			label = new Label(32, 4, "虫害密度", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(33, 4, 33, 5);
			label = new Label(33, 4, "虫害虫类", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(34, 4, 34, 5);
			label = new Label(34, 4, "虫粮等级", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(35, 3, 35, 5);
			label = new Label(35, 3, "检查人", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(36, 3, 36, 5);
			label = new Label(36, 3, "备注", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			List<Object> areas;
			int y = 6;
			double max1, max2, max3, max4;
			if(!Lang.isEmpty(ts)){
				for(TestData t : ts){
					max1 = 0;
					max2 = 0;
					max3 = 0;
					max4 = 0;
					label = new Label(0, y, Times.format("MM", t.getTestDate()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(1, y, Times.format("dd", t.getTestDate()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(2, y, Codes.get("TYPE_TQ", "" + t.getWeaTag()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(3, y, String.format("%.1f", t.getOutT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(4, y, String.format("%.1f", t.getOutH()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(5, y, String.format("%.1f", t.getInT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(6, y, String.format("%.1f", t.getInH()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					if(t.getAreaTsMap() != null){
						if(t.getAreaTsMap().containsKey("3")){
							areas = (List<Object>)(t.getAreaTsMap().get("3"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(7, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(8, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(9, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(10, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						if(t.getAreaTsMap().containsKey("5")){
							areas = (List<Object>)(t.getAreaTsMap().get("5"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(11, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(12, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(13, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(14, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						if(t.getAreaTsMap().containsKey("4")){
							areas = (List<Object>)(t.getAreaTsMap().get("4"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(15, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(16, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(17, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(18, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						if(t.getAreaTsMap().containsKey("4")){
							areas = (List<Object>)(t.getAreaTsMap().get("1"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(19, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(20, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(21, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(22, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						if(t.getAreaTsMap().containsKey("2")){
							areas = (List<Object>)(t.getAreaTsMap().get("2"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(23, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(24, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(25, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(26, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						label = new Label(27, y, String.format("%.1f", max1), XlsTools.getNolmalStyle());
						sheet.addCell(label);
						label = new Label(28, y, String.format("%.1f", max2), XlsTools.getNolmalStyle());
						sheet.addCell(label);
						label = new Label(29, y, String.format("%.1f", max3), XlsTools.getNolmalStyle());
						sheet.addCell(label);
						label = new Label(30, y, String.format("%.1f", max4), XlsTools.getNolmalStyle());
						sheet.addCell(label);
					}
					label = new Label(31, y, String.format("%.1f", t.getAvgT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					for(int i = 32; i < 37; i++){
						label = new Label(i, y, "", XlsTools.getNolmalStyle());
						sheet.addCell(label);
					}
					y += 1;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (book != null) {
				try {
					book.write();
					book.close();
					book = null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return filePath;
	}
	
	@SuppressWarnings("unchecked")
	public String xlsAll2(String path, String testDate, String houseNo){
		String tmpFilePath = path + "/" + Times.format("yyyyMMdd", Times.now());
		Files.createDirIfNoExists(path);
		Files.createDirIfNoExists(tmpFilePath);
		String filePath = tmpFilePath
				+ "/原型仓粮情记录" + Times.format("yyyyMMddHHmmss", Times.now()) + ".xls";
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("testDate");
		cri.where().andEquals("reportFlag", 1);
		cri.where().and("testDate", "<=", testDate);
		cri.where().andEquals("houseNo", houseNo);
		cri.where().and("testDate", ">=", testDate.substring(0, 5) + "01-01");
		List<TestData> ts = dao.query(TestData.class, cri);
		WritableWorkbook book = null;
		try {
			XlsUtil xls = new XlsUtil();
			book = Workbook.createWorkbook(new File(filePath));
			WritableSheet sheet = xls.createSheet(book, "粮情检查记录表_房式仓");
			for(int i = 0; i < 29; i++){
				sheet.setColumnView(i, 8);
			}
			sheet.mergeCells(0, 1, 28, 1);
			Label label;
			label = new Label(0, 1, "粮情检查记录表", XlsTools.getTitleStyle());
			sheet.addCell(label);
			label = new Label(0, 2, "仓号：", XlsTools.getCeneterStyle());
			sheet.addCell(label);
			label = new Label(1, 2, Codes.get("houses", houseNo), XlsTools.getCeneterStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(0, 3, 1, 3);
			label = new Label(0, 3, "日期", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(0, 4, 1, 4);
			label = new Label(0, 4, testDate.substring(0, 4), XlsTools.getNolmalStyle());
			sheet.addCell(label);
			label = new Label(0, 5, "月", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			label = new Label(1, 5, "日", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(2, 3, 2, 5);
			label = new Label(2, 3, "天气", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(3, 3, 3, 5);
			label = new Label(3, 3, "大气温度(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(4, 3, 4, 5);
			label = new Label(4, 3, "大气湿度(%)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(5, 3, 5, 5);
			label = new Label(5, 3, "仓温(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(6, 3, 6, 5);
			label = new Label(6, 3, "仓湿(%)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(7, 3, 23, 3);
			label = new Label(7, 3, "粮温情况(℃)", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(7, 4, 10, 4);
			label = new Label(7, 4, "内圈", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			for(int i = 7; i < 23; i++){
				switch ((i - 7)%4) {
				case 0:
					label = new Label(i, 5, "上层", XlsTools.getNolmalStyle());
					break;
				case 1:
					label = new Label(i, 5, "中上层", XlsTools.getNolmalStyle());
					break;
				case 2:
					label = new Label(i, 5, "中下层", XlsTools.getNolmalStyle());
					break;
				case 3:
					label = new Label(i, 5, "下层", XlsTools.getNolmalStyle());
					break;
				default:
					break;
				}
				sheet.addCell(label);
			}
			
			sheet.mergeCells(11, 4, 14, 4);
			label = new Label(11, 4, "中圈", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(15, 4, 18, 4);
			label = new Label(15, 4, "外圈", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(19, 4, 22, 4);
			label = new Label(19, 4, "最高粮温", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(23, 4, 23, 5);
			label = new Label(23, 4, "平均粮温", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(24, 3, 26, 3);
			label = new Label(24, 3, "虫害情况", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(24, 4, 24, 5);
			label = new Label(24, 4, "虫害密度", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(25, 4, 25, 5);
			label = new Label(25, 4, "虫害虫类", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(26, 4, 26, 5);
			label = new Label(26, 4, "虫粮等级", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(27, 3, 27, 5);
			label = new Label(27, 3, "检查人", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			
			sheet.mergeCells(28, 3, 28, 5);
			label = new Label(28, 3, "备注", XlsTools.getNolmalStyle());
			sheet.addCell(label);
			List<Object> areas;
			int y = 6;
			double max1, max2, max3, max4;
			if(!Lang.isEmpty(ts)){
				for(TestData t : ts){
					max1 = 0;
					max2 = 0;
					max3 = 0;
					max4 = 0;
					label = new Label(0, y, Times.format("MM", t.getTestDate()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(1, y, Times.format("dd", t.getTestDate()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(2, y, Codes.get("TYPE_TQ", "" + t.getWeaTag()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(3, y, String.format("%.1f", t.getOutT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(4, y, String.format("%.1f", t.getOutH()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(5, y, String.format("%.1f", t.getInT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					label = new Label(6, y, String.format("%.1f", t.getInH()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					if(t.getAreaTsMap() != null){
						if(t.getAreaTsMap().containsKey("1")){
							areas = (List<Object>)(t.getAreaTsMap().get("1"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(7, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(8, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(9, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(10, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						if(t.getAreaTsMap().containsKey("2")){
							areas = (List<Object>)(t.getAreaTsMap().get("2"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(11, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(12, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(13, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(14, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						if(t.getAreaTsMap().containsKey("3")){
							areas = (List<Object>)(t.getAreaTsMap().get("3"));
							if(areas.size() > 0){
								max1 = Math.max(max1, (Double)areas.get(0));
								label = new Label(15, y, String.format("%.1f", areas.get(0)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 1){
								max2 = Math.max(max2, (Double)areas.get(1));
								label = new Label(16, y, String.format("%.1f", areas.get(1)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 2){
								max3 = Math.max(max3, (Double)areas.get(2));
								label = new Label(17, y, String.format("%.1f", areas.get(2)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
							if(areas.size() > 3){
								max4 = Math.max(max4, (Double)areas.get(3));
								label = new Label(18, y, String.format("%.1f", areas.get(3)), XlsTools.getNolmalStyle());
								sheet.addCell(label);
							}
						}
						label = new Label(19, y, String.format("%.1f", max1), XlsTools.getNolmalStyle());
						sheet.addCell(label);
						label = new Label(20, y, String.format("%.1f", max2), XlsTools.getNolmalStyle());
						sheet.addCell(label);
						label = new Label(21, y, String.format("%.1f", max3), XlsTools.getNolmalStyle());
						sheet.addCell(label);
						label = new Label(22, y, String.format("%.1f", max4), XlsTools.getNolmalStyle());
						sheet.addCell(label);
					}
					label = new Label(23, y, String.format("%.1f", t.getAvgT()), XlsTools.getNolmalStyle());
					sheet.addCell(label);
					for(int i = 24; i < 29; i++){
						label = new Label(i, y, "", XlsTools.getNolmalStyle());
						sheet.addCell(label);
					}
					y += 1;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (book != null) {
				try {
					book.write();
					book.close();
					book = null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return filePath;
	}
	
	@SuppressWarnings("unchecked")
	public String getPrints(String testDate){
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("HouseNo");
		cri.where().andEquals("reportFlag", TypeFlag.YES.val());
		if(Strings.isBlank(testDate)){
			testDate = Times.format("yyyy-MM-dd", new Date());
		}
		cri.where().andLikeL("testDate", testDate);
		List<TestData> list = dao.query(TestData.class, cri);
		StoreHouse house;
		GrainInfo grain;
		List<Double> lyers;
		TempInfo eqment;
		List<List<List<PointInfo>>> points;
		StringBuffer sb = new StringBuffer();
		for(TestData t : list){
			if(t == null || Strings.isBlank(t.getHouseNo())){
				continue;
			}
			house = HouseUtil.get(t.getHouseNo(), TypeHouseConf.HOUSE.code(), StoreHouse.class);
			grain = HouseUtil.get(t.getHouseNo(), TypeHouseConf.GRAIN.code(), GrainInfo.class);
			sb.append("<div class=\"printDiv\"><div class=\"binding\"></div><div class=\"sheet-title\">")
			    .append("<div class=\"rucang\">入仓时间：")
			    .append(Times.format("yyyy-MM-dd HH:mm", grain.getDateOfIn()))
			    .append("</div>")
			    .append("<div class=\"jiance\">检测时间：")
			    .append(Times.format("yyyy-MM-dd HH:mm", t.getTestDate()))
			    .append("</div>")
				.append(PassUtil._NAME)
				.append(house.getHouseName())
				.append("粮情报表</div><table class=\"sheet-title2\" ><tbody><tr><td>仓房名称：</td><td style=\"width=10%;\">")
				.append(house.getHouseName())
				.append("</td><td >仓房类型：</td><td>")
				.append(Enums.get("TYPE_HOUSE", "" + house.getHouseType()))
				.append("</td><td style=\"width:10%;\">设计储量：</td><td style=\"width:100px;\">")
				.append(house.getStoreCount())
				.append("t</td><td>实际储量：</td><td>")
				.append(grain.getGrainCount())
				.append("t</td></tr><tr><td>粮食品种：</td><td>")
				.append(Codes.get("TYPE_GRAIN", "" + grain.getGrainCode()))
				.append("</td><td>保管员：</td><td>")
				.append(grain.getKeeperName())
				.append("</td><td>入仓水分：</td><td >")
				.append(grain.getGrainInWater())
				.append("%</td><td>存储方式：</td><td >")
				.append(Codes.get("TYPE_STORE", "" + grain.getStorageMode()))
				.append("</td></tr><tr><td>入仓杂质：</td><td>")
				.append(grain.getImpurity())
				.append("%</td><td>等级：</td><td>")
				.append(Codes.get("TYPE_GRADE", grain.getGrade()))
				.append("</td><td>储粮性质：</td><td>")
				.append(Codes.get("TYPE_NATURE", "" + grain.getNature()))
				.append("</td>")
				.append("<td>不完善粒：</td><td>")
				.append(grain.getUnsoKer())
				.append("%</td>")
				.append("</tr><tr><td>仓温</td><td>")
				.append(t.getInT())
				.append("℃</td><td>仓湿：</td><td>")
				.append(t.getInH())
				.append("%</td><td>气温：</td><td>")
				.append(t.getOutT())
				.append("℃</td><td>气湿：</td><td>")
				.append(t.getOutH())
				.append("%</td></tr><tr><td width=\"75\"></td></td><td>  <td width=\"34\">高</td></td><td>  <td width=\"34\">低</td></td><td>  <td width=\"34\">均</td></tr><tr><td>整仓：</td><td></td>  <td>")
				.append(t.getMaxT())
				.append("</td><td></td> <td>")
				.append(t.getMinT())
				.append("</td><td></td> <td>")
				.append(t.getAvgT())
				.append("</td></tr>");
			if(t.getLayerTsMap() != null){
				for(Map.Entry<String, Object> entry : t.getLayerTsMap().entrySet()){
					lyers = (List<Double>)entry.getValue();
					sb.append("<tr><td>")
						.append(ParseUtil.toInt(entry.getKey()) + 1)
						.append("层：</td> <td></td>  <td>");
					if(lyers != null && lyers.size() == 3){
						sb.append(lyers.get(1))
							.append("</td></td><td>  <td>")
							.append(lyers.get(2))
							.append("</td></td><td>  <td>")
							.append(lyers.get(0))
							.append("</td></td><td>  </tr>");
					} else {
						sb.append("</td><td></td><td></td></tr>");
					}
				}
			}
			sb.append("</tbody></table>");
			eqment = HouseUtil.get(t.getHouseNo(), TypeHouseConf.TEMP.code(), TempInfo.class);
			points = PointUtil.getPointLevel(t.getHouseNo());
			sb.append(PointUtil.getSheet(points, eqment, t));
			sb.append("<div class=\"sheet-lq\">粮情分析：<br><br><table class=\"sheet-title3\"><tbody><tr><td>注：</td><td>最高粮温:\"↑\"</td><td>最低粮温:\"↓\"</td><td>无温度:\"-\"</td><td>温度单位:℃</td><td>湿度单位:%</td><td>制表时间:")
				.append(Times.format("yyyy-MM-dd HH:mm", new Date()))
				.append("</td></tr></tbody></table></div></div><div class=\"pageBreaker\"></div>");
		}
		return sb.toString();
	}

	
	
	
	/**
	 * @author	yucy
	 * @date	2018-05-22
	 * 导出所有温度点
	 */
	@Override
	public String xlsDataExp(String downFileDir) {
		//保存xlsExps
		List<TestData> Relist = ReportUtil.reportDataList;
		if(Relist == null || Relist.size() == 0) throw new RuntimeException("请选择导出的仓房！");
		
		String filePath = xlsBungalow(downFileDir, Relist);
		filePath = xlsBungalow(downFileDir, Relist);
		return filePath;
	}
	
	//平房仓导出excel
	private String xlsBungalow(String downFileDir, List<TestData> list) {
		
		
		String filePrefix = "粮情报表";
		String tmpFilePath = downFileDir + "/" + System.currentTimeMillis();
		Files.createDirIfNoExists(downFileDir);
		Files.createDirIfNoExists(tmpFilePath);

		String filePath = tmpFilePath
				+ "/"
				+ filePrefix
				+ Times.format("yyyyMMdd_HH：mm", new Date())
				+ ".xls";
		
		int len = list.size();
		int num = 1;
		
		//获取这次检测记录
		
		Label label = null;
		WritableWorkbook wwb = null;
		try{
			wwb = Workbook.createWorkbook(new File(filePath));
			WritableSheet sheet;
			String houseNo;
			TestData ts = null;
			int x = 0;
			int y = 0;
			int xx = 0;
			int maxY = 0;
			List<List<PointInfo>> pss;
			List<PointInfo> ps;
			PointInfo p;
			double tempDouble;
			String temp;
			WritableCellFormat title = setWSStyle("");
			WritableCellFormat normal = setWSStyle("normal");
			//WritableCellFormat noBorder = setWSStyle("noBorder");
			WritableCellFormat border = setWSStyle("border");
			for(int i = 0; i < list.size(); i++){
				//获取该仓房的电缆排布信息
				TempInfo tempi = HouseUtil.get(list.get(i).getHouseNo(), TypeHouseConf.TEMP.code(), TempInfo.class);
				
				String name = "";
				for(int j = num * i; j < Math.min(num * (i+1), len); j++){
					name += Codes.get("houses", list.get(i).getHouseNo());
					if(j < Math.min(num * (i+1), len)-1){
						name += ",";
					}
				}
				sheet = wwb.createSheet(name, Math.max(i, 0));
				x = 0;
				xx = 0;
				ExpUtil.setCellWidth(sheet, 0, 0, 2);
				for(int j = num * i; j < Math.min(num * (i+1), len); j++){
					y = 0;
					maxY = 0;
					//设置列宽
					
					ExpUtil.setCellWidth(sheet, xx+1, xx + 14, 6);
					houseNo = list.get(i).getHouseNo();
					sheet.mergeCells(xx + 1, y, xx + 14, y);
					label = new Label(xx + 1, y, Codes.get("houses", houseNo) + " 粮  情  报  表", title);
					sheet.addCell(label);
					ts = null;
					pss = getPoints(houseNo);
					
					for(TestData t : list){
						if(t.getHouseNo().equals(houseNo)){
							ts = t;
							break;
						}
					}
					y = y+1;
					label = new Label(xx + 1, y, "仓温");
					sheet.mergeCells(xx + 2, y, xx + 3, y);
					sheet.addCell(label);
					label = new Label(xx + 2, y, ts != null ? ("" + ts.getInT() + "℃") : "-");
					sheet.addCell(label);
					
					label = new Label(xx + 4, y, "仓湿");
					sheet.addCell(label);
					sheet.mergeCells(xx + 5, y, xx + 6, y);
					label = new Label(xx + 5, y, ts != null ? ("" + ts.getInH() + "%") : "-");
					sheet.addCell(label);
					
					label = new Label(xx + 7, y, "外温");
					sheet.addCell(label);
					sheet.mergeCells(xx + 8, y, xx + 9, y);
					label = new Label(xx + 8, y, ts != null ? ("" + ts.getOutT() + "℃") : "-");
					sheet.addCell(label);
					
					label = new Label(xx + 10, y, "外湿");
					sheet.addCell(label);
					sheet.mergeCells(xx + 11, y, xx + 13, y);
					label = new Label(xx + 11, y, ts != null ? ("" + ts.getOutH() + "%") : "-");
					sheet.addCell(label);
					y = y+1;
					
					xx+=1;
					sheet.mergeCells(xx, y, xx+1, y);
					label = new Label(xx , y, "检测时间:");
					sheet.addCell(label);
					sheet.mergeCells(xx+2, y, xx+4, y);
					try {
						label = new Label(xx+2,y, Times.format("yyyy/MM/dd HH:mm", ts.getTestDate()));
						sheet.addCell(label);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					sheet.mergeCells(xx+6, y, xx+7, y);
					label = new Label(xx+6,y ,"打印时间:");
					sheet.addCell(label);
					sheet.mergeCells(xx+8, y, xx+10, y);
					label = new Label(xx+8,y,Times.format("yyyy/MM/dd HH:mm", new Date()));
					sheet.addCell(label);
					
					xx-=1;
					
					y = y+1;
					for(int m = 0; m < pss.size(); m++){
						ps = pss.get(m);
						//表示一行有几个温度
						
						//TODO 
						x = m % tempi.getVnum();
						if(x == 0){
							y = y + maxY;
							maxY = 0;
							label = new Label(xx + 1, y, "点/缆", border);
							sheet.setRowView(y, 300);
							sheet.addCell(label);
						}
						for(int n = 0; n < ps.size(); n++){
							//for(int n = ps.size()-1; n >=0 ; n--){
							p = ps.get(n);
							if(n == 0){
								label = new Label(xx + x + 2, y, "C" + p.getCableNo1(), border);
								sheet.addCell(label);
							}
							if(ts != null && (ts.getTset().length >= p.getPoinNo1() * 2)){
								tempDouble = MathUtil.byte2IntNb(ts.getTset()[(p.getPoinNo1() - 1) * 2], ts.getTset()[(p.getPoinNo1() - 1) * 2 + 1])/10.0D;
								temp = tempDouble == 0 ? "-" : String.valueOf(tempDouble);
							} else {
								temp = "-";
							}
							label = new Label(xx + x + 2, y + n + 1, temp, normal);
							//温度倒换
							//label = new Label(xx + x + 2, y + (ps.size()-1-n) + 1, temp, normal);
							sheet.addCell(label);
						}
						//最大z
						maxY = Math.max(ps.size() + 1, maxY);
						//标记列
						if(x == 9 || m == pss.size()-1){
							for(int n = 1; n < maxY; n++){
								label = new Label(xx + 1, y + n, getLayer(n), border);
								sheet.setRowView(y + n, 380);
								sheet.addCell(label);
							}
						}
					}
					xx += 14;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			if (wwb != null) {
				try{
					wwb.write();
					wwb.close();
				} catch(Exception e){}
				wwb = null;
			}
		}
		return filePath;
	}
	private List<List<PointInfo>> getPoints(String houseNo){
		Criteria cri = Cnd.cri();
		cri.where().andEquals("houseNo", houseNo);
		cri.getOrderBy().asc("cableNo1");
		cri.getOrderBy().asc("zaxis");
		List<PointInfo> list = dao.query(PointInfo.class, cri);
		List<List<PointInfo>> pss = new LinkedList<List<PointInfo>>();
		List<PointInfo> ps;
		int[] x = {-1, -1};
		//拼装参数
		for(PointInfo p : list){
			if(x[0] == p.getCableNo1()){
				ps = pss.get(x[1]);
			} else {
				ps = new LinkedList<PointInfo>();
				pss.add(ps);
				x[0] = p.getCableNo1();
				x[1] = pss.size() - 1;
			}
			ps.add(p);
		}
		return pss;
	}
	private WritableCellFormat setWSStyle(String type) throws WriteException {
		WritableFont fontStyle ;
		if (type=="normal") {
			fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,  
			        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,  
			        jxl.format.Colour.BLACK);
		}else if (type=="red") {
			fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,  
			        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,  
			        jxl.format.Colour.BLACK);
		}else if (type=="blue") {
			fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,  
			        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, 
			        jxl.format.Colour.BLACK);
		}else if (type=="noBorder") {
			fontStyle = new WritableFont(WritableFont.createFont("Microsoft Yahei"), 11,  
			        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,  
			        jxl.format.Colour.BLACK);
		}else if (type=="border") {
			fontStyle = new WritableFont(WritableFont.createFont("Microsoft Yahei"), 11,  
			        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,  
			        jxl.format.Colour.BLACK);
		}else if (type=="division") {
			fontStyle = new WritableFont(WritableFont.createFont("Microsoft Yahei"), 11,  
			        WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,  
			        jxl.format.Colour.BLACK);
		}else {
			fontStyle = new WritableFont(WritableFont.createFont("Microsoft Yahei"), 14,  
			        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,  
			        jxl.format.Colour.BLACK);
		}
		WritableCellFormat format = new WritableCellFormat(fontStyle); // 单元格定义  
		if (type=="normal"){
			NumberFormat nf = new NumberFormat("#0.0");     
			format = new WritableCellFormat(nf); 
			format.setFont(fontStyle);
		}
		format.setBackground(jxl.format.Colour.WHITE);
		if (type=="red"){
			format.setBackground(jxl.format.Colour.RED);
		}
		format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		format.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式  
		if (type=="normal"||type=="border"||type=="red"||type=="blue") 		
		format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //设置边框
		return format;
	}
	
	public static String getLayer(int n){
		String layer = "";
		if(n == 1) layer = "上层";
		if(n == 2) layer = "中上层";
		if(n == 3) layer = "中下层";
		if(n == 4) layer = "下层";
		return layer;
	}
	
}