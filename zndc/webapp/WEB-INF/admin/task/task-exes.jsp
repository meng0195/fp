<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div style="width:700px;">
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/basis/task/exes/${planCode}/${planType}" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td>开始时间：</td>
				<td><input name="ft_GE_D_times" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="readonly" value="${param.ft_GE_D_times}" size="20"/></td>
				<td>结束时间：</td>
				<td><input name="ft_LE_D_times" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="readonly" value="${param.ft_LE_D_times}" size="20"/></td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<form id="toTaskExesDetail" onsubmit="return divSearch(this, 'task-exes-right');" action="" method="post"></form>
<table class="table" layoutH="94" width="100%">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th>执行时间</th>
			<th width="120">检测仓房数量</th>
			<th width="120">正常仓房数量</th>
			<th width="120">异常仓房数量</th>
			<th width="80">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" >
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td class="center"><fmt:formatDate value="${u.times}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td class="center">${u.totals}</td>
				<td class="center">${u.normals}</td>
				<td class="center">${u.warns}</td>
				<td class="center">
					<a onclick="submitTaskExes(this)" style="cursor: pointer;" shref="${CTX_PATH}/basis/task/exes/detail/${u.planCode}/${u.times.time}/${planType}" title="检测详情" class="font-blue">详情</a>
					<a target="navTab" href="" title="">&nbsp;</a>
				</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>
</div>
<div id="task-exes-right" style="left: 703px;top: 0;position: absolute;" layoutH="5"></div>
<script type="text/javascript">
$(function(){
	var $width = $("#task-exes-right").parent().width() - 703
	$("#task-exes-right").css({width:$width + "px"})
})
function submitTaskExes(target){
	$("#toTaskExesDetail").attr("action", $(target).attr("shref")).submit();
}
</script>