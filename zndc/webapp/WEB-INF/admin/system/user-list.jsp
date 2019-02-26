<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/admin/system/user/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
	<table class="searchContent">
		<thead>
			<tr>
				<td>登录名：<input name="ft_LIKEA_S_loginName" value="${param.ft_LIKEA_S_loginName}" size="20"/></td>
				<td>所属部门：<input name="ft_LIKEA_S_deptName" value="${param.ft_LIKEA_S_deptName}" size="20"/></td>
				<td>状态：</td>
				<td>
					<select id="user_status" name="ft_EQ_I_status" class="combox">
						<c:forEach var="e" items="${enums['TYPE_STATUS']}">
							<option value="${e.key}">${e.value}</option>
						</c:forEach>
					</select>
				</td>
				<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</thead>
	</table>
	</div>
</form>
</div>
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="add" target="navTab" rel="tab-user-edit" href="${CTX_PATH}/admin/system/user/edit/0?tid=${param.tid}" title="用户信息新增"><span>新增</span></a></li>
		<li><a class="edit" target="navTab" rel="tab-user-edit" href="${CTX_PATH}/admin/system/user/edit/{uid}?tid=${param.tid}" title="用户信息编辑"><span>编辑</span></a></li>
		<li><a class="delete" target="ajaxTodo" title="删除后数据不可恢复，确认删除吗?" href="${CTX_PATH}/admin/system/user/del/{uid}?tid=${param.tid}"><span>删除</span></a></li>
		<li><a class="delete" target="form" rel="userBatchForm" title="删除后数据不可恢复，确认删除吗?" href="${CTX_PATH}/admin/system/user/del?tid=${param.tid}"><span>批量删除</span></a></li>
		<li><a class="start" target="ajaxTodo" title="启用后用户将系统登录及相关角色权限，请确认?" href="${CTX_PATH}/admin/system/user/enable/{uid}/1?tid=${param.tid}"><span>启用</span></a></li>
		<li><a class="stop" target="ajaxTodo" title="禁用后该用户将无法登录本系统直至再次启用，请确认?" href="${CTX_PATH}/admin/system/user/enable/{uid}/0?tid=${param.tid}"><span>禁用</span></a></li>
		<li><a class="password" target="ajaxTodo" title="用户密码初始化操作，请确认?" href="${CTX_PATH}/admin/system/user/passwd/init/{uid}?tid=${param.tid}"><span>密码初始化</span></a></li>
	</ul>
</div>
<form id="userBatchForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">
<table class="table" layoutH="127">
	<thead>
		<tr>
			<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids" /></th>
			<th width="35">序号</th>
			<th width="130">登录名</th>
			<th width="120">姓名</th>
			<th width="120">所属部门</th>
			<th>联系电话</th>
			<th width="80">状态</th>
			<th width="400">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center"><input type="checkbox" name="uids" value="${u.id}"/></td>
				<td>${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td>${u.loginName}</td>
				<td>${u.name}</td>
				<td>${u.deptName}</td>
				<td>${u.phone}</td>
				<td class="center">${map:get(enums['TYPE_STATUS'], u.status)}</td>
				<td class="center">
					<a target="navTab" rel="tab-user-edit" href="${CTX_PATH}/admin/system/user/edit/${u.id}?tid=${param.tid}" title="用户信息编辑" class="font-blue">编辑</a>
					<a target="ajaxTodo" title="删除后用户将不可恢复，请确认?" href="${CTX_PATH}/admin/system/user/del/${u.id}?tid=${param.tid}" class="font-red">删除</a>
					<c:choose>
						<c:when test="${u.status == 1}">
							<a target="ajaxTodo" title="禁用后该用户将无法登录本系统直至再次启用，请确认?" href="${CTX_PATH}/admin/system/user/enable/${u.id}/0?tid=${param.tid}" class="font-red">禁用</a>
						</c:when>
						<c:otherwise>
							<a target="ajaxTodo" title="启用后用户将系统登录及相关角色权限，请确认?" href="${CTX_PATH}/admin/system/user/enable/${u.id}/1?tid=${param.tid}" class="font-blue">启用</a>
						</c:otherwise>
					</c:choose>
					<a target="ajaxTodo" title="用户密码初始化操作，请确认?" href="${CTX_PATH}/admin/system/user/passwd/init/${u.id}?tid=${param.tid}" class="font-blue">密码初始化</a>
					<a target="dialog" rel="dialog-user-power-houses" width="500" height="400" mask="true" href="${CTX_PATH}/admin/system/user/power/house/${u.id}?tid=${param.tid}" class="font-blue">仓房权限</a>
					<a target="dialog" rel="dialog-msg-user" width="500" height="400" mask="true" href="${CTX_PATH}/admin/msg/user/${u.id}?tid=${param.tid}" class="font-blue">消息权限</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
</form>
<%@ include file="../pager.jsp" %>