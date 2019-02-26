<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveCameraForm" action="${CTX_PATH}/cam/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="c.id" value="${c.id}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveCameraForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="4" class="title1">摄像头配置信息</th>
			</tr>
			<tr>
				<th width="15%">设备名称</th>
				<td width="35%"><input name="c.camName" value="${c.camName}" class="required" maxlength="25" /></td>
				<th width="15%">设备IP</th>
				<td width="35%"><input name="c.camIP" value="${c.camIP}" class="required isIp"/></td>
			</tr>
			<tr>
				<th>设备端口</th>
				<td><input name="c.camPort" value="${c.camPort}" class="required digits"/></td>
				<th>通道号</th>
				<td><input name="c.channelsNum" value="${c.channelsNum}" class="required digits"/></td>
			</tr>
			<tr>
				<th>登陆用户名</th>
				<td><input name="c.userName" value="${c.userName}" class="required"/></td>
				<th>登陆密码</th>
				<td><input name="c.password" value="${c.password}" class="required"/></td>
			</tr>
			<tr>
				<th>设备类型</th>
				<td>
					<select id="cam_type_edit" name="c.type" class="combox" >
						<c:forEach var="e" items="${codes['TYPE_CAM']}">
			  				<option value="${e.key}" ${e.key == c.type ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>厂商</th>
				<td>
					<select id="cam_vendor_edit" name="c.vendor" class="combox" >
						<c:forEach var="e" items="${codes['TYPE_VENDOR']}">
			  				<option value="${e.key}" ${e.key == c.vendor ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
		</tbody>
	</table>
</form>
