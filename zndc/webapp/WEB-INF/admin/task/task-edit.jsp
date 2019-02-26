<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveTaskEditForm" action="${CTX_PATH}/basis/task/save" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="p.id" value="${p.id}"/>
	<input type="hidden" name="p.status" value="${p.status}"/>
	<input type="hidden" name="p.planCode" value="${p.planCode}" />
	<input type="hidden" name="p.maxNum" value="${p.maxNum}" />
	<input type="hidden" name="p.type" value="${p.type}" />
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveTaskEditForm" >保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="30">
		<tbody>
			<tr>
				<th width="120px">任务名称：</th>
				<td colspan="9">
					<input name="p.planName" value="${p.planName}" class="required" maxlength="100" style="width:75%;"/>
				</td>
			</tr>
			<tr>
				<th>任务触发时间：</th>
				<td>
					<input id="tempAddTimeThree" name="p.timeThree" class="required date" value="${p.timeThree}" dateFmt="HH:mm:ss" readonly="readonly"/>
				</td>
				<td>
					<select id="check-time-timeOne" name="p.timeOne" class="required combox">
						<c:forEach var="em" items="${enums['TYPE_TIME1']}">
							<option value="${em.key}" ${em.key == p.timeOne ? 'selected' : ''}>${em.value}</option>
						</c:forEach>
					</select>
				</td>
				<td colspan="7">
					<div id="check-time-timeTwo" ></div>
				</td>
			</tr>
			<c:if test="${p.type == 1}">
				<tr>
					<th width="120px">报表数据标识：</th>
					<td colspan="9">
						<c:forEach var="em" items="${enums['TYPE_FLAG']}">
							<input type="radio" name="p.reportTag" value="${em.key}" ${p.reportTag == em.key ? 'checked' : ''}/>${em.value}
						</c:forEach>
					</td>
				</tr>
			</c:if>
			<tr>
				<th colspan="10"><a class="btn-norel" onclick="checkAll('#saveTaskEditForm', 'houseNos')">全选</a><a class="btn-norel" onclick="unCheckAll('#saveTaskEditForm', 'houseNos')">全不选</a><a class="btn-norel" onclick="opCheck('#saveTaskEditForm', 'houseNos')">反选</a></th>
			</tr>
			${cts}
		</tbody>
	</table>
</form>
<script type="text/javascript">
var t1 = "${p.timeOne}", t2 = "${p.timeTwo}", t2s = [], timeWeeks = "", timeMonths = "",timeCustom = "";
$(function(){ 
	initTimeTwo(); 
});
function initTimeTwo(){
	if($("#check-time-timeOne").val()==5) $("#tempAddTimeThree").val("00:00:00");
	if(t2) t2s = t2.split(",");
	if(t1 == 2) {//每周
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" " + (isArr(t2s, i) ? 'checked' : '') + "/>" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + i + "号</label>";
		}
		getCustom();
		$("#check-time-timeTwo").html(timeWeeks);
	} else if(t1 == 3) {//每月
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" "+ (isArr(t2s, i) ? 'checked' : '') +"/>" + i + "号</label>";
		}
		getCustom();
		$("#check-time-timeTwo").html(timeMonths);
	} else if(t1 == 4) {//自定义
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + i + "号</label>";
		}
		getCustom();
		$("#check-time-timeTwo").append(timeCustom);
	} else if(t1 == 5) {//每小时
		$("#tempAddTimeThree").val("00:00:00");
	} else {
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + i + "号</label>";
		}
	}
}
$("#check-time-timeOne").change(function(){
	var $this = $(this);
	if($this.val() == 2){
		$("#check-time-timeTwo").html(timeWeeks);
		$("#tempAddTimeThree").val("");
	} else if($this.val() == 3){
		$("#check-time-timeTwo").html(timeMonths);
		$("#tempAddTimeThree").val("");
	} else if($this.val() == 4){
		$("#check-time-timeTwo").html("");
		getCustom();
		$("#check-time-timeTwo").append(timeCustom);
		$("#tempAddTimeThree").val("");
	} else if($this.val() == 5){
		$("#tempAddTimeThree").val("00:00:00");
	} else {
		$("#check-time-timeTwo").html("");
		$("#tempAddTimeThree").val("");
	}
});

function getCustom(){//渲染加载自定义选项
	timeCustom = $("<input id=\"tempAddTimeTwo\" name=\"p.timeTwo\" class=\"required date\" value=\"${p.timeTwo}\" dateFmt=\"yyyy-MM-dd\" readonly=\"readonly\"/>");
	
	var opts = {};
	if (timeCustom.attr("dateFmt")) opts.pattern = timeCustom.attr("dateFmt");
	timeCustom.datepicker(opts);
}
</script>