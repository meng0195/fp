package cn.com.bjggs.report.util;

import jxl.write.WritableSheet;

/**
 * @author	wc
 * @date	2017-08-01
 */
public class ExpUtil {

	public static final void setCellWidth(WritableSheet ws, int begin, int end, int width){
		for(int i = begin; i <= end; i++){
			ws.setColumnView(i, width);
		}
	}
	
}
