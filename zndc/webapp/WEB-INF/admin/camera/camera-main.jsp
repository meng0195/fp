<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="cameraDiv" layoutH="-2">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<c:forEach var="u" items="${list}" varStatus="st">
		<c:choose>
			<c:when test="${u.type == 2}">
				<a class="cameraQiu" target="dialog" rel="dialog-cam-qiang" width="1200" height="600"  style="left:${empty u.xaxis ? 0 : u.xaxis}%;top:${empty u.yaxis ? 0 : u.yaxis}%;" 
					mask="true" href="${CTX_PATH}/cam/in/${u.id}"></a>
			</c:when>
			<c:otherwise>
				<a class="cameraQiang" target="dialog" rel="dialog-cam-qiang" width="1000" height="575"  style="left:${empty u.xaxis ? 0 : u.xaxis}%;top:${empty u.yaxis ? 0 : u.yaxis}%;" 
					mask="true" href="${CTX_PATH}/cam/out/${u.id}"></a>
			</c:otherwise>
		</c:choose>
		
	</c:forEach>
</div>