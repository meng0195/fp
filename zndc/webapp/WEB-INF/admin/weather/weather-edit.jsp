<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveWeatherForm" action="${CTX_PATH}/conf/weather/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="w.id" value="${w.id}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveWeatherForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="4" class="title1">气象站配置信息</th>
			</tr>
			<tr>
				<th>气象站类型</th>
				<td>
					<select id="weather-weatherType" name="w.type" class="combox">
						<option value="">请选择</option>
						<c:forEach var="c" items="${enums['TYPE_WEATHER']}">
			  				<option value="${c.key}" ${w.type == c.key ? 'selected' : ''}>${c.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM口</th>
				<td><input name="w.ip" value="${w.ip}" min="0"/></td>
			</tr>
			<tr>
				<th>端口号</th>
				<td><input name="w.port" value="${w.port}" class="digits" /></td>
				<th>气象站位置</th>
				<td colspan="3"><input name="w.address" value="${w.address}" /></td>
			</tr>
			<tr>
				<th>刷新频率 (m)</th>
				<td><input name="w.refreTime" value="${w.refreTime}" class="required" /></td>
				<th>保存频率 (m)</th>
				<td><input name="w.saveTime" value="${w.saveTime}" class="required" /></td>
			</tr>
			<tr>
				<th>气温气湿类型</th>
				<td>
					<select class="combox required" name="w.outType">
						<option value="0" ${w.outType == 0 ? 'selected' : ''}>气象站</option>
						<option value="1" ${w.outType == 1 ? 'selected' : ''}>ARM</option>
					</select>
				</td>
				<th>气温气湿IP</th>
				<td><input name="w.outIp" value="${w.outIp}" class="isIp" /></td>
			</tr>
			<tr>
				<th>气温气湿端口</th>
				<td><input name="w.outPort" value="${w.outPort}" class="digits" /></td>
				<th>气温气湿通道号</th>
				<td><input name="w.outWay" value="${w.outWay}" class="digits" /></td>
			</tr>
		</tbody>
	</table>
</form>
