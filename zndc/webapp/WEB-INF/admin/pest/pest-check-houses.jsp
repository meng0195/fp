<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="savePestHousesForm" action="${CTX_PATH}/check/pest/house/begin" method="post"
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="savePestHousesForm">开始检测</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<c:if test="${empty sessionScope._SYS_USER_.houses}">
				<c:forEach var="house" items="${codes['houses']}" varStatus="st">
					<c:if test="${st.index % 3 == 0}"><tr></c:if>
					<td width="33%"><input type="checkbox" name="houses" value="${house.key}" />${house.value}</td>
					<c:if test="${st.index % 3 == 2}"></tr></c:if>
					<c:if test="${st.index % 3 == 1 && st.last}"><td></td></tr></c:if>
					<c:if test="${st.index % 3 == 0 && st.last}"><td></td><td></td></tr></c:if>
				</c:forEach>
			</c:if>
			<c:if test="${not empty sessionScope._SYS_USER_.houses}">
				<c:forEach var="house" items="${sessionScope._SYS_USER_.houses}" varStatus="st">
					<c:if test="${st.index % 3 == 0}"><tr></c:if>
					<td width="33%"><input type="checkbox" name="houses" value="${house}" />${map:get(codes['houses'], house)}</td>
					<c:if test="${st.index % 3 == 2}"></tr></c:if>
					<c:if test="${st.index % 3 == 1 && st.last}"><td></td></tr></c:if>
					<c:if test="${st.index % 3 == 0 && st.last}"><td></td><td></td></tr></c:if>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</form>