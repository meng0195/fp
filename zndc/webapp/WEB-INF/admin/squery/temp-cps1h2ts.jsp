<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<style type="text/css">
.searchBar .cps .searchContent td{padding-right:20px; white-space:nowrap; height:25px}
</style>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/temp/cps1h2ts" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<div class="cps">
			<table class="searchContent">
				<tr>
					<th>仓房：</th>
					<td>
						<select id="squery-cps1h2ts-houseNo" name="t1.houseNo" class="combox">
							<option value="">请选择</option>
							<c:forEach var="house" items="${codes['houses']}">
				  				<option value="${house.key}" ${t1.houseNo == house.key ? 'selected' : ''}>${house.value}</option>
				  			</c:forEach>
				  		</select>
				  	</td>
				  	<th>开始时间1：</th>
					<td>
						<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="t1.startTime" value='<fmt:formatDate value="${t1.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
				  	</td>
				  	<th>结束时间1：</th>
					<td>
						<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="t1.endTime" value='<fmt:formatDate value="${t1.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
				  	</td>
				  	<td>-</td>
				  	<th>开始时间2：</th>
					<td>
						<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="t2.startTime" value='<fmt:formatDate value="${t2.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
				  	</td>
				  	
				  	<th>结束时间2：</th>
					<td>
						<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="t2.endTime" value='<fmt:formatDate value="${t2.endTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
				  	</td>
				  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
					<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
				</tr>
			</table>
		</div>
	</div>
</form>
</div>
<div width="100%" layoutH="30" style="margin-top: 15px;">
	<div id="squery-cps1h2ts-left" style="width:47%;height:450px;margin:0 auto;float: left;"></div>
	<div id="squery-cps1h2ts-right" style="width:47%;height:450px;margin:0 auto;float: right;"></div>
</div>
<script type="text/javascript">
var myChartLeft = echarts.init(document.getElementById("squery-cps1h2ts-left"));
var myChartRight = echarts.init(document.getElementById("squery-cps1h2ts-right"));
var datasL = eval('(' + '${jsonL}' + ')');
var datasR = eval('(' + '${jsonR}' + ')');
optionL = {
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
		data : ['内温', '外温', '均温', '最高温', '最低温','内湿', '外湿'],
		x : 'left'
	},
	toolbox: {
		show : true,
		feature : {
			mark : {show: false},
			dataView : {show: false, readOnly: false},
			magicType : {show: false, type: ['line', 'bar']},
			restore : {show: false},
			saveAsImage : {show: false}
		}
	},

	xAxis : [
		{
			type : 'category',
			boundaryGap : false,
			axisLine: {onZero: false},
			data : datasL.xAs
		}
	],
	yAxis : [
		{
			name: '温度(℃)',
			type : 'value',
			axisLabel : {
                formatter: '{value} °C'
			},
			min:-50,
			max:50
		},
		{
            name: '湿度(%)',
            type: 'value',
            axisLabel : {
                formatter: '{value} (%)'
			},
			min:0,
			max:100
        }
	],
	series : [
		{
			name : '内温',
            type : 'line',
            data : datasL.inTs,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '外温',
            type : 'line',
            data : datasL.outTs,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '均温',
            type : 'line',
            smooth:true,
            data : datasL.avgTs,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '最高温',
            type : 'line',
            data : datasL.maxTs,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '最低温',
            type : 'line',
            data : datasL.minTs,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '内湿',
            type : 'line',
            yAxisIndex : 1,
            smooth:true,
            animation : false,
            data : datasL.inHs,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '外湿',
            type : 'line',
            yAxisIndex : 1,
            smooth:true,
            animation : false,
            data : datasL.outHs,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        }
	]
};

optionR = {
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
			data : ['内温', '外温', '均温', '最高温', '最低温','内湿', '外湿'],
			x : 'left'
		},
		toolbox: {
			show : true,
			feature : {
				mark : {show: false},
				dataView : {show: false, readOnly: false},
				magicType : {show: false, type: ['line', 'bar']},
				restore : {show: false},
				saveAsImage : {show: false}
			}
		},

		xAxis : [
			{
				type : 'category',
				boundaryGap : false,
				axisLine: {onZero: false},
				data : datasR.xAs
			}
		],
		yAxis : [
			{
				name: '温度(℃)',
				type : 'value',
				axisLabel : {
	                formatter: '{value} °C'
				},
				min:-50,
				max:50
			},
			{
	            name: '湿度(%)',
	            type: 'value',
	            axisLabel : {
	                formatter: '{value} (%)'
				},
				min:0,
				max:100
	        }
		],
		series : [
			{
				name : '内温',
	            type : 'line',
	            data : datasR.inTs,
	            smooth:true,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name : '外温',
	            type : 'line',
	            data : datasR.outTs,
	            smooth:true,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name : '均温',
	            type : 'line',
	            smooth:true,
	            data : datasR.avgTs,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name : '最高温',
	            type : 'line',
	            data : datasR.maxTs,
	            smooth:true,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name : '最低温',
	            type : 'line',
	            data : datasR.minTs,
	            smooth:true,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name : '内湿',
	            type : 'line',
	            yAxisIndex : 1,
	            smooth:true,
	            animation : false,
	            data : datasR.inHs,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        },
	        {
	            name : '外湿',
	            type : 'line',
	            yAxisIndex : 1,
	            smooth:true,
	            animation : false,
	            data : datasR.outHs,
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            }
	        }
		]
	};
myChartLeft.setOption(optionL);
myChartRight.setOption(optionR);
</script>