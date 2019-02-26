<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<div id="equipRankMap" class="side0" layoutH="30">
	<c:forEach var="equip" items="${equips.equips}" varStatus="st">
		<div class="equip equip-${equip.value.type} ${equip.value.clazz}" side="${equip.value.sideTag}" style="display:${equip.value.sideTag == 0 ? 'block':'none'};left:${equip.value.xaxis}px;top:${equip.value.yaxis}px;" eType="${equip.value.type}" eCode="${equip.value.equipNo}" ><span>${equip.value.equipName}</span></div>
	</c:forEach>
</div>
<div id="sideChangeBtn" title="正反面切换"></div>
<div id="tfBtn" class="eqBtn btn-tf" title="通风"></div>
<div id="nhlBtn" class="eqBtn btn-nhl" title="内环流"></div>
<div id="zmBtn" class="eqBtn btn-zm" title="照明"></div>
<div id="ktBtn" class="eqBtn btn-kt" title="空调"></div>
<div id="spjkBtn" class="eqBtn btn-spjk" title="视频监控"></div>
<div id="sbztDiv" layoutH="60" style="top:75px;">
	<span>设备状态：</span>
	<c:forEach var="equip" items="${equips.equips}" varStatus="st">
		<span style="width:240px;color:${equip.value.clazz == 'bad' ? 'red' : '#000'}" id="no_${equip.value.equipNo}" eName="${equip.value.equipName}">${equip.value.equipName}:${warn:getEquipStatus(equip.value.model, equip.value.status)}</span>
	</c:forEach>
</div>
<script type="text/javascript">
$("#sideChangeBtn").click(function(){
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
</script>