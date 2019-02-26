<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return divSearch(this, 'report-detail-main');" action="${CTX_PATH}/report/detail/sheet" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="report-detail-main-houseNo" name="houseNo" class="combox">
						<c:forEach var="house" items="${codes['houses']}" varStatus="st">
		  					<option value="${house.key}">${house.value}</option>
		  				</c:forEach>
			  		</select>
			  	</td>
			  	<th>检测日期：</th>
				<td>
					<input id="report-detail-date" style="width: 80px;" class="date" dateFmt="yyyy-MM-dd" readonly="readonly" callback="getTestDate()">
			  	</td>
			  	<th>检测时间：</th>
				<td>
					<select id="report-detail-time" style="width: 90px;" name="time" class="normal"></select>
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div id="report-detail-main" layoutH="55"></div>
<script type="text/javascript">
function getTestDate(){
	var houseNo = $("#report-detail-main-houseNo").val() || 0;
	var date = $("#report-detail-date").val() || 0;
	$.ajax({
		type : 'POST',
		url : '${CTX_PATH}/squery/temp/getDate/' + houseNo + '/' + date + '?tid=${param.tid}',
		dataType : "html",
		cache : false,
		success : function(html){
			$("#report-detail-time").html(html);
		},
		error : ""
	});
}
</script>