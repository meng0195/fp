<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveConfGasForm" action="${CTX_PATH}/conf/gas/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="g.id" value="${g.id}"/>
	<input type="hidden" name="g.houseNo" value="${g.houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveConfGasForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="30">
		<tbody>
			<tr>
				<th>仓房名称</th>
				<td>${map:get(codes['houses'], g.houseNo)}</td>
				<th>控制板类型</th>
				<td>
					<select id="basis_gas_ctrType_edit" name="g.ctrType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_CTR_GAS']}">
			  				<option value="${e.key}" ${e.key == g.ctrType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th width="15%">排空时间</th>
				<td width="35%"><input name="g.drainTime" value="${g.drainTime}" class="digits" min="1" max="127" /></td>
				<th width="15%">控制板IP</th>
				<td width="35%"><input name="g.ctrIp" value="${g.ctrIp}" class="required"/></td>
			</tr>
			<tr>
				<th></th>
				<td></td>
				<th>控制板端口</th>
				<td><input name="g.ctrPort" value="${g.ctrPort}" class="required digits" min="1" max="65535"/></td>
			</tr>
			<tr>
				<th>模式板类型</th>
				<td>
					<select id="basis_gas_modelType_edit" name="g.modelType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_CTR_GAS']}">
			  				<option value="${e.key}" ${e.key == g.modelType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>时间同步板类型</th>
				<td>
					<select id="basis_gas_timesType_edit" name="g.timesType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_CTR_GAS']}">
			  				<option value="${e.key}" ${e.key == g.timesType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th>模式板IP</th>
				<td><input name="g.modelIp" value="${g.modelIp}" class="required"/></td>
				<th>时间同步板IP</th>
				<td><input name="g.timesIp" value="${g.timesIp}" class="required"/></td>
			</tr>
			<tr>
				<th>模式板端口</th>
				<td><input name="g.modelPort" value="${g.modelPort}" class="required digits" min="1" max="65535"/></td>
				<th>时间同步板端口</th>
				<td><input name="g.timesPort" value="${g.timesPort}" class="required digits" min="1" max="65535"/></td>
			</tr>
			<tr>
				<th>风路数量</th>
				<td><input name="g.wayNumb" value="${g.wayNumb}" class="digits" min="1" max="<fmt:bundle basename="static"><fmt:message key="gas.max.way"/></fmt:bundle>" onchange="changeGasWayNumb(this.value)"/></td>
				<th>风路直径(mm)</th>
				<td><input name="g.wayDiameter" value="${g.wayDiameter}" class="required digits" min="1" max="127"/></td>
			</tr>
			<tr>
				<th>各风路抽气时间</th>
				<td colspan="3" id="basis_gas_wayTime_edit">
					<c:forEach begin="0" end="${(g.wayNumb - 1 > 0) ? g.wayNumb - 1 : 0}" varStatus="st">
						风路${st.index + 1}长度(m)：<input class="w60 digits" min="1" max="65535" name="g.lens" value="${g.lens[st.index]}"/>
						抽气时间(s)：<input readonly="readonly" class="w60 digits" min="1" max="65535" name="g.times" value="${g.times[st.index]}"/></br>
					</c:forEach>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
function changeGasWayNumb(v){
	var str = "";
	for(var i = 0; i < parseInt(v); i++){
		str += "风路" + (i+1) + "长度(m)：<input class=\"w60 digits\" min=\"1\" max=\"65535\" name=\"g.lens\" value=\"0\"/>抽气时间(s)：<input class=\"w60 digits\" min=\"1\" max=\"65535\" name=\"g.times\" value=\"0\"/></br>";
	}
	$("#basis_gas_wayTime_edit").html(str);
	initChange();
}
$(function(){
	initChange();
	$("input[name='g.wayDiameter']", $("#saveConfGasForm")).change(function(){
		var dimter = $(this).val();
		$("input[name='g.lens']", $("#saveConfGasForm")).each(function(){
			var $this = $(this);
			$this.next().val(calculateGasTime(dimter, $this.val()));
		})
	})
})
function initChange(){
	$("input[name='g.lens']", $("#saveConfGasForm")).change(function(){
		var dimter = $("input[name='g.wayDiameter']", $("#saveConfGasForm")).val();
		var $this = $(this);
		$this.next().val(calculateGasTime(dimter, $this.val()));
	})
}
function calculateGasTime(diameter, len){
	return parseInt(len * 1.57 * diameter * 0.001 * diameter * 15);
}
</script>