<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerDetailForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/power/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td>电费单价(元)：</td>
				<td>
					<input id="energy-price" value="${price}" size="20" />
			  	</td>
			  	<td>总费用核算(元)：</td>
				<td id="energy-sum"></td>
			</tr>
		</table>
	</div>
</form>
</div>
<table class="table" width="100%" layoutH="107">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="100">仓房名称</th>
			<th width="100">设备名称</th>
			<th width="100">开启时间</th>
			<th width="100">结束时间</th>
			<th width="100">能耗(Kw)</th>
			<th width="100">费用(元)</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${es}" varStatus="st">
			<tr>
				<td class="center">${st.count}</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center">${map:get(enums['TYPE_ENERGY'], u.modelType)}</td>
				<td class="center"><fmt:formatDate value="${u.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center"><fmt:formatDate value="${u.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center kwhs">${u.kwhs}</td>
				<td class="center">${u.kwhs * price}</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<script type="text/javascript" >
$(function(){
	setTimeout(function(){
		var price = $("#energy-price").val();
		changeCharge(price);
	}, 200)
})
$("#energy-price").change(function(){
	var price = parseFloat($(this).val());
	changeCharge(price);
})
function changeCharge(price){
	var all = 0, p = 0;
	$(".kwhs").each(function(){
		p = (parseFloat($(this).find("div").html()) * price).toFixed(2);
		all = (parseFloat(all) + parseFloat(p)).toFixed(2);
		$(this).next().find("div").html(p);
	})
	$("#energy-sum").html(all);
};
</script>
