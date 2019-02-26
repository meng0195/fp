<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/conf/warn/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房名称：</th>
				<td>
					<input name="ft_LIKEA_S_houseName" value="${param.ft_LIKEA_S_houseName}" size="20" />
			  	</td>
			  	<th>仓房类型：</th>
				<td>
					<select id="conf-warns-list-houseType" name="ft_EQ_S_houseType" class="combox">
						<option value="">请选择</option>
						<c:forEach var="c" items="${enums['TYPE_HOUSE']}">
			  				<option value="${c.key}" ${param.ft_EQ_S_houseType == c.key ? 'selected' : ''}>${c.value}</option>
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
<table class="table" layoutH="95">
	<thead>
		<tr>
			<%--<th width="25"><input type="checkbox" class="checkboxCtrl" group="uids"/></th>--%>
			<th width="35">序号</th>
			<th>仓房名称</th>
			<th width="120">仓房类型</th>
			<th width="100">测温</th>
			<th width="100">储粮</th>
			<th width="100">测虫</th>
			<th width="100">测气</th>
			<th width="100">控制</th>
			<th width="150">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<%--<td class="center"><input type="checkbox" name="uids" value="${u.id}"/></td>--%>
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize }</td>
				<td class="center">${u.houseName}</td>
				<td class="center">${map:get(enums['TYPE_HOUSE'], u.houseType)}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.warnTag[0])}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.warnTag[1])}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.warnTag[2])}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.warnTag[3])}</td>
				<td class="center">${map:get(enums['TYPE_FLAG'], u.warnTag[4])}</td>
				<td class="center">
					<a target="navTab" rel="tab-pest-conf-edit" href="${CTX_PATH}/conf/warn/edit/${empty u.houseNo ? '0' : u.houseNo}?tid=${param.tid}" title="报警配置" class="font-blue">编辑</a>
					<a target="navTab" rel="tab-pest-conf-point" href="" title="" >&nbsp;</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><%--<td></td>--%><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>