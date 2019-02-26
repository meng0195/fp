package cn.com.bjggs.temp.util;

import java.util.List;
import java.util.Map;

import cn.com.bjggs.basis.enums.TypeTestTag;
import cn.com.bjggs.basis.util.HouseUtil;
import cn.com.bjggs.temp.domain.ChecksLoop;
import cn.com.bjggs.temp.domain.ChecksOne;
import cn.com.bjggs.temp.domain.TempResults;
import cn.com.bjggs.temp.domain.TestData;

public class JointUtil {
	
	public static final String initOneHtml(List<ChecksOne> list){
		StringBuffer sb = new StringBuffer();
		TempResults res;
		String key;
		boolean tag = false;
		for(Map.Entry<String, String> entry : HouseUtil.houseMap.entrySet()){
			res = ChecksUtil.lastChecks.get(entry.getKey());
			if(res == null) res = new TempResults();
			if(res.getDatas() == null) res.setDatas(new TestData());
			key = entry.getKey();
			sb.append("<a houseNo=\"")
				.append(key)
				.append("\" ")
				.append(" class=\"houseTest");
			
			tag = true;
			for(ChecksOne c : list){
				if(key.equals(c.getHouseNo())){
					if(ChecksUtil.ONEINGS.contains(res.getHouseNo())){
						sb.append(" checking\"");
					} else if (ChecksUtil.WAITS.contains(res.getHouseNo())){
						sb.append(" yellow\"");
					} else {
						if(res.getDatas().getTestTag() == TypeTestTag.ABNORMAL.val()){
							sb.append(" red\"");
						} else {
							sb.append(" green\"");
						}
					}
					
					sb.append(" onclick=\"showCheckDetail('")
						.append(key)
						.append("','','1')\">")
						.append(entry.getValue());
					tag = false;
					break;
				}
			}
			if(tag){
				sb.append(" gray\" onclick=\"showCheckDetail('")
					.append(key)
					.append("','','1')\">")
					.append(entry.getValue());
			}
			sb.append("</a>");
		}
		return sb.toString();
	}

	public static final String initLoopHtml(List<ChecksLoop> list){
		StringBuffer sb = new StringBuffer();
		TempResults res;
		String key;
		boolean tag = false;
		for(Map.Entry<String, String> entry : HouseUtil.houseMap.entrySet()){
			res = ChecksUtil.lastChecks.get(entry.getKey());
			if(res == null) res = new TempResults();
			if(res.getDatas() == null) res.setDatas(new TestData());
			key = entry.getKey();
			sb.append("<a houseNo=\"")
				.append(key)
				.append("\" ")
				.append(" class=\"houseTest");
			tag = true;
			for(ChecksLoop cl : list){
				if(key.equals(cl.getHouseNo())){
					if(ChecksUtil.LOOPINGS.contains(res.getHouseNo())){
						sb.append(" checking ");
					}
					if(res.getDatas().getTestTag() == TypeTestTag.ABNORMAL.val()){
						sb.append(" red\"");
					} else {
						sb.append(" green\"");
					}
					sb.append(" onclick=\"showCheckDetail('")
						.append(key)
						.append("','','1')\">")
						.append(entry.getValue());
					tag = false;
					break;
				}
			}
			if(tag){
				sb.append(" gray\" onclick=\"showCheckDetail('")
					.append(key)
					.append("','','1')\">")
					.append(entry.getValue());
			}
			sb.append("</a>");
		}
		return sb.toString();
	}
	
}
