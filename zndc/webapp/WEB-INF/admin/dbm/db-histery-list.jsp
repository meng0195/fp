<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<%--  
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="delete" target="form" rel="dbmDelsForm" title="删除数据不可恢复，请确认?" href="${CTX_PATH}/dbm/dels?tid=${param.tid}"><span>批量删除</span></a></li>
	</ul>
</div>
<form id="dbmDelsForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">--%>
	<table class="table" layoutH="127">
		<thead>
			<tr>
				<%-- <th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>--%>
				<th width="35">序号</th>
				<th>文件名称</th>
				<th width="150">文件大小</th>
				<th width="180">创建时间</th>
				<th width="120">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="u" items="${files}" varStatus="st">
				<tr target="uid" rel="${u.name}">
					<%--<td class="center"><input type="checkbox" name="uids" value="${u.name}"/></td>--%>
					<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
					<td class="center">${u.name}</td>
					<td class="center">${warn:getFileSize(u, 1)}</td>
					<td class="center">${warn:getFileTime(u)}</td>
					<td class="center">
						<a target="ajaxTodo" title="删除后不可恢复，请确认?" href="${CTX_PATH}/dbm/del/${u.name}?tid=${param.tid}" class="font-red">删除</a>
						<a target="ajaxTodo">&nbsp;</a>
					</td>
				</tr>
			</c:forEach>
			<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
				<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></tr>
			</c:forEach>
		</tbody>
	</table>
<%--</form>--%>