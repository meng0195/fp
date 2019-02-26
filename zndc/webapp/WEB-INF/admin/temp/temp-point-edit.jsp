<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisPointForm" action="${CTX_PATH}/conf/temp/save/point" method="post" 
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="divId" value="${divId}"/>
	<input type="hidden" name="sTag" value="${sTag}"/>
	<input type="hidden" name="p.id" value="${p.id}"/>
	<input type="hidden" name="p.houseNo" value="${p.houseNo}"/>
	<input type="hidden" name="p.xaxis" value="${p.xaxis}"/>
	<input type="hidden" name="p.yaxis" value="${p.yaxis}"/>
	<input type="hidden" name="p.zaxis" value="${p.zaxis}"/>
	<input type="hidden" name="p.area" value="${p.area}"/>
	<input type="hidden" name="p.poinNo1" value="${p.poinNo1}"/>
	<input type="hidden" name="p.cableNo1" class="required" value="${p.cableNo1}">
	<input type="hidden" name="p.sensorNo1" class="required" value="${p.sensorNo1}">
	<input type="hidden" name="p.leve" class="required" value="${p.leve}">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisPointForm" >保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="30">
		<tbody>
			<tr>
				<th width="25%">温度点编号</th>
				<td width="75%">${p.poinNo1}</td>
			</tr>
			<%-- <tr>
				<th>温度电缆编号</th>
				<td><input name="p.cableNo1" class="required" value="${p.cableNo1}"></td>
			</tr>
			<tr>
				<th>温度传感器编号</th>
				<td><input name="p.sensorNo1" class="required" value="${p.sensorNo1}"></td>
			</tr> --%>
			<tr>
				<th>坐标1</th>
				<td><input name="p.length" class="required dims" value="${p.length}"></td>
			</tr>
			<tr>
				<th>坐标2</th>
				<td><input name="p.width" class="required dims" value="${p.width}"></td>
			</tr>
			<tr>
				<th>坐标3</th>
				<td><input name="p.height" class="required dims" value="${p.height}"></td>
			</tr>
			<tr>
				<th>有效点</th>
				<td>
					<c:choose>
						<c:when test="${p.valid == 1 }">
							<label><input type="radio" name="p.valid" value="0"/>有效</label>&nbsp;&nbsp;&nbsp;
							<label><input type="radio" name="p.valid" value="1" checked/>无效</label>
						</c:when>
						<c:otherwise>
							<label><input type="radio" name="p.valid" value="0" checked/>有效</label>&nbsp;&nbsp;&nbsp;
							<label><input type="radio" name="p.valid" value="1"/>无效</label>
						</c:otherwise>
					</c:choose>
					
				</td>
			</tr>
		</tbody>
	</table>
</form>