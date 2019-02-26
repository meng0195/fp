<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="toReportAllDetail" onsubmit="return divSearch(this, 'report-all-main');" action="${CTX_PATH}/report/all/sheet" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
			  	<th>开始时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="t.startTime" value='<fmt:formatDate value="${t.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
			  	</td>
			  	<th>结束时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="t.endTime" value='<fmt:formatDate value="${t.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="toReportAllDetail">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="toReportAllDetail">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div id="report-all-main" layoutH="55"></div>