/**
 * @author 张慧华 z@j-ui.com
 * 
 */
(function($){
	$.printBox = function(rel){
		var _printBoxId = 'printBox';
		var $contentBox = rel ? $('#'+rel) : $("body"),
			$printBox = $('#'+_printBoxId);
			
		if ($printBox.size()==0){
			$printBox = $('<div id="'+_printBoxId+'"></div>').appendTo("body");
		}

		$printBox.html($contentBox.html()).find("[layoutH]").height("auto");
		if(rel == "printData1"){
			$("body").find(".dialog").css({"display" : "none"});
			$("body").find(".shadow").css({"display" : "none"});
		}
		window.print();
		if(rel == "printData1"){
			$("body").find(".dialog").css({"display" : "block"});
			$("body").find(".shadow").css({"display" : "block"});
		}
	}

})(jQuery);
