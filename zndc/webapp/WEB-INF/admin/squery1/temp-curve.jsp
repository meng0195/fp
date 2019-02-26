<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery1/curve/temp" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="squery-three-houseNo" name="q.houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${q.houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>开始时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="q.startTime" value='<fmt:formatDate value="${q.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
			  	</td>
			  	<th>结束时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="q.endTime" value='<fmt:formatDate value="${q.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div width="100%" layoutH="30">
	<div id="squery1-curve" style="width:95%;height:450px;margin:0 auto;"></div>
</div>
<script type="text/javascript">
var myChart = echarts.init(document.getElementById("squery1-curve"));
var datas = eval('(' + '${json}' + ')');
option = {
	//title : {
	//	text: datas.title,
	//	subtext: datas.subtext,
	//	x: 'center'
	//},
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
		data : ['内温', '外温', '均温', '最高温', '最低温','内湿', '外湿','夹层温度','通风口温度','吊顶温度'],
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
		end : (15/datas.size)*100
	},
	xAxis : [
		{
			type : 'category',
			boundaryGap : false,
			axisLine: {onZero: false},
			data : datas.xAs
		}
	],
	yAxis : [
		{
			name: '温度(℃)',
			type : 'value',
			axisLabel : {
                formatter: '{value} °C'
			}
		},
		{
            name: '湿度(%)',
            nameLocation: 'start',
            type: 'value',
            axisLabel : {
                formatter: '{value} (%)'
			}
        }
	],
	series : [
		{
			name : '内温',
            type : 'line',
            data : datas.inTs,
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
            data : datas.outTs,
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
            data : datas.avgTs,
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
            data : datas.maxTs,
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
            data : datas.minTs,
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
            data : datas.inHs,
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
            data : datas.outHs,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '夹层温度',
            type : 'line',
            data : datas.layTs,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '通风口温度',
            type : 'line',
            data : datas.ventTs,
            smooth:true,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '吊顶温度',
            type : 'line',
            data : datas.topTs,
            smooth:true,
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