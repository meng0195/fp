<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveTempAreaForm" action="${CTX_PATH}/conf/temp/area/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="houseNo" value="${rs.house.houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveTempAreaForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="10%">仓房编号</th>
				<td width="15%">${rs.house.houseNo}</td>
				<th width="10%">仓房名称</th>
				<td width="15%">${rs.house.houseName}</td>
				<th width="10%">仓房类型</th>
				<td width="15%">${map:get(enums['TYPE_HOUSE'], rs.house.houseType)}</td>
				<th width="10%">保管员</th>
				<td width="15%">${rs.house.keeperName}</td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${rs.house.houseType == 1}">
						<th>起始点</th>
						<td>${map:get(enums['TYPE_START_WARE'], rs.eqment.begins)}</td>
						<th>线缆排序方向</th>
						<td>${map:get(enums['TYPE_SORT_WARE'], rs.eqment.sortOri)}</td>
					</c:when>
					<c:otherwise>
						<th>起始点</th>
						<td>${map:get(enums['TYPE_START_SILO'], rs.eqment.begins)}</td>
						<th>线缆排序方向</th>
						<td>${map:get(enums['TYPE_SORT_SILO'], rs.eqment.sortOri)}</td>
					</c:otherwise>
				</c:choose>
				<th>线缆连接方式</th>
				<td>${map:get(enums['TYPE_SORT_RULE'], rs.eqment.sortRule)}</td>
				<th>传感器排序规则</th>
				<td>${map:get(enums['TYPE_POINT_RULE'], rs.eqment.pointRule)}</td>
			</tr>
		</tbody>
	</table>
	<div layoutH="80">
		<div id="conf-area-join">${rs.html}</div>
		<c:if test="${rs.houseType == 1}">
			<table id="conf-area-leve" class="edit" style="width:600px;">
				<tr>
					<th width="150">上层</th>
					<th width="150">中上层</th>
					<th width="150">中下层</th>
					<th width="150">下层</th>
				</tr>
				<tr code="1">
					<td><input id="t11" type="hidden" name="t1" value="${empty rs.t1 ? '' : rs.t1[0]}"><input id="t11Name" /></td>
					<td><input id="t21" type="hidden" name="t2" value="${empty rs.t2 ? '' : rs.t2[0]}"><input id="t21Name" /></td>
					<td><input id="t31" type="hidden" name="t3" value="${empty rs.t3 ? '' : rs.t3[0]}"><input id="t31Name" /></td>
					<td><input id="t41" type="hidden" name="t4" value="${empty rs.t4 ? '' : rs.t4[0]}"><input id="t41Name" /></td>
				</tr>
			</table>
		</c:if>
		<c:if test="${rs.houseType != 1}">
			<table id="conf-area-leve" class="edit" style="width:660px;">
				<tr>
					<th width="60">圈/层</th>
					<th width="150">上层</th>
					<th width="150">中上层</th>
					<th width="150">中下层</th>
					<th width="150">下层</th>
				</tr>
				<c:forEach begin="1" end="${rs.eqment.vnum}" varStatus="st">
					<tr code="${st.index}">
						<th>${st.index == 1 ? '内圈' : st.index == 2 ? '中圈' : '外圈'}</th>
						<td><input id="t1${st.index}" type="hidden" name="t1" value="${empty rs.t1 ? '' : rs.t1[st.index-1]}"><input id="t1${st.index}Name" readonly /></td>
						<td><input id="t2${st.index}" type="hidden" name="t2" value="${empty rs.t2 ? '' : rs.t2[st.index-1]}"><input id="t2${st.index}Name" readonly /></td>
						<td><input id="t3${st.index}" type="hidden" name="t3" value="${empty rs.t3 ? '' : rs.t3[st.index-1]}"><input id="t3${st.index}Name" readonly /></td>
						<td><input id="t4${st.index}" type="hidden" name="t4" value="${empty rs.t4 ? '' : rs.t4[st.index-1]}"><input id="t4${st.index}Name" readonly /></td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</div>
</form>
<div id="wear-select-div" class="area-select-div">
	<c:forEach var="em" items="${enums['TYPE_POINT_AREA']}">
		<span code="${em.key}">${em.value}</span>
	</c:forEach>
</div>
<div id="squat-select-div" class="area-select-div">
	<c:forEach var="em" items="${enums['TYPE_POINT_SQUAT']}">
		<span code="${em.key}">${em.value}</span>
	</c:forEach>
</div>
<script type="text/javascript">
$(function(){
	$("input[name='t1'],input[name='t2'],input[name='t3'],input[name='t4']").each(function(){
		var $this = $(this);
		var arr = $this.val().split(".");
		var ss = "";
		for(var i=0; i < arr.length; i++){
			ss += (parseInt(arr[i]) + 1) + ".";
		}
		if(ss.length > 0) ss = ss.substring(0, ss.length - 1);
		$this.next().val(ss);
	})
})
var $div = ('${rs.houseType}' == 1) ? $("#wear-select-div") : $("#squat-select-div");
$("#conf-area-join").click(function(e){
	var $target = $(e.target);
	if(e.target.tagName.toUpperCase() == "SPAN"){
		$div.hide();
		var x = e.pageX;
		var y = e.pageY;
		var x1 = $(window).width();
		var y1 = $(window).height();
		if(x1 - x > 50){
			$div.css({left:(x - 210) + "px"});
		} else {
			$div.css({left:(x - 260) + "px"});
		}
		if(y1 - y > 100){
			$div.css({top:(y - 90) + "px"});
		} else {
			$div.css({top:(y - 170) + "px"});
		}
		$target.parent().children().eq(0).css({color:"red"});
		$div.show();
		$div.click(function(e1){
			if(e1.target.tagName.toUpperCase() == "SPAN"){
				var code = $(e1.target).attr("code");
				$target.parent().find("input[name='area']").val(code);
				$target.parent().children().eq(1).html($(e1.target).html());
				$target.parent().children().eq(0).css({color:"#000"});
				changeClass($target, code);
				$div.unbind();
				$div.hide();
			}
		});
	}
})

function changeClass(target, code){
	target.parents("ul").removeClass("bred bgray bblue byellow bgreen");
	var clazz = "";
	var code = parseInt(code, 10);
	switch (code) {
	case 1:clazz = "bred";break;
	case 2:clazz = "bgray";break;
	case 3:clazz = "bblue";break;
	case 4:clazz = "byellow";break;
	case 5:clazz = "bgreen";break;
	default:
		break;
	}
	target.parents("ul").addClass(clazz);
}

$("#conf-area-leve").click(function(e){
	var $target = $(e.target);
	var houseNo = '${rs.house.houseNo}';
	if(e.target.tagName.toUpperCase() == "INPUT"){
		var code = $target.parents("tr").attr("code");
		var sel = $target.prev().val();
		var id = $target.prev().attr("id");
		var options = {
			width : 600,
			height : 300,
			max : false,
			mask : true,
			maxable : false,
			minable : false,
			fresh : false,
			resizable : false,
			drawable : false
		};
		url = unescape("${CTX_PATH}/conf/temp/area/leve/" + (houseNo || 0) + "/" + (code || 0) + "/" + (sel || -1) + "/" + (id || 0)).replaceTmById($(this).parents(".unitBox:first"));
		DWZ.debug(url);
		if (!url.isFinishedTm()) {
			alertMsg.error($this.attr("warn") || DWZ.msg("alertSelectMsg"));
			return false;
		}
		$.pdialog.open(url, "temp-conf-leve", "层范围选择", options);
	}
})
</script>