<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/conf/temp/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房名称：</th>
				<td>
					<input name="ft_LIKEA_S_houseName" value="${param.ft_LIKEA_S_houseName}" size="20" />
			  	</td>
			  	<th>仓房类型：</th>
				<td>
					<select id="basis-eqment-list-houseType" name="ft_EQ_S_houseType" class="combox">
						<option value="">请选择</option>
						<c:forEach var="c" items="${enums['TYPE_HOUSE']}">
			  				<option value="${c.key}" ${param.ft_EQ_S_houseType == c.key ? 'selected' : ''}>${c.value}</option>
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
		<li><a class="fresh" target="ajaxTodo" title="请确认是否更新缓存?" href="${CTX_PATH}/basis/eqment/reload?tid=${param.tid}"><span>更新点排布缓存</span></a></li>
	</ul>
</div>
<table class="table" layoutH="127">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="120">仓房名称</th>
			<th width="100">仓房类型</th>
			<th width="100">起始电缆编号</th>
			<th width="100">列数/圈数</th>
			<th width="250">行数/个数</th>
			<th width="250">层数</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.houseNo}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td>${u.houseName}</td>
				<td class="center">${map:get(enums['TYPE_HOUSE'], u.houseType)}</td>
				<td class="right">${u.beginNum}</td>
				<td class="right">${u.vnum}</td>
				<td class="right">${u.hnum}</td>
				<td class="right">${u.lnum}</td>
				<td class="center">
					<a target="navTab" rel="tab-eqment-edit" href="${CTX_PATH}/conf/temp/edit/${u.houseNo}?tid=${param.tid}" title="配置信息编辑" class="font-blue">编辑</a>
					<a target="navTab" rel="tab-eqment-rank" href="${CTX_PATH}/conf/temp/rank/${u.houseNo}?tid=${param.tid}" class="font-blue" title="排布详情" >排布详情</a>
					<a target="navTab" rel="tab-eqment-area" href="${CTX_PATH}/conf/temp/area/${u.houseNo}?tid=${param.tid}001" class="font-blue" title="区域划分" >区域划分</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>