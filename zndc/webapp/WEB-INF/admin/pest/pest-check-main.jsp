<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="panelBar">
	<ul class="toolBar">
		<li><a target="dialog" rel="dialog-pest-houses-check" width="600" height="400" mask="true" href="${CTX_PATH}/check/pest/to/houses?tid=${param.tid}" title="多仓检测" class="begin"><span>多仓检测</span></a></li>
	</ul>
</div>
<div id="pestCheckMain" layoutH="33" class="houseMap">
	<c:if test="${empty sessionScope._SYS_USER_.houses}">
		<c:forEach var="code" items="${codes['houses']}">
			<a class="houseTest green" houseNo="${code.key}" target="dialog" rel="dialog-pest-check-detail" width="1050" height="550" mask="true" href="${CTX_PATH}/check/pest/detail/${code.key}/0/check">${code.value}</a>
		</c:forEach>
	</c:if>
	<c:if test="${not empty sessionScope._SYS_USER_.houses}">
		<c:forEach var="code" items="${codes['houses']}">
			<c:forEach var="h" items="${sessionScope._SYS_USER_.houses}">
				<c:if test="${code.key == h}">
					<a class="houseTest green" houseNo="${code.key}" target="dialog" rel="dialog-pest-check-detail" width="1050" height="550" mask="true" href="${CTX_PATH}/check/pest/detail/${code.key}/0/check">${code.value}</a>
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:if>
</div>
<script type="text/javascript">
var pestCheckMain = $("#pestCheckMain");
$(function(){
	$($.parseJSON('${cs}')).each(function(){
		var $a = $("a[houseNo='" + this + "']", pestCheckMain);
		$a.addClass("checking");
		$a.attr("href", $a.attr("href").replace("/0/check",　"/0/view"));
	})
	$($.parseJSON('${warns}')).each(function(){
		var $a = $("a[houseNo='" + this + "']", pestCheckMain);
		$a.removeClass("green");
		$a.addClass("red");
	})
})
</script>