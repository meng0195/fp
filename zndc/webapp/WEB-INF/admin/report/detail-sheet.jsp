<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="reportDetail">
	<tbody>
		<tr>
			<th colspan="10" class="title1" style="text-align: center;"><h1>${title}单仓储粮情况详报表</h1></th>
		</tr>
		<tr>
			<th width="80px" rowspan="3">基本情况</th>
			<th width="110px">仓房名称</th>
			<td width="150px">${house.houseName}</td>
			<th width="110px">仓房类型</th>
			<td width="110px">${map:get(enums['TYPE_HOUSE'], house.houseType)}</td>
			<th width="110px">设计仓容(t)</th>
			<td width="110px">${house.storeCount}</td>
			<th width="110px">储粮品种</th>
			<td width="110px">${map:get(codes['TYPE_GRAIN'], g.grainCode)}</td>
		</tr>
		<tr>
			<th>储粮性质</th>
			<td>${map:get(codes['TYPE_NATURE'], g.nature)}</td>
			<th>存储方式</th>
			<td>${map:get(codes['TYPE_STORE'], g.storageMode)}</td>
			<th>实存数量(t)</th>
			<td>${g.grainCount}</td>
			<th>入仓等级</th>
			<td>${map:get(codes['TYPE_GRADE'], g.grade)}</td>
		</tr>
		<tr>
			<th>入仓时间</th>
			<td><fmt:formatDate value="${g.dateOfIn}" pattern="yyyy-MM-dd"/></td>
			<th>产地</th>
			<td>${g.origin}</td>
			<th>入仓水分</th>
			<td>${g.grainInWater}</td>
			<th>保管员</th>
			<td>${g.keeperName}</td>
		</tr>
		<tr>
			<th>气体</th>
			<th>检测时间</th>
			<td><fmt:formatDate value="${gas.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<th>PH3浓度(ppm)</th>
			<td>${gas.avgPH3}</td>
			<th>O2浓度(%)</th>
			<td>${gas.avgO2}</td>
			<th>CO2浓度(%)</th>
			<td>${gas.avgCO2}</td>
		</tr>
		<tr>
			<th>虫情</th>
			<th>检测时间</th>
			<td><fmt:formatDate value="${pest.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<th>最多虫数(头/点)</th>
			<td>${pest.maxNum}</td>
			<th>最少虫数(头/点)</th>
			<td>${pest.minNum}</td>
			<th>平均虫数(头/点)</th>
			<td>${pest.avgNum}</td>
		</tr>
		<tr>
			<th  rowspan="8">粮情</th>
			<th>检测时间</th>
			<td><fmt:formatDate value="${temp.testDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<th>风速（m/s）</th>
			<td>${weather.windSpeed}</td>
			<th>仓外温度（℃）</th>
			<td>${temp.outT}</td>
			<th>仓内温度（℃）</th>
			<td>${temp.inT}</td>
		</tr>
		<tr>
			<th>天气情况</th>
			<td>${weather.sleet}</td>
			<th>仓外湿度（%）</th>
			<td>${temp.outH}</td>
			<th>仓内湿度（%）</th>
			<td>${temp.inH}</td>
			<th>冷芯温度（°C）</th>
			<td>${temp.ccT}</td>
		</tr>
		<tr>
			<th>部门/项目</th>
			<th>平均粮温（°C）</th>
			<th>最高粮温（°C）</th>
			<th>最低粮温（°C）</th>
			<th>高限温度点数</th>
			<th>高限温升点数</th>
			<th>缺点率（%）</th>
			<th>备注</th>
		</tr>
		<tr>
			<th>整仓情况</th>
			<td>${temp.avgT}</td>
			<td>${temp.maxT}</td>
			<td>${temp.minT}</td>
			<td>${warn1s}</td>
			<td>${warn2s}</td>
			<td>${warn3s/10.0}</td>
			<td></td>
		</tr>
		<c:forEach var="layer" items="${temp.layerTsMap}" varStatus="st">
			<tr>
				<th>${layer.key + 1}层</th>
				<td>${layer.value[0]}</td>
				<td>${layer.value[1]}</td>
				<td>${layer.value[2]}</td>
				<td>${warn1 == null ? 0 : warn1[layer.key]}</td>
				<td>${warn2 == null ? 0 : warn2[layer.key]}</td>
				<td>-</td>
				<td></td>
			</tr>
		</c:forEach>
		<tr>
			<th  rowspan="2">检测结论</th>
			<td colspan="8"></td>
			
		</tr>
	</tbody>
</table>