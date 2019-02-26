<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="stereoDing" class="ding ${sTag} ${back}" ></div>
<div class="stereo ${sTag}">${html}</div>
<script type="text/javascript">
$(".stereo span.stereo-btn").click(function(){
	var type = $(this).attr("type");
	var val = $(this).attr("val");
	$(".stereo span").hide().css({background : "yellow", color : "#000"});
	$(".stereo span.stereo-btn").show().css({background : "#f1fdfe", color : "#000"});
	$(this).css({background : "red", color : "#FFF"});
	var min = 100, max = -50, mins = [], maxs = [], vals;
	$(".stereo span[" + type +"axis=" + val + "]").each(function(){
		$(this).show();
		vals = $(this).html();
		if(vals == "-"){
		} else if(vals < min){
			mins = [$(this)];
			min = parseFloat(vals);
		} else if(vals == min){
			mins.push($(this));
		} else if(vals == max){
			maxs.push($(this));
		} else if(vals > max){
			maxs = [$(this)];
			max = parseFloat(vals);
		}
	})
	$(mins).each(function(){
		$(this).css({background : "blue", color : "#FFF"});
	});
	$(maxs).each(function(){
		$(this).css({background : "red", color : "#FFF"});
	});
	var left = 377;
	var top = 80;
	var tabTag = eval('${tabTag}') || false;
	if(tabTag){
		left = ($("#stereoDing").parent().width()-500)/2 + 25;
		top = 20 + $("#maxznum").val() * (val-1);
	} else {
		top = 80 + $("#maxznum").val() * (val-1);
	}
	$("#stereoDing").css({left : left + "px", top : top + "px"});
})
$(function(){
	$(".stereo span.stereo-btn:first").click();
})
</script>