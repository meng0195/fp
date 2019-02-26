<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/temp/warn/list" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房名称：</th>
				<td>
					<select id="squery-warn-list-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
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
		<li><a class="export" target="form" rel="storeThExportForm" title="确定要导出数据吗?"><span>导出EXCEL</span></a></li>
		<form id="storeThExportForm" action="${CTX_PATH}/exp/xls" method="post">
		  	<input type="hidden" name="json" value='{"tag":"Warns","name":"报警表${param.ft_EQ_S_houseNo}","suffix":true,"type":1,"zip":false,"orders":{},"titles":[{"title":"仓房名称","width":20,"field":"houseNo"}, {"title":"检测时间","width":40,"field":"testDate"},{"title":"报警类型","width":18,"field":"warnType"},{"title":"报警点数/层数","width":18,"field":"pointNum"},{"title":"预置阀值","width":18,"field":"warnVal"},{"title":"是否处理","width":18,"field":"status"}]}'/>
		  	<input name="ft_EQ_S_houseNo" value="${param.ft_EQ_S_houseNo}" type="hidden"/>
		  	<input name="ft_EQ_I_warnType" value="${param.ft_EQ_I_warnType}" type="hidden"/>
		</form>
	</ul>
</div>
<table class="table" width="100%" layoutH="125">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="150">仓房名称</th>
			<th>检测时间</th>
			<th width="150">预警类型</th>
			<th width="150">预警类型1</th>
			<th width="150">预警点数/层数</th>
			<th width="150">设置阀值</th>
			<th width="150">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td>${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center"><fmt:formatDate value="${u.testDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center">${warn:get1(u.type)}</td>
				<td class="center">${warn:get2(u.type, u.type1)}</td>
				<td class="center">${u.nums}</td>
				<td class="center">${u.threshold}</td>
				<td class="center">
					<a target="dialog" rel="dialog-warn-detail" width="1200" height="650" mask="true" href="${CTX_PATH}/squery/warn/detail/${u.id}" title="异常详情" class="font-blue">查看</a>
					<a target="navTab" href="">&nbsp;</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>