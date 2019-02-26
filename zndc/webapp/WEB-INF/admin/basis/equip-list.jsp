<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/basis/equip/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="basis-equip-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
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
<table class="table" layoutH="93">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="100">仓房名称</th>
			<th width="100">保管员</th>
			<th width="100">设备数量</th>
			<th width="100">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center">${map:get(codes['keppers'], u.houseNo)}</td>
				<td class="center">${u.nums}</td>
				<td class="center">
					<a target="navTab" rel="page_${param.tid}cc" href="${CTX_PATH}/basis/equip/dlist/${u.houseNo}?tid=${param.tid}cc" title="设备列表" class="font-blue">查看</a>
					<a target="navTab" rel="tab-basis-equip-edit" href="${CTX_PATH}/basis/equip/edit/0/${u.houseNo}?tid=${param.tid}" title="新增设备" class="font-blue">新增设备</a>
					<a target="dialog" rel="dialog-house-equip-rank" width="950" height="600" mask="true" href="${CTX_PATH}/basis/equip/rank/${u.houseNo}" title="设备排布" class="font-blue">排布</a>
					<a target="navTab" rel="tab-basis-equip-ips-edit" href="${CTX_PATH}/basis/equip/ips/edit/${u.houseNo}?tid=${param.tid}" title="模式设置" class="font-blue">模式设置</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>