<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/power/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="power-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<td>查看日期：</td>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="date" value="${date}" readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<table class="table" width="100%" layoutH="107">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="100">仓房名称</th>
			<th width="100">上次检测时间</th>
			<th width="100">本次检测时间</th>
			<th width="100">时间间隔(小时)</th>
			<th width="100">检测能耗(Kw)</th>
			<th width="100">消耗能耗(Kw)</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="us" items="${page.result}" varStatus="st">
			<tr>
				<td class="center">${st.count}</td>
				<td class="center">${map:get(codes['houses'], us.houseNo)}</td>
				<td class="center"><fmt:formatDate value="${us.lastTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center"><fmt:formatDate value="${us.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center"><fmt:formatNumber value="${us.intervalT/1000/60/60}" pattern="#0"/></td>
				<td class="center"><fmt:formatNumber value="${us.checkPower}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${us.power}" pattern="#0.00"/></td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>