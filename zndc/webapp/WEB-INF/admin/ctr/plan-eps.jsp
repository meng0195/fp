<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<table class="table" layoutH="94" width="100%">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th>设备名称</th>
			<th width="130">操作类型</th>
			<th width="130">操作结果</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${cs}" varStatus="st">
			<tr target="uid" >
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td class="center">${u.equipName}</td>
				<td class="center">${warn:getOperName(u.doType)}</td>
				<td class="center">${u.doResult eq 1 ? '成功' : '失败'}</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>