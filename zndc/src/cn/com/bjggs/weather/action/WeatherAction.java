package cn.com.bjggs.weather.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.com.bjggs.core.util.DwzUtil;
import cn.com.bjggs.system.action.SysAction;
import cn.com.bjggs.weather.domain.TestWeather;
import cn.com.bjggs.weather.domain.WeatherInfo;
import cn.com.bjggs.weather.service.IWertherService;
import cn.com.bjggs.weather.util.WeatherUtil;

@IocBean
@At({"/conf/weather"})
public class WeatherAction extends SysAction{
	
	private final Log log = Logs.getLog(getClass());
	
	@Inject
	private IWertherService wertherService;
	
	@At({"/edit"})
	@Ok("jsp:/WEB-INF/admin/weather/weather-edit.jsp")
	public void edit(HttpServletRequest req){
		WeatherInfo w = wertherService.fetch(WeatherInfo.class, Cnd.cri());
		if (w == null) {
			w = new WeatherInfo();
		}
		req.setAttribute("w", w);
	}
	
	@At({"/save"})
	public Object save(@Param("::w")WeatherInfo w, String tid){
		try {	
			wertherService.update(w);
			return DwzUtil.nothing("气象站信息保存成功!", tid);
		} catch (Exception e) {
			if (this.log.isErrorEnabled()) {
				this.log.errorf("气象站信息保存失败：%s", new Object[] { e.getMessage() });
			}
			return DwzUtil.stopPageError("气象站信息保存失败: " + e.getMessage());
		}
	}

	@At({"/local"})
	@Ok("jsp:/WEB-INF/admin/weather/weather-detail.jsp")
	public void detail(HttpServletRequest req){
		TestWeather tw = WeatherUtil.testWeather;
		req.setAttribute("t", tw);
	}
	
	@At({"/refresh"})
	@Ok("jsp:/WEB-INF/admin/weather/weather-detail.jsp")
	public void refresh(HttpServletRequest req){
		TestWeather tw = wertherService.refresh();
		req.setAttribute("t", tw);
	}
}





