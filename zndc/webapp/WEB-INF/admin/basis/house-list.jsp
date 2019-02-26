<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/basis/house/list" method="post">
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
					<select id="basis-house-list-houseType" name="ft_EQ_S_houseType" class="combox">
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
		<li><a class="add" target="navTab" rel="tab-basis-house-edit" href="${CTX_PATH}/basis/house/edit/0?tid=${param.tid}" title="仓房信息新增"><span>新增</span></a></li>
		<%-- <li><a class="delete" target="form" rel="basisHouseForm" title="删除仓房会同步删除其他相关数据，且数据不可恢复，确认删除吗?" href="${CTX_PATH}/basis/house/del?tid=${param.tid}"><span>批量删除</span></a></li> --%>
	</ul>
</div>
<form id="basisHouseForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">
	<table class="table" layoutH="127">
		<thead>
			<tr>
				<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>
				<th width="35">序号</th>
				<th width="100">仓房名称</th>
				<th width="100">保管员</th>
				<th width="100">粮食品种</th>
				<th width="100">仓房类型</th>
				<th width="100">设计储量(t)</th>
				<th width="100">实际储量(t)</th>
				<th width="100">杂质(%)</th>
				<th width="100">等级</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="u" items="${page.result}" varStatus="st">
				<tr target="uid" rel="${u.id}">
					<td class="center"><input type="checkbox" name="uids" value="${u.id}"/></td>
					<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
					<td class="center">${u.houseName}</td>
					<td class="center">${u.keeperName}</td>
					<td class="center">${map:get(codes['TYPE_GRAIN'], u.grainCode)}</td>
					<td class="center">${map:get(enums['TYPE_HOUSE'], u.houseType)}</td>
					<td class="right">${u.storeCount}</td>
					<td class="right">${u.grainCount}</td>
					<td class="right">${u.impurity}</td>
					<td class="center">${map:get(codes['TYPE_GRADE'], u.grade)}</td>
					<td class="center">
						<a target="dialog" rel="dialog-house-detail" width="800" height="480" mask="true" href="${CTX_PATH}/basis/house/detail/${u.id}" title="仓房详情" class="font-blue">查看</a>
						<a target="navTab" rel="tab-basis-house-edit" href="${CTX_PATH}/basis/house/edit/${u.id}?tid=${param.tid}" title="仓房信息编辑" class="font-blue">编辑</a>
						<%-- <a target="ajaxTodo" title="删除仓房会同步删除其他相关数据，且数据不可恢复，确认删除吗?" href="${CTX_PATH}/basis/house/del/${u.id}?tid=${param.tid}" class="font-red">删除</a> --%>
					</td>
				</tr>
			</c:forEach>
			<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
				<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<%@ include file="../pager.jsp" %>