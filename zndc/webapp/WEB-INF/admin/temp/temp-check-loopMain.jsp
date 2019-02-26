<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<a class="btn-t-loop-1" target="navTab" rel="tab-check-loop-conf" href="${CTX_PATH}/check/temp/loopConf?tid=${param.tid}" title="循环检测配置">配</a></li>
<a class="btn-t-loop-2" target="ajaxTodo" title="是否开启循环检测，请确认?" href="${CTX_PATH}/check/temp/loopStart?tid=${param.tid}">开</a></li>
<a class="btn-t-loop-3" target="ajaxTodo" title="停止检测命令发送后，会检测完当前仓房在执行停止命令，确认结束吗?" href="${CTX_PATH}/check/temp/loopStop?tid=${param.tid}">结</a></li>
<div id="check-loop-div">
	${rightContent}
</div>
<script type="text/javascript">
//这个函数是提供给后台推送的时候 调用的
//var loops = {};
$(function(){
	$("a.houseTest", $("#check-loop-div")).each(function(e){
		var $this = $(this);
		loops[$this.attr('houseNo')] = $this;
	});
});
/* function showCheckLoop(data){
    var json = eval('(' + data + ')');
    for(var i = 0; i < json.looping.length; i++){
    	var $this = loops[json.looping[i]];
		$this.addClass("checking");
    }
    for(var i = 0; i < json.loops.length; i++){
    	var $this = loops[json.loops[i]];
    	$this.removeClass("checking");
    	if(json.tags[i] == 1){
    		$this.removeClass("green");
 			$this.removeClass("gray");
    		$this.addClass("red");
    	} else {
    		$this.removeClass("red");
 			$this.removeClass("gray");
    		$this.addClass("green");
    	}
    }
} */
</script>