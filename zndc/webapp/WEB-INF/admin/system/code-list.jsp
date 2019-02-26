<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/admin/system/code/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
	<table class="searchContent">
		<thead>
			<tr>
				<td>类别:</td>
				<td>
					<select id="code_type" name="ft_EQ_S_type" class="combox">
						<option value="">全部</option>
				  		<c:forEach var="dic" items="${dics['SYSCODE']}">
				  			<option value="${dic.key}" ${param.ft_EQ_S_type == dic.key ? 'selected' : ''}>${dic.value}</option>
				  		</c:forEach>
				  	</select>
				</td>
				<td>编码：<input name="ft_LIKEA_S_code" value="${param.ft_LIKEA_S_code}" size="20"/></td>
				<td>名称：<input name="ft_LIKEA_S_name" value="${param.ft_LIKEA_S_name}" size="20"/></td>
				<td>状态：</td>
				<td>
					<select id="ft_EQ_I_status" name="ft_EQ_I_status" class="combox">
						<option value="">全部</option>
						<c:forEach var="e" items="${enums['TYPE_STATUS_CODE']}">
							<option value="${e.key}" ${param.ft_EQ_I_status == e.key ? 'selected' : ''}>${e.value}</option>
						</c:forEach>
					</select>
				</td>
				<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
				<td><div class="buttonActive"><a target="dialog" mask="true" maxable="false" resizable="false" width="500" height="260" rel="dlg-code-edit" href="${CTX_PATH}/admin/system/code/edit/0?tid=${param.tid}" title="系统编码添加"><span>添加系统编码</span></a></div></td>
			</tr>
		</thead>
	</table>
	</div>
</form>
</div>
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="sort" target="dialog" width="430" height="500" mask="true" href="${pageContext.request.contextPath}/admin/system/sort/before/code?tid=${param.tid}"><span>排序</span></a></li>
	</ul>
</div>
<table class="table" layoutH="127">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="250">类别</th>
			<th width="250">编码</th>
			<th>名称</th>
			<th width="120">状态</th>
			<th width="250">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="c" items="${page.result}" varStatus="st">
			<tr>
				<td>${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td>${map:get(dics['SYSCODE'], c.type)}</td>
				<td>${c.code}</td>
				<td>${c.name}</td>
				<td class="center">${map:get(enums['TYPE_STATUS_CODE'], c.status)}</td>
				<td class="center">
					<c:if test="${not empty c.type}">
						<c:choose>
							<c:when test="${c.status == 0}">
								<a target="dialog" mask="true" maxable="false" resizable="false" width="500" height="260" rel="dlg-code-edit" href="${CTX_PATH}/admin/system/code/edit/${c.id}?tid=${param.tid}" title="系统编码编辑" class="font-blue">编辑</a>
								<a target="ajaxTodo" title="删除后将不可恢复，请确认?" href="${CTX_PATH}/admin/system/code/del/${c.id}?tid=${param.tid}" class="font-red">删除</a>
								<a target="ajaxTodo" title="固化后该编码将不能再编辑或删除，请确认?" href="${CTX_PATH}/admin/system/code/fix/${c.id}?tid=${param.tid}" class="font-red">固化</a>
							</c:when>
							<c:otherwise>
								<a class="font-gray">编辑</a>
								<a class="font-gray">删除</a>
							</c:otherwise>
						</c:choose>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>