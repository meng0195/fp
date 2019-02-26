<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/report/shunde" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			  	<td>检测日期：</td>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="ft_LIKEL_S_testDate" value="${ft_LIKEL_S_testDate}" readonly="readonly">
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
		<li><a class="export" target="form" rel="shundeExportForm" title="确定要导出数据吗?" ><span>导出EXCEL</span></a></li>
		<%-- <form id="shundeExportForm" action="${CTX_PATH}/report/xls" method="post"> --%>
		<form id="shundeExportForm" action="${CTX_PATH}/report/xls/save" method="post">
		  	<input name="testDate" value="${ft_LIKEL_S_testDate}" type="hidden"/>
		</form>
	</ul>
</div>
<style>
tr.report th{background: #f0f0f0;border: solid 1px #c2c2c2;text-align: center}
</style>
<table class="table" width="1720px" layoutH="95">
	<tr class="report">
		<th rowspan="2" width="60">仓号</th>
		<th rowspan="2" width="80">粮食品种</th>
		<th rowspan="2" width="100">库存数量(吨)</th>
		<th rowspan="2" width="80">收获年度</th>
		<th rowspan="2" width="80">入库年月</th>
		<th rowspan="2" width="100">粮食水分(%)</th>
		<th rowspan="2" width="60">仓温(℃)</th>
		<th rowspan="2" width="80">仓湿(RH%)</th>
		<th colspan="7" width="800">粮温(℃)</th>
		<th rowspan="2" width="100">密闭与否</th>
		<th rowspan="2" width="120">熏蒸(富氮)与否</th>
		<th rowspan="2" width="100">进出仓状态</th>
		<th rowspan="2" width="60">操作</th>
	</tr>
	<tr class="report">
		<th width="140">上层平均粮温</th>
		<th width="140">中上层平均粮温</th>
		<th width="140">中下层平均粮温</th>
		<th width="140">下层平均粮温</th>
		<th width="80">最高粮温</th>
		<th width="80">最低粮温</th>
		<th width="80">平均粮温</th>
	</tr>
	<c:forEach var="u" items="${list}" varStatus="st">
		<tr target="uid" rel="${u.id}">
			<td class="center">${u.houseName}</td>
			<td class="center">${u.grainName}</td>
			<td class="center"><fmt:formatNumber value="${u.grainCount}" pattern="#0.000"/></td>
			<td class="center">${u.year == -1024 ? '-' : u.year}</td>
			<td class="center">${u.inDate}</td>
			<td class="center">${u.water == -1024 ? '-' : u.water}</td>
			<td class="center">${u.inT == -1024 ? '-' : u.inT}</td>
			<td class="center">${u.inH == -1024 ? '-' : u.inH}</td>
			<td class="center">${u.avgT1 == -1024 ? '-' : u.avgT1}</td>
			<td class="center">${u.avgT2 == -1024 ? '-' : u.avgT2}</td>
			<td class="center">${u.avgT3 == -1024 ? '-' : u.avgT3}</td>
			<td class="center">${u.avgT4 == -1024 ? '-' : u.avgT4}</td>
			<td class="center">${u.maxT == -1024 ? '-' : u.maxT}</td>
			<td class="center">${u.minT == -1024 ? '-' : u.minT}</td>
			<td class="center">${u.avgT == -1024 ? '-' : u.avgT}</td>
			<td class="center">${map:get(codes['TYPE_MB'], u.mbTag)}</td>
			<td class="center">${map:get(codes['TYPE_XZ'], u.xzTag)}</td>
			<td class="center">${map:get(codes['TYPE_JCC'], u.jccTag)}</td>
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
		</tr>
	</c:forEach>
	<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
		<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	</c:forEach>
</table>
</div>
<%@ include file="../pager.jsp" %>