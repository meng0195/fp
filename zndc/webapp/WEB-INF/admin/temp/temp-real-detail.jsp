<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<input type="hidden" name="houseNo" value="${houseNo}"/>
<input type="hidden" name="tid" value="${param.tid}"/>
<div layoutH="5">
	<div style="height: 30px; margin-right: 20px;">
		<select id="timeGap" onchange="changeTime()" style="float: right;">
			<option value="3000">3s</option>
			<option value="5000" selected="selected">5s</option>
			<option value="10000">10s</option>
			<option value="20000">20s</option>
			<option value="30000">30s</option>
		</select>
		<span style="float: right; margin-top: 5px;">刷新频率：</span>
	</div>
	<div id="showReal">
	</div>
</div>

<script type="text/javascript">
	var tempTimeIntv;
	
	$(function(){
		ajaxs(5000);
	});
	
	function changeTime(){
		ajaxs($("#timeGap").val());
	}
	
	function creatTimer(time){
		tempTimeIntv = window.setTimeout(function(){
			ajaxs(time);
		}, time);
	}
	
	function ajaxs(time){
		var $showReal = $("#showReal");
		if($showReal && $showReal.length > 0){
			$.ajax({
				type : 'POST',
				url : '${CTX_PATH}/check/temp/showRealDetail/${houseNo}?tid=${param.tid}',
				dataType : "html",
				cache : false,
				success : function(html){
					$("#showReal").html(html);
					window.clearTimeout(tempTimeIntv);
					creatTimer(time);
				},
				error : ""
			});
		}
	}
</script>