var pestpoints = {
	def : {
		//5层排布方式的名称
		title : ["表层", "中上层", "中层", "中下层", "底层"],
		//5层排布方式的标记
		sign : ["BC", "ZS", "ZC", "ZX", "DC"],
		//各层诱捕器深度
		depth : ["0.5m", "1.5m", "2.5m", "4m", "5.5m"],
		//各层排布横纵列点数
		rowAndCols : [[3, 9], [4, 7], [4, 6], [4, 7], [4, 6]],
		/* 
		 * 默认按层级匹配计算的方式 数组中存储的是 title, depth, rowAndCols 的index，
		 * 对应 1层排布，2层排布，3层排布，4层排布，5层排布 
		 * 如果排布方式发生变化 请同步调整 title、depth、sign、rowAndCols 的结构 
		 */
		configIndex : [[0], [0, 4], [0, 2, 4], [0, 1, 3, 4], [0, 1, 2, 3, 4]],
		//层数
		layers : 3,
		tables : [],
		current : -1,
		tag : "edit",
		width : 49,	//	百分比
		fleft : true,
		diff : []
	},
	initTable : function($target, opts, index, tag80){
		var $table = $("<table id=\"paib-table-" + parseInt(opts.current + 1) + "\" class=\"paib-tab\"></table>");
		for(var i = 0; i < opts.tables.length; i++){
			if(opts.tables[i] == $table) return false;
		}
		var $box = $("<div class=\"paib-box paib-left\"></div>");
		if(tag80){
			$box.css({ width : "80%"});
		} else {
			$box.css({ width : opts.width+"%", "margin-top" : "10px"});
		}
		if(opts.fleft && !tag80){
			$box.css({ float : "left", "margin-left" : "5px"});
		} else {
			$box.css({"margin" : "10px auto 0 auto"});
		}
		pestpoints.initTitle($table, opts, index);
		$box.append($table);
		pestpoints.initTbody($table, opts, index);
		$target.append($box);
		opts.current ++;
		opts.tables[opts.current] = $table;
	},
	initTitle : function($target, opts, index){
		var $caption = $("<caption></caption>");
		if("edit" == opts.tag){
			$caption.append(opts.title[index] + "<span style=\"color:#000;\">（" + opts.depth[index] + "）</span>");
			$target.append($caption);
			var $clear = $("<span class=\"pest-clear\">清空</span>");
			$caption.append($clear);
			$clear.on("click", function(){
				$target.find("input").val("");
			});
		} else if ("check" == opts.tag) {
			var $allCheck = $("<label style=\"line-height: 33px;\"></label>");
			$allCheck.append("<input type=\"checkbox\" class=\"checkboxCtrl\" group=\"p.points" + index + "\"/>");
			$allCheck.append(opts.title[index] + "<span style=\"color:#000;\">（" + opts.depth[index] + "）</span>");
			$caption.append($allCheck);
			$target.append($caption);
		} else if ("view" == opts.tag) {
			$caption.append(opts.title[index] + "<span style=\"color:#000;\">（" + opts.depth[index] + "）</span>");
			$target.append($caption);
		} else if ("diff" == opts.tag) {
			$caption.append(opts.title[index] + "<span id='title-" + index + "' style=\"color:#000;font-size:12px;font-weight:bold;margin-left:10px;\"></span>");
			$target.append($caption);
		}
	},
	initTbody : function($target, opts, index){
		var $tbody = $("<tbody></tbody>");
		$target.append($tbody);
		var $tr, $td;
		for(var i = 1; i <= opts.rowAndCols[index][0]; i++){
			$tr = $("<tr></tr>");
			$tbody.append($tr);
			for(var j = 1; j <= opts.rowAndCols[index][1]; j++){
				$td = $("<td width=\"" + 100/parseInt(opts.rowAndCols[index][1]) + "%\"></td>");
				$tr.append($td);
				pestpoints.initText($td, opts, i, j, index);
			}
		}
	},
	initText : function($target, opts, x, y, index){
		if ("edit" == opts.tag) {
			var $inputXtq = $("<input id=\"xtq-"+ index + "-" + x + "-" + y +"\" name=\"p.gateNo\"/>");
			var $inputDw = $("<input id=\"dw-"+ index + "-" + x + "-" + y +"\" name=\"p.pointNo\"/>");
			$target.append($inputXtq);
			$target.append($inputDw);
			$target.append("<input id=\"row-"+ index + "-" + x + "-" + y +"\" name=\"p.yaxis\" type=\"hidden\"/>");
			$target.append("<input id=\"col-"+ index + "-" + x + "-" + y +"\" name=\"p.xaxis\" type=\"hidden\"/>");
			$target.append("<input id=\"layer-"+ index + "-" + x + "-" + y +"\" name=\"p.zaxis\" type=\"hidden\"/>");
			pestpoints.initBlurXtq($inputXtq);
			pestpoints.initBlurDw($inputDw);
		} else if ("view" == opts.tag) {
			$target.append("<label style='vertical-align:center;height:16px;' id=\"view-"+ index + "-" + x + "-" + y +"\"></label>");
		} else if ("check" == opts.tag) {
			var tag = true;
			$(opts.datas).each(function(indexs, obj){
				if(index == obj.zaxis && y == obj.xaxis && x == obj.yaxis){
					$target.append("<input value=\"" + obj.gateNo + "_" + obj.pointNo + "_" + obj.points + "\" id=\"check-"+ obj.points +"\" type=\"checkbox\" name=\"p.points"+ index + "\" class=\"checkboxCtrl\" child=\"true\">");
					tag = false;
				}
			})
			if(tag){
				$target.append("<div style=\"height:19px;\"></div>");
			}
		} else if ("diff" == opts.tag) {
			$target.append("<span style='display:block;float:left;color:blue;font-weight:bold;width:100%;height:20px;line-height:20px;' id='diff-" + index + "-" + x + "-" + y + "'></span>");
		}
	},
	initBlurXtq : function($target){
		$target.blur(function(){
			var $this = $(this);
			if($this.val()){
				if(!$this.next().val()) {
					$this.next().val("1");
					var $row = $this.siblings("[name='p.yaxis']");
					var $col = $this.siblings("[name='p.xaxis']");
					var $layer = $this.siblings("[name='p.zaxis']");
					$row.val($row.attr("id").split("-")[2]);
					$col.val($col.attr("id").split("-")[3]);
					$layer.val($layer.attr("id").split("-")[1]);
				}
			} else {
				if($this.next().val()) {
					$this.nextAll().val("");
				}
			}
		})
	},
	initBlurDw : function($target){
		$target.blur(function(){
			var $this = $(this);
			if($this.val()){
				if(!$this.prev().val()){
					$this.prev().val("1");
					var $row = $this.siblings("[name='p.yaxis']");
					var $col = $this.siblings("[name='p.xaxis']");
					var $layer = $this.siblings("[name='p.zaxis']");
					$row.val($row.attr("id").split("-")[2]);
					$col.val($col.attr("id").split("-")[3]);
					$layer.val($layer.attr("id").split("-")[1]);
				}
			} else {
				if($this.prev().val()) {
					$this.prev().val("");
					$this.nextAll().val("");
				}
			}
		})
	},
	initData : function($target, opts){
		if("edit" == opts.tag){
			$(opts.datas).each(function(index, obj){
				var $xtq = $("#xtq-" + parseInt(obj.zaxis) + "-" + obj.yaxis + "-" + obj.xaxis, $target);
				$xtq.val(obj.gateNo);
				$xtq.next().val(obj.pointNo);
				$xtq.siblings("[name='p.yaxis']").val(obj.yaxis);
				$xtq.siblings("[name='p.xaxis']").val(obj.xaxis);
				$xtq.siblings("[name='p.zaxis']").val(obj.zaxis);
			});
		} else if ("view" == opts.tag){
			$(opts.datas).each(function(index, obj){
				var $view = $("#view-" + parseInt(obj.zaxis) + "-" + obj.yaxis + "-" + obj.xaxis, $target);
				$view.css({display:"inline-block", width:"60px", height:"16px"});
				var index = parseInt(obj.points-1) || 0;
				if(opts.isAll || opts.results.overs[index] == 1){
					if(opts.results && opts.results.pest){
						if(opts.results.pest.warns){
							if(opts.results.pest.warns[index] == 1){
								$view.css({color:"#f44336"});
							}else if(opts.results.pest.warns[index] >= 2){
								$view.css({color:"#f1e587"});
							}
						}
						$view.html("<span style=\"font-weight:bold;font-size:14px;margin-left:4px;\">" + opts.results.pest.pset[index] + "</span>");
						pestpoints.addImg(opts.results.pest.picArrs[index], $view);
					}
				}
				//TODO 检测动画
				if(!opts.isAll && opts.results.pointIng == obj.points){
					$view.html("<span style=\"display:block;width:60px;height:30px;\" class=\"checking\">&nbsp;</span>");
				}
			});
		} else if ("check" == opts.tag){
			$(opts.datas).each(function(index, obj){
				var $check = $("#check-" + parseInt(obj[0]-1) + "-" + obj[1] + "-" + obj[2], $target);
				$check.attr("checked", true);
				//写到多点检测再实现
			});
		} else if ("diff" == opts.tag){
			var diffs = [], d = 0, a = {}, diff, d1, d2;
			for(var i = 0; i < opts.datas.length; i++){
				d1 = opts.datas1[opts.datas[i].points-1];
				d2 = opts.datas2[opts.datas[i].points-1];
				diff = $("#diff-" + opts.datas[i].zaxis + "-" + opts.datas[i].yaxis + "-" + opts.datas[i].xaxis, $target);
				if(!isNaN(d1)){
					d = parseInt(d1-d2);
					a = diffs[opts.datas[i].zaxis];
					if(a && a.SL){
						a.SL = Math.max(Math.abs(d), a.SL);
					} else {
						diffs[opts.datas[i].zaxis] = {IDX : opts.datas[i].zaxis, SL : Math.abs(d)};
					}
					diff.html("<span style='color:#000;font-weight:bold;height:20px;line-height:20px;'>" + d +"</span>" + "(" + d1 + "-" + "<span style='color:red;font-weight:bold;height:20px;line-height:20px;' >" + d2 +"</span>" + ")");
				} else {
					diff.html("<span style='color:red;font-weight:bold;height:20px;line-height:20px;' >" + d2 + "</span>");
				}
			}
			$(diffs).each(function(index, o){
				if(o){
					$("#title-" + o.IDX, $target).html("(最大差值：" + o.SL + ")")
				}
			});
		}
	},
	addImg : function(url, $target){
		var urls = "";
		if(url){
			urls = url.replaceAll("\/", "-").replace(".jpg", "");
		}
		$target.prepend("<a style='display:inline-block;float:left;' target=\"dialog\" rel=\"pest-pic-detail\" href=\"/zndc/squery/pest/pic/detail/" +urls+ "\" mask=\"true\" title=\"查看大图\" width=\"1050\" height=\"550\" rel=\"showPic\" ><img style=\"width:25px;height:15px;\" src=\"/"+url+"\"/></a>");
	},
	init : function($target, options){
		var opts = $.extend(true, {}, pestpoints.def, options);
		$target.children().remove();
		if(opts.layers > opts.configIndex.length) {
			alertMsg.warn("层数设定超出可处理范围!");
			return false;
		}
		var indexs = opts.configIndex[parseInt(opts.layers-1)];
		var tag80 = false;
		$(indexs).each(function(ix, obj){
			if(opts.rowAndCols[obj][1] > 7 && opts.width < 80){
				tag80 = true;
				return false;
			}
		});
		$(indexs).each(function(index, obj){
			pestpoints.initTable($target, opts, obj, tag80);
		})
		//初始化数据
		if(opts.datas && opts.datas.length > 0){
			pestpoints.initData($target, opts);
		}
		if(opts.tag == "view" && opts.counts && opts.counts[0]){
			var $title = $("<div style='width:60%;height:25px;line-height:25px;font-size:16px;text-align:left;margin-top:10px;padding-left:30px;'></div>");
			$target.prepend($title);
			var ct = opts.counts[0];
			$title.append("状态："+ (ct.ZT == "ZC" ? "基本无虫" : ct.ZT == "BJ" ? "严重虫粮" : "一般虫粮") + "<span style='font-size:12px;font-style:italic;margin-left:20px;color:#2196f3'>平均虫数：" + ct.PJSL + "  最大虫数：" + ct.ZGSL + "  最小虫数：" + ct.ZDSL+"<span>");
		}
		initUI($target);
	}
}