<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisEqmentForm" action="${CTX_PATH}/conf/temp/save/0" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="tb.id" value="${t.id}"/>
	<input type="hidden" name="tb.houseNo" value="${s.houseNo}"/>
	<input type="hidden" name="houseNo" value="${s.houseNo}"/>
	<input type="hidden" name="t.maxLNum" value="${t.maxLNum}"/>
	<input type="hidden" name="t.maxHNum" value="${t.maxHNum}"/>
	<input type="hidden" name="t.pointNum" value="${t.pointNum}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisEqmentForm" >保存</span></a></li>
			<li><a class="add"><span submitForm="saveBasisEqmentForm" href="${CTX_PATH}/conf/temp/save/1">单独更新分机设置</span></a></li>
		</ul>
	</div>
	<div  layoutH="30">
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="11">仓房信息</th>
			</tr>
			<tr>
				<th>仓房名称</th>
				<td>${s.houseName}</td>
				<th>仓房类型</th>
				<td>${map:get(enums['TYPE_HOUSE'], s.houseType)}</td>
				<%-- <th>保管员</th>
				<td colspan="6">${h.keeperName}</td> --%>
				<th colspan="7"></th>
			</tr>
			<tr>
				<th colspan="11">分机设置</th>
			</tr>
			<tr>
				<th>仓温仓湿<input type="hidden" name="tb.tempType" value="5"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeIn_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[4].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[4].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[4].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td colspan="3">
					<input name="tb.wayNo" value="${tb[4].wayNo}" class="digits w60" min="0" max="30"/>
					<input type="hidden" name="tb.cableNo" value="${tb[4].cableNo}"/>
				</td>
			</tr>
			<tr>
				<th>气温气湿<input type="hidden" name="tb.tempType" value="6"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeOut_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[5].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[5].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[5].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td colspan="3">
					<input name="tb.wayNo" value="${tb[5].wayNo}" class="digits w60" min="0" max="30"/>
					<input type="hidden" name="tb.cableNo" value="${tb[5].cableNo }"/>
				</td>
			</tr>
			<tr>
				<th>夹层温度<input type="hidden" name="tb.tempType" value="7"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeLay_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[6].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[6].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[6].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td colspan="3">
					<input name="tb.wayNo" value="${tb[6].wayNo}" class="digits w60" min="0" max="30"/>
					<input type="hidden" name="tb.cableNo" value="${tb[6].cableNo }"/>
				</td>
			</tr>
			<tr>
				<th>通风口温<input type="hidden" name="tb.tempType" value="8"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeVent_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[7].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[7].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[7].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td colspan="3">
					<input name="tb.wayNo" value="${tb[7].wayNo}" class="digits w60" min="0" max="30"/>
					<input type="hidden" name="tb.cableNo" value="${tb[7].cableNo }"/>
				</td>
			</tr>
			<tr>
				<th>吊顶温度<input type="hidden" name="tb.tempType" value="9"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeTop_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[8].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[8].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[8].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td colspan="3">
					<input name="tb.wayNo" value="${tb[8].wayNo}" class="digits w60" min="0" max="30"/>
					<input type="hidden" name="tb.cableNo" value="${tb[8].cableNo }"/>
				</td>
			</tr>
			<tr>
				<th width="80">粮温分机1<input type="hidden" name="tb.tempType" value="1"/></th>
				<th width="80">分机类型</th>
				<td width="100">
					<select id="basis_eqment_typeOne_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[0].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th width="75">IP/COM</th>
				<td width="140"><input name="tb.ip" value="${tb[0].ip}" class="required isIp" maxlength="15" /></td>
				<th width="75">端口/RTU</th>
				<td width="60"><input name="tb.port" value="${tb[0].port}" class="required digits w60" min="0" max="65535"/></td>
				<th width="75">通道号</th>
				<td width="60"><input name="tb.wayNo" value="${tb[0].wayNo}" class="required digits w60" min="0" max="1000"/></td>
				<th width="75">线缆</th>
				<td><input name="tb.cableNo" value="${tb[0].cableNo}" class="larger" maxlength="200"/></td>
			</tr>
			<tr>
				<th>粮温分机2<input type="hidden" name="tb.tempType" value="2"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeTwo_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[1].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[1].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[1].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td><input name="tb.wayNo" value="${tb[1].wayNo}" class="digits w60" min="0" max="1000"/></td>
				<th>线缆</th>
				<td><input name="tb.cableNo" value="${tb[1].cableNo}" class="larger" maxlength="200"/></td>
			</tr>
			<tr>
				<th>粮温分机3<input type="hidden" name="tb.tempType" value="3"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeThree_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[2].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[2].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[2].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td><input name="tb.wayNo" value="${tb[2].wayNo}" class="digits w60" min="0" max="1000"/></td>
				<th>线缆</th>
				<td><input name="tb.cableNo" value="${tb[2].cableNo}" class="larger" maxlength="200"/></td>
			</tr>
			<tr>
				<th>粮温分机4<input type="hidden" name="tb.tempType" value="4"/></th>
				<th>分机类型</th>
				<td>
					<select id="basis_eqment_typeFour_edit" name="tb.boardType" class="combox" >
						<c:forEach var="e" items="${enums['TYPE_PROTOCOL']}">
			  				<option value="${e.key}" ${e.key == tb[3].boardType ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>IP/COM</th>
				<td><input name="tb.ip" value="${tb[3].ip}" class="isIp" maxlength="15" /></td>
				<th>端口/RTU</th>
				<td><input name="tb.port" value="${tb[3].port}" class="digits w60" min="0" max="65535"/></td>
				<th>通道号</th>
				<td><input name="tb.wayNo" value="${tb[3].wayNo}" class="digits w60" min="0" max="1000"/></td>
				<th>线缆</th>
				<td><input name="tb.cableNo" value="${tb[3].cableNo}" class="larger" maxlength="200"/></td>
			</tr> 
		</tbody>
	</table>
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="9">线缆排布</th>
			</tr>
			<tr>
				<th>起始缆号</th>
				<th colspan="8"><input name="t.beginNum" value="${t.beginNum}" class="digits" min="1" max="1000"></th>
			</tr>
			<c:choose>
				<c:when test="${s.houseType == 1}">
					<tr>
						<th>排布规则</th>
						<th>线缆起始位置</th>
						<td>
							<select id="basis_eqment_begins_edit" name="t.begins" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_START_WARE']}">
					  				<option value="${e.key}" ${e.key == t.begins ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
						<th>线缆排序方向</th>
						<td>
							<select id="basis_eqment_sortOri_edit" name="t.sortOri" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_SORT_WARE']}">
					  				<option value="${e.key}" ${e.key == t.sortOri ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
						<th>线缆连接方式</th>
						<td>
							<select id="basis_eqment_sortRule_edit" name="t.sortRule" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_SORT_RULE']}">
					  				<option value="${e.key}" ${e.key == t.sortRule ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
						<th>传感器排序规则</th>
						<td>
							<select id="basis_eqment_pointRule_edit" name="t.pointRule" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_POINT_RULE']}">
					  				<option value="${e.key}" ${e.key == t.pointRule ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
					</tr>
					<tr>
						<th>列数</th>
						<td colspan="9"><input name="t.vnum" value="${t.vnum}" class="required digits mini" min="1" max="99" onchange="changeVnum(this.value)"/></td>
					</tr>
					<tr>
						<th>行数</th>
						<td colspan="9" id="eqment-hnum-td">
							<c:forEach  var="hs" items="${t.hnum}" varStatus="st">
								<input name="t.hnum" value="${hs}" class="required digits mini" min="1" max="99" />
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th>层数</th>
						<td colspan="9" id="eqment-lnum-td">
							<c:forEach  var="ls" items="${t.lnum}" varStatus="st">
								<input name="t.lnum" value="${ls}" class="required digits mini" min="1" max="99" />
							</c:forEach>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<th>排布规则</th>
						<th>线缆起始位置</th>
						<td>
							<select id="basis_eqment_begins_edit" name="t.begins" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_START_SILO']}">
					  				<option value="${e.key}" ${e.key == t.begins ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
						<th>线缆排序方向</th>
						<td>
							<select id="basis_eqment_sortOri_edit" name="t.sortOri" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_SORT_SILO']}">
					  				<option value="${e.key}" ${e.key == t.sortOri ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
						<th>线缆连接方式</th>
						<td>E型<input type="hidden" name="t.sortRule" value="2" /></td>
						<th>传感器排序规则</th>
						<td>
							<select id="basis_eqment_pointRule_edit" name="t.pointRule" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_POINT_RULE']}">
					  				<option value="${e.key}" ${e.key == t.pointRule ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</td>
					</tr>
					<tr>
						<th>圈数</th>
						<td colspan="9"><input name="t.vnum" value="${t.vnum}" class="required digits mini" min="1" max="99" onchange="changeVnum(this.value)"/></td>
					</tr>
					<tr>
						<th>个数</th>
						<td colspan="9" id="eqment-hnum-td">
							<c:forEach  var="hs" items="${t.hnum}" varStatus="st">
								<input name="t.hnum" value="${hs}" class="required digits mini" min="1" max="99" />
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th>层数</th>
						<td colspan="9" id="eqment-lnum-td">
							<c:forEach  var="ls" items="${t.lnum}" varStatus="st">
								<input name="t.lnum" value="${ls}" class="required digits mini" min="1" max="99" />
							</c:forEach>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<table class="edit">
		<tbody>
			<tr>
				<th width="15%">冷芯范围(从0开始)：</th>
				<th>列：</th>
				<td><input name="t.miny" value="${t.miny}" class="digits mini" min="0" max="99" />-<input name="t.maxy" value="${t.maxy}" class="digits mini" min="0" max="99" /></td>
				<th>行：</th>
				<td><input name="t.minx" value="${t.minx}" class="digits mini" min="0" max="99" />-<input name="t.maxx" value="${t.maxx}" class="digits mini" min="0" max="99" /></td>
				<th>层：</th>
				<td><input name="t.minz" value="${t.minz}" class="digits mini" min="0" max="99" />-<input name="t.maxz" value="${t.maxz}" class="digits mini" min="0" max="99" /></td>
			</tr>
		</tbody>
	</table>
	</div>
</form>
<script type="text/javascript">
function addHnums($target){
	$target.append(" <input name='t.hnum' value='1' class='required digits mini' min='1' max='99' />");
}
function addLnums($target){
	$target.append(" <input name='t.lnum' value='1' class='required digits mini' min='1' max='99' />");
}
function changeVnum(val){
	if(!isNaN(val)){
		var $htd = $("#eqment-hnum-td");
		var $ltd = $("#eqment-lnum-td");
		$htd.html('');
		$ltd.html('');
		for(var i = 0; i < val; i++){
			addHnums($htd);
			addLnums($ltd);
		}
		initChange($htd);
		initChange($ltd);
	}
}
function initChange(page){
	$("input[name='t.lnum']", page).change(function(e){
		var $this = $(this);
		$this.nextAll().val($this.val());
	});
	$("input[name='t.hnum']", page).change(function(e){
		var $this = $(this);
		$this.nextAll().val($this.val());
	});
}
$(function(){
	initChange($("#eqment-hnum-td"));
	initChange($("#eqment-lnum-td"));
});
</script>