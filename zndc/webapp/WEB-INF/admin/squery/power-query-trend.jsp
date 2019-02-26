<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerTrendForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/power/trend" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>选择类型：</th>
				<td>
					<c:forEach var="type" items="${enums['TYPE_ENERGY']}">
						<label><input name="qt.modelType" type="checkbox" value="${type.key}" 
							<c:forEach var="q" items="${qt.modelType}">
								${q == type.key ? 'checked' : ''}
							</c:forEach>
						/>${type.value}</label>
		  			</c:forEach>
			  	</td>
			  	<th>仓房：</th>
				<td>
					<select id="squery-trend-houseNo" name="qt.houseNo" class="combox">
						<option value="">全部</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${qt.houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  
			  	<th>时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="qt.time" value="${qt.time}" readonly="readonly">
			  	</td>
			  	<td>
					<select id="squery-trend-timeType" name="qt.timeType" class="combox">
						<option value="1"${qt.timeType == 1 ? 'selected' : ''}>按天</option>
						<option value="2"${(qt.timeType == 2 || qt.timeType == 0) ? 'selected' : ''}>按周</option>
						<option value="3"${qt.timeType == 3 ? 'selected' : ''}>按月</option>
						<option value="4"${qt.timeType == 4 ? 'selected' : ''}>按季度</option>
						<option value="5"${qt.timeType == 5 ? 'selected' : ''}>按年</option>
			  		</select>
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerTrendForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerTrendForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div width="100%" layoutH="30">
	<div id="squery-trend" style="width:95%;height:450px;margin:0 auto;"></div>
</div>
<script type="text/javascript">
var myChart = echarts.init(document.getElementById("squery-trend"));
var datas = eval('(' + '${json}' + ')');
option = {
	title : {
        text: '能耗趋势'
	    },
	tooltip : {
		trigger: 'axis',
	},
	legend: {
		data : ['能耗']
	},
	toolbox: {
		show : false,
		feature : {
			mark : {show: false},
			dataView : {show: false, readOnly: false},
			magicType : {show: true, type: ['line']}
		}
	},
	xAxis : [
		{
			type : 'category',
			data : datas.times
		}
	],
	yAxis : [
		{
			name: '能耗(Kw)',
			type : 'value',
			axisLabel : {
                formatter: '{value} Kw'
			}
		}
	],
	series : [
		{
			name : '能耗',
            type : 'line',
            data : datas.kwhs,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        }
	]
};
myChart.setOption(option);
</script>