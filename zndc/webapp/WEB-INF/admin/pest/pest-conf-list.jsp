<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/conf/pest/list" method="post">
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
					<select id="conf-pest-list-houseType" name="ft_EQ_S_houseType" class="combox">
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
<table class="table" layoutH="95">
	<thead>
		<tr>
			<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>
			<th width="35">序号</th>
			<th>仓房名称</th>
			<th width="120">仓房类型</th>
			<th width="130">控制板IP</th>
			<th width="120">控制板端口</th>
			<th width="130">摄像头IP</th>
			<th width="120">摄像头管理端口</th>
			<th width="120">摄像头通道号</th>
			<th width="150">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center"><input type="checkbox" name="uids" value="${u.id}"/></td>
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
				<td class="center">${u.houseName}</td>
				<td class="center">${map:get(enums['TYPE_HOUSE'], u.houseType)}</td>
				<td class="center">${u.ctrIp}</td>
				<td class="center">${u.ctrPort}</td>
				<td class="center">${u.camIp}</td>
				<td class="center">${u.camPort}</td>
				<td class="center">${u.camWay}</td>
				<td class="center">
					<a target="navTab" rel="tab-pest-conf-edit" href="${CTX_PATH}/conf/pest/edit/${empty u.houseNo ? '0' : u.houseNo}?tid=${param.tid}" title="测虫配置" class="font-blue">编辑</a>
					<a target="navTab" rel="tab-pest-conf-point" href="${CTX_PATH}/conf/pest/point/${empty u.houseNo ? '0' : u.houseNo}?tid=${param.tid}" title="检测点排布" class="font-blue">检测点排布</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>