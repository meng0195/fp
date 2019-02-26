<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="savePowerConfForm" action="${CTX_PATH}/power/conf/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="p.id" value="${p.id}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="savePowerConfForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="4" class="title1">电表信息</th>
			</tr>
			<tr>
				<th width="15%">仓房名称</th>
				<td>
					<select id="power_conf_houseNo_edit" name="p.houseNo" class="combox" >
						<c:forEach var="h" items="${codes['houses']}">
			  				<option value="${h.key}" ${h.key == p.houseNo ? 'selected' : ''}>${h.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>模块板类型</th>
				<td>
					<select id="power_conf_boardType_edit" name="p.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_BOARD']}">
			  				<option value="${e.key}" ${e.key == p.boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th>模块IP</th>
				<td><input name="p.powerIp" value="${p.powerIp}" class="required"/></td>
				<th>模块端口</th>
				<td><input name="p.powerPort" value="${p.powerPort}"/></td>
			</tr>
			
			<tr>
				<th>全设备寄存器位</th>
				<td colspan=3><input name="p.regAdd" value="${p.regAdd}" min="0" max="99999999"/></td>
			</tr>
		</tbody>
	</table>
</form>