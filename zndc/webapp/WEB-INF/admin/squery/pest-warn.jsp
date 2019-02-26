<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="toPestWarnDetail" onsubmit="return divSearch(this, 'pest-warn-detail');" action="" method="post"></form>
<table class="checkDetail" style="width:100%;">
	<tr>
		<td>仓房名称</td>
		<td>${map:get(codes['houses'], r.houseNo)}</td>
		<td>检测时间</td>
		<td><fmt:formatDate value="${r.pest.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>-<fmt:formatDate value="${r.pest.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>
	<tr>
		<td width="15%">报警类别</td>
		<td width="28%"><a target="form" rel="toPestWarnDetail" href="${CTX_PATH}/squery/pest/warn/detail/${houseNo}/${testId}/1" class="font-blue">报警</a></td>
		<td width="28%"><a target="form" rel="toPestWarnDetail" href="${CTX_PATH}/squery/pest/warn/detail/${houseNo}/${testId}/2" class="font-blue">预警</a></td>
		<td width="28%"><a target="form" rel="toPestWarnDetail" href="${CTX_PATH}/squery/pest/warn/detail/${houseNo}/${testId}/0" class="font-blue">检测异常</a></td>
	</tr>
	<tr>
		<td>是否报警</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.an0 && r.an0.nums > 0) ? 1 : 0)}</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.an1 && r.an1.nums > 0) ? 1 : 0)}</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.an && r.an.nums > 0) ? 1 : 0)}</td>
	</tr>
	<tr>
		<td>数量统计</td>
		<td>${not empty r.an0 ? r.an0.nums : "-"}</td>
		<td>${not empty r.an1 ? r.an0.nums : "-"}</td>
		<td>${not empty r.an ? r.an.nums : "-"}</td>
	</tr>
</table>
<div id="pest-warn-detail" layoutH="120"></div>