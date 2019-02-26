<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<style type="text/css">
.searchBar .cps .searchContent td{padding-right:20px; white-space:nowrap; height:25px}
</style>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return divSearch(this, 'squery-cps1h2t');" action="${CTX_PATH}/squery/temp/cpsList" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<div class="cps">
			<table class="searchContent" id="squery-cps1h2ts-table">
				<tr>
					<th>仓房：</th>
					<td>
						<select id="squery-cps1h2ts-houseNo" name="t.houseNo" class="combox">
							<option value="" >请选择</option>
							<c:forEach var="house" items="${codes['houses']}" varStatus="st">
				  				<option value="${house.key}" ${st.first ? 'selected' : ''}>${house.value}</option>
				  			</c:forEach>
				  		</select>
				  	</td>
				  	<th>检测日期1：</th>
					<td>
						<input style="width: 80px;" class="date" id="squery-cps1h2ts-date1" dateFmt="yyyy-MM-dd" value='<fmt:formatDate value="${t.time1}" pattern="yyyy-MM-dd"/>' readonly="readonly" callback="getTestDate1()">
				  	</td>
				  	<th>检测时间1：</th>
					<td>
						<select id="squery-cps1h2ts-testdate1" style="width: 90px;" name="t.time1" class="normal"></select>
				  	</td>
				  	<td>-</td>
				  	<th>检测日期2：</th>
					<td>
						<input style="width: 80px;"  class="date" id="squery-cps1h2ts-date2"  dateFmt="yyyy-MM-dd" value='<fmt:formatDate value="${t.time2}" pattern="yyyy-MM-dd"/>' readonly="readonly"  callback="getTestDate2()">
				  	</td>
				  	<th>检测时间2：</th>
					<td>
						<select style="width: 90px;"  id="squery-cps1h2ts-testdate2" name="t.time2" class="normal"></select>
				  	</td>
				  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
					<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
				</tr>
			</table>
		</div>
	</div>
</form>
</div>
<div id="squery-cps1h2t" width="100%" layoutH="45"></div>
<script type="text/javascript">

function getTestDate1(){
	var houseNo = $("#squery-cps1h2ts-houseNo").val() || 0;
	var date = $("#squery-cps1h2ts-date1").val() || 0;
	$.ajax({
		type : 'POST',
		url : '${CTX_PATH}/squery/temp/getDate/'+houseNo+'/'+date+'?tid=${param.tid}',
		dataType : "html",
		cache : false,
		success : function(html){
			$("#squery-cps1h2ts-testdate1").html(html);
		},
		error : ""
	});
}
function getTestDate2(){
	var houseNo = $("#squery-cps1h2ts-houseNo").val() || 0;
	var date = $("#squery-cps1h2ts-date2").val() || 0;
	$.ajax({
		type : 'POST',
		url : '${CTX_PATH}/squery/temp/getDate/'+houseNo+'/'+date+'?tid=${param.tid}',
		dataType : "html",
		cache : false,
		success : function(html){
			$("#squery-cps1h2ts-testdate2").html(html);
		},
		error : ""
	});
}

</script>