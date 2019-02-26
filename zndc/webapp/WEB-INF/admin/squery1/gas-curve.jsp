<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery1/curve/gas" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="squery-gas-curve-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>开始时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="ft_GE_DT_startTime" value="${param.ft_GE_DT_startTime}" readonly="readonly">
			  	</td>
			  	<th>结束时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="ft_LE_DT_startTime" value="${param.ft_LE_DT_startTime}" readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div width="100%" layoutH="30">
	<div id="squery1-gas-curve" style="width:95%;height:450px;margin:0 auto;"></div>
</div>
<script type="text/javascript">
var myGasChart = echarts.init(document.getElementById("squery1-gas-curve"));
var datasGas = eval('(' + '${json}' + ')');
option = {
	title : {
		text: datasGas.title || "",
		subtext: datasGas.subtext || "",
		x: 'center'
	},
	tooltip : {
		trigger: 'axis',
		formatter: function(params) {
			var str = params[0].axisValue + '<br/>';
			if(params.length){
				for(var i = 0; i < params.length; i++){
					str += params[i].seriesName + ' : ' + params[i].value + ' <br/>';
				}
			}
			return str;
		}
	},
	legend: {
		data : ['CO2', 'PH3', 'O2'],
		x : 'left'
	},
	toolbox: {
		show : true,
		x:"1100",
		feature : {
			mark : {show: false},
			dataView : {show: false, readOnly: false},
			magicType : {show: true, type: ['line', 'bar']},
			restore : {show: true},
			saveAsImage : {show: true}
		}
	},
	dataZoom : {
		show : true,
		realtime : true,
		start : 0,
		end : (15/datasGas.size)*100
	},
	xAxis : [
		{
			type : 'category',
			boundaryGap : false,
			axisLine: {onZero: false},
			data : datasGas.xAs
		}
	],
	yAxis : [
		{
			name: '(ppm)/(%)',
			type : 'value',
			axisLabel : {
                formatter: '{value}'
			}
		}
	],
	series : [
		{
			name : 'CO2',
            type : 'line',
            data : datasGas.avgCO2,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : 'PH3',
            type : 'line',
            data : datasGas.avgPH3,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : 'O2',
            type : 'line',
            smooth:true,
            data : datasGas.avgO2,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        }
	]
};
myGasChart.setOption(option);
</script>