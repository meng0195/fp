<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return divSearch(this, 'squery-pest-compare-div');" action="${CTX_PATH}/squery/pest/compare/detail" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent" id="squeryPestCompareTable">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="squery-pest-compare-houseNo" name="ft_EQ_S_houseNo" class="combox" onchange="pcChangeHouseNo(this.value)">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" >${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>检测时间(蓝)：</th>
				<td id="squery-pest-compare-time1"></td>
			  	<th>检测时间(红)：</th>
				<td id="squery-pest-compare-time2"></td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div id="squery-pest-compare-div" width="100%" layoutH="30"></div>
<script type="text/javascript">
function pcChangeHouseNo(v){
	$.ajax({
		type : 'POST',
		url: '${CTX_PATH}/squery/pest/compare/times',
		data: {houseNo : v},
		dataType : "json",
		success : function(req){
			var times = $.parseJSON(req.json);
			if(times && times.length > 0){
				var $time1 = $("<select name='ft_EQ_DT_TIME1' class='combox'><option value=''>请选择</option></select>");
				var $time2 = $("<select name='ft_EQ_DT_TIME2' class='combox'><option value=''>请选择</option></select>");
				for(var i = 0; i < times.length; i++){
					$time1.append("<option value='" + times[i].testdate + "'>" + times[i].testdate + "</option>");
					$time2.append("<option value='" + times[i].testdate + "'>" + times[i].testdate + "</option>");
				}
				$("#squery-pest-compare-time1").html($time1);
				$("#squery-pest-compare-time2").html($time2);
			} else {
				$("#squery-pest-compare-time1").html('');
				$("#squery-pest-compare-time2").html('');
			}
			initUI($("#squeryPestCompareTable"));
		}
	})
}
</script>