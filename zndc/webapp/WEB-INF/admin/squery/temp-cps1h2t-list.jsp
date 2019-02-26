<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>

<table class="table">
	<thead>
		<tr>
			<th colspan="2"></th>
			<th style="width: 25%">时间1数据</th>
			<th style="width: 25%">时间2数据</th>
			<th style="width: 25%">比较差值</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td colspan="2" class="center">最高温</td>
			<td>${t.result1.maxT}</td>
			<td>${t.result2.maxT}</td>
			<td style="color:${t.diff.maxT > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.maxT}" /></td>
		</tr>
		<tr>
			<td colspan="2" class="center">最低温</td>
			<td>${t.result1.minT}</td>
			<td>${t.result2.minT}</td>
			<td style="color:${t.diff.minT > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.minT}" /></td>
		</tr>
		<tr>
			<td colspan="2" class="center">平均温</td>
			<td>${t.result1.avgT}</td>
			<td>${t.result2.avgT}</td>
			<td style="color:${t.diff.avgT > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.avgT}" /></td>
		</tr>
		<tr>
			<td  colspan="2" class="center">仓温</td>
			<td>${t.result1.inT}</td>
			<td>${t.result2.inT}</td>
			<td style="color:${t.diff.inT > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.inT}" /></td>
		</tr>
		<tr>
			<td  colspan="2" class="center">仓湿</td>
			<td>${t.result1.inH}</td>
			<td>${t.result2.inH}</td>
			<td style="color:${t.diff.inH > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.inH}" /></td>
		</tr>
		<tr>
			<td  colspan="2" class="center">外温</td>
			<td>${t.result1.outT}</td>
			<td>${t.result2.outT}</td>
			<td style="color:${t.diff.outT > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.outT}" /></td>
		</tr>
		<tr>
			<td  colspan="2" class="center">外湿</td>
			<td>${t.result1.outH}</td>
			<td>${t.result2.outH}</td>
			<td style="color:${t.diff.outH > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${t.diff.outH}" /></td>
		</tr>
		<c:forEach var="u" items="${t.result1.layerTsMap}">
			<tr>
				<td width="8%" rowspan="3" class="center">第${u.key + 1}层</td>
				<td width="17%" class="center">平均温</td>
				<td>${u.value[0]}</td>
				<td>${t.result2.layerTsMap[u.key][0]}</td>
				<c:set var="valLeve" value="${u.value[0] - t.result2.layerTsMap[u.key][0]}"></c:set>
				<td style="color:${valLeve > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${valLeve}" /></td>
			</tr>
			<tr>
				<td class="center">最高温</td>
				<td>${u.value[1]}</td>
				<td>${t.result2.layerTsMap[u.key][1]}</td>
				<c:set var="valLeve" value="${u.value[1] - t.result2.layerTsMap[u.key][1]}"></c:set>
				<td style="color:${valLeve > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${valLeve}" /></td>
			</tr>
			<tr>
				<td class="center">最低温</td>
				<td>${u.value[2]}</td>
				<td>${t.result2.layerTsMap[u.key][2]}</td>
				<c:set var="valLeve" value="${u.value[2] - t.result2.layerTsMap[u.key][2]}"></c:set>
				<td style="color:${valLeve > 0 ? 'red' : 'blue'};"><fmt:formatNumber pattern="#0.0" value="${valLeve}" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>