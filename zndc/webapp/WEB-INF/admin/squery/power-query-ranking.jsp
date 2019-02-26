<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerRankForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/power/ranking" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<c:forEach var="house" items="${codes['houses']}">
						<label><input name="qr.houseNo" type="checkbox" value="${house.key}" 
							<c:forEach var="q" items="${qr.houseNo}">
								${q == house.key ? 'checked' : ''}
							</c:forEach>
						/>${house.value}</label>
		  			</c:forEach>
			  	</td>
			  </tr>
			  <tr>
			  	<th>开始时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="qr.startTime" value="${qr.startTime}" readonly="readonly">
			  	</td>
			  	<th>结束时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="qr.endTime" value="${qr.endTime}" readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerRankForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerRankForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div width="100%" layoutH="30">
	<div id="squery-ranking" style="width:95%;height:450px;margin:0 auto;"></div>
</div>
<script type="text/javascript">
var myChart = echarts.init(document.getElementById("squery-ranking"));
var datas = eval('(' + '${json}' + ')');
option = {
	title : {
        text: '能耗排名',
        subtext: '纯属虚构'
	    },
	tooltip : {
		trigger: 'axis',
	},
	legend: {
		data : ['通风能耗', '照明能耗', '空调能耗', '内环流能耗', '排积热能耗']
	},
	toolbox: {
		show : false,
		feature : {
			mark : {show: false},
			dataView : {show: false, readOnly: false},
			magicType : {show: true, type: ['bar']}
		}
	},
	calculable : true,
	xAxis : [
		{
			type : 'category',
			data : datas.houseNos
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
			name : '通风能耗',
            type : 'bar',
            data : datas.vent,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '照明能耗',
            type : 'bar',
            data : datas.nvc,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '空调能耗',
            type : 'bar',
            data : datas.air,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '内环流能耗',
            type : 'bar',
            data : datas.inLoop,
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            }
        },
        {
            name : '排积热能耗',
            type : 'bar',
            data : datas.heat,
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