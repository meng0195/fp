<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveSysCodeForm" action="${CTX_PATH}/admin/system/code/save" method="post" 
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="c.id" value="${c.id}"/>
	<input type="hidden" name="c.status" value="${c.status}">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="save"><span submitForm="saveSysCodeForm">保存系统编码信息</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="0">
		<tbody>
			<tr>
				<th width="20%" nowrap="nowrap">类别</th>
				<td width="80%">
					<c:choose>
						<c:when test="${not empty c.id}">
							<input type="hidden" name="c.type" value="${c.type}"/>
							${map:get(dics['SYSCODE'], c.type)}
						</c:when>
						<c:otherwise>
							<select id="c_type" name="c.type" prompt="全部" selectedValue="${c.type}" class="combox required">
						  		<c:forEach var="dic" items="${dics['SYSCODE']}">
						  			<option value="${dic.key}">${dic.value}</option>
						  		</c:forEach>
							</select>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>编码</th>
				<td><input name="c.code" value="${c.code}" class="required" maxlength="15" style="width:95%" autocomplete="off"/></td>
			</tr>
			<tr>
				<th>名称</th>
				<td><input name="c.name" value="${c.name}" class="required" maxlength="15" style="width:95%" autocomplete="off"/></td>
			</tr>
			<tr>
				<th>排序号</th>
				<td><input name="c.seqno" value="${c.seqno}" class="required digits" maxlength="3" style="width:95%" autocomplete="off"/></td>
			</tr>
		</tbody>
	</table>
</form>