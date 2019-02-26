<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<a class="btn-t-one-1" target="navTab" rel="tab-check-one-conf" href="${CTX_PATH}/check/temp/oneConf?tid=${param.tid}" title="手动检测配置">配</a>
<a class="btn-t-one-2" target="ajaxTodo" title="是否开启手动检测，请确认?" href="${CTX_PATH}/check/temp/oneStart/0?tid=${param.tid}">开</a>
<a class="btn-t-one-2" target="ajaxTodo" title="是否开启手动检测，并将检测结果置为报表数据，请确认?" href="${CTX_PATH}/check/temp/oneStart/1?tid=${param.tid}">报</a>
<div id="check-one-div">${r}</div>
<script type="text/javascript">
//这个函数是提供给后台推送的时候 调用的
var ones = {};
$(function(){
	$("a.houseTest", $("#check-one-div")).each(function(e){
		var $this = $(this);
		ones[$this.attr('houseNo')] = $this;
	});
});
function showCheckOne(data){
    var json = eval('(' + data + ')');
    for(var i = 0; i < json.ones.length; i++){
    	var $this = ones[json.ones[i]];
    	$this.removeClass("checking");
    	if(!$this.hasClass("gray")){
	    	if(json.tags[i] == 1){
	    		$this.removeClass("green");
	    		$this.removeClass("yellow");
	    		$this.addClass("red");
	    	} else {
	    		$this.removeClass("red");
	    		$this.removeClass("yellow");
	    		$this.addClass("green");
	    	}
    	}
    }
    for(var i = 0; i < json.waits.length; i++){
    	var $this = ones[json.waits[i]];
    	if(!$this.hasClass("gray")){
    		$this.removeClass("red");
    		$this.removeClass("green");
			$this.addClass(" yellow ");
    	}
    }
    for(var i = 0; i < json.oneing.length; i++){
    	var $this = ones[json.oneing[i]];
    	if(!$this.hasClass("gray")){
    		$this.removeClass("red");
    		$this.removeClass("green");
			$this.addClass("checking");
    	}
    }

}
</script>