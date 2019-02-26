<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<c:choose>
	<c:when test="${planType == 1}">
		<c:forEach var="u" items="${tds}">
			<a class="${u.testTag == 1 ? 'red' : 'green'}" houseNo="${u.houseNo}" target="dialog" rel="dialog-check-detail" width="1050" height="550" mask="true" href="${CTX_PATH}/squery/show/0/${u.id}/1">${map:get(codes['houses'], u.houseNo)}</a>
		</c:forEach>
	</c:when>
	<c:when test="${planType == 2}">
		<c:forEach var="u" items="${tps}">
			<a class="${u.testTag == 1 ? 'red' : 'green'}" houseNo="${u.houseNo}" target="dialog" rel="dialog-check-detail" width="1050" height="550" mask="true" href="${CTX_PATH}/squery/show/0/${u.id}/2">${map:get(codes['houses'], u.houseNo)}</a>
		</c:forEach>
	</c:when>
	<c:when test="${planType == 4}">
		<c:forEach var="u" items="${tgs}">
			<a class="${u.testTag == 1 ? 'red' : 'green'}" houseNo="${u.houseNo}" target="dialog" rel="dialog-check-detail" width="1050" height="550" mask="true" href="${CTX_PATH}/squery/show/0/${u.id}/4">${map:get(codes['houses'], u.houseNo)}</a>
		</c:forEach>
	</c:when>
</c:choose>

