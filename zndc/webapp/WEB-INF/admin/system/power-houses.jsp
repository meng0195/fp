<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveUserPowerHousesForm" action="${CTX_PATH}/admin/system/user/power/house/save" method="post" 
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="uid" value="${uid}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="save"><span submitForm="saveUserPowerHousesForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="0">
		<c:forEach var="house" items="${codes['houses']}" varStatus="st">
			<c:if test="${st.index % 3 == 0}"><tr></c:if>
			<td><label><input type="checkbox" value="${house.key}" name="houses" 
			<c:forEach var="h" items="${houses}" varStatus="st1">
				<c:if test="${h.houseNo == house.key}">checked</c:if>
			</c:forEach>
			/>${house.value}</label></td>
			<c:if test="${st.last && st.index % 3 == 0}"><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index % 3 == 1}"><td></td></tr></c:if>
			<c:if test="${st.index % 3 == 2 }"></tr></c:if>
		</c:forEach>
	</table>
</form>