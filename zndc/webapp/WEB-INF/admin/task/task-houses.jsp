<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="edit" layoutH="30">
	<tbody>
		<tr>
			<th width="33%">仓房名称</th>
			<th width="33%">仓房名称</th>
			<th width="33%">仓房名称</th>
		</tr>
		<c:forEach var="ct" items="${list}" varStatus="st">
			<c:if test="${st.index % 3 == 0}" ><tr></c:if>
			<td>${map:get(codes['houses'], ct.houseNo)}</td>
			<c:if test="${st.index % 3 == 0 and st.last}">
			<td></td><td></td>
			</c:if>
			<c:if test="${st.index % 3 == 1 and st.last}">
			<td></td>
			</c:if>
			<c:if test="${st.index % 3 == 2}" ></tr></c:if>
		</c:forEach>
	</tbody>
</table>
