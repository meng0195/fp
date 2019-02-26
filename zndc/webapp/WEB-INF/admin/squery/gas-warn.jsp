<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="toGasWarnDetail" onsubmit="return divSearch(this, 'gas-warn-detail');" action="" method="post"></form>
<table class="checkDetail" style="width:100%;">
	<tr>
		<td>仓房名称</td>
		<td>${map:get(codes['houses'], r.houseNo)}</td>
		<td>检测时间</td>
		<td colspan="2"><fmt:formatDate value="${r.gas.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>-<fmt:formatDate value="${r.gas.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>
	<tr>
		<td width="15%">报警类别</td>
		<td width="21%"><a target="form" rel="toGasWarnDetail" href="${CTX_PATH}/squery/gas/warn/detail/${houseNo}/${testId}/1" class="font-blue">磷化氢超标</a></td>
		<td width="21%"><a target="form" rel="toGasWarnDetail" href="${CTX_PATH}/squery/gas/warn/detail/${houseNo}/${testId}/2" class="font-blue">CO2超标</a></td>
		<td width="21%"><a target="form" rel="toGasWarnDetail" href="${CTX_PATH}/squery/gas/warn/detail/${houseNo}/${testId}/3" class="font-blue">氧气浓度过低</a></td>
		<td width="21%"><a target="form" rel="toGasWarnDetail" href="${CTX_PATH}/squery/gas/warn/detail/${houseNo}/${testId}/0" class="font-blue">检测异常</a></td>
	</tr>
	<tr>
		<td>是否报警</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.anPH3 && r.anPH3.nums > 0) ? 1 : 0)}</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.anCO2 && r.anCO2.nums > 0) ? 1 : 0)}</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.anO2 && r.anO2.nums > 0) ? 1 : 0)}</td>
		<td>${map:get(enums['TYPE_FLAG'], (not empty r.an && r.an.nums > 0) ? 1 : 0)}</td>
	</tr>
	<tr>
		<td>数量统计</td>
		<td>${not empty r.anPH3 ? r.anPH3.nums : "-"}</td>
		<td>${not empty r.anCO2 ? r.anCO2.nums : "-"}</td>
		<td>${not empty r.anO2 ? r.anO2.nums : "-"}</td>
		<td>${not empty r.an ? r.an.nums : "-"}</td>
	</tr>
</table>
<div id="gas-warn-detail" layoutH="120"></div>