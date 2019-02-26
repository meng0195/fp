<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/admin/msg/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房编号：</th>
				<td>
					<select id="msg-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>消息类型：</th>
				<td>
					<select id="msg-list-msgType" name="ft_EQ_I_msgType" class="combox">
						<option value="">请选择</option>
						<c:forEach var="em" items="${enums['TYPE_MSG']}">
			  				<option value="${em.key}" ${param.ft_EQ_I_msgType == em.key ? 'selected' : ''}>${em.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>消息状态：</th>
				<td>
					<select id="msg-list-status" name="ft_EQ_I_status" class="combox">
						<option value="">请选择</option>
						<c:forEach var="em" items="${enums['TYPE_MSG_READ']}">
			  				<option value="${em.key}" ${param.ft_EQ_I_status == em.key ? 'selected' : ''}>${em.value}</option>
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
		<li><a class="delete" target="form" rel="msgListReadForm" title="是否将选中的消息都标记为已读，请确认?" href="${CTX_PATH}/admin/msg/reads?tid=${param.tid}" title="标记已读"><span>已读</span></a></li>
	</ul>
</div>
<form id="msgListReadForm" onsubmit="return validateCallback(this, navTabAjaxDone);" action="" method="post">
	<table class="table" layoutH="127">
		<thead>
			<tr>
				<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>
				<th width="35">序号</th>
				<th width="120">仓房名称</th>
				<th width="150">归属模块</th>
				<th width="100">消息类型</th>
				<th>消息内容</th>
				<th width="180">通知时间</th>
				<th width="100">消息状态</th>
				<th width="150">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="u" items="${page.result}" varStatus="st">
				<tr target="uid" rel="${u.msgCode}">
					<td class="center"><input type="checkbox" name="uids" value="${u.msgCode}"/></td>
					<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
					<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
					<td class="center">${warn:getModel(u.modType)}</td>
					<td class="center">${map:get(enums['TYPE_MSG'], u.msgType)}</td>
					<td>${u.msg}</td>
					<td class="center"><fmt:formatDate value="${u.time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td class="center">${map:get(enums['TYPE_MSG_READ'], u.status)}</td>
					<td class="center">
						<c:choose>
							<c:when test="${u.modType == 0}">
								<a target="dialog" rel="dialog-show-detail" width="1200" height="650" mask="true" href="${CTX_PATH}/squery/show/0/${u.testId}/1" title="检测详情" class="font-blue">详情</a>
							</c:when>
						</c:choose>
						<a target="ajaxTodo" title="请确认是否将该信息标记为已读?" href="${CTX_PATH}/admin/msg/read/${u.msgCode}?tid=${param.tid}" title="标记已读" class="font-red">已读</a>
					</td>
				</tr>
			</c:forEach>
			<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
				<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
			</c:forEach>
		</tbody>
	</table>
</form>
<%@ include file="../pager.jsp" %>