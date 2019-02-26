<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisEquipForm" action="${CTX_PATH}/basis/equip/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="q.id" value="${q.id}"/>
	<input type="hidden" name="q.houseNo" value="${q.houseNo}"/>
	<input type="hidden" name="q.equipNo" value="${q.equipNo}"/>
	<input type="hidden" name="q.xaxis" value="${q.xaxis}"/>
	<input type="hidden" name="q.yaxis" value="${q.yaxis}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisEquipForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="15%">仓房名称</th>
				<td width="35%">${map:get(codes['houses'], q.houseNo)}</td>
				<th width="15%">设备类型</th>
				<td width="35%">
					<select id="basis-equip-edit-type" name="q.type" class="combox required" >
						<c:forEach var="e" items="${enums['TYPE_EQUIP']}">
			  				<option value="${e.key}" ${e.key == q.type ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th>设备名称</th>
				<td><input name="q.equipName" value="${q.equipName}" maxlength="10" class="required"/></td>
				<th>功率(kw/h)</th>
				<td><input name="q.power" value="${q.power}" class="required double4" /></td>
			</tr>
			<tr>
				<th>所在面</th>
				<td>
					<c:forEach var="e" items="${enums['TYPE_EQUIP_SIDE']}">
						<input type="radio" name="q.sideTag" value="${e.key}" ${q.sideTag == e.key ? "checked" : ""}/>${e.value}
					</c:forEach>
				</td>
				<th>设备模式</th>
				<td>
					<select id="basis-equip-edit-model" name="q.model" class="combox required" >
						<c:forEach var="e" items="${enums['TYPE_CTR_MODEL']}">
			  				<option value="${e.key}" ${e.key == q.model ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			
			<tr>
				<th>模式DI</th>
				<td>
					<input name="q.diWay" value="${q.diWay}" class="digits"/>
				</td>
				<th>故障DI</th>
				<td>
					<input name="q.diWay2" value="${q.diWay2}" class="digits"/>
				</td>
			</tr>
			
			<tr>
				<th>模块位</th>
				<td>
					<input name="q.modelWay" value="${q.modelWay}" class="digits" />
				</td>
				<th>寄存器位</th>
				<td>
					<input name="q.registerWay" value="${q.registerWay}" class="digits" />
				</td>
			</tr>
			<tr>
				<th>绑定风窗IP</th>
				<td>
					<input name="q.bindIp" value="${q.bindIp}" class="isIp"/>
				</td>
				<th>绑定风窗端口</th>
				<td>
					<input name="q.bindPort" value="${q.bindPort}" class="digits"/>
				</td>
			</tr>
			<tr>
				<th>绑定风窗模块位</th>
				<td>
					<input name="q.bindModel" value="${q.bindModel}" class="digits"/>
				</td>
				<th>绑定风窗寄存器位</th>
				<td>
					<input name="q.bindRegister" value="${q.bindRegister}" class="digits"/>
				</td>
			</tr>
			<tr>
				<th>DO1</th>
				<td>
					<input name="q.doWay" value="${q.doWay}" class="digits" />
				</td>
				<th>DO2</th>
				<td>
					<input name="q.doWay1" value="${q.doWay1}" class="digits" />
				</td>
				
			</tr>
			<tr>
				<th>状态DI1</th>
				<td>
					<input name="q.diWay1" value="${q.diWay1}" class="digits" />
				</td>
				<th>状态DI2</th>
				<td>
					<input name="q.diWay3" value="${q.diWay3}" class="digits" />
				</td>
			</tr>
			<tr>
				<th>绑定风窗Do1</th>
				<td>
					<input name="q.windDo1" value="${q.windDo1}" class="digits"/>
				</td>
				<th>绑定风窗Do2</th>
				<td>
					<input name="q.windDo2" value="${q.windDo2}" class="digits"/>
				</td>
			</tr>
			<tr>
				<th>绑定风窗Di1</th>
				<td>
					<input name="q.windDi1" value="${q.windDi1}" class="digits"/>
				</td>
				<th>绑定风窗Di2</th>
				<td>
					<input name="q.windDi2" value="${q.windDi2}" class="digits"/>
				</td>
			</tr>
			<tr>
				<th>内环流绑设备</th>
				<td>
				<select id="basis-equip-edit-ca" name="q.caEquipNo" class="combox" >
					<c:forEach var="e" items="${list}">
		  				<option value="${e.equipNo}" ${e.equipNo == q.caEquipNo ? 'selected' : ''}>${e.equipName}</option>
		  			</c:forEach>
		  		</select>
				</td>
				<th>能耗类型</th>
				<td>
				<select id="basis-equip-edit-power" name="q.powerType" class="combox required" >
					<c:forEach var="e" items="${enums['TYPE_POWER']}">
		  				<option value="${e.key}" ${e.key == q.powerType ? 'selected' : ''}>${e.value}</option>
		  			</c:forEach>
		  		</select>
				</td>
			</tr>
		</tbody>
	</table>
</form>