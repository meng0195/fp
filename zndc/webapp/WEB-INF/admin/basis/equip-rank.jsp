<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveEquipRankForm" action="${CTX_PATH}/basis/equip/rank/save" method="post"
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="houseNo" value="${houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveEquipRankForm">保存</span></a></li>
		</ul>
	</div>
	<div id="equipRankMap" class="side0" layoutH="30">
		<c:forEach var="equip" items="${list}" varStatus="st">
			<input type="hidden" name="xaxis" value="${equip.xaxis}"/>
			<div class="equip equip-${equip.type}" side="${equip.sideTag}" style="display:${equip.sideTag == 0 ? 'block':'none'};left:${equip.xaxis}px;top:${equip.yaxis}px;" eType="${equip.type}"><span>${equip.equipName}</span></div>
			<input type="hidden" name="yaxis" value="${equip.yaxis}"/>
		</c:forEach>
	</div>
	<div id="sideChangeBtn" ></div>
	<div id="tfBtn" class="eqBtn btn-tf"></div>
	<div id="nhlBtn" class="eqBtn btn-nhl"></div>
	<div id="zmBtn" class="eqBtn btn-zm"></div>
	<div id="ktBtn" class="eqBtn btn-kt"></div>
	<div id="spjkBtn" class="eqBtn btn-spjk"></div>
</form>
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
	$(".equip").mousedown(function(e){
		var $this = $(this);
		$this.addClass("now");
		var lefts = $equipRankMap.offset().left;
		var tops = $equipRankMap.offset().top;
		$this.css("cursor", "move");
		$equipRankMap.bind("mousemove",function(ev){
			$this.stop();//加上这个之后 
			var _x = parseInt(ev.pageX - lefts - 20);
			var _y = parseInt(ev.pageY - tops - 20);
			$this.animate({left : _x + "px", top : _y + "px"}, 10); 
		});
	})
	$equipRankMap.mouseup(function(){
		var $now = $(".equip.now");
		if($now && $now.length == 1){
			$now.removeClass("now");
			$now.css("cursor", "default");
			$now.prev().val(parseInt($now.position().left));
			$now.next().val(parseInt($now.position().top));
			$(this).unbind("mousemove");
		}
	})
})
$("div.eqBtn").click(function(){
	$(this).siblings("div.eqBtn").removeClass("sel");
	$(this).addClass("sel")
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
$("#spjkBtn").click(function(){
	var side = $equipRankMap.hasClass("side0") == true ? 0 : 1;
	$("div", $equipRankMap).hide();
	$("div[side='" + side + "'][etype='11']", $equipRankMap).show();
})
</script>