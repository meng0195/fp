<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div id="ctrMain" layoutH="2" class="houseMap">
	<c:if test="${empty sessionScope._SYS_USER_.houses}">
		<c:forEach var="code" items="${codes['houses']}">
			<a class="houseTest green" houseNo="${code.key}" target="dialog" rel="dialog-ctr-house" width="1200" height="575" mask="true" href="${CTX_PATH}/control/house/${code.key}">${code.value}</a>
		</c:forEach>
	</c:if>
	<c:if test="${not empty sessionScope._SYS_USER_.houses}">
		<c:forEach var="code" items="${codes['houses']}">
			<c:forEach var="h" items="${sessionScope._SYS_USER_.houses}">
				<c:if test="${code.key == h}">
					<a class="houseTest green" houseNo="${code.key}" target="dialog" rel="dialog-ctr-house" width="1200" height="575" mask="true" href="${CTX_PATH}/control/house/${code.key}">${code.value}</a>
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:if>
</div>
<script type="text/javascript">
var pestCheckMain = $("#pestCheckMain");
$(function(){
	var $last = $.parseJSON('${last}');
})
</script>