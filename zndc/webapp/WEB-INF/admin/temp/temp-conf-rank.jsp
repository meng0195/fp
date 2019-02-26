<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="panelBar">
	<ul class="toolBar">
		<li><span>线缆排布信息</span></li>
	</ul>
</div>
<table class="edit">
	<tbody>
		<tr>
			<th width="10%">仓房编号</th>
			<td width="15%">${rs.house.houseNo}</td>
			<th width="10%">仓房名称</th>
			<td width="15%">${rs.house.houseName}</td>
			<th width="10%">仓房类型</th>
			<td width="15%">${map:get(enums['TYPE_HOUSE'], rs.house.houseType)}</td>
			<th width="10%">保管员</th>
			<td width="15%">${rs.house.keeperName}</td>
		</tr>
		<tr>
			<c:choose>
				<c:when test="${rs.house.houseType == 1}">
					<th>起始点</th>
					<td>${map:get(enums['TYPE_START_WARE'], rs.eqment.begins)}</td>
					<th>线缆排序方向</th>
					<td>${map:get(enums['TYPE_SORT_WARE'], rs.eqment.sortOri)}</td>
				</c:when>
				<c:otherwise>
					<th>起始点</th>
					<td>${map:get(enums['TYPE_START_SILO'], rs.eqment.begins)}</td>
					<th>线缆排序方向</th>
					<td>${map:get(enums['TYPE_SORT_SILO'], rs.eqment.sortOri)}</td>
				</c:otherwise>
			</c:choose>
			<th>线缆连接方式</th>
			<td>${map:get(enums['TYPE_SORT_RULE'], rs.eqment.sortRule)}</td>
			<th>传感器排序规则</th>
			<td>${map:get(enums['TYPE_POINT_RULE'], rs.eqment.pointRule)}</td>
		</tr>
	</tbody>
</table>
<div layoutH="80">${rs.html}</div>
<script type="text/javascript">
$("#points-rank").click(function(e){
	var $target = $(e.target);
	if(e.target.tagName.toUpperCase() == "SPAN"){
		var id = $target.parent().attr("pointId") || 0;
		var divId = $target.parent().attr("id") || false;
		var sTag = $target.parent().attr("sTag") || false;
		var options = {};
		if(!divId){
			return;
		}
		options.width = 500;
		options.height = 400;
		options.mask = true;
		options.maxable = false;
		options.minable = false;
		options.fresh = false;
		options.resizable = false;
		options.drawable = false;
		$.pdialog.open("${CTX_PATH}/conf/temp/edit/point/" + id + "/" + divId + "/" + sTag, "dialog-point-edit", "温度检测点编辑", options);
	}
});
</script>