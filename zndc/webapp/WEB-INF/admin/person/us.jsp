<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="edit">
	<tr>
		<th width="20%">官方网站</th>
		<td width="80%"><fmt:bundle basename="static"><fmt:message key="us.info.web"/></fmt:bundle></td>
	</tr>
	<tr>
		<th>企业微信</th>
		<td><fmt:bundle basename="static"><fmt:message key="us.info.wx"/></fmt:bundle></td>
	</tr>
	<tr>
		<th>服务热线</th>
		<td><fmt:bundle basename="static"><fmt:message key="us.info.tel"/></fmt:bundle></td>
	</tr>
	<tr>
		<th>企业邮箱</th>
		<td><fmt:bundle basename="static"><fmt:message key="us.info.email"/></fmt:bundle></td>
	</tr>
</table>