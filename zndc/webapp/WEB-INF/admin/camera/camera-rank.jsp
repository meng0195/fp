<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>

<form id="saveCamRankForm" action="${CTX_PATH}/cam/rank/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<div id="camDivId" class="cameraDiv" layoutH="-2">
		<input type="hidden" name="tid" value="${param.tid}"/>
		<div class="panelBar">
			<ul class="toolBar">
				<li><a class="add"><span submitForm="saveCamRankForm">保存</span></a></li>
			</ul>
		</div>
		<div id="camRankMap" layoutH="30">
			<c:forEach var="u" items="${list}" varStatus="st">
				<input type="hidden" name="xaxis" value="${empty u.xaxis ? 0 : u.xaxis}" />
				<div class="camRanking" style="left:${empty u.xaxis ? 0 : u.xaxis}%;top:${empty u.yaxis ? 0 : u.yaxis}%;" >
					<span class="${u.type == 2 ? 'picQiu' : 'picQiang'}"></span>
					<span>${u.camName}</span>
				</div>
				<input type="hidden" name="yaxis" value="${empty u.yaxis ? 0 : u.yaxis}" />
			</c:forEach>
		</div>
	</div>
</form>


<script type="text/javascript">
var $camRankMap = $("#camDivId");
var $camDivId = $("#camDivId");
$(function(){
	$(".camRanking").mousedown(function(e){
		var $this = $(this);
		$this.addClass("now");
		var lefts = $camRankMap.offset().left;
		var tops = $camRankMap.offset().top;
		$this.css("cursor", "move");
		$camRankMap.bind("mousemove",function(ev){
			$this.stop();//加上这个之后 
			var _x = parseInt(ev.pageX - lefts - 20);
			var _y = parseInt(ev.pageY - tops - 20);
			$this.animate({left : _x + "px", top : _y + "px"}, 10); 
		});
	})
	$camRankMap.mouseup(function(){
		var $now = $(".camRanking.now");
		if($now && $now.length == 1){
			$now.removeClass("now");
			$now.css("cursor", "default");
			$now.prev().val(parseInt($now.position().left/$camRankMap.width()*1000)/10);
			$now.next().val(parseInt(($now.position().top+30)/$camRankMap.height()*1000)/10);
			$(this).unbind("mousemove");
		}
	})
})
</script>