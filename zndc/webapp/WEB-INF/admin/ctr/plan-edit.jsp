<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveCtrPlanEditForm" action="${CTX_PATH}/control/plan/save" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="p.id" value="${p.id}"/>
	<input type="hidden" name="p.status" value="${p.status}"/>
	<input type="hidden" name="p.planCode" value="${p.planCode}" />
	<input type="hidden" name="p.userCode" value="${p.userCode}" />
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveCtrPlanEditForm" >保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="30">
		<tbody>
			<tr>
				<th >任务名称：</th>
				<td colspan="5">
					<input name="p.planName" value="${p.planName}" class="required" maxlength="100" style="width:75%;"/>
				</td>
			</tr>
			<tr>
				<th width="17%">任务触发时间：</th>
				<td width="17%">
					<input id="ctrPlanTimeThree" name="p.timeThree" class="required date" value="${p.timeThree}" dateFmt="HH:mm:ss" readonly="readonly"/>
				</td>
				<td width="17%">
					<select id="ctr-plan-timeOne" name="p.timeOne" class="required combox">
						<c:forEach var="em" items="${enums['TYPE_TIME1']}">
							<option value="${em.key}" ${em.key == p.timeOne ? 'selected' : ''}>${em.value}</option>
						</c:forEach>
					</select>
				</td>
				<td colspan="3">
					<div id="ctr-plan-timeTwo" ></div>
				</td>
			</tr>
			<tr id="plan-ctr-tr-equip">
				<th>操作类型</th>
				<td colspan="2">
					<c:forEach var="em" items="${enums['TYPE_ON_OR_OFF']}">
						<label><input type="radio" name="p.type" value="${em.key}" ${em.key == p.type ? 'checked' : ''}/>${em.value}</label>
					</c:forEach>
				</td>
				<th width="17%">仓房</th>
				<td colspan="2">
					<select id="plan-ctr-sel-house" class="combox" name="p.houseNo" class="required">
						<option value="">请选择</option> 
						<c:if test="${empty sessionScope._SYS_USER_.houses}">
							<c:forEach var="h" items="${codes['houses']}">
								<option value="${h.key}" ${h.key == p.houseNo ? 'selected' : ''}>${h.value}</option>
							</c:forEach>
						</c:if>
						<c:if test="${not empty sessionScope._SYS_USER_.houses}">
							<c:forEach var="h" items="${sessionScope._SYS_USER_.houses}">
								<option value="${h}" ${h == p.houseNo ? 'selected' : ''}>${map:get(codes['houses'], h)}</option>
							</c:forEach>
						</c:if>
					</select>
				</td>
			</tr>
			<c:forEach var="ep" items="${es}" varStatus="st">
				<c:if test="${st.index % 6 == 0}"><tr></c:if>
					<td><label><input type="checkbox" name="equipNo" value="${ep.equipNo}" ${ep.sel == 1 ? 'checked' : ''}/>${ep.equipName}</label></td>
				<c:if test="${st.index % 6 == 5}"></tr></c:if>
				<c:if test="${st.index % 6 == 4 and st.last}"><td></td></tr></c:if>
				<c:if test="${st.index % 6 == 3 and st.last}"><td></td><td></td></tr></c:if>
				<c:if test="${st.index % 6 == 2 and st.last}"><td></td><td></td><td></td></tr></c:if>
				<c:if test="${st.index % 6 == 1 and st.last}"><td></td><td></td><td></td><td></td></tr></c:if>
				<c:if test="${st.index % 6 == 0 and st.last}"><td></td><td></td><td></td><td></td><td></td></tr></c:if>
			</c:forEach>
		</tbody>
	</table>
</form>
<script type="text/javascript">
var t1 = "${p.timeOne}", t2 = "${p.timeTwo}", t2s = [], timeWeeks = "", timeMonths = "";
$(function(){ 
	initTimeTwo(); 
});
function initTimeTwo(){
	if(t2) t2s = t2.split(",");
	if(t1 == 2) {//每周
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" " + (isArr(t2s, i) ? 'checked' : '') + "/>" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + i + "号</label>";
		}
		$("#ctr-plan-timeTwo").html(timeWeeks);
	} else if(t1 == 3) {
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" "+ (isArr(t2s, i) ? 'checked' : '') +"/>" + i + "号</label>";
		}
		$("#ctr-plan-timeTwo").html(timeMonths);
	} else {
		for(var i = 1; i < 8; i++){
			timeWeeks += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + toWeekStr(i) + "</label>";
		}
		for(var i = 1; i < 31; i++){
			timeMonths += "<label><input type=\"checkbox\" value=\"" + i + "\" name=\"p.timeTwo\" />" + i + "号</label>";
		}
	}
}
$("#ctr-plan-timeOne").change(function(){
	var $this = $(this);
	if($this.val() == 2){
		$("#ctr-plan-timeTwo").html(timeWeeks);
	} else if($this.val() == 3){
		$("#ctr-plan-timeTwo").html(timeMonths);
	} else {
		$("#ctr-plan-timeTwo").html("");
	}
});
$("#plan-ctr-sel-house").change(function(){
	$.ajax({
		url:"${CTX_PATH}/control/plan/equip/" + ($(this).val() || 0),
		type : 'POST',
		dataType : "json",
		cache : false,
		success : function(json){
			if(json[DWZ.keys.statusCode] == DWZ.statusCode.ok){
				var str = "";
				var len = json.body.length - 1;
				$(json.body).each(function(index, obj){
					if(index % 6 == 0) {
						str += "<tr class='epsTrs'>";
					}
					str += "<td><label><input type='checkbox' name='equipNo' value='" + obj.equipNo + "' />" + obj.equipName + "</label></td>";
					if(index == len){
						for(var i = index % 6; i < 5; i++){
							str += "<td></td>";
						}
						str += "</tr>";
					} else if(index % 6 == 5){
						str += "</tr>";
					}
				});
				$("#plan-ctr-tr-equip").nextAll().remove();
				$("#plan-ctr-tr-equip").after(str);
			} else {
				alertMsg.error(json.message);
			}
		},
		error : ""
	})
})
</script>