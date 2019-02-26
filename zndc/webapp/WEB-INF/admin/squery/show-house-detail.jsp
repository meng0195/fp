<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<table class="edit">
	<tbody>
		<tr>
			<th colspan="11" class="title1">仓房信息</th>
		</tr>
		<tr>
			<th>仓房名称</th>
			<td>${house.houseName}</td>
			<th>仓房类型</th>
			<td>${map:get(enums['TYPE_HOUSE'],house.houseType)}</td>
			<th>保管员</th>
			<td>${house.keeperName}</td>
			<th>储粮品种</th>
			<td>${map:get(codes['TYPE_GRAIN'],house.grainCode)}</td>
			<th colspan="3"></th>
		</tr>
		<tr>
			<th colspan="11" class="title1">粮情信息</th>
		</tr>
		<tr>
			<th>最高温</th>
			<td>${temp.maxT}</td>
			<th>最低温</th>
			<td>${temp.minT}</td>
			<th>平均温</th>
			<td>${temp.avgT}</td>
			<th>状态</th>
			<td>${temp.testTag == 0 ? '正常':'异常'}</td>
			<th>检测时间</th>
			<td colspan="2"><fmt:formatDate value="${temp.testDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		</tr>
		<tr>
			<th colspan="11" class="title1">气体浓度信息</th>
		</tr>
		<tr>
			<th>氧气浓度</th>
			<td>${temp.maxT}</td>
			<th>二氧化碳浓度</th>
			<td>${temp.minT}</td>
			<th>磷化氢浓度</th>
			<td>${temp.avgT}</td>
			<th colspan="5"></th>
		</tr>
		<tr>
			<th colspan="11" class="title1">虫情信息</th>
		</tr>
		<tr>
			<th>最大虫数</th>
			<td>${pest.pest.maxNum}</td>
			<th>最小虫数</th>
			<td>${pest.pest.minNum}</td>
			<th>平均虫数</th>
			<td>${pest.pest.avgNum}</td>
			<th>检测时间</th>
			<td colspan="2"><fmt:formatDate value="${pest.pest.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<th colspan="2"></th>
		</tr>
	</tbody>
</table>
<table class="edit">
	<tbody>
		<tr>
			<th colspan="5" class="title1">设备信息</th>
		</tr>
		<c:forEach var="u" items="${equips.equips}" varStatus="st">
			<c:if test="${st.index%5 == 0}"><tr></c:if>
			<td>${u.value.equipName}:${warn:getEquipStatus(u.value.model, u.value.status)}</td>
			<c:if test="${st.index%5 == 4}"></tr></c:if>
			<c:if test="${st.last && st.index%5 == 0}"><td></td><td></td><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index%5 == 1}"><td></td><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index%5 == 2}"><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index%5 == 3}"><td></td></tr></c:if>
		</c:forEach>
		<tr>
			<th colspan="5" class="title1">执行计划</th>
		</tr>
		<c:forEach var="u" items="${plans}" varStatus="st">
			<c:if test="${st.index%5 == 0}"><tr></c:if>
			<td>${u.planName}</td>
			<c:if test="${st.index%5 == 4}"></tr></c:if>
			<c:if test="${st.last && st.index%5 == 0}"><td></td><td></td><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index%5 == 1}"><td></td><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index%5 == 2}"><td></td><td></td></tr></c:if>
			<c:if test="${st.last && st.index%5 == 3}"><td></td></tr></c:if>
		</c:forEach>
	</tbody>
</table>