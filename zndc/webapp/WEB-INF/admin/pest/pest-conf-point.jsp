<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="savePestPointForm" action="${CTX_PATH}/conf/pest/point/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="p.houseNo" value="${houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="savePestPointForm">保存</span></a></li>
		</ul>
	</div>
	<div id="pest-conf-point" class="pest-points" layoutH="40"></div>
</form>
<script type="text/javascript">
$(function(){
	pestpoints.init($("#pest-conf-point"), {
		layers : '${layers}',
		tag : "edit",
		datas : $.parseJSON('${datas}')
	});
})
</script>