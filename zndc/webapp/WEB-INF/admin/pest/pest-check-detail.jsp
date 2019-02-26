<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<c:if test="${type == 'check'}">
<form id="checkPestPointForm" action="${CTX_PATH}/check/pest/point/begin" method="post"
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="p.houseNo" value="${houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="checkPestPointForm">开始检测</span></a></li>
		</ul>
	</div>
	<div id="pest-detail-point" class="pest-points" layoutH="40"></div>
</form>
</c:if>
<c:if test="${type == 'view'}">
<div id="pest-detail-point" class="pest-points" layoutH="40"></div>
</c:if>
<script type="text/javascript">
$(function(){
	pestpoints.init($("#pest-detail-point"), {
		layers : '${layers}',
		tag : '${type}',
		datas : $.parseJSON('${datas}'),
		results : $.parseJSON('${reqs}'),
		isAll : false
	});
})
</script>