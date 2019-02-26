<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/equip/history" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="squery-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
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
<table class="table" width="100%" layoutH="95">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="120">仓房名称</th>
			<th width="120">设备名称</th>
			<th width="120">设备类型</th>
			<th>启动时间</th>
			<th>结束时间</th>
			<!-- <th width="120">启动能耗</th>
			<th width="120">结束能耗</th> -->
			<th width="120">运行时间(分钟)</th>
			<!-- <th width="120">消耗能耗</th> -->
			<th width="120">运行状态</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center">${u.equipName}</td>
				<td class="center">${map:get(enums['TYPE_EQUIP'], u.equipType)}</td>
				<td class="center"><fmt:formatDate value="${u.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center"><fmt:formatDate value="${u.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<%-- <td class="center">${u.startPower}</td>
				<td class="center">${u.endPower}</td> --%>
				<td class="center"><fmt:formatNumber value="${u.runTime/60000}" pattern="#0"/></td>
				<%-- <td class="center">${u.power}</td> --%>
				<td class="center">${u.status == 0?"已关闭":"已开启"}</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>