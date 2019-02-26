<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/cam/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>摄像头名称：</th>
				<td>
					<input name="ft_LIKEA_S_camName" value="${param.ft_LIKEA_S_camName}" size="20" />
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
		<li><a class="add" target="navTab" rel="tab-camera-edit" href="${CTX_PATH}/cam/edit/0?tid=${param.tid}" title="摄像头信息新增"><span>新增</span></a></li>
		<li><a class="delete" target="form" rel="cameraForm" title="确认删除选中的摄像头吗?" href="${CTX_PATH}/cam/del?tid=${param.tid}"><span>批量删除</span></a></li>
		<li><a class="delete" target="navTab" rel="tab-camera-rank" href="${CTX_PATH}/cam/rank?tid=${param.tid}" title="摄像头排布"><span>排布</span></a>
	</ul>
</div>
<form id="cameraForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">
	<table class="table" layoutH="127">
		<thead>
			<tr>
				<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>
				<th width="35">序号</th>
				<th width="100">设备名称</th>
				<th width="100">设备IP</th>
				<th width="100">设备端口</th>
				<th width="100">设备类型</th>
				<!-- <th width="100">启用状态</th>
				<th width="100">切换</th> -->
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="u" items="${page.result}" varStatus="st">
				<tr target="uid" rel="${u.id}">
					<td class="center"><input type="checkbox" name="uids" value="${u.id}"/></td>
					<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
					<td class="center" title="${u.camName}">${u.camName}</td>
					<td class="center">${u.camIP}</td>
					<td class="center">${u.camPort}</td>
					<td class="center">${map:get(codes['TYPE_CAM'], u.type)}</td>
					<%-- <td class="center">${u.status == 0 ? "未启用" : "启用"}</td>
					<td class="center">
						<c:choose>
							<c:when test="${u.status == 0}">
								<a target="ajaxTodo" title="是否启用摄像头，请确认?" href="${CTX_PATH}/cam/status/${u.id}/1?tid=${param.tid}" class="font-blue" >启动</a>
							</c:when>
							<c:otherwise>
								<a target="ajaxTodo" title="是否关闭摄像头，请确认?" href="${CTX_PATH}/cam/status/${u.id}/0?tid=${param.tid}" class="font-red" >关闭</a>
							</c:otherwise>
						</c:choose>
						<a target="navTab" href="" title="">&nbsp;</a>
					</td> --%>
					<td class="center">
						<a target="dialog" rel="dialog-camera-detail" width="800" height="480" mask="true" href="${CTX_PATH}/cam/detail/${u.id}" title="摄像头详情" class="font-blue">查看</a>
						<a target="navTab" rel="tab-camera-edit" href="${CTX_PATH}/cam/edit/${u.id}?tid=${param.tid}" title="摄像头信息编辑" class="font-blue">编辑</a>
						<a target="ajaxTodo" title="确认删除摄像头信息吗?" href="${CTX_PATH}/cam/del/${u.id}?tid=${param.tid}" class="font-red">删除</a>
					</td>
				</tr>
			</c:forEach>
			<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
				<tr target="uid" rel=""><!-- <td></td><td></td> --><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<%@ include file="../pager.jsp" %>