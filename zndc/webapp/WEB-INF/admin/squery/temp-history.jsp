<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/temp/history" method="post">
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
			  	<%-- <th>开始时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="ft_GE_DT_testDate" value="${param.ft_GE_DT_testDate}" readonly="readonly">
			  	</td>
			  	<th>结束时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="ft_LE_DT_testDate" value="${param.ft_LE_DT_testDate}" readonly="readonly">
			  	</td> --%>
			  	<th>检测时间：</th>
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
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="export" target="form" rel="storeThExportForm" title="确定要导出数据吗?"><span>导出EXCEL</span></a></li>
		<form id="storeThExportForm" action="${CTX_PATH}/exp/xls" method="post">
		  	<input type="hidden" name="json" value='{"tag":"TestData","name":"仓温表${param.ft_GE_D_testDate}-${param.ft_LE_D_testDate}","suffix":true,"type":1,"zip":false,"orders":{},"titles":[{"title":"仓房名称","width":20,"field":"houseNo"}, {"title":"检测时间","width":40,"field":"testDate"},{"title":"内温(℃)","width":18,"field":"inT"},{"title":"內湿(%RH)","width":18,"field":"inH"},{"title":"外温(℃)","width":18,"field":"outT"},{"title":"外湿(%RH)","width":18,"field":"outH"},{"title":"均温(℃)","width":18,"field":"avgT"},{"title":"最低温(℃)","width":18,"field":"minT"},{"title":"最高温(℃)","width":18,"field":"maxT"}]}'/>
		  	<input name="ft_GE_D_testDate" value="${param.ft_GE_D_testDate}" type="hidden"/>
		  	<input name="ft_LE_D_testDate" value="${param.ft_LE_D_testDate}" type="hidden"/>
		  	<input name="ft_EQ_S_houseNo" value="${param.ft_EQ_S_houseNo}" type="hidden"/>
		</form>
		<li><a class="change" target="form" rel="tempHistoryForm" title="请确认是否标记当前选择数据为报表数据?" href="${CTX_PATH}/basis/house/del?tid=${param.tid}"><span>批量标记报表数据</span></a></li>
		<li><a class="change" target="form" rel="tempHistoryForm" title="请确认是否标记当前选择数据为曲线数据?" href="${CTX_PATH}/basis/house/del?tid=${param.tid}"><span>批量标记报表数据</span></a></li>
	</ul>
</div>
<table class="table" width="100%" layoutH="127">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="90">仓房名称</th>
			<th>检测时间</th>
			<th width="100">内温(℃)</th>
			<th width="100">內湿(%RH)</th>
			<th width="100">外温(℃)</th>
			<th width="100">外湿(%RH)</th>
			<th width="100">平均温(℃)</th>
			<!-- <th width="100">最低温(℃)</th>
			<th width="100">最高温(℃)</th> -->
			<th width="100">报表记录</th>
			<th width="100">曲线记录</th>
			<th width="100">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center"><fmt:formatDate value="${u.testDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center">${u.inT}</td>
				<td class="center">${u.inH}</td>
				<td class="center">${u.outT}</td>
				<td class="center">${u.outH}</td>
				<td class="center">${u.avgT}</td>
				<%-- <td class="center">${u.minT}</td>
				<td class="center">${u.maxT}</td> --%>
				<td class="center">
					<c:choose>
						<c:when test="${u.reportFlag == 1}">
							<a target="ajaxTodo" title="是否取消标记为报表数据，请确认?" href="${CTX_PATH}/squery/temp/report/stop/${u.id}?tid=${param.tid}" class="font-red" >已标记</a>
						</c:when>
						<c:otherwise>
							<a target="ajaxTodo" title="是否标记为报表数据，请确认?" href="${CTX_PATH}/squery/temp/report/start/${u.id}?tid=${param.tid}" class="font-blue" >未标记</a>
						</c:otherwise>
					</c:choose>
					<a href="" title="">&nbsp;</a>
				</td>
				<td class="center">
					<c:choose>
						<c:when test="${u.curveFlag == 1}">
							<a target="ajaxTodo" title="是否取消标记为曲线数据，请确认?" href="${CTX_PATH}/squery/temp/curve/stop/${u.id}?tid=${param.tid}" class="font-red" >已标记</a>
						</c:when>
						<c:otherwise>
							<a target="ajaxTodo" title="是否标记为曲线数据，请确认?" href="${CTX_PATH}/squery/temp/curve/start/${u.id}?tid=${param.tid}" class="font-blue" >未标记</a>
						</c:otherwise>
					</c:choose>
					<a href="" title="">&nbsp;</a>
				</td>
				<td class="center">
					<%-- <a target="navTab" rel="tab-store-th-detail" href="${CTX_PATH}/squery/temp/sheet/${u.houseNo}/0" title="温度表格" class="font-blue">查看表格</a>
					<a target="navTab" rel="tab-store-th-stereo" href="${CTX_PATH}/squery/temp/stereo/${u.houseNo}/0" title="立体仓温" class="font-blue">立体仓温</a> --%>
					<a target="navTab">&nbsp;</a>
					<a target="dialog" rel="tab-warn-history"  width="1200" height="650" mask="true" href="${CTX_PATH}/squery/show/0/${u.id}/1" title="温度历史信息" class="font-blue">查看</a>
					<c:if test="${changeTag}">
						<a target="navTab" rel="tab-store-th-edit" href="${CTX_PATH}/check/temp/to/change/${u.id}" title="温度修改" class="font-blue">改温</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>