<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveConfPestForm" action="${CTX_PATH}/conf/pest/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="p.id" value="${p.id}"/>
	<input type="hidden" name="p.houseNo" value="${p.houseNo}"/>
	<%--<input type="hidden" name="g.houseNo" value="${p.houseNo}"/>--%>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveConfPestForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="15%">控制板IP</th>
				<td width="35%"><input name="p.ctrIp" value="${p.ctrIp}" class="required"/></td>
				<th width="15%">控制板端口</th>
				<td width="15%"><input name="p.ctrPort" value="${p.ctrPort}" class="required digits" min="1" max="65535"/></td>
			</tr>
			<tr>
				<th>摄像头IP</th>
				<td><input name="p.camIp" value="${p.camIp}"/></td>
				<th>摄像头端口</th>
				<td><input name="p.camPort" value="${p.camPort}" class="digits" min="1" max="65535"/></td>
			</tr>
			<tr>
				<th>摄像头用户</th>
				<td><input name="p.camUser" value="${p.camUser}" maxlength="20"/></td>
				<th>摄像头密码</th>
				<td><input name="p.camPw" value="${p.camPw}" maxlength="20"/></td>
			</tr>
			<tr>
				<th>摄像头通道号</th>
				<td><input name="p.camWay" value="${p.camWay}" class="digits"/></td>
				<th>检测点排布层数</th>
				<td><input name="p.layers" value="${p.layers}" class="required digits" min="1" max="10"/></td>
			</tr>
			<tr>
				<th>点抽气时间(s)</th>
				<td><input name="p.times" value="${p.times}" class="required digits" min="10" max="65535"/></td>
				<th>总点数</th>
				<td><input name="p.pointNum" value="${p.pointNum}" class="required digits" min="1" max="1000"/></td>
			</tr>
		</tbody>
	</table>
	<%--<table class="edit">
		<tbody>
			<c:forEach var="g" items="${list}">
				<tr>
					<th width="9%">选通器编号</th>
					<td width="14%"><input name="g.gateNo" value="${g.gateNo}" class="digits" min="1" max="7" /></td>
					<th width="9%">起始点号</th>
					<td width="14%"><input name="g.pointStart" value="${g.pointStart}" class="digits" min="1" max="32" /></td>
					<th width="9%">结束点号</th>
					<td width="14%"><input name="g.pointEnd" value="${g.pointEnd}" class="digits" min="1" max="32" /></td>
				</tr>
			</c:forEach>
			<c:forEach begin="${size + 1}" end="3">
				<tr>
					<th width="9%">选通器编号</th>
					<td width="14%"><input name="g.gateNo" value="" class="digits" min="1" max="7" /></td>
					<th width="9%">起始点号</th>
					<td width="14%"><input name="g.pointStart" value="" class="digits" min="1" max="32" /></td>
					<th width="9%">结束点号</th>
					<td width="14%"><input name="g.pointEnd" value="" class="digits" min="1" max="32" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>--%>
</form>