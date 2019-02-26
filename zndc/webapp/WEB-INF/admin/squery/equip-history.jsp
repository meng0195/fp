<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div class="pageHeader">
<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${CTX_PATH}/squery/equip/history" method="post">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="page.pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="page.pageSize" value="${page.pageSize}"/>
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<th>仓房：</th>
				<td>
					<select id="squery-equip-houseNo" name="ft_EQ_S_houseNo" class="combox">
						<option value="">请选择</option>
						<c:forEach var="house" items="${codes['houses']}">
			  				<option value="${house.key}" ${param.ft_EQ_S_houseNo == house.key ? 'selected' : ''}>${house.value}</option>
			  			</c:forEach>
			  		</select>
			  	</td>
			  	<th>操作时间：</th>
				<td>
					<input class="date" dateFmt="yyyy-MM-dd" name="date" value="${date}" readonly="readonly">
			  	</td>
			  	<td><div class="buttonActive"><span submitForm="pagerForm">查询</span></div></td>
				<td><div class="buttonActive"><span clearForm="pagerForm">清空</span></div></td>
			</tr>
		</table>
	</div>
</form>
</div>
<%-- <div class="panelBar">
	<ul class="toolBar">
		<li><a class="export" target="form" rel="storeThExportForm" title="确定要导出数据吗?"><span>导出EXCEL</span></a></li>
		<form id="storeThExportForm" action="${CTX_PATH}/exp/xls" method="post">
		  	<input type="hidden" name="json" value='{"tag":"TestData","name":"仓温表${param.ft_GE_D_testDate}-${param.ft_LE_D_testDate}","suffix":true,"type":1,"zip":false,"orders":{},"titles":[{"title":"仓房名称","width":20,"field":"houseNo"}, {"title":"检测时间","width":40,"field":"testDate"},{"title":"内温(℃)","width":18,"field":"inT"},{"title":"內湿(%RH)","width":18,"field":"inH"},{"title":"外温(℃)","width":18,"field":"outT"},{"title":"外湿(%RH)","width":18,"field":"outH"},{"title":"均温(℃)","width":18,"field":"avgT"},{"title":"最低温(℃)","width":18,"field":"minT"},{"title":"最高温(℃)","width":18,"field":"maxT"}]}'/>
		  	<input name="ft_GE_D_testDate" value="${param.ft_GE_D_testDate}" type="hidden"/>
		  	<input name="ft_LE_D_testDate" value="${param.ft_LE_D_testDate}" type="hidden"/>
		  	<input name="ft_EQ_S_houseNo" value="${param.ft_EQ_S_houseNo}" type="hidden"/>
		</form>
	</ul>
</div> --%>
<table class="table" width="100%" layoutH="94">
	<thead>
		<tr>
			<th width="35">序号</th>
			<th width="150">仓房名称</th>
			<th>操作时间</th>
			<th width="150">设备名称</th>
			<th width="150">操作类型</th>
			<th width="150">模式类型</th>
			<th width="150">操作人</th>
			<th width="150">操作结果</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="u" items="${page.result}" varStatus="st">
			<tr target="uid" rel="${u.id}">
				<td class="center">${st.count+(page.pageNo-1)*page.pageSize}</td>
				<td class="center">${map:get(codes['houses'], u.houseNo)}</td>
				<td class="center"><fmt:formatDate value="${u.time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="center">${u.equipName}</td>
				<td class="center">${u.doType==0?"关":"开"}</td>
				<td class="center">${map:get(enums['TYPE_CTR_TAG'],u.modelType)}</td>
				<td class="center">${u.userName}</td>
				<td class="center">${u.doResult==0?"失败":"成功"}</td>
			</tr>
		</c:forEach>
		<c:forEach begin="1" end="${page.pageSize - fn:length(page.result)}">
			<tr target="uid" rel=""><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
		</c:forEach>
	</tbody>
</table>
<%@ include file="../pager.jsp" %>