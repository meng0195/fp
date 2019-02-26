<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisChecksRealForm" action="${CTX_PATH}/check/temp/realSave" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisChecksRealForm">保存</span></a></li>
		</ul>
	</div>
	<div layoutH="30">
		<table class="edit">
			<tbody>
				<tr>
					<th width="180px">同时检测仓房数量</th>
					<td><input class="required digits" name="c.maxCheckNum" value="${c.maxCheckNum}" min="1" max="10"/></td>
				</tr>
				<tr>
					<th width="180px">刷新频率(秒)</th>
					<td><input class="required digits" name="c.intervalTimes" value="${c.intervalTimes}" min="1" max="5000"/></td>
				</tr>
				<tr>
					<th width="180px">未操作停止刷新时间(分钟)</th>
					<td><input class="required digits" name="c.closeTimes" value="${c.closeTimes}" min="1" max="5000"/></td>
				</tr>
			</tbody>
		</table>
	</div>
</form>