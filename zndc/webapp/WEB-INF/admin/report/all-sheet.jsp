<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="reportAll">
	<tbody>
		<tr>
			<th colspan="21" class="title1" style="text-align: center;"><h1>${title }单仓储粮情况汇总表</h1></th>
		</tr>
		<tr>
			<th colspan="8" style="text-align: left;">库点名称:</td>
			<th colspan="7" style="text-align: left;">检查时段:</td>
			<th colspan="6" style="text-align: left;">报表编号:</td>
		</tr>
		<tr>
			<th colspan="5">仓房信息</th>
			<th colspan="9">粮情</th>
			<th colspan="2">虫情</th>
			<th colspan="3">气体</th>
			<th rowspan="2">保管员</th>
			<th rowspan="2">备注</th>
		</tr>
		<tr>
			<th>仓号</th>
			<th>储粮<br/>品种</th>
			<th>储粮<br/>性质</th>
			<th>设计<br/>仓容<br/>（t）</th>
			<th>储粮<br/>数量<br/>（t）</th>
			
			<th>外湿<br/>（%）</th>
			<th>外温<br/>（℃）</th>
			<th>仓湿<br/>（%）</th>
			<th>仓温<br/>（℃）</th>
			<th>平均<br/>粮温<br/>（℃）</th>
			<th>最高<br/>粮温<br/>（℃）</th>
			<th>最低<br/>粮温<br/>（℃）</th>
			<th>高温<br/>点数</th>
			<th>高温升<br/>率点数</th>
			
			<th>最多<br/>虫数</th>
			<th>最少<br/>虫数</th>
			
			<th>氧气<br/>浓度<br/>（%）</th>
			<th>二氧化<br/>碳浓度<br/>（%）</th>
			<th>磷化氢<br/>浓度<br/>（ppm）</th>
			
		</tr>
		
		<c:forEach var="sh" items="${shs}" varStatus="st">
			<c:set var="map" value="${maps[sh.key]}"></c:set>
			<tr>
				<td>${map:get(codes['houses'], map.s.houseNo)}</td>
				<td>${map:get(codes['TYPE_GRAIN'],map.g.grainCode)}</td>
				<td>${map:get(codes['TYPE_NATURE'],map.g.nature)}</td>
				<td>${map.s.storeCount }</td>
				<td>${map.g.grainCount }</td>
				
				<td>${map.temp.outH }</td>
				<td>${map.temp.outT }</td>
				<td>${map.temp.inH }</td>
				<td>${map.temp.inT }</td>
				<td>${map.temp.avgT }</td>
				<td>${map.temp.maxT }</td>
				<td>${map.temp.minT }</td>
				<td>${map.w_t_1}</td>
				<td>${map.w_t_2}</td>
				
				<td>${map.pest.maxNum }</td>
				<td>${map.pest.minNum }</td>
				
				<td>${map.gas.avgO2 }</td>
				<td>${map.gas.avgCO2 }</td>
				<td>${map.gas.avgPH3 }</td>
				
				<td>${map.g.keeperName }</td>
				<td></td>
			</tr>
		</c:forEach>
		
	</tbody>
</table>