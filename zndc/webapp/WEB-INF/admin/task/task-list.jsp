<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/basis/task/list/${type}" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>任务名称：</th>
				<td><input name="ft_LIKEA_S_planName" value="${param.ft_LIKEA_S_planName}" size="20"/></td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="add" target="navTab" rel="tab-basis-task-edit" href="${CTX_PATH}/basis/task/edit/${type}/0?tid=${param.tid}" title="新增定时任务"><span>新增</span></a></li>
		<li><a class="begin" target="ajaxTodo" title="是否启动全部定时任务，请确认?" href="${CTX_PATH}/basis/task/starts/${type}?tid=${param.tid}"><span>全部启动</span></a></li>
		<li><a class="end" target="ajaxTodo" title="是否停止全部定时任务（执行当前任务后结束），请确认?" href="${CTX_PATH}/basis/task/stops/${type}?tid=${param.tid}"><span>全部停止</span></a></li>
	</ul>
</div>
<table class="table" layoutH="126" width="100%">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th>任务名称</th>
			<th width="350">时间点描述</th>
			<th width="80">是否执行</th>
			<th width="80">切换</th>
			<th width="450">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td>${u.planName}</td>
				<td title="${u.timeDesc}">${u.timeDesc}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.status)}</td>
				<td class="center">
					<c:choose>
						<c:when test="${u.status == 0}">
							<a target="ajaxTodo" title="是否启动任务，请确认?" href="${CTX_PATH}/basis/task/start/${u.id}?tid=${param.tid}" class="font-blue" >启动</a>
						</c:when>
						<c:otherwise>
							<a target="ajaxTodo" title="是否关闭任务，请确认?" href="${CTX_PATH}/basis/task/stop/${u.planCode}?tid=${param.tid}" class="font-red" >关闭</a>
						</c:otherwise>
					</c:choose>
					<a target="navTab" href="" title="">&nbsp;</a>
				</td>
				<td class="center">
					<a target="dialog" rel="dialog-basis-task-houses" width="400" height="480" mask="true" href="${CTX_PATH}/basis/task/houses/${u.planCode}" title="检测列表" class="font-blue">检测列表</a>
					<c:choose>
						<c:when test="${u.status == 0}">
							<a target="navTab" rel="tab-basis-task-edit" href="${CTX_PATH}/basis/task/edit/${type}/${u.id}?tid=${param.tid}" title="配置定时任务" class="font-blue">编辑</a>
						</c:when>
						<c:otherwise>
							<a title="配置定时任务" class="font-gray">编辑</a>
						</c:otherwise>
					</c:choose>
					<a target="navTab" rel="tab-basis-task-exes" href="${CTX_PATH}/basis/task/exes/${u.planCode}/${type}?tid=${param.tid}" title="任务执行详情" class="font-blue">任务执行详情</a>
					<c:choose>
						<c:when test="${u.status == 0}">
							<a target="ajaxTodo" href="${CTX_PATH}/basis/task/del/${u.planCode}?tid=${param.tid}" title="请确认是否删除?" class="font-blue">删除</a>
						</c:when>
						<c:otherwise>
							<a class="font-gray">删除</a>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>