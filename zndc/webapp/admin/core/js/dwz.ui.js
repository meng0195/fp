function initEnv() {
	$("body").append(DWZ.frag["dwzFrag"]);

	$(window).resize(function(){
		initLayout();
		$(this).trigger(DWZ.eventType.resizeGrid);
	});

	var ajaxbg = $("#background,#progressBar");
	ajaxbg.hide();
	$(document).ajaxStart(function(){
		ajaxbg.show();
	}).ajaxStop(function(){
		ajaxbg.hide();
	});
	
	if ($.fn.jBar) $("#leftside").jBar({minW:150, maxW:700});
	
	if ($.taskBar) $.taskBar.init();
	if ($.fn.switchEnv) $("#switchEnvBox").switchEnv();
	if ($.fn.navMenu) $("#navMenu").navMenu();
		
	setTimeout(function(){
		initLayout();
		if (window.navTab) navTab.init();

		// 注册DWZ插件。
		DWZ.regPlugins.push(initUI); //第三方jQuery插件注册方法：DWZ.regPlugins.push(function($p){});

		// 首次初始化插件
		$(document).initUI();
		
		// navTab styles
		var jTabsPH = $("div.tabsPageHeader");
		jTabsPH.find(".tabsLeft").hoverClass("tabsLeftHover");
		jTabsPH.find(".tabsRight").hoverClass("tabsRightHover");
		jTabsPH.find(".tabsMore").hoverClass("tabsMoreHover");

		$(document).trigger(DWZ.eventType.initEnvAfter);
	}, 10);

}
function initLayout(){
	var iContentW = $(window).width() - (DWZ.ui.sbar ? $("#sidebar").width() + 10 : 34) - 5;
	var iContentH = $(window).height() - $("#header").height() - 34;

	$("#container").width(iContentW);
	$("#container .tabsPageContent").height(iContentH - 34).find("[layoutH]").layoutH();
	$("#sidebar, #sidebar_s .collapse, #splitBar, #splitBarProxy").height(iContentH - 5);
	$("#taskbar").css({top: iContentH + $("#header").height() + 5, width:$(window).width()});
}

function initUI($p){
	//tables
	if ($.fn.jTable) $("table.table", $p).jTable();

	// css tables
	if ($.fn.cssTable) $('table.list', $p).not('.nowrap').cssTable();

	if ($.fn.jPanel) $("div.panel", $p).jPanel();

	//auto bind tabs
	$("div.tabs", $p).each(function(){
		var $this = $(this);
		var options = {};
		options.currentIndex = $this.attr("currentIndex") || 0;
		options.eventType = $this.attr("eventType") || "click";
		$this.tabs(options);
	});

	if ($.fn.jTree) $("ul.tree", $p).jTree();

	if ($.fn.jTree){
		$('div.accordion', $p).each(function(){
			var $this = $(this);
			$this.accordion({fillSpace:$this.attr("fillSpace"),alwaysOpen:true,active:0});
		});
	}

	if ($.fn.checkboxCtrl){
		$(":button.checkboxCtrl, :checkbox.checkboxCtrl", $p).checkboxCtrl($p);
	}
	
	if ($.fn.comboxtree) $("select.comboxtree",$p).comboxtree();
	
	if ($.fn.xheditor) {
		$("textarea.editor", $p).each(function(){
			var $this = $(this);
			var op = {html5Upload:false, skin: 'vista',tools: $this.attr("tools") || 'full'};
			var upAttrs = [
				["upLinkUrl","upLinkExt","zip,rar,txt"],
				["upImgUrl","upImgExt","jpg,jpeg,gif,png"],
				["upFlashUrl","upFlashExt","swf"],
				["upMediaUrl","upMediaExt","avi"]
			];
			
			$(upAttrs).each(function(i){
				var urlAttr = upAttrs[i][0];
				var extAttr = upAttrs[i][1];
				
				if ($this.attr(urlAttr)) {
					op[urlAttr] = $this.attr(urlAttr);
					op[extAttr] = $this.attr(extAttr) || upAttrs[i][2];
				}
			});
			
			$this.xheditor(op);
		});
	}
	
	if ($.fn.uploadify) {
		$(":file[uploaderOption]", $p).each(function(){
			var $this = $(this);
			var options = {
				fileObjName: $this.attr("name") || "file",
				auto: true,
				multi: true,
				onUploadError: uploadifyError
			};
			
			var uploaderOption = DWZ.jsonEval($this.attr("uploaderOption"));
			$.extend(options, uploaderOption);

			DWZ.debug("uploaderOption: "+DWZ.obj2str(uploaderOption));
			
			$this.uploadify(options);
		});
	}
	
	// init styles
	$("input[type=text], input[type=password], textarea", $p).addClass("textInput").focusClass("focus");

	$("input[readonly], textarea[readonly]", $p).addClass("readonly");
	$("input[disabled=true], textarea[disabled=true]", $p).addClass("disabled");

	$("input[type=text]", $p).not("div.tabs input[type=text]", $p).filter("[alt]").inputAlert();

	//Grid ToolBar
	$("div.panelBar li, div.panelBar", $p).hoverClass("hover");

	//Button
	$("div.button", $p).hoverClass("buttonHover");
	$("div.buttonActive", $p).hoverClass("buttonActiveHover");
	
	//tabsPageHeader
	$("div.tabsHeader li, div.tabsPageHeader li, div.accordionHeader, div.accordion", $p).hoverClass("hover");

	//validate form
	if ($.fn.validate) {
		$("form.required-validate", $p).each(function(){
			var $form = $(this);
			$form.validate({
				onsubmit: false,
				focusInvalid: false,
				focusCleanup: true,
				errorElement: "span",
				ignore:".ignore",
				invalidHandler: function(form, validator) {
					var errors = validator.numberOfInvalids();
					if (errors) {
						var message = DWZ.msg("validateFormError",[errors]);
						alertMsg.error(message);
					}
				}
			});

			$form.find('input[customvalid]').each(function(){
				var $input = $(this);
				$input.rules("add", {
					customvalid: $input.attr("customvalid")
				})
			});
		});
	}

	if ($.fn.datepicker){
		$('input.date', $p).each(function(){
			var $this = $(this);
			var opts = {};
			if ($this.attr("dateFmt")) opts.pattern = $this.attr("dateFmt");
			if ($this.attr("mmStep")) opts.mmStep = $this.attr("mmStep");
			if ($this.attr("ssStep")) opts.ssStep = $this.attr("ssStep");
			if ($this.attr("callback")) opts.callback = $this.attr("callback");
			if ($this.attr("minId")) opts.minId = $this.attr("minId");
			if ($this.attr("maxId")) opts.maxId = $this.attr("maxId");
			if ($this.attr("minDate")) opts.minDate = $this.attr("minDate");
			if ($this.attr("maxDate")) opts.minDate = $this.attr("maxDate");
			$this.datepicker(opts);
		});
	}
	// navTab
	$("a[target=navTab]", $p).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var title = $this.attr("title") || $this.text();
			var tabid = $this.attr("rel") || "_blank";
			var fresh = eval($this.attr("fresh") || "true");
			var external = eval($this.attr("external") || "false");
			var url = unescape($this.attr("href")).replaceTmById($(event.target).parents(".unitBox:first"));
			DWZ.debug(url);
			if (!url.isFinishedTm()) {
				alertMsg.error($this.attr("warn") || DWZ.msg("alertSelectMsg"));
				return false;
			}
			navTab.openTab(tabid, url,{title:title, fresh:fresh, external:external});

			event.preventDefault();
		});
	});
	//dialogs
	$("a[target=dialog]", $p).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var title = $this.attr("title") || $this.text();
			var rel = $this.attr("rel") || "_blank";
			var options = {};
			var w = $this.attr("width");
			var h = $this.attr("height");
			if (w) options.width = w;
			if (h) options.height = h;
			options.max = eval($this.attr("max") || "false");
			options.mask = eval($this.attr("mask") || "false");
			options.maxable = eval($this.attr("maxable") || "true");
			options.minable = eval($this.attr("minable") || "true");
			options.fresh = eval($this.attr("fresh") || "true");
			options.resizable = eval($this.attr("resizable") || "true");
			options.drawable = eval($this.attr("drawable") || "true");
			options.close = eval($this.attr("close") || "");
			options.param = $this.attr("param") || "";

			var url = unescape($this.attr("href")).replaceTmById($(event.target).parents(".unitBox:first"));
			DWZ.debug(url);
			if (!url.isFinishedTm()) {
				alertMsg.error($this.attr("warn") || DWZ.msg("alertSelectMsg"));
				return false;
			}
			$.pdialog.open(url, rel, title, options);
			
			return false;
		});
	});
	$("a[target=ajax]", $p).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var rel = $this.attr("rel");
			if (rel) {
				var $rel = $("#"+rel);
				var url = unescape($this.attr("href")).replaceTmById($(event.target).parents(".unitBox:first"));
				DWZ.debug(url);
				if (!url.isFinishedTm()) {
					alertMsg.error($this.attr("warn") || DWZ.msg("alertSelectMsg"));
					return false;
				}

				$rel.loadUrl(url, {}, function(){
					$rel.find("[layoutH]").layoutH();
				});
			}

			event.preventDefault();
		});
	});
	
	$("div.pagination", $p).each(function(){
		var $this = $(this);
		$this.pagination({
			targetType:$this.attr("targetType"),
			rel:$this.attr("rel"),
			totalCount:$this.attr("totalCount"),
			numPerPage:$this.attr("numPerPage"),
			pageNumShown:$this.attr("pageNumShown") || 10,
			currentPage:$this.attr("currentPage")
		});
	});

	if ($.fn.sortDrag) $("div.sortDrag", $p).sortDrag();

	// dwz.ajax.js
	if ($.fn.ajaxTodo) $("a[target=ajaxTodo]", $p).ajaxTodo();
	if ($.fn.dwzExport) $("a[target=dwzExport]", $p).dwzExport();

	if ($.fn.lookup) $("a[lookupGroup]", $p).lookup();
	if ($.fn.multLookup) $("[multLookup]:button", $p).multLookup();
	if ($.fn.multLookup) $("a[multLookup]", $p).multLookup();
	if ($.fn.suggest) $("input[suggestFields]", $p).suggest();
	if ($.fn.itemDetail) $("table.itemDetail", $p).itemDetail();
	if ($.fn.selectedTodo) $("a[target=selectedTodo]", $p).selectedTodo();
	if ($.fn.pagerForm) $("form[rel=pagerForm]", $p).pagerForm({parentBox:$p});
	
	$("a[target=form]",$p).each(function(){
		var $target=$(this);
		var formId=$target.attr("rel");
		var href=$target.attr("href");
		var beforeFun=eval($target.attr("beforeExecFun")||null);
		$target.click(function(e){
			if(!formId)return false;
			var $form=$("#"+formId);
			var action=$form.attr("action");
			if(beforeFun && typeof beforeFun == "function") {
				var ret=beforeFun();
				if(!ret) {
					return false;
				}
			}
			if(href) {
				$form.attr("action", href);
			} else {
				var url = unescape(action).replaceTmById($(e.target).parents(".unitBox:first"));
				if (!url.isFinishedTm()) {
					alertMsg.error($form.attr("warn") || DWZ.msg("alertSelectMsg"));
					return false;
				}
			}
			var title = $target.attr("title");
			if (title) {
				alertMsg.confirm(title,{
					okCall: function(){
						$form.submit();
						$form.attr("action", action);
					}
				});
			}else{
				$form.submit();
				$form.attr("action", action);
			}
			
			if (e.preventDefault) e.preventDefault();
			return false;
		});
	});
	
	$("span[submitForm]",$p).each(function(){
		var $target=$(this);
		var title=$target.attr("title");
		var href=$target.attr("href");
		var beforeFun=eval($target.attr("beforeExecFun")||null);
		var formId=$target.attr("submitForm");
		$target.click(function(e){
			if(!formId)return false;
			var $form=$("#"+formId, "div.unitBox:visible");
			var action=$form.attr("action");
			if(beforeFun && typeof beforeFun == "function") {
				var ret=beforeFun();
				if(!ret) {
					return false;
				}
			}
			//富文本编辑器内容同步
			$form.find("textarea.editor").each(function(e){
				$(this).val(($.fn.editor.get(this.id).html()||"").escapeSpecial());
			});
			
			if(href) {
				$form.attr("action", href);
			} else {
				var url = unescape(action).replaceTmById($(e.target).parents(".unitBox:first"));
				if (!url.isFinishedTm()) {
					alertMsg.error($form.attr("warn") || DWZ.msg("alertSelectMsg"));
					return false;
				}
			}
			if(title){
				alertMsg.confirm(title,{
					okCall:function(){
						$form.submit();
						$form.attr("action", action);
					}
				});
			}else{
				$form.submit();
				$form.attr("action", action);
			}
			
			if (e.preventDefault) e.preventDefault();
			return false;
		});
	});
	
	$("div.zTreeDiv",$p).each(function(){
		$(this).treextend();
	});
	//清空按钮
	$("span[clearForm]",$p).each(function(){
		var $target=$(this);
		var formId=$target.attr("clearForm");
		$target.click(function(e){
			if(!formId)return false;
			var $form=$("#"+formId, "div.unitBox:visible");
			$form.find("input:not(:hidden)").val("");
			$form.find("select").comboxReset();
			$form.find("select.normal").html("<option value=''>-无数据-</option>");
			if (e.preventDefault) e.preventDefault();
			return false;
		});
	});
	
	$("tbody[checkboxEvent]").each(function() {
		var $target = $(this);
		$target.on("click", "tr", function(e) {
			if(e.target.type != "checkbox") {
				var $chk = $(this).find(":checkbox");
				$chk.attr("checked", !$chk.attr("checked"));
			}
		});
		
		var $group = $("#" + $target.attr("groupId"));
		var $chks = $(":checkbox[name=" + $target.attr("chkName") + "]");
		var chksSize = $chks.size();
		if(chksSize > 0 && chksSize == $chks.filter(":checked").size()) {
			$group.attr("checked", true);
		}
	});
	
	$("select.combox",$p).each(function(){
		if(this.multiple){
			$(this).multicombox();
		}else{
			$(this).combox();
		}
	});
}
