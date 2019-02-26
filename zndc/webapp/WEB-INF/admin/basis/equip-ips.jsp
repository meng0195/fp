<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisEquipIpsForm" action="${CTX_PATH}/basis/equip/ips/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="q.id" value="${q.id}"/>
	<input type="hidden" name="q.houseNo" value="${q.houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisEquipIpsForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th>仓房名称</th>
				<td>${map:get(codes['houses'], q.houseNo)}</td>
				<th>模块类型</th>
				<td>
					<select id="equip_ips_conf_boardType_edit" name="q.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_BOARD']}">
			  				<option value="${e.key}" ${e.key == q.boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th width="15%">开放模式IP</th>
				<td width="35%"><input name="q.dioIp" value="${q.dioIp}" maxlength="16" class="isIp"/></td>
				<th width="15%">开放模式端口</th>
				<td width="35%"><input name="q.dioPort" value="${q.dioPort}" class="digits" /></td>
			</tr>
			<tr>
				<th>通风窗模式IP1</th>
				<td><input name="q.windIp1" value="${q.windIp1}" maxlength="16" class="isIp"/></td>
				<th>通风窗模式端口1</th>
				<td><input name="q.windPort1" value="${q.windPort1}" class="digits" /></td>
			</tr>
			<tr>
				<th>通风窗模式IP2</th>
				<td><input name="q.windIp2" value="${q.windIp2}" maxlength="16" class="isIp"/></td>
				<th>通风窗模式端口2</th>
				<td><input name="q.windPort2" value="${q.windPort2}" class="digits" /></td>
			</tr>
			<tr>
				<th>通风窗模式IP3</th>
				<td><input name="q.windIp3" value="${q.windIp3}" maxlength="16" class="isIp"/></td>
				<th>通风窗模式端口3</th>
				<td><input name="q.windPort3" value="${q.windPort3}" class="digits" /></td>
			</tr>
			<tr>
				<th>单项风机模式IP</th>
				<td><input name="q.onewIp" value="${q.onewIp}" maxlength="16" class="isIp"/></td>
				<th>单项风机模式端口</th>
				<td><input name="q.onewPort" value="${q.onewPort}" class="digits" /></td>
			</tr>
			<tr>
				<th>窗+单项风机模式IP</th>
				<td><input name="q.woneIp" value="${q.woneIp}" maxlength="16" class="isIp"/></td>
				<th>窗+单项风机模式端口</th>
				<td><input name="q.wonePort" value="${q.wonePort}" class="digits" /></td>
			</tr>
			<tr>
				<th>双向风机模式IP</th>
				<td><input name="q.twowIp" value="${q.twowIp}" maxlength="16" class="isIp"/></td>
				<th>双向风机模式端口</th>
				<td><input name="q.twowPort" value="${q.twowPort}" class="digits" /></td>
			</tr>
			<tr>
				<th width="15%">ARM开放模式IP</th>
				<td width="35%"><input name="q.armDioIp" value="${q.armDioIp}" maxlength="16" class="isIp"/></td>
				<th width="15%">ARM开放模式端口</th>
				<td width="35%"><input name="q.armDioPort" value="${q.armDioPort}" class="digits" /></td>
			</tr>
		</tbody>
	</table>
</form>