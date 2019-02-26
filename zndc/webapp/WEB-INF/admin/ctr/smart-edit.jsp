<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<form id="saveSmartConfForm" action="${CTX_PATH}/control/smart/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="s.id" value="${s.id}"/>
	<input type="hidden" name="s.houseNo" value="${s.houseNo}"/>
	<input type="hidden" name="s.modelType" value="${s.modelType}"/>
	<input type="hidden" name="s.modelCode" value="${s.modelCode}"/>
	<input type="hidden" name="s.ingTag" value="${s.ingTag}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveSmartConfForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="30">
		<tbody>
			<tr>
				<th width="15%">仓房</th>
				<td width="35%">${map:get(codes['houses'], s.houseNo)}</td>
				<th width="15%">粮食品种</th>
				<td width="35%">${map:get(codes['TYPE_GRAIN'], s.grainCode)}</td>
			</tr>
			<tr>
				<th>模式类型</th>
				<td>${(s.modelType > 0 and s.modelType < 5) ? '智能通风' : (s.modelType == 5 or s.modelType == 6) ? '低温储粮' : '其他'}</td>
				<th>模式名称</th>
				<td>${map:get(enums['TYPE_SMART'], s.modelType)}</td>
			</tr>
			<tr>
				<th>是否开启</th>
				<td>
					<c:forEach var="em" items="${enums['TYPE_FLAG']}">
						<input type="radio" name="s.status" value="${em.key}" ${em.key == s.status ? 'checked' : ''}/>${em.value}
					</c:forEach>
				</td>
				<th>是否运行</th>
				<td>${map:get(enums['TYPE_FLAG'], s.ingTag)}</td>
			</tr>
			<tr>
				<th>开启条件</th>
				<td colspan="3">${warn:getModelMsg(true, s.modelType)}</td>
			</tr>
			<tr>
				<th>关闭条件</th>
				<td colspan="3" >${warn:getModelMsg(false, s.modelType)}</td>
			</tr>
			<tr>
				<th>参数1</th>
				<td><input name="s.param1" value="${s.param1}" class="double2"></td>
				<th>参数2</th>
				<td><input name="s.param2" value="${s.param1}" class="double2"></td>
			</tr>
			<tr>
				<th>设备集</th>
				<td colspan="3">
					<input name="s.equips" value="${s.equips}" type="hidden" >
					<input name="s.equipNames" value="${s.equipNames}" style="width:90%;">
					<a href="${CTX_PATH}/control/smart/equip/${s.id}" lookupgroup="s" style="color:blue;text-decoration:none;">请选择</a>
				</td>
			</tr>
		</tbody>
	</table>
</form>