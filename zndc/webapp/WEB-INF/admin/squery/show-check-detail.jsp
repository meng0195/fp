<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="toShowDetail" onsubmit="return divSearch(this, 'show-detail');" action="${CTX_PATH}/squery/temp/warn/${hosueNo}/${id}" method="post"></form>
<div class="panelBar">
	<ul class="toolBar" id="showCheckDetailUL">
		<c:choose>
			<c:when test="${type == 0}">
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/detail/${hosueNo}/${id}" ><span>仓房概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/warn/${hosueNo}/${id}" ><span>温度概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/sheet/${hosueNo}/${id}" ><span>温度表格</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/stereo/${hosueNo}/${id}" ><span>立体仓温</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/3d/${hosueNo}/${id}" ><span>温度漫游</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/pest/warn/${hosueNo}/${id}" ><span>虫害概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/pest/sheet/${hosueNo}/${id}" ><span>虫点数据</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/gas/warn/${hosueNo}/${id}" ><span>气体概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/gas/sheet/${hosueNo}/${id}" ><span>气体浓度</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/equip/sheet/${hosueNo}" ><span>设备详情</span></a></li>
			</c:when>
			<c:when test="${type == 1}">
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/warn/${hosueNo}/${id}" ><span>检测概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/sheet/${hosueNo}/${id}" ><span>温度表格</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/stereo/${hosueNo}/${id}" ><span>立体仓温</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/temp/3d/${hosueNo}/${id}" ><span>温度漫游</span></a></li>
			</c:when>
			<c:when test="${type == 2}">
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/pest/warn/${hosueNo}/${id}" ><span>检测概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/pest/sheet/${hosueNo}/${id}" ><span>虫点数据</span></a></li>
			</c:when>
			<c:when test="${type == 4}">
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/gas/warn/${hosueNo}/${id}" ><span>检测概览</span></a></li>
				<li><a class="change" target="form" rel="toShowDetail" href="${CTX_PATH}/squery/gas/sheet/${hosueNo}/${id}" ><span>气体浓度</span></a></li>
			</c:when>
		</c:choose>
	</ul>
</div>
<div id="show-detail"></div>
<script type="text/javascript">
$(function(){
	setTimeout(function(){
		$("#showCheckDetailUL").find("li").eq(0).find("a").click();
	}, 100);
})

$("#showCheckDetailUL li a").click(function(){
	$("#showCheckDetailUL li a").removeClass("font-color-orange");
	$(this).addClass("font-color-orange");
	});

</script>