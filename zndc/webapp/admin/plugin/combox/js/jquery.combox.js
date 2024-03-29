if (typeof(Comboxs) == "undefined") Comboxs = new Object();

(function($) {
	COMBOXS_KEY_PREFIX = "combox.";
	$.fn.combox = function() {
		return this.each(function() {
			var $this = $(this);
			var p = {
				comboxW : eval($this.attr("comboxW") || 100),
				colNum : $this.attr("colNum"),
				colW : $this.attr("colW") || 80,
				boxW : $this.attr("boxW") || 100,
				boxH : $this.attr("boxH") || 100,
				prompt : $this.attr("prompt") || "",
				isDisable : ($this.attr("disabled") == "disabled"),
				liIdPrefix : "",
				autoW : $this.attr("autoW") || "false",
				autoH : $this.attr("autoH") || "false",
				url : $this.attr("link"),
				childId : $this.attr("childId"),
				datasrc : "param",
				selectedValue : $this.attr("selectedValue"),
				selectedText : "",
				selectedFlag : false,
				liDomArray : [],
				optionArray : []
			};
			var noRepeatFlag = generateIndex(1);
			var divId = "select" + noRepeatFlag + "_div";
			var tableId = "select" + noRepeatFlag + "_table";
			var selectBoxId = "select" + noRepeatFlag + "_input";
			var selectBtnId = "select" + noRepeatFlag + "_button";
			var containerId = "select" + noRepeatFlag + "_container";
			p.liIdPrefix = "li" + noRepeatFlag + "_input_";
			generateCombox(null, divId, tableId, selectBoxId, selectBtnId,
					containerId, $this, p);
			var $comboxDiv = $("#" + divId);
			var $table = $("#" + tableId);
			var $selectBox = $("#" + selectBoxId);
			var $selectBtn = $("#" + selectBtnId);
			var $container = $("#" + containerId);
			initComboxDefaultValue($selectBox, $this, p);
			$this.on("change", function(e) {
						$.fn.combox.updateValidateClass(this);
					});
			$container.find("ul").click(function(event) {
				var target = event.target;
				var $target = $(target);
				$target.addClass("selected").siblings(".selected")
						.removeClass("selected");
				selectValueChanged($selectBox, $this, p, target.id, $target
								.text());
				hiddenComboxContainer($container)
			}).mouseover(function(event) {
						var target = event.target;
						if (target.tagName.toLowerCase() == "li") {
							$(target).addClass("current")
						}
					}).mouseout(function(event) {
						var target = event.target;
						if (target.tagName.toLowerCase() == "li") {
							$(target).removeClass("current")
						}
					});
			$table.click(function(event) {
						if (event.stopPropagation) {
							event.stopPropagation()
						}
						$comboxDiv.css("z-index", generateZIndex($comboxDiv
												.css("z-index"), 2));
						hiddenComboxContainer($("div.combox-div")
								.find("div.selectbox-wrapper").not($container));
						if (($this.attr("disabled") || "") == "disabled") {
							return false
						}
						toggleComboxContainer($comboxDiv, $container)
					});
			if (this.id) {
				putCombox2Managers(this.id, {
							props : p,
							comboxId : this.id,
							$combox : $this,
							selectBoxId : selectBoxId,
							$selectBox : $selectBox,
							selectBtnId : selectBtnId,
							$selectBtn : $selectBtn,
							comboxDivId : divId,
							$comboxDiv : $comboxDiv,
							containerId : containerId,
							$container : $container
						})
			}
		})
	};
	function debug($obj) {
		if (window.console && window.console.log) {
			window.console.log("combox init count: " + $obj.size())
		}
	}
	function consoleLog(message) {
		if (window.console && window.console.log) {
			window.console.log(message)
		}
	}
	function generateCombox(data, comboxDivId, tableId, selectBoxId,
			selectBtnId, containerId, $combox, p) {
		loadDataPushArray(data, $combox, p);
		createComboxHTML(comboxDivId, tableId, selectBoxId, selectBtnId,
				containerId, $combox, p)
	}
	function loadDataPushArray(data, $combox, p) {
		if (typeof(p.prompt) != "undefined" && p.prompt != "") {
			p.liDomArray.push('<li id="' + p.liIdPrefix + '">' + p.prompt
					+ "</li>");
			p.optionArray.push('<option value="">' + p.prompt + "</option>")
		}
		p.datasrc = "param";
		if (data == null || typeof(data) == "undefined") {
			data = $combox.attr("data")
		}
		if (data != null && typeof(data) != "undefined") {
			p.datasrc = "attr";
			data = eval(data || []);
			$.each(data, function(i, item) {
						pushTags2Array(item.text, item.value, p)
					})
		} else {
			if (p.url != null && typeof(p.url) != "undefined") {
				p.datasrc = "url";
				var paramValue = $combox.attr("paramValue");
				var url = p.url
						+ (typeof(paramValue) != "undefined" ? paramValue : "");
				$.ajax({
							type : "GET",
							url : url,
							dataType : "text",
							cache : false,
							async : false,
							success : function(data) {
								data = eval(data || []);
								$.each(data, function(i, item) {
											pushTags2Array(item.text,
													item.value, p)
										})
							},
							error : function(data) {
								return false
							}
						})
			} else {
				p.datasrc = "option";
				data = $combox.find("option") || [];
				$.each(data, function(i, item) {
							pushTags2Array($(item).text(), $(item)
											.attr("value"), p)
						})
			}
		}
	}
	function pushTags2Array(text, value, p) {
		if (p.selectedValue && p.selectedValue == value) {
			p.selectedFlag = true;
			p.selectedText = text;
			p.liDomArray.push('<li id="' + p.liIdPrefix + value
					+ '" class="selected">' + text + "</li>");
			p.optionArray.push('<option value="' + value
					+ '" selected=selected>' + text + "</option>")
		} else {
			p.liDomArray.push('<li id="' + p.liIdPrefix + value + '">' + text
					+ "</li>");
			p.optionArray.push('<option value="' + value + '">' + text
					+ "</option>")
		}
	}
	function createComboxHTML(divId, tableId, selectBoxId, selectBtnId,
			containerId, $combox, p) {
		if (typeof(p.colNum) != "undefined") {
			p.colNum = Math.min(p.liDomArray.length, parseInt(p.colNum));
			p.colW = Math.max(parseInt(p.comboxW
							/ (p.colNum <= 0 ? 1 : p.colNum)), p.colW);
			p.boxW = p.colNum * (parseInt(p.colW) + 6) + 1;
			p.autoW = "true";
			p.autoH = "true"
		} else {
			p.boxW = Math.max(p.comboxW, p.boxW)
		}
		var tableTag = '<table id="'
				+ tableId
				+ '" class="select-table" cellspacing="0" cellpadding="0" style="border-style: none;"><tbody><tr><td style="border-style:none;padding:0pt;margin:0pt;text-align:left;vertical-align:middle;"><input type="text" id="'
				+ selectBoxId
				+ '" class="selectbox'
				+ (p.isDisable ? " inputDisabled selectbox_disabled" : "")
				+ '" autocomplete="off" readonly="readonly" style="width:'
				+ (p.comboxW - 24)
				+ 'px;cursor:pointer;"><td style="border-style:none;padding:0pt;margin:0pt;text-align:left;'
				+ (p.isDisable ? "disabled:disabled;" : "")
				+ '"><input type="button" class="selBtn'
				+ (p.isDisable ? " selBtn_disabled" : "") + '" id="'
				+ selectBtnId + '" '
				+ (p.isDisable ? 'style="disabled:disabled;"' : "")
				+ "></td></tr></tbody></table>";
		var comboxContainer = '<div id="'
				+ containerId
				+ '" class="selectbox-wrapper" hasfocus="0" style="display:none;overflow-y:auto;overflow-x:hidden;'
				+ (p.autoW === "true"
						? ("min-width:" + (Math.max(p.comboxW, p.boxW) - 1) + "px;")
						: ("width:" + (p.boxW - 1) + "px;"))
				+ (p.autoH === "true" ? "" : ("height:" + (p.boxH) + "px;"))
				+ 'top:23px;"><ul>' + p.liDomArray.join("") + "</ul></div>";
		var loader = '<div class="loader" style="display:none;">数据加载中...</div>';
		var $comboxDiv = $('<div id="' + divId
				+ '" class="maincon combox-div"></div>');
		$combox.html(p.optionArray.join("")).hide().before($comboxDiv
				.html(tableTag + comboxContainer + loader));
		addLiClass(containerId, p)
	}
	function addLiClass(containerId, p) {
		if (typeof(p.colNum) != "undefined") {
			$("#" + containerId).find("li").addClass("li_left").css("width",
					p.colW)
		}
	}
	function initComboxDefaultValue($selectBox, $combox, p) {
		if (p.selectedFlag) {
			$selectBox.val(p.selectedText).attr("title", p.selectedText);
			$combox.data("preVal", p.liIdPrefix + p.selectedValue).attr(
					"relVal", p.selectedValue).attr("relText", p.selectedText)
		} else {
			$combox[0].selectedIndex = 0;
			var value = $combox.val();
			p.selectedText = $combox.find('option[value="' + value + '"]')
					.text();
			$combox.data("preVal", p.liIdPrefix + value).attr("relVal", value)
					.attr("relText", p.selectedText);
			$("#" + p.liIdPrefix + value).addClass("selected");
			$selectBox.val(p.selectedText).attr("title", p.selectedText)
		}
	}
	function hiddenComboxContainer($container) {
		$container.attr("hasfocus", 0).hide();
		$("div.combox-multi-div").find("div.selectbox-wrapper[hasfocus=1]")
				.attr("hasfocus", 0).hide();
		$("div.selectbox-tree[hasfocus=1]").attr("hasfocus", 0).hide()
	}
	function toggleComboxContainer($comboxDiv, $container) {
		var hasfocus = $container.attr("hasfocus");
		if (hasfocus == "1") {
			$container.attr("hasfocus", 0).hide()
		} else {
			var left = $comboxDiv.offset().left;
			var top = $comboxDiv.offset().top + 23;
			$container.css({
						left : left,
						top : top
					}).show().attr("hasfocus", 1)
		}
	}
	function putCombox2Managers(comboxId, obj) {
		ComboxManagers[MANAGER_COMBOX_KEY_PREFIX + comboxId] = obj
	}
	function generateZIndex(zIndex, step) {
		var oldZIndex = ComboxManagers.zindex;
		if (typeof(oldZIndex) == "undefined") {
			ComboxManagers.zindex = oldZIndex = 30
		}
		if (parseInt(zIndex) < parseInt(oldZIndex)) {
			var newZIndex = (parseInt(ComboxManagers.zindex) + parseInt(step));
			ComboxManagers.zindex = newZIndex;
			return newZIndex
		} else {
			return zIndex
		}
	}
	function generateIndex(step) {
		var oldIndex = ComboxManagers.index;
		if (typeof(oldIndex) == "undefined") {
			ComboxManagers.index = 0
		}
		var newIndex = (parseInt(ComboxManagers.index) + parseInt(step));
		ComboxManagers.index = newIndex;
		return newIndex
	}
	function selectValueChanged($selectBox, $combox, p, value, text) {
		$selectBox.val(text).attr("title", text);
		var comboxRealValue = value.replace(p.liIdPrefix, "");
		$combox.attr("relVal", comboxRealValue).attr("relText", text);
		if (!($combox.data("preVal") == value)) {
			selectCombineEvent($combox, value, comboxRealValue, p)
		} else {
			$combox.val(comboxRealValue).data("preVal", value)
		}
	}
	function selectCombineEvent($combox, comboxValue, comboxRealValue, p) {
		$combox.val(comboxRealValue).data("preVal", comboxValue)
				.trigger("change");
		if (p.datasrc == "url") {
			if (p.childId == null || typeof(p.childId) == "undefined") {
				return false
			}
			var $childCombox = $("#" + p.childId);
			if ($childCombox.size() != 1) {
				return false
			}
			$childCombox.attr("paramValue", comboxRealValue);
			$.fn.combox.reload(p.childId, null)
		}
	}
	function clearSelectBox(comboxManager) {
		if (comboxManager == null) {
			return false
		}
		var prompt = comboxManager.props.prompt;
		comboxManager.$selectBox.attr("title", prompt).val(prompt);
		comboxManager.$container.find("ul").empty();
		var $combox = comboxManager.$combox;
		if (typeof($combox.attr("data")) != "undefined"
				|| typeof($combox.attr("url")) != "undefined") {
			$combox.empty()
		}
	}
	$.fn.combox.updateValidateClass = function(target) {
		var comboxId = target.id;
		var $target = $(target);
		if ($target.hasClass("required")) {
			var $selectBox = $.fn.combox.getComboxManager(comboxId).$selectBox;
			if ($target.val() === "") {
				$selectBox.addClass("combox-error");
			} else {
				$selectBox.removeClass("combox-error");
			}
		}
	};
	$.fn.combox.otherClickEventCombox = function(event) {
		var $container = $("div.combox-div").find("div.selectbox-wrapper");
		if (event === "window") {
			hiddenComboxContainer($container)
		}
		var $target = $(event.target);
		if ($container.is(":visible") && !$target.hasClass("selectbox-wrapper")
				&& $container.find($target).length == 0) {
			hiddenComboxContainer($container)
		}
	};
	$.fn.combox.disable = function(comboxId) {
		var comboxManager = $.fn.combox.getComboxManager(comboxId);
		if (comboxManager == null) {
			return false
		}
		comboxManager.$combox.attr("disabled", "disabled");
		comboxManager.$selectBox.css("disabled", "disabled")
				.addClass("inputDisabled selectbox_disabled");
		comboxManager.$selectBtn.css("disabled", "disabled")
				.addClass("selBtn_disabled")
	};
	$.fn.combox.enable = function(comboxId) {
		var comboxManager = $.fn.combox.getComboxManager(comboxId);
		if (comboxManager == null) {
			return false
		}
		comboxManager.$combox.removeAttr("disabled");
		comboxManager.$selectBox.css("disabled", "")
				.removeClass("inputDisabled selectbox_disabled");
		comboxManager.$selectBtn.css("disabled", "")
				.removeClass("selBtn_disabled")
	};
	$.fn.combox.clear = function(comboxId, data) {
		var comboxManager = $.fn.combox.getComboxManager(comboxId);
		return clearSelectBox(comboxManager)
	};
	$.fn.combox.reload = function(comboxId, data) {
		var comboxManager = $.fn.combox.getComboxManager(comboxId);
		if (comboxManager == null) {
			return false
		}
		clearSelectBox(comboxManager);
		var $loader = comboxManager.$comboxDiv.find("div.loader");
		$loader.show();
		var $combox = comboxManager.$combox;
		var p = comboxManager.props;
		p.liDomArray = [], p.optionArray = [];
		loadDataPushArray(data, $combox, p);
		comboxManager.$container.find("ul").html(p.liDomArray.join(""));
		$combox.html(p.optionArray.join(""));
		addLiClass(comboxManager.containerId, p);
		var paramValue = $combox.attr("paramValue");
		if (typeof(paramValue) != "undefined") {
			p.selectedFlag = false;
			p.selectedValue = ""
		}
		initComboxDefaultValue(comboxManager.$selectBox, $combox, p);
		selectCombineEvent($combox, p.liIdPrefix, "", p);
		$loader.hide()
	};
	$.fn.combox.setDefaultValue = function(comboxId, defaultValue) {
		var comboxManager = $.fn.combox.getComboxManager(comboxId);
		if (comboxManager == null) {
			return false
		}
		if ((comboxManager.$combox.attr("disabled") || "") == "disabled") {
			return false
		}
		var $combox = comboxManager.$combox;
		var $option = $combox.find('[option[value="' + defaultValue + '"]');
		if (typeof($option) != "undefined") {
			var liIdPrefix = comboxManager.props.liIdPrefix;
			var liId = liIdPrefix + defaultValue;
			var $li = $("#" + liId);
			if ($li && $li.size() == 1) {
				$li.addClass("selected").siblings(".selected")
						.removeClass("selected");
				selectValueChanged(comboxManager.$selectBox, $combox,
						comboxManager.props, liId, $option.text())
			}
		}
	};
	$.fn.combox.getComboxManager = function(comboxId) {
		return ComboxManagers[MANAGER_COMBOX_KEY_PREFIX + comboxId]
	};
	$.fn.combox.defaults = {}
})(jQuery);
$(document).on("click", function(a) {
			$.fn.combox.otherClickEventCombox(a);
		});
$(window).on("scroll", function(a) {
			$.fn.combox.otherClickEventCombox("window");
		});