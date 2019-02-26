<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="toWarnDetail" onsubmit="return divSearch(this, 'check-warn-detail');" action="" method="post"></form>
<table class="checkDetail 5" style="width:100%;">
	<tr>
		<td>仓房名称</td>
		<td>${map:get(codes['houses'], res.houseNo)}</td>
		<td>检测时间</td>
		<td colspan="5"><fmt:formatDate value="${res.datas.testDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>
	<tr>
		<td width="100px">报警类别</td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/1" class="font-blue">温度高限</a></td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/2" class="font-blue">温度升高率</a></td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/3" class="font-blue">温度异常点</a></td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/4" class="font-blue">层均温</a></td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/5" class="font-blue">缺点率</a></td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/6" class="font-blue">冷心异常</a></td>
		<td width="100px"><a target="form" rel="toWarnDetail" href="${CTX_PATH}/squery/temp/show/detail/${houseNo}/${id}/0" class="font-blue">检测异常</a></td>
	</tr>
	<tr>
		<td>是否报警</td>
		<td>${(empty res.an0 or res.an0.nums eq 0) ? '否' : '是'}</td>
		<td>${(empty res.an1 or res.an1.nums eq 0) ? '否' : '是'}</td>
		<td>${(empty res.an2 or res.an2.nums eq 0) ? '否' : '是'}</td>
		<td>${(empty res.an3 or res.an3.nums eq 0) ? '否' : '是'}</td>
		<td>${(empty res.an4 or res.an4.nums eq 0) ? '否' : '是'}</td>
		<td>${(empty res.an5 or res.an5.nums eq 0) ? '否' : '是'}</td>
		<td>${(empty res.an or res.an.nums eq 0) ? '否' : '是'}</td>
	</tr>
	<tr>
		<td>数量统计</td>
		<td>${empty res.an0 ? '-' : res.an0.nums}</td>
		<td>${empty res.an1 ? '-' : res.an1.nums}</td>
		<td>${empty res.an2 ? '-' : res.an2.nums}</td>
		<td>${empty res.an3 ? '-' : res.an3.nums}</td>
		<td>${empty res.an4 ? '-' : res.an4.nums/10.0}</td>
		<td>${empty res.an5 ? '-' : res.an5.nums}</td>
		<td>${empty res.an ? '-' : res.an.nums}</td>
	</tr>
</table>
<div id="check-warn-detail"></div>
<script type="text/javascript">
$(function(){
	$(".checkDetail td a").on("click",function(){
		
		$(".checkDetail td a").removeClass("font-orange");
		$(this).addClass("font-orange");
	});	
});
</script>