<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/basis/equip/dlist/${houseNo}" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>设备名称：</th>
				<td>
					<input name="ft_LIKEA_S_equipName" value="${param.ft_LIKEA_S_equipName}" size="20" />
			  	</td>
			  	<th>设备类型：</th>
				<td>
					<select id="basis-equip-dlist-type" name="ft_EQ_I_type" class="combox">
						<option value="">请选择</option>
						<c:forEach var="c" items="${enums['TYPE_EQUIP']}">
			  				<option value="${c.key}" ${param.ft_EQ_I_type == c.key ? 'selected' : ''}>${c.value}</option>
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
		<li><a class="add" target="navTab" rel="tab-basis-equip-edit" href="${CTX_PATH}/basis/equip/edit/0/${houseNo}?tid=${param.tid}" title="新增设备"><span>新增设备</span></a></li>
		<li><a class="delete" target="form" rel="basisEquipForm" title="删除后数据不可恢复，确认删除吗?" href="${CTX_PATH}/basis/equip/del?tid=${param.tid}"><span>批量删除</span></a></li>
	</ul>
</div>
<form id="basisEquipForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">
	<table class="table" layoutH="127">
		<thead>
			<tr>
				<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>
				<th width="35">序号</th>
				<th width="120">仓房名称</th>
				<th width="120">设备类型</th>
				<th>设备名称</th>
				<th width="120">功率(kw/h)</th>
				<th width="120">模式类型</th>
				<th width="180">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="u" items="${page.result}" varStatus="st">
				<tr target="uid" rel="${u.id}">
					<td class="center"><input type="checkbox" name="uids" value="${u.id}"/></td>
					<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
					<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
					<td class="center">${map:get(enums['TYPE_EQUIP'], u.type)}</td>
					<td>${u.equipName}</td>
					<td class="right">${u.power}</td>
					<td class="right">${map:get(enums['TYPE_CTR_MODEL'], u.model)}</td>
					<td class="center">
						<a target="navTab" rel="tab-basis-equip-edit" href="${CTX_PATH}/basis/equip/edit/${u.id}/${houseNo}?tid=${param.tid}" title="编辑设备信息" class="font-blue">编辑</a>
						<a target="ajaxTodo" title="删除后数据不可恢复，确认删除吗?" href="${CTX_PATH}/basis/equip/del/${u.id}?tid=${param.tid}" class="font-red">删除</a>
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