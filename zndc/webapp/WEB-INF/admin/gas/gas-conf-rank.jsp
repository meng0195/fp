<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveConfGasRankForm" action="${CTX_PATH}/conf/gas/rank/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="houseNo" value="${houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveConfGasRankForm">保存</span></a></li>
		</ul>
	</div>
	<div id="gasRankMap" layoutH="30">
		<c:forEach var="a" begin="0" end="${wayNumb - 1}" varStatus="st">
			<input type="hidden" name="xaxis" value="${empty wayXaxis[st.index] ? 0 : wayXaxis[st.index]}" />
			<div class="gasWayRanking" style="left:${empty wayXaxis[st.index] ? 0 : wayXaxis[st.index]}px;top:${empty wayYaxis[st.index] ? 0 : wayYaxis[st.index]}px;" >
				<span class="fenglu">风路${st.index + 1}</span>
				<span class="v-co2">C:</span>
				<span class="pic"></span>
				<span class="v-ph3">P:</span>
				<span class="v-o2">O:</span>
			</div>
			<input type="hidden" name="yaxis" value="${empty wayYaxis[st.index] ? 0 : wayYaxis[st.index]}" />
		</c:forEach>
	</div>
</form>
<script type="text/javascript">
var $gasRankMap = $("#gasRankMap");
$(function(){
	$(".gasWayRanking").mousedown(function(e){
		var $this = $(this);
		$this.addClass("now");
		var lefts = $gasRankMap.offset().left;
		var tops = $gasRankMap.offset().top;
		$this.css("cursor", "move");
		$gasRankMap.bind("mousemove",function(ev){
			$this.stop();//加上这个之后 
			var _x = parseInt(ev.pageX - lefts - 20);
			var _y = parseInt(ev.pageY - tops - 20);
			$this.animate({left : _x + "px", top : _y + "px"}, 10); 
		});
	})
	$gasRankMap.mouseup(function(){
		var $now = $(".gasWayRanking.now");
		if($now && $now.length == 1){
			$now.removeClass("now");
			$now.css("cursor", "default");
			$now.prev().val(parseInt($now.position().left));
			$now.next().val(parseInt($now.position().top));
			$(this).unbind("mousemove");
		}
	})
})
</script>