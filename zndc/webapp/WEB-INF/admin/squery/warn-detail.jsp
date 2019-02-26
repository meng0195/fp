<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<form id="toWarnDetail" onsubmit="return divSearch(this, 'check-warn-detail');" action="" method="post"></form>
<table class="checkDetail" style="width:100%;">
	<tr>
		<td>仓房名称</td>
		<td>${map:get(codes['houses'], an.houseNo)}</td>
		<td>检测时间</td>
		<td colspan="5"><fmt:formatDate value="${an.testDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>
	<tr>
		<td width="100px">类型</td>
		<td width="100px">${warn:get1(an.type)}</td>
		<td width="100px">类型1</td>
		<td width="100px">${warn:get2(an.type, an.type1)}</td>
	</tr>
	<tr>
	
	</tr>
	<tr>
		
	</tr>
</table>
<div id="check-warn-detail" layoutH="100">
	<table class="checkDetail" style="width:100%;">
		${html}
	</table>
</div>