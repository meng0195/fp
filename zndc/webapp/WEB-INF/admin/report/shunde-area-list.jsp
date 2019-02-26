<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerAreaForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/report/shunde/area" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			  	<td>检测日期：</td>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="ft_EQ_S_testDate" value="${testDate}" readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerAreaForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerAreaForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="export" target="form" rel="shundeAreaExportForm" title="确定要导出数据吗?"><span>导出EXCEL</span></a></li>
		<form id="shundeAreaExportForm" action="${CTX_PATH}/exp/xls" method="post">
		  	<input type="hidden" name="json" value='{"tag":"ReportShunDe","name":"粮情区域报表","suffix":true,"type":1,"zip":false,"orders":{},"titles":[{"title":"仓号","width":18,"field":"houseName"}, {"title":"西北区平均温","width":18,"field":"area1AvgT"}, {"title":"西北区最高温","width":18,"field":"area1MaxT"}, {"title":"西北区最低温","width":18,"field":"area1MinT"}, {"title":"东北区平均温","width":18,"field":"area2AvgT"}, {"title":"东北区最高温","width":18,"field":"area2MaxT"}, {"title":"东北区最低温","width":18,"field":"area2MinT"}, {"title":"中央区平均温","width":18,"field":"area3AvgT"}, {"title":"中央区最高温","width":18,"field":"area3MaxT"}, {"title":"中央区最低温","width":18,"field":"area3MinT"}, {"title":"西南区平均温","width":18,"field":"area4AvgT"}, {"title":"西南区最高温","width":18,"field":"area4MaxT"}, {"title":"西南区最低温","width":18,"field":"area4MinT"}, {"title":"东南区平均温","width":18,"field":"area5AvgT"}, {"title":"东南区最高温","width":18,"field":"area5MaxT"}, {"title":"东南区最低温","width":18,"field":"area5MinT"}, {"title":"平均温","width":18,"field":"avgT"}, {"title":"最高温","width":18,"field":"maxT"}, {"title":"最低温","width":18,"field":"minT"}]}'/>
		  	<input name="ft_EQ_S_testDate" value="${testDate}" type="hidden"/>
		</form>
	</ul>
</div>
<table class="table" layoutH="127" style="width:2290px">
	<thead>
		<tr>
			<th width="40">序号</th>
			<th width="90">仓号</th>
			<th width="120">西北区平均温</th>
			<th width="120">西北区最高温</th>
			<th width="120">西北区最低温</th>
			<th width="120">东北区平均温</th>
			<th width="120">东北区最高温</th>
			<th width="120">东北区最低温</th>
			<th width="120">中央区平均温</th>
			<th width="120">中央区最高温</th>
			<th width="120">中央区最低温</th>
			<th width="120">西南区平均温</th>
			<th width="120">西南区最高温</th>
			<th width="120">西南区最低温</th>
			<th width="120">东南区平均温</th>
			<th width="120">东南区最高温</th>
			<th width="120">东南区最低温</th>
			<th width="90">平均温</th>
			<th width="90">最高温</th>
			<th width="90">最低温</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td class="center">${u.houseName}</td>
				<td class="center"><fmt:formatNumber value="${u.area1AvgT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area1MaxT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area1MinT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area2AvgT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area2MaxT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area2MinT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area3AvgT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area3MaxT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area3MinT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area4AvgT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area4MaxT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area4MinT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area5AvgT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area5MaxT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.area5MinT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.avgT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.maxT}" pattern="#0.0"/></td>
				<td class="center"><fmt:formatNumber value="${u.minT}" pattern="#0.0"/></td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>