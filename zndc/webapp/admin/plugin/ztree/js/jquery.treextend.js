/**
 *	jquery.treextend.js	
 *	version 1.0
 *	author	wq	wqmain@163.com 
 */
if(typeof (TreeManagers) == "undefined") TreeManagers = new Object();
/* 创建一个闭包 */
(function($){
	MANAGER_TREEXTEND_KEY_PREFIX = "treextend.";
	/* 定义下拉树插件 */
	$.fn.treextend = function(options){
		//debug(this);
		var opts = $.extend({}, $.fn.treextend.defaults, options);
		var zTreeSetting = {
			check: { enable: false },
			data: {
				key: {
					name: "text",
					children: "childs"
				}/*,
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pid"
				}*/
			},
			view: {
				selectedMulti: false,
				dblClickExpand: false
			},
			callback: {
				beforeClick: null,
				onClick: null,
				beforeCheck: null,
				onCheck: null
			}
		};
		
		return this.each(function(){
			var $this = $(this);
			var p = {
				divOffsetHeight : eval($this.attr("divOffsetHeight")||150),
				beforeClick : eval($this.attr("beforeClick")||null),
				onClick : eval($this.attr("onClick")||null),
				afterExecFun : eval($this.attr("afterExecFun")||null),
				data : eval($this.attr("data"))||[],
				isMulti : eval($this.attr("multiMode")||false),
				onCheck : eval($this.attr("onCheck")||null),
				treeSetting : zTreeSetting
			};
			zTreeSetting.check.enable = p.isMulti;	/* 设置是否为多选模式 */
			if(p.isMulti) {
				zTreeSetting.callback.beforeClick=p.beforeClick;
				zTreeSetting.callback.onClick=p.onClick;
				zTreeSetting.callback.onCheck=p.onCheck;
			} else {
				zTreeSetting.callback.beforeClick=p.beforeClick;
				zTreeSetting.callback.onClick=p.onClick;
				zTreeSetting.callback.onCheck=null;
			}
			
			var divId = this.id;
			if(divId && divId != "" && divId.indexOf("Div") != -1) {
				var treeId = divId.replace("Div","");
				var $ztree = $this.find("ul.ztree").attr("id",treeId);
				var zTreeObj = $.fn.zTree.init($ztree, zTreeSetting, p.data);
				if(typeof p.afterExecFun == 'function') {
					p.afterExecFun();
				}
			
				if(this.id) putTree2Managers(this.id, {
					"props":p,
					"treeDivId":divId,"treeDivObj":$this,
					"treeId":treeId,"treeObj":zTreeObj,
					"treeDomObj":$ztree
				});
			}
			$this.height($(document).height()-p.divOffsetHeight);
		});
	};
	function putTree2Managers(treeDivId, obj) {
        TreeManagers[MANAGER_TREEXTEND_KEY_PREFIX+treeDivId] = obj;
	};
	/* 清空下拉树 */
	$.fn.treextend.clear = function(treeDivId) {
		var treeManager = $.fn.treextend.getTreeManager(treeDivId);
		if(treeManager == null) return false;
		$.fn.zTree.init(treeManager.treeDomObj, treeManager.props.treeSetting, null);
	};
	/* 重新初始化下拉树 */
	$.fn.treextend.reload = function(treeDivId, data) {
		var treeManager = $.fn.treextend.getTreeManager(treeDivId);
		if(treeManager == null) return false;
		$.fn.treextend.clear(treeDivId);
		if(typeof(data)=="undefined") data = treeManager.treeDivObj.attr("data");
		$.fn.zTree.init(treeManager.treeDomObj, treeManager.props.treeSetting, eval(data||'[]'));
	};
	/* 根据DIV(class="comboxtree")的ID获取下拉树管理器 */
	$.fn.treextend.getTreeManager = function(treeDivId) {
		return TreeManagers[MANAGER_TREEXTEND_KEY_PREFIX+treeDivId];
	}
	/* 插件的default */
	$.fn.treextend.defaults = {
	};

})(jQuery);