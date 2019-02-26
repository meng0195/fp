<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/pest/history" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="squery-pest-history-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<%-- <th>开始时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="ft_GE_DT_startTime" value="${param.ft_GE_DT_startTime}" readonly="readonly">
			  	</td>
			  	<th>结束时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="ft_LE_DT_startTime" value="${param.ft_LE_DT_startTime}" readonly="readonly">
			  	</td> --%>
			  	<th>检测时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="pestDate" value="${pestDate}" readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<table class="table" layoutH="94">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="90">仓房名称</th>
			<!-- <th width="100">保管员</th> -->
			<th>开始时间</th>
			<th>结束时间</th>
			<th width="100">平均值(只)</th>
			<th width="100">最大值(只)</th>
			<th width="100">最小值(只)</th>
			<th width="100">状态</th>
			<th width="100">曲线记录</th>
			<th width="90">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<%-- <td class="center">${map:get(codes['keppers'], u.houseNo)}</td> --%>
				<td class="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${u.startTime}"/></td>
				<td class="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${u.endTime}"/></td>
				<td class="right">${u.avgNum}</td>
				<td class="right">${u.maxNum}</td>
				<td class="right">${u.minNum}</td>
				<td class="center">${map:get(enums['TYPE_TEST_TAG'], u.testTag)}</td>
				<td class="center">
					<c:choose>
						<c:when test="${u.curveFlag == 1}">
							<a target="ajaxTodo" title="是否取消标记为曲线数据，请确认?" href="${CTX_PATH}/squery/pest/curve/stop/${u.id}?tid=${param.tid}" class="font-red" >已标记</a>
						</c:when>
						<c:otherwise>
							<a target="ajaxTodo" title="是否标记为曲线数据，请确认?" href="${CTX_PATH}/squery/pest/curve/start/${u.id}?tid=${param.tid}" class="font-blue" >未标记</a>
						</c:otherwise>
					</c:choose>
					<a href="" title="">&nbsp;</a>
				</td>
				<td class="center">
					<a target="dialog" rel="dialog-house-detail" width="1050" height="550" mask="true" href="${CTX_PATH}/squery/show/0/${u.id}/2" title="检测详情" class="font-blue">查看</a>
					<a target="navTab" href="" >&nbsp;</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>