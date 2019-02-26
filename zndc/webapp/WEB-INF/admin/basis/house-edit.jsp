<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisHouseForm" action="${CTX_PATH}/basis/house/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="s.id" value="${s.id}"/>
	<input type="hidden" name="s.orgCode" value="${_SYS_USER_.deptCode}"/>
	<input type="hidden" name="s.houseNo" value="${s.houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisHouseForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="4" class="title1" style="font-weight: bold;">仓房信息</th>
			</tr>
			<tr>
				<th width="15%">仓房名称</th>
				<td width="35%"><input name="s.houseName" value="${s.houseName}" class="required" maxlength="25" /></td>
				<th>仓房类型</th>
				<td>
					<c:choose>
						<c:when test="${0 == s.houseType}">
							<select id="basis_house_houseType_edit" name="s.houseType" class="combox" >
								<c:forEach var="e" items="${enums['TYPE_HOUSE']}">
					  				<option value="${e.key}" ${e.key == s.houseType ? 'selected' : ''}>${e.value}</option>
					  			</c:forEach>
					  		</select>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="s.houseType" value="${s.houseType}">
							${map:get(enums['TYPE_HOUSE'], s.houseType)}
						</c:otherwise>
					</c:choose>
			  		<c:choose>
			  			<c:when test="${0 == s.houseType || s.houseType == 1}">
			  				<c:set var="thDim1" value="长度(m)"/>
				  			<c:set var="thDim2" value="宽度(m)"/>
				  			<c:set var="thDim3" value="高度(m)"/>
			  			</c:when>
			  			<c:otherwise>
				  			<c:set var="thDim1" value="半径(m)"/>
				  			<c:set var="thDim2" value="檐高(m)"/>
				  			<c:set var="thDim3" value="顶高(m)"/>
			  			</c:otherwise>
			  		</c:choose>
				</td>
			</tr>
			<tr>
				<th>设计储量(t)</th>
				<td><input name="s.storeCount" value="${s.storeCount}" class="double4" min="0" max="99999999"/></td>
				<th>角度(°)</th>
				<td><input name="s.angle" value="${s.angle}" class="required digits" min="-90" max="90"/></td>
			</tr>
			<tr>
				<th id="house-edit-th-dim1">${thDim1}</th>
				<td><input name="s.dim1" value='<fmt:formatNumber value="${s.dim1}" pattern="#0.0"/>' class="required dims" min="0.1" /></td>
				<th id="house-edit-th-dim2">${thDim2}</th>
				<td><input name="s.dim2" value="<fmt:formatNumber value="${s.dim2}" pattern="#0.0"/>" class="required dims" min="0.1" /></td>
			</tr>
			<tr>
				<th id="house-edit-th-dim3">${thDim3}</th>
				<td><input name="s.dim3" value="<fmt:formatNumber value="${s.dim3}" pattern="#0.0"/>" class="required dims" min="0.1" /></td>
				<th>建筑年份</th>
				<td><input name="s.builtYear" value="${s.builtYear}" class="digits" min="1900" max="2099"/></td>
			</tr>
			<tr>
				<th>熏蒸(富氮)与否</th>
				<td>
					<c:forEach var="e" items="${codes['TYPE_XZ']}">
						<label><input type="radio" name="s.xzTag" value="${e.key}" ${s.xzTag == e.key ? "checked" : ""}/>${e.value}</label>
					</c:forEach>
				</td>
				<th>密闭与否</th>
				<td>
					<c:forEach var="e" items="${codes['TYPE_MB']}">
						<label><input type="radio" name="s.mbTag" value="${e.key}" ${s.mbTag == e.key ? "checked" : ""}/>${e.value}</label>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th>进出仓状态</th>
				<td colspan="3">
					<c:forEach var="e" items="${codes['TYPE_JCC']}">
						<label><input type="radio" name="s.jccTag" value="${e.key}" ${s.jccTag == e.key ? "checked" : ""}/>${e.value}</label>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th colspan="4" class="title1" style="font-weight: bold;">储粮信息</th>
			</tr>
			<tr>
				<th>粮食品种</th>
				<td>
					<select id="basis_house_grainCode_edit" name="g.grainCode" class="combox" >
						<c:forEach var="e" items="${codes['TYPE_GRAIN']}">
			  				<option value="${e.key}" ${e.key == g.grainCode ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th>储粮性质</th>
				<td>
					<select id="basis_house_grainAttr_edit" name="g.nature" class="combox" >
						<c:forEach var="e" items="${codes['TYPE_NATURE']}">
			  				<option value="${e.key}" ${e.key == g.nature ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th>存储方式</th>
				<td>
					<select id="basis_house_storageMode_edit" name="g.storageMode" class="combox" >
						<c:forEach var="e" items="${codes['TYPE_STORE']}">
			  				<option value="${e.key}" ${e.key == g.storageMode ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
				<th width="15%">保管员</th>
				<td width="35%">
					<input type="hidden" name="g.keeperCode" value="${g.keeperCode}">
					<input class="required" readonly="readonly" name="g.keeperName" value="${g.keeperName}" suggestfields="keeperCode,keeperName" suggesturl="${CTX_PATH}/admin/system/user/look" lookupgroup="g">
					<a href="${CTX_PATH}/admin/system/user/look" lookupgroup="g" class="font-blue">选择用户</a>
				</td>
			</tr>
			<tr>
				<th>入仓日期</th>
				<td>
					<input maxId="grainOutDate" id="grainInDate" name="g.dateOfIn" class="required date" datefmt="yyyy-MM-dd" readonly="true" value="<fmt:formatDate value='${g.dateOfIn}' pattern='yyyy-MM-dd' />">
				</td>
				<th>出仓日期(预计)</th>
				<td>
					<input minId="grainInDate" id="grainOutDate" name="g.dateOfOut" class="date required" datefmt="yyyy-MM-dd" readonly="true" value="<fmt:formatDate value='${g.dateOfOut}' pattern='yyyy-MM-dd' />">
				</td>
			</tr>
			<tr>
				<th>入仓水分(%)</th>
				<td>
					<input name="g.grainInWater" class="required ispt" value="<fmt:formatNumber value='${g.grainInWater}' pattern='#0.0' />">
				</td>
				<th>当前水分(%)</th>
				<td>
					<input name="g.grainWater" class="ispt" value="<fmt:formatNumber value='${g.grainWater}' pattern='#0.0' />">
				</td>
			</tr>
			<tr>
				<th>杂质(%)</th>
				<td>
					<input name="g.impurity" class="required dims" value="<fmt:formatNumber value='${g.impurity}' pattern="#0.0"/>">
				</td>
				<th>等级</th>
				<td>
					<select id="basis_house_grade_edit" name="g.grade" class="combox" >
						<c:forEach var="e" items="${codes['TYPE_GRADE']}">
			  				<option value="${e.key}" ${e.key == g.grade ? 'selected' : ''}>${e.value}</option>
			  			</c:forEach>
			  		</select>
				</td>
			</tr>
			<tr>
				<th>不完善粒(%)</th>
				<td>
					<input name="g.unsoKer" class="required" value="<fmt:formatNumber value='${g.unsoKer}' pattern="#0.0"/>">
				</td>
				<th>产地</th>
				<td>
					<input name="g.origin" value="${g.origin}" class="larger required" maxlength="30" />
				</td>
			</tr>
			<tr>
				<th>实际储量</th>
				<td>
					<input name="g.grainCount" value="${g.grainCount}" class="required" maxlength="30" />
				</td>
				<th>生产年份</th>
				<td>
					<input name="g.gainYear" value="${g.gainYear}" class="digits" min="1900" max="2099" />
				</td>
			</tr>
			<tr>
				<th>三防设备</th>
				<td colspan="3">
					<input name="g.proof3" value="${g.proof3}" class="normal" maxlength="6" />
				</td>
			</tr>
		</tbody>
	</table>
</form>

<script type="text/javascript">

$("#basis_house_houseType_edit").change(function(){
	var tag = ($(this).val() == 1);
	if(tag){
		$("#house-edit-th-dim1").html("长度(m)");
		$("#house-edit-th-dim2").html("宽度(m)");
		$("#house-edit-th-dim3").html("高度(m)");
	} else {
		$("#house-edit-th-dim1").html("直径(m)");
		$("#house-edit-th-dim2").html("檐高(m)");
		$("#house-edit-th-dim3").html("顶高(m)");
	}
});
</script>