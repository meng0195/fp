<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/power/conf/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="power-conf-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="add" target="navTab" rel="tab-power-conf-edit" href="${CTX_PATH}/power/conf/edit/0?tid=${param.tid}" title="新增配置"><span>新增</span></a></li>
	</ul>
</div>
<table class="table" layoutH="93">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="100">仓房名称</th>
			<th width="100">模块板类型</th>
			<th width="100">模块板IP</th>
			<th width="100">模块板Port</th>
			<th width="100">寄存器位</th>
			<th width="100">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center">${map:get(enums['TYPE_BOARD'], u.boardType)}</td>
				<td class="center">${u.powerIp}</td>
				<td class="center">${u.powerPort}</td>
				<td class="center">${u.regAdd}</td>
				<td class="center">
					<a target="navTab" rel="page_power-conf-detail" href="${CTX_PATH}/power/conf/detail/${u.id}?tid=${param.tid}" title="查看" class="font-blue">查看</a>
					<a target="navTab" rel="tab-power-conf-edit" href="${CTX_PATH}/power/conf/edit/${u.id}?tid=${param.tid}" title="设置" class="font-blue">编辑</a>
					<a target="ajaxTodo" title="确认删除吗?" href="${CTX_PATH}/power/conf/del/${u.id}?tid=${param.tid}" class="font-red">删除</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>