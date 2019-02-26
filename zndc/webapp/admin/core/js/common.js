$(function(){
	DWZ.init("dwz.frag.xml", {
		loginUrl:"login/dialog", loginTitle:"系统登录",
		statusCode:{ok:200, error:300, timeout:301},
		pageInfo:{pageNum:"page.pageNo", numPerPage:"page.pageSize", orderField:"page.orderBy", orderDirection:"page.order"},
		keys: {statusCode:"statusCode", message:"message"},
		debug:false,
		callback:function(){
			initEnv();
			$("#themeList").theme({themeBase:"themes"});
			setTimeout(function(){
				$("li.main").click();
			}, 500);
		}
	});
	
	loadFormValidate();
	/**
	 * 换肤
	 */
	var skin = skinGetCookie("zndcSkinCookie");
	if(skin){
		$(".skin li").removeClass("sel");
		$("li[code='" + skin + "']").addClass("sel");
		$("#skinLink").attr("href", "/zndc/admin/core/themes/" + skin + "/style.css");
		skinSetCookie("zndcSkinCookie", skin);
	} else {
		skinSetCookie("zndcSkinCookie", "default");
	}
	
});
var showCDUrl = window.location.href.substring(0, window.location.href.indexOf("/zndc/")) + "/zndc/squery/show/";
function showCheckDetail(houseNo, testDataId, type){
	var options = {
		width : 1200,
		height : 600,
		max : false,
		mask : true,
		maxable : false,
		minable : false,
		fresh : false,
		resizable : false,
		drawable : false
	};
	url = unescape(showCDUrl + (houseNo ? houseNo : 0) + "/" + (testDataId ? testDataId : 0) + "/" + (type || 0)).replaceTmById($(this).parents(".unitBox:first"));
	DWZ.debug(url);
	if (!url.isFinishedTm()) {
		alertMsg.error($this.attr("warn") || DWZ.msg("alertSelectMsg"));
		return false;
	}
	$.pdialog.open(url, "show-check-detail", "检测详情", options);
}
function loginDialogAjaxDone(json) {
	DWZ.ajaxDone(json);
	if (json[DWZ.keys.statusCode] == DWZ.statusCode.ok){
		$("#loginAjaxUserForm").submit();
		$("#loginAjaxMenuForm").submit();
		$.pdialog.closeCurrent();
	}
	return false;
}

function logout(name, url) {
	alertMsg.confirm("您好, " + name + ", 确认要退出登录吗？", {
		okCall: function(){
			window.location.href = url;
		}
	});
}

function loadFormValidate() {
	$.validator.addMethod("login", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]{6,16}$/i.test(value);
	}, "长度在6至16之间的英文字母或数字"); 
	$.validator.addMethod("passwd", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]{6,32}$/i.test(value);
	}, "长度在6至32之间的英文字母或数字"); 
	$.validator.addMethod("normal", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9\u4e00-\u9fa5]+$/i.test(value);
	}, "禁止输入特殊字符"); 
	$.validator.addMethod("tel", function(value, element) {
		return this.optional(element) || /^[1]{1}[3578]{1}[0-9]{9}$/i.test(value);
	}, "手机号码格式不正确"); 
	$.validator.addMethod("mail", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9_\-]+@[a-zA-Z0-9_\-]+\.(com|com.cn|net|org)$/i.test(value);
	}, "电子邮箱格式不正确");
	$.validator.addMethod("vehicleNum", function(value, element) {
		return this.optional(element) || /^[\u4e00-\u9fa5A-Z]{1}[A-Z0-9]{5}[A-Z0-9学警]{1}$/i.test(value);
	}, "车牌号码格式不正确");
	$.validator.addMethod("float", function(value, element) {
		return this.optional(element) || /^(\d{1,8}|\d{1,8}\.\d{1})$/.test(value);
	}, "长度为8位的数值，最多1位小数"); 
	$.validator.addMethod("double2", function(value, element) {
		return this.optional(element) || /^(\d{1,8}|\d{1,8}\.\d{1,2})$/.test(value);
	}, "长度为8位的数值，最多2位小数");
	$.validator.addMethod("double3", function(value, element) {
		return this.optional(element) || /^(\d{1,6}|\d{1,6}\.\d{1,3})$/.test(value);
	}, "长度为6位的数值，最多3位小数");
	$.validator.addMethod("double4", function(value, element) {
		return this.optional(element) || /^(\d{1,8}|\d{1,8}\.\d{1,4})$/.test(value);
	}, "长度为8位的数值，最多4位小数");
	$.validator.addMethod("double6", function(value, element) {
		return this.optional(element) || /^(\d{1,4}|\d{1,4}\.\d{1,6})$/.test(value);
	}, "长度为12位的数值，最多6位小数");
	
}

function disableKeys() {
	$(document).on("contextmenu", function(e) {
		return false;
	});
	$(document).on("keydown", function(e) {
		var code = e.keyCode;
		//禁用退格键、Alt + <- 和 Alt + ->组合键
		if(code == 37 || code == 39) {
			return false;
		}
	});
}

function loadMainTabAjaxDone(json) {
	if(json == null) return false;
	if (json.statusCode == DWZ.statusCode.ok) {
		alertMsg.correct(json.message);
		$("#mainForm").submit();
	}else{
		alertMsg.error(json.message);
	}
}

function loadMainDialogAjaxDone(json) {
	if(json == null) return false;
	if (json.statusCode == DWZ.statusCode.ok) {
		alertMsg.correct(json.message);
		if($.pdialog) {
			$.pdialog.closeCurrent();
		}
		$("#mainForm").submit();
	}else{
		alertMsg.error(json.message);
	}
}

function treeAfterSaveCallback(json) {
	if(json == null) return false;
	
	if (json.statusCode == DWZ.statusCode.ok) {
		alertMsg.correct(json.message);
		navTab.reloadFlag(json.navTabId);
	} else {
		alertMsg.error(json.message);
	}
}


function deleteTreeNode(json, treeId){
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	var treeNode = zTree.getNodeByParam("id", json.extra);
	if(treeNode != null) {
		var pNode = treeNode.getParentNode();
		zTree.removeNode(treeNode);
		/*
		if(pNode && pNode != null) {
			zTree.selectNode(pNode);
		} else {
			navTab.reloadFlag(json.navTabId);
		}*/
		navTab.reloadFlag(json.navTabId);
		return pNode;
	}
	return null;
}

function showInfoMsg(message) {
	if (alertMsg) {
		alertMsg.info(message);
	}
}

function showWarnMsg(message) {
	if (alertMsg) {
		alertMsg.warn(message);
	}
}

function showErrMsg(message) {
	if (alertMsg) {
		alertMsg.error(message);
	}
}

function getObjectId() {
	return "objSample";
}

var loops = {};
function showCheckLoop(data){
    var json = eval('(' + data + ')');
    for(var i = 0; i < json.looping.length; i++){
    	var $this = loops[json.looping[i]] || false; 
    	if($this){
			$this.addClass("checking");
    	}
    }
    for(var i = 0; i < json.loops.length; i++){
    	var $this = loops[json.loops[i]] || false;
    	if($this){
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
    }
}


function changeTest(param){
	var json = $.parseJSON(param);
	if(json){
		var pestCheckMain = $("#pestCheckMain");
		if(pestCheckMain && pestCheckMain.length > 0){
			for(var key in json){
				var $a = $("a[houseNo='" + key + "']", pestCheckMain);
				if(json[key] == 4){
					$a.addClass("checking");
					$a.attr("href", $a.attr("href").replace("/0/check", "/0/view"));
				} else if(json[key] == 5){
					$a.removeClass("checking");
					$a.removeClass("green");
					$a.removeClass("red");
					$a.addClass("yellow");
				} else if(json[key] == 1){
					$a.removeClass("checking");
					$a.removeClass("green");
					$a.removeClass("yellow");
					$a.addClass("red");
				} else {
					$a.removeClass("checking");
					$a.removeClass("red");
					$a.removeClass("yellow");
					$a.addClass("green");
				}
			}
		}
	}
}

function changeEquip(param){
	var json = $.parseJSON(param);
	if(json){
		var equipRankMap = $("#equipRankMap");
		if(equipRankMap && equipRankMap.length > 0){
			var $equip = $("div[eCode='" + json.equipNo + "']", equipRankMap);
			$equip.removeClass("ing close half open bad ing1");
			$equip.addClass(json.clazz);
		}
		$("#czrzDiv-span").after("<span>" + json.msg + "</span>");
		var $eqTag = $("#no_" + json.equipNo);
		$eqTag.html($eqTag.attr("eName") + ":" + getEquipStatus(json.model, json.status));
	}
}


function changeWayCheck(param){debugger
	var gasRankMap = $("#gasRankMap");
	if (gasRankMap) {
		if(param != "stop"){
			var way = $.parseJSON(param);
			$("span[wayNo]", gasRankMap).removeClass("picing");
			$("span[wayNo]", gasRankMap).addClass("pic");
			var $span = $("span[wayNo='" + way + "']", gasRankMap);
			$span.removeClass("pic");
			$span.addClass("picing");
		} else {
			$("span[wayNo]", gasRankMap).removeClass("picing");
			$("span[wayNo]", gasRankMap).addClass("pic");
		}
	}
}

function skinSetCookie(name, value){//建立cookie记录  
    var day=30;//有效时间  
    var exp=new Date();//建立Date对象  
    exp.setTime(exp.getTime()+day*24*60*1000);//setTime()，以毫秒设置Date对象。etTime()可返回距 1970 年 1 月 1 日之间的毫秒数。  
    //写入cookie，escape() 函数可对字符串进行编码，这样就可以在所有的计算机上读取该字符串  
    document.cookie=name+"="+escape(value)+";expires="+exp.toGMTString();  
}  
function skinGetCookie(name){  
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");//正则表达式  
    if(arr=document.cookie.match(reg)){//match() 方法可在字符串内检索指定的值，或找到一个或多个正则表达式的匹配。  
        return (arr[2]);  
    }  
    else{  
        return null;  
    }  
}