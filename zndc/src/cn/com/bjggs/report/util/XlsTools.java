package cn.com.bjggs.report.util;

import org.nutz.lang.Strings;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class XlsTools {

	public static final WritableCellFormat getTitleStyle() {
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 16);
		WritableCellFormat format = null;
		try {
			font.setBoldStyle(WritableFont.BOLD);
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
		} catch (WriteException e) {
		}
		return format;
	}
	
	public static final WritableCellFormat getCeneterStyle() {
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10);
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);
		} catch (WriteException e) {
		}
		return format;
	}
	
	public static final WritableCellFormat getNolmalStyle(){
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 12);
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
		} catch (WriteException e) {
		}
		return format;
	}
	
	public static final WritableCellFormat getNolmalStyle10(){
		WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10);
		WritableCellFormat format = null;
		try {
			format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setVerticalAlignment(VerticalAlignment.CENTRE);
			format.setWrap(true);
			format.setBorder(Border.ALL, BorderLineStyle.THIN);
		} catch (WriteException e) {
		}
		return format;
	}
	
	public static final String changeYear(String date){
		if(!Strings.isBlank(date)){
			String[] ds = date.split("-");
			if(ds != null && ds.length == 2){
				return ds[0] + "年" + ds[1] + "月";
			}
		}
		return "";
	}
	
}
