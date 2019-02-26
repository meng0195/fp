<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/control/smart/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="basis-equip-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>粮食品种：</th>
				<td>
					<select id="basis-equip-list-grainCode" name="ft_EQ_I_grainCode" class="combox">
						<option value="">请选择</option>
						<c:forEach var="grain" items="${codes['TYPE_GRAIN']}">
			  				<option value="${grain.key}" ${param.ft_EQ_I_grainCode == grain.key ? 'selected' : ''}>${grain.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>是否开启：</th>
				<td>
					<select id="basis-equip-list-status" name="ft_EQ_I_status" class="combox">
						<option value="">请选择</option>
						<c:forEach var="s" items="${enums['TYPE_FLAG']}">
			  				<option value="${s.key}" ${param.ft_EQ_I_status == s.key ? 'selected' : ''}>${s.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>是否运行：</th>
				<td>
					<select id="basis-equip-list-ingTag" name="ft_EQ_I_ingTag" class="combox">
						<option value="">请选择</option>
						<c:forEach var="s" items="${enums['TYPE_FLAG']}">
			  				<option value="${s.key}" ${param.ft_EQ_I_ingTag == s.key ? 'selected' : ''}>${s.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<div class="panelBar">
	<ul class="toolBar">
		<form id="smartAddForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">
			<select id="smart-add-select" name="houseNo" class="combox">
				<option value="">请选择</option>
				<c:forEach var="house" items="${codes['houses']}">
	  				<option value="${house.key}" >${house.value}</option>
	  			</c:forEach>
	  		</select>
		</form>
		<li><a class="add" target="form" rel="smartAddForm" title="选中仓房的智能模式配置将被初始化，请确认?" href="${CTX_PATH}/control/conf/add?tid=${param.tid}"><span>初始化仓房模式</span></a></li>
	</ul>
</div>
<table class="table" layoutH="93">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="100">仓房名称</th>
			<th width="100">粮食品种</th>
			<th width="100">模块</th>
			<th width="100">模式</th>
			<th width="100">是否开启</th>
			<th width="100">是否运行</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center">${map:get(codes['TYPE_GRAIN'], u.grainCode)}</td>
				<td class="center">${(u.modelType > 0 and u.modelType < 5) ? '智能通风' : (u.modelType == 5 or u.modelType == 6) ? '低温储粮' : '其他'}</td>
				<td class="center">${map:get(enums['TYPE_SMART'], u.modelType)}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.status)}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.ingTag)}</td>
				<td class="center">
					<a target="navTab" href="${CTX_PATH}/control/smart/edit/${u.id}?tid=${param.tid}" title="编辑" class="font-blue">编辑</a>
					<%--<a target="navTab" href="${CTX_PATH}/control/smart/edit/${u.houseNo}?tid=${param.tid}" title="编辑" class="font-blue">编辑</a>--%>
					<a target="navTab" rel="tab-ctr-plan-exes" href="/zndc/control/plan/exes/2/${u.houseNo}${u.modelType}?tid=${param.tid}" title="任务执行详情" class="font-blue">任务执行情况</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>