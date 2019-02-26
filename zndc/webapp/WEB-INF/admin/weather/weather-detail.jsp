<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="add" target="navTab" rel="page_007001" title="气象站模式" href="${CTX_PATH}/conf/weather/refresh"><span>刷新</span></a></li>
	</ul>
</div>
<table class="edit">
	<tbody>
		<tr>
			<th colspan="4" class="title1">气象站信息</th>
		</tr>
		<tr>
			<th width="15%">环境温度</th>
			<td width="35%">${t.temp}°C</td>
			<th width="15%">环境湿度</th>
			<td width="35%">${t.humidity}%</td>
		</tr>
		<tr>
			<th>风向</th>
			<td>${t.direcStr}</td>
			<th>风速</th>
			<td>${t.windSpeed}m/s</td>
		</tr>
		<tr>
			<th>气压</th>
			<td>${t.kpa}kpa</td>
			<th>雨雪</th>
			<td>${t.sleet == 0 ? "无雨雪" : "有雨雪"}</td>
		</tr>
		<tr>
			<th>光照</th>
			<td>${t.illumina}LUX</td>
			<th>CO2</th>
			<td>${t.co2}ppm</td>
		</tr>
		<tr>
			<th>检测时间</th>
			<td colspan="3"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${t.testDate}" /></td>
		</tr>
	</tbody>
</table>