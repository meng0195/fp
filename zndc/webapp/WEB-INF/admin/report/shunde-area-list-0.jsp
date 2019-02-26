<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/report/shunde/area" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="report-area-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<td>截止日期：</td>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="ft_LE_S_testDate" value="${ft_LE_S_testDate}" readonly="readonly">
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
		<li><a class="export" target="form" rel="areaExportForm" title="确定要导出数据吗?" ><span>导出EXCEL</span></a></li>
		<form id="areaExportForm" action="${CTX_PATH}/report/xls1" method="post">
		  	<input name="testDate" value="${ft_LE_S_testDate}" type="hidden"/>
		  	<input name="houseNo" value="${ft_EQ_S_houseNo}" type="hidden"/>
		</form>
	</ul>
</div>
<style>
tr.report th{background: #f0f0f0;border: solid 1px #c2c2c2;text-align: center}
</style>
<table class="table" width="1660px" layoutH="95">
	<tr class="report">
		<th colspan="2" width="80">日期</th>
		<th rowspan="3" width="60">天气</th>
		<th rowspan="3" width="80">大气温度</br>(℃)</th>
		<th rowspan="3" width="80">大气湿度</br>(RH%)</th>
		<th rowspan="3" width="60">仓温</br>(℃)</th>
		<th rowspan="3" width="60">仓湿</br>(RH%)</th>
		<th colspan="17" width="1020">粮温情况(℃)</th>
		<th rowspan="3" width="80">操作</th>
	</tr>
	<tr class="report">
		<th colspan="2">${year}</th>
		<th colspan="4" width="240">内圈</th>
		<th colspan="4" width="240">中圈</th>
		<th colspan="4" width="240">外圈</th>
		<th colspan="4" width="240">最高粮温</th>
		<th rowspan="2" width="80">平均粮温</th>
	</tr>
	<tr class="report">
		<th width="40">月</th>
		<th width="40">日</th>
		<th width="60">上层</th>
		<th width="60">中上层</th>
		<th width="60">中下层</th>
		<th width="60">下层</th>
		<th width="60">上层</th>
		<th width="60">中上层</th>
		<th width="60">中下层</th>
		<th width="60">下层</th>
		<th width="60">上层</th>
		<th width="60">中上层</th>
		<th width="60">中下层</th>
		<th width="60">下层</th>
		<th width="60">上层</th>
		<th width="60">中上层</th>
		<th width="60">中下层</th>
		<th width="60">下层</th>
	</tr>
	<c:forEach var="u" items="${reports}">
			<td class="center">${u.month}</td>
			<td class="center">${u.day}</td>
			<td class="center">${map:get(codes['TYPE_TQ'], u.weaTag)}</td>
			<td class="center">${u.outT == -1024 ? '-' : u.outT}</td>
			<td class="center">${u.outH == -1024 ? '-' : u.outH}</td>
			<td class="center">${u.inT == -1024 ? '-' : u.inT}</td>
			<td class="center">${u.inH == -1024 ? '-' : u.inH}</td>
			<td class="center">${u.a11 == -1024 ? '-' : u.a11}</td>
			<td class="center">${u.a12 == -1024 ? '-' : u.a12}</td>
			<td class="center">${u.a13 == -1024 ? '-' : u.a13}</td>
			<td class="center">${u.a14 == -1024 ? '-' : u.a14}</td>
			<td class="center">${u.a21 == -1024 ? '-' : u.a21}</td>
			<td class="center">${u.a22 == -1024 ? '-' : u.a22}</td>
			<td class="center">${u.a23 == -1024 ? '-' : u.a23}</td>
			<td class="center">${u.a24 == -1024 ? '-' : u.a24}</td>
			<td class="center">${u.a31 == -1024 ? '-' : u.a31}</td>
			<td class="center">${u.a32 == -1024 ? '-' : u.a32}</td>
			<td class="center">${u.a33 == -1024 ? '-' : u.a33}</td>
			<td class="center">${u.a34 == -1024 ? '-' : u.a34}</td>
			<td class="center">${u.max1 == -1024 ? '-' : u.max1}</td>
			<td class="center">${u.max2 == -1024 ? '-' : u.max2}</td>
			<td class="center">${u.max3 == -1024 ? '-' : u.max3}</td>
			<td class="center">${u.max4 == -1024 ? '-' : u.max4}</td>
			<td class="center">${u.avgT == -1024 ? '-' : u.avgT}</td>
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
		<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	</c:forEach>
</table>
<%@ include file="../pager.jsp" %>