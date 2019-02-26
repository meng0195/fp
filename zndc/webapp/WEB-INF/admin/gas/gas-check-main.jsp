<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="navTab-panel tabsPageContent layoutBox">
	<div id="mainContent" class="page unitBox">
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="begin" target="ajaxTodo" title="是否开始检测?" href="${CTX_PATH}/check/gas/starts?tid=${param.tid}"><span>开始检测</span></a></li>
				<li><a class="add" target="navTab" rel="tab-check-gas-conf" href="${CTX_PATH}/check/gas/conf?tid=${param.tid}" title="气体检测配置"><span>检测配置</span></a></li>
			</ul>
		</div>
	</div>
</div>
<div id="gasCheckShow" style="width:100%;" layoutH="30" class="houseMap">
	${house}
</div>

<script type="text/javascript">
function changeGasCheck(param){
	var json = $.parseJSON(param);
	if(json){
		var gasCheckShow = $("#gasCheckShow");
		if(gasCheckShow && gasCheckShow.length > 0){
			for(var key in json){
				var $a = $("a[houseNo='" + key + "']", gasCheckShow);
				if ($a.hasClass("gray")) {
					continue;
				}
				if(json[key] == 4){
					$a.addClass("checking");
					$a.attr("href", "/zndc/check/gas/detail/"+key);
				} else if(json[key] == 5){
					$a.removeClass("checking");
					$a.removeClass("green");
					$a.removeClass("red");
					$a.addClass("yellow");
					$a.attr("href", "/zndc/squery/show/"+key+"/0/4");
				} else if(json[key] == 1){
					$a.removeClass("checking");
					$a.removeClass("green");
					$a.removeClass("yellow");
					$a.addClass("red");
					$a.attr("href", "/zndc/squery/show/"+key+"/0/4");
				} else {
					$a.removeClass("checking");
					$a.removeClass("red");
					$a.removeClass("yellow");
					$a.addClass("green");
					$a.attr("href", "/zndc/squery/show/"+key+"/0/4");
				}
			}
		}
	}
}
</script>