<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="edit">
	<tbody>
		<tr>
			<th colspan="4" class="title1" style="font-weight: bold;">仓房信息</th>
		</tr>
		<tr>
			<th width="15%">仓房名称</th>
			<td width="35%">${s.houseName}</td>
			<th width="15%">仓房类型</th>
			<td width="35%">${map:get(enums['TYPE_HOUSE'],s.houseType)}</td>
		</tr>
		<tr>
			<th>设计储量</th>
			<td>${s.storeCount}</td>
			<th>角度</th>
			<td>${s.angle}</td>
		</tr>
		<tr>
			<th>长度</th>
			<td>${s.dim1}</td>
			<th>宽度</th>
			<td>${s.dim2}</td>
		</tr>
		<tr>
			<th>高度</th>
			<td colspan="3">${s.dim3}</td>
		</tr>
		
		<tr>
			<th colspan="4" class="title1" style="font-weight: bold;">储粮信息</th>
		</tr>
		<tr>
			<th>粮食品种</th>
			<td>${map:get(codes['TYPE_GRAIN'], g.grainCode)}</td>
			<th>储量性质</th>
			<td>${map:get(codes['TYPE_NATURE'],g.nature)}</td>
		</tr>
		<tr>
			<th>保管员</th>
			<td>${g.keeperName}</td>
			<th>存储方式</th>
			<td>${map:get(codes['TYPE_STORE'],g.storageMode)}</td>
		</tr>
		<tr>
			<th>入仓时间</th>
			<td><fmt:formatDate value="${g.dateOfIn}" pattern="yyyy-MM-dd"/></td>
			<th>出仓时间</th>
			<td><fmt:formatDate value="${g.dateOfOut}" pattern="yyyy-MM-dd"/></td>
		</tr>
		<tr>
			<th>入仓水分</th>
			<td>${g.grainInWater}</td>
			<th>当前水分</th>
			<td>${g.grainWater}</td>
		</tr>
		<tr>
			<th>杂质</th>
			<td>${g.impurity}</td>
			<th>等级</th>
			<td>${map:get(codes['TYPE_GRADE'],g.grade)}</td>
		</tr>
		<tr>
			<th>不完善粒</th>
			<td>${g.unsoKer}</td>
			<th>产地</th>
			<td>${g.origin}</td>
		</tr>
		<tr>
			<th>实际储量</th>
			<td colspan="3">${g.grainCount}</td>
		</tr>
	</tbody>
</table>