function checkAll(target, group){
	$("input[group='" + group + "']", $(target)).attr("checked", true);
}

function unCheckAll(target, group){
	$("input[group='" + group + "']", $(target)).attr("checked", false);
}

function opCheck(target, group){
	$("input[group='" + group + "']", $(target)).each(function(e){
		var $this = $(this);
		if($this.attr("checked")){
			$this.attr("checked", false);
		} else {
			$this.attr("checked", true);
		}
	});
}
function isArr(arrs, param){
	var tag = false;
	for(var i = 0; i < arrs.length; i++){
		if(arrs[i] == param){
			tag = true;
			break;
		}
	}
	return tag;
}
function toWeekStr(num){
	var str;
	switch (num) {
	case 1 : str = "星期日"; break;
	case 2 : str = "星期一"; break;
	case 3 : str = "星期二"; break;
	case 4 : str = "星期三"; break;
	case 5 : str = "星期四"; break;
	case 6 : str = "星期五"; break;
	default : str = "星期六"; break;
	}
	return str;
}