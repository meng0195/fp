<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
	<form id="pagerPieForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/power/out/curves" method="post">
		<input type="hidden" name="tid" value="${param.tid}"/>
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<th>仓房：</th>
					<td>
						<select id="squery-out-curves-houseNo" name="qt.houseNo" class="combox">
							<option value="">全部</option>
							<c:forEach var="house" items="${codes['houses']}">
				  				<option value="${house.key}" ${qt.houseNo == house.key ? 'selected' : ''}>${house.value}</option>
				  			</c:forEach>
				  		</select>
				  	</td>
				  	<th>开始时间：</th>
					<td>
						<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="qt.startTime" value="${qt.startTime}" readonly="readonly">
				  	</td>
				  	<th>结束时间：</th>
					<td>
						<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="qt.endTime" value="${qt.endTime}" readonly="readonly">
				  	</td>
				  	<td><div class="buttonActive"><span submitForm="pagerPieForm">查询</span></div></td>
					<td><div class="buttonActive"><span clearForm="pagerPieForm">清空</span></div></td>
				</tr>
			</table>
		</div>
	</form>
</div>
<div width="100%" layoutH="30">
	<div id="squery-out-curves" style="width:65%;height:450px;margin:0 auto;"></div>
</div>
<script type="text/javascript">
var myChart = echarts.init(document.getElementById("squery-out-curves"));
var datas = eval('(' + ('${json}' || 0) + ')');
if(!datas) datas = [];
var ds = [];
option = {
		title : {
	        text: '能耗曲线'
		    },
		tooltip : {
			trigger: 'axis',
		},
		legend: {
			data : ['能耗']
		},
		toolbox: {
			show : true,
			feature : {
				mark : {show: false},
				dataView : {show: false, readOnly: false},
				magicType : {show: true, type: ['bar','line']},
				restore : {show: true},
				saveAsImage : {show: true}
			}
		},
	    xAxis: {
	        type: 'category',
	        data: datas.date
	    },
	    yAxis : [
	     		{
	     			name: '能耗(Kw)',
	     			type : 'value',
	     			axisLabel : {
	                     formatter: '{value} Kw'
	     			}
	     		}
	     	],
	    series: [{
	        data: datas.power,
	        type: 'line'
	    }]
	};
myChart.setOption(option);
</script>
