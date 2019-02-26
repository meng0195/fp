<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="sheet-house-${m.house.houseType == 1 ? 'ping' : 'yuan'}">
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="print" href="javascript:$.printBox('printData1')"><span>打印</span></a></li>
		<%--<li><a class="export" target="form" rel="detailSheetExportForm" title="确定要导出数据吗?"><span>导出EXCEL</span></a></li>--%>
		<form id="detailSheetExportForm" action="${CTX_PATH}/squery/sheet/xls/${m.temps.houseNo}/${m.temps.id}" method="post"></form>
		<li><a class="btnpwall" onclick="showAll1()"><span>全展示</span></a></li>
		<li><a class="btnpwno" onclick="hideAll1()"><span>全隐藏</span></a></li>
		<li><a class="btnpw0" onclick="showPW01()"><span>高温预警</span></a></li>
		<li><a class="btnpw1" onclick="showPW11()"><span>温度升高率</span></a></li>
		<li><a class="btnpw2" onclick="showPW21()"><span>温度异常点</span></a></li>
		<!-- <li><a class="btnpw3" onclick="showPW31()"><span>温度点报警</span></a></li> -->
	</ul>
</div>
<div layoutH="60" style="position: relative;z-index:1" id="printData1">
<div class="printDiv print-${m.house.houseType == 1 ? 'ping' : 'yuan'}">
<div class="binding"></div>
<div class="sheet-title"><span class="span-wd">天气情况：${m.wd}</span>${m.title}${m.house.houseName}粮情报表<span class="span-time">检测时间：<fmt:formatDate value="${m.temps.testDate}" pattern="yyyy-MM-dd HH:mm"/></div>
<c:if test="${m.house.houseType == 1}">
<table class="sheet-title2" >
	<tbody>
		<tr>
			<td width="80">仓房名称：</td>
			<td width="100">${m.house.houseName}</td>
			<td width="80">仓房类型：</td>
			<td width="100">${map:get(enums['TYPE_HOUSE'], m.house.houseType)}</td>
			<td width="80">粮食品种：</td>
			<td width="100">${map:get(codes['TYPE_GRAIN'], m.grain.grainCode)}</td>
			<td width="80">保管员：</td>
			<td width="100">${m.grain.keeperName}</td>
		</tr>
		<tr>
			<td>储粮性质：</td>
			<td>${map:get(codes['TYPE_NATURE'], m.grain.nature)}</td>
			<td>设计储量：</td>
			<td>${m.house.storeCount}t</td>
			<td>实际储量：</td>
			<td>${m.grain.grainCount}t</td>
			<td>等级：</td>
			<td>${map:get(codes['TYPE_GRADE'], m.grain.grade)}</td>
		</tr>
		<tr>
			<td>存储方式：</td>
			<td>${map:get(codes['TYPE_STORE'], m.grain.storageMode)}</td>
			<td>入仓水分：</td>
			<td>${m.grain.grainInWater}%</td>
			<td>入仓杂质：</td>
			<td>${m.grain.impurity}%</td>
			<td>仓顶温度</td>
			<td>${m.temps.topT}</td>
		</tr>
	</tbody>
</table>
</c:if>
<c:if test="${m.house.houseType != 1}">
<table class="sheet-title2" >
	<tbody>
		<tr>
			<td width="80">仓房名称：</td>
			<td width="100">${m.house.houseName}</td>
			<td width="80">仓房类型：</td>
			<td width="100">${map:get(enums['TYPE_HOUSE'], m.house.houseType)}</td>
			<td width="80">粮食品种：</td>
			<td width="100">${map:get(codes['TYPE_GRAIN'], m.grain.grainCode)}</td>
			<td width="80">保管员：</td>
			<td width="100">${m.grain.keeperName}</td>
			<td width="80">储粮性质：</td>
			<td width="100">${map:get(codes['TYPE_NATURE'], m.grain.nature)}</td>
			<td width="80">设计储量：</td>
			<td width="100">${m.house.storeCount}t</td>
		</tr>
		<tr>
			<td>实际储量：</td>
			<td>${m.grain.grainCount}t</td>
			<td>等级：</td>
			<td>${map:get(codes['TYPE_GRADE'], m.grain.grade)}</td>
			<td>存储方式：</td>
			<td>${map:get(codes['TYPE_STORE'], m.grain.storageMode)}</td>
			<td>入仓水分：</td>
			<td>${m.grain.grainInWater}%</td>
			<td>入仓杂质：</td>
			<td>${m.grain.impurity}%</td>
			<td>不完善率：</td>
			<td>${m.grain.unsoKer}%</td>
		</tr>
	</tbody>
</table>
</c:if>
${m.html}
<table class="checkDetail" >
	<tbody>
		<tr>
			<td width="80">仓温</td>
			<td width="50">${m.temps.inT}</td>
			<td width="110">温\层</td>
			<td>整仓</td>
			<c:forEach var="layer" items="${m.temps.layerTsMap}" varStatus="st">
				<td>${st.index + 1}层</td>
			</c:forEach>
		</tr>
		<tr>
			<td>仓湿</td>
			<td>${m.temps.inH}</td>
			<td>平均粮温</td>
			<td>${m.temps.avgT}</td>
			<c:forEach var="layer" items="${m.temps.layerTsMap}" varStatus="st">
				<td>${layer.value[0]}</td>
			</c:forEach>
		</tr>
		<tr>
			<td>气温</td>
			<td>${m.temps.outT}</td>
			<td>最高粮温</td>
			<td>${m.temps.maxT}</td>
			<c:forEach var="layer" items="${m.temps.layerTsMap}" varStatus="st">
				<td>${layer.value[1]}</td>
			</c:forEach>
		</tr>
		<tr>
			<td>气湿</td>
			<td>${m.temps.outH}</td>
			<td>最低粮温</td>
			<td>${m.temps.minT}</td>
			<c:forEach var="layer" items="${m.temps.layerTsMap}" varStatus="st">
				<td>${layer.value[2]}</td>
			</c:forEach>
		</tr>
	</tbody>
</table>
<div class="sheet-lq">粮情分析：
<br><br><br><br><br><br><br>

</div>
<table class="sheet-title3" >
	<tbody>
		<tr>
			<td>注：</td>
			<td>最高粮温:"↑"</td>
			<td>最低粮温:"↓"</td>
			<td>无温度:"-"</td>
			<td>温度单位:℃</td>
			<td>湿度单位:%</td>
			<td>制表时间:${m.now}</td>
		</tr>
	</tbody>
</table>
</div>
</div>
</div>
<script type="text/javascript">
function showAll1(){
	var $p = $("#printData1");
	$(".pointW0", $p).css({width:"5px", height:"5px;"});
	$(".pointW1", $p).css({width:"5px", height:"5px;"});
	$(".pointW2", $p).css({width:"5px", height:"5px;"});
	$(".pointW3", $p).css({width:"5px", height:"5px;"});
	$("#temp-sheet", $p).find("td").css({"font-weight" : "normal"});
	$("#temp-sheet", $p).find("td").css({"color" : "#000"});
}
function hideAll1(){
	var $p = $("#printData1");
	$(".pointW0", $p).css({width:"0px", height:"0px;"});
	$(".pointW1", $p).css({width:"0px", height:"0px;"});
	$(".pointW2", $p).css({width:"0px", height:"0px;"});
	$(".pointW3", $p).css({width:"0px", height:"0px;"});
	$("#temp-sheet", $p).find("td").css({"font-weight" : "normal", "color" : "#000"});
}
function showPW01(){
	hideAll1();
	$(".pointW0", $("#printData1")).parent().css({"font-weight" : "bold", "color" : "red"});
}
function showPW11(){
	hideAll1();
	$(".pointW1", $("#printData1")).parent().css({"font-weight" : "bold", "color" : "#ffbe00"});
}
function showPW21(){
	hideAll1();
	$(".pointW2", $("#printData1")).parent().css({"font-weight" : "bold", "color" : "#ff00c7"});
}
function showPW31(){
	hideAll1();
	$(".pointW3", $("#printData1")).parent().css({"font-weight" : "bold", "color" : "#b500fc"});
}
</script>