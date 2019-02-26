<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<div class="pageHeader" style="padding: 2px;">
	<div class="subBar">
		<a class="font-blue" multlookup="s" warn="请选择设备" style="cursor: pointer;">确认</a>
	</div>
</div>
<table class="edit">
	<c:forEach var="equip" items="${eps}" varStatus="st">
		<c:if test="${st.index % 4 == 0}"><tr></c:if>
		<td><label><input type="checkbox" name="s" value="{equips:'${equip.equipNo}', equipNames:'${equip.equipName}'}" ${equip.status == 1 ? 'checked' : ''}/>${equip.equipName}</label></td>
		<c:if test="${st.last && st.index % 4 == 0}"><td></td><td></td><td></td></tr></c:if>
		<c:if test="${st.last && st.index % 4 == 1}"><td></td><td></td></tr></c:if>
		<c:if test="${st.last && st.index % 4 == 2}"><td></td></tr></c:if>
		<c:if test="${st.index % 4 == 3}"></tr></c:if>
	</c:forEach>
</table>