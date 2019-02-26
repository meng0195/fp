<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="navTab-panel tabsPageContent layoutBox">
	<div id="mainContent" class="page unitBox">
		<form id="toMainShow" onsubmit="return divSearch(this, 'mainShow');" action="" method="post"></form>
		<div class="panelBar">
			<ul class="toolBar">
				<li><a id="mainCheckOne" class="change" target="form" rel="toMainShow" href="${CTX_PATH}/check/temp/oneMain?tid=${param.tid}"><span>单次检测</span></a></li>
				<%-- <li><a id="mainCheckLoop" class="fresh" target="form" rel="toMainShow" href="${CTX_PATH}/check/temp/loopMain?tid=${param.tid}"><span>循环检测</span></a></li> --%>
				<li><a id="mainCheckReal" class="fresh" target="form" rel="toMainShow" href="${CTX_PATH}/check/temp/realMain?tid=${param.tid}"><span>实时数据</span></a></li>
				<li><a id="mainCheckClear" class="fresh" target="ajaxTodo" rel="toMainShow" href="${CTX_PATH}/check/temp/tempClear?tid=${param.tid}"><span>清除异常</span></a></li>
				
			</ul>
		</div>
		
	</div>
</div>
<div id="mainShow" style="width:100%;" layoutH="30" class="houseMap"></div>
<script type="text/javascript">
$(function(){
	setTimeout(function(){
		$("#mainCheckOne").click();
	}, 100);
})
</script>