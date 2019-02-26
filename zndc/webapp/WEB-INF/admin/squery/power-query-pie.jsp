<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
	<form id="pagerPieForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/power/pie" method="post">
		<input type="hidden" name="tid" value="${param.tid}"/>
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<th>仓房：</th>
					<td>
						<select id="squery-list-houseNo" name="qt.houseNo" class="combox">
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
	<div id="squery-pie" style="width:65%;height:450px;margin:0 auto;"></div>
</div>
<script type="text/javascript">
var myChart = echarts.init(document.getElementById("squery-pie"));
var datas = eval('(' + ('${json}' || 0) + ')');
if(!datas) datas = [];
var ds = [];
option = {
	title : {
        text: '能耗占比',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['通风','照明','空调','内环流','排积热']
    },
    series : [
        {
            name: '能耗',
            type: 'pie',
            radius : '75%',
            center: ['50%', '60%'],
            data:datas,
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};
myChart.setOption(option);
</script>
