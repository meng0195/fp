package cn.com.bjggs;

import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;

import cn.com.bjggs.core.filter.LoginAdminFilter;

@Ok("json")
@Fail("json")
@Modules
@IocBy(args={"*js", "ioc/", "*anno", "cn.com.bjggs"})
@Filters({
	@By(type=LoginAdminFilter.class, args={"^/(web|mobile|code|temp|iface)/|(.xml|.jsp)$", Constant.SS_USER, "/admin/login/"})
})
@SetupBy(value = MainSetup.class)  
public class MainModule {
	
}
