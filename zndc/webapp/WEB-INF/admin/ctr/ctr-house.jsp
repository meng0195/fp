<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<div id="equipRankMap" class="side0" layoutH="30">
	<c:forEach var="equip" items="${maps}" varStatus="st">
		<div class="equip equip-${equip.value.type} ${equip.value.clazz}" side="${equip.value.sideTag}" style="display:${equip.value.sideTag == 0 ? 'block':'none'};left:${equip.value.xaxis}px;top:${equip.value.yaxis}px;" eType="${equip.value.type}" eCode="${equip.value.equipNo}" ><span>${equip.value.equipName}</span></div>
	</c:forEach>
</div>
<div id="sideChangeBtn" title="正反面切换"></div>
<div id="tfBtn" class="eqBtn btn-tf" title="通风"></div>
<div id="nhlBtn" class="eqBtn btn-nhl" title="内环流"></div>
<div id="zmBtn" class="eqBtn btn-zm" title="照明"></div>
<div id="ktBtn" class="eqBtn btn-kt" title="空调"></div>
<a id="spjkBtn" class="eqBtn btn-spjk" target="dialog" mask="true" maxable="false" resizable="false" width="1200" height="575" rel="dialog-ctr-house" href="${CTX_PATH}/control/clear/${houseNo}" title="${map:get(codes['houses'], houseNo)}"></a>
<a id="sxztBtn" class="eqBtn btn-sxzt" target="dialog" mask="true" maxable="false" resizable="false" width="1200" height="575" rel="dialog-ctr-house" href="${CTX_PATH}/control/reload/${houseNo}" title="${map:get(codes['houses'], houseNo)}"></a>
<div id="czrzDiv">
	<span id="czrzDiv-span">操作信息：</span>
</div>
<div id="sbztDiv" layoutH="260">
	<span>设备状态：</span>
	<c:forEach var="equip" items="${maps}" varStatus="st">
		<span id="no_${equip.value.equipNo}" eName="${equip.value.equipName}">${equip.value.equipName}:${warn:getEquipStatus(equip.value.model, equip.value.status)}</span>
	</c:forEach>
</div>
<script type="text/javascript">
$("#sideChangeBtn").click(function(){
	$("div.eqBtn").removeClass("sel");
	var $map = $("#equipRankMap");
	if($map.hasClass("side0")){
		$("#equipRankMap").removeClass("side0");
		$("#equipRankMap").addClass("side1");
		$("div[side='0']", $equipRankMap).hide();
		$("div[side='1']", $equipRankMap).show();
	} else {
		$("#equipRankMap").removeClass("side1");
		$("#equipRankMap").addClass("side0");
		$("div[side='0']", $equipRankMap).show();
		$("div[side='1']", $equipRankMap).hide();
	}
})
var $equipRankMap = $("#equipRankMap");
$(function(){
	$("div[etype='10']", $equipRankMap).html("");
	$("div[etype='3']", $equipRankMap).html("");
	$("div[etype='5']", $equipRankMap).html("");
	$equipRankMap.find(".equip").css({cursor: "pointer"});
})
$("#tfBtn").click(function(){
	var side = $equipRankMap.hasClass("side0") == true ? 0 : 1;
	$("div", $equipRankMap).hide();
	$("div[side='" + side + "'][etype='1']", $equipRankMap).show();
	$("div[side='" + side + "'][etype='2']", $equipRankMap).show();
	$("div[side='" + side + "'][etype='3']", $equipRankMap).show();
	$("div[side='" + side + "'][etype='4']", $equipRankMap).show();
	$("div[side='" + side + "'][etype='5']", $equipRankMap).show();
	$("div[side='" + side + "'][etype='9']", $equipRankMap).show();
	$("div[side='" + side + "'][etype='10']", $equipRankMap).show();
})
$("div.eqBtn").click(function(){
	$(this).siblings("div.eqBtn").removeClass("sel");
	$(this).addClass("sel")
})
$("#nhlBtn").click(function(){
	var side = $equipRankMap.hasClass("side0") == true ? 0 : 1;
	$("div", $equipRankMap).hide();
	$("div[side='" + side + "'][etype='8']", $equipRankMap).show();
})
$("#zmBtn").click(function(){
	var side = $equipRankMap.hasClass("side0") == true ? 0 : 1;
	$("div", $equipRankMap).hide();
	$("div[side='" + side + "'][etype='6']", $equipRankMap).show();
})
$("#ktBtn").click(function(){
	var side = $equipRankMap.hasClass("side0") == true ? 0 : 1;
	$("div", $equipRankMap).hide();
	$("div[side='" + side + "'][etype='7']", $equipRankMap).show();
})
$equipRankMap.click(function(e){
	var $target = $(e.target)
	var houseNo = '${houseNo}';
	 if($target.hasClass("equip")){
		if (($target.attr("eType") == 11)) {
			var options = {
					width : 1200,
					height : 600,
					max : false,
					mask : true,
					maxable : false,
					minable : false,
					fresh : false,
					resizable : false,
					drawable : false
				};
				url = "${CTX_PATH}/control/showCam/" + houseNo;
				$.pdialog.open(url, "show-camera-detail", "摄像头详情", options);
		} else {
			$.ajax({
				url:"${CTX_PATH}/control/doall/" + houseNo + "/" + ($target.attr("eCode") || 0),
				type : 'POST',
				dataType : "json",
				cache : false,
				success : function(json){
					if(json[DWZ.keys.statusCode] == DWZ.statusCode.ok){
						alertMsg.info(json.message);
					} else {
						alertMsg.error(json.message);
					}
				},
				error : ""
			})
		}
	}
})
</script>