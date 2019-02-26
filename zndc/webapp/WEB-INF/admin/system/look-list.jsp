<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
	<form id="pagerForm" onsubmit="return dwzSearch(this, 'dialog');" action="${CTX_PATH}/admin/system/user/look" method="post">
		<input type="hidden" name="tid" value="${param.tid}"/>
		<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
		<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>登录名：<input name="ft_LIKEA_S_loginName" value="${param.ft_LIKEA_S_loginName}" size="20"/></td>
					<td>真实姓名：<input name="ft_LIKEA_S_name" value="${param.ft_LIKEA_S_name}" size="20"/></td>
					<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
					<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
				</tr>
			</table>
		</div>
	</form>
</div>
<table class="table" style="width:100%;margin-top:2px" layoutH="113">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="130">登录名</th>
			<th width="120">姓名</th>
			<th width="120">所属部门</th>
			<th width="60">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td>${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td>${u.loginName}</td>
				<td>${u.name}</td>
				<td>${u.deptName}</td>
				<td class="center">
					<a href="">&nbsp;</a>
					<a href="javascript:$.bringBack({keeperCode:'${u.id}', keeperName:'${u.name}'})" class="font-blue">选择</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>