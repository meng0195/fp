<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="dbBakForm" action="${CTX_PATH}/dbm/rollbak" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<table class="edit" layoutH="30">
		<tbody>
			<tr>
				<th width="15%">数据备份</th>
				<td width="35%"><a class="font-blue" target="ajaxTodo" title="一天内只能保留 最后一次备份数据，请确认是否备份？" href="${CTX_PATH}/dbm/bak?tid=${param.tid}">备份</a></td>
				<th width="15%">数据还原</th>
				<td width="35%">
					<select id="db-bak-fileName" name="fileName" class="combox required" >
						<option value="">请选择</option>
						<c:forEach var="m" items="${map}">
			  				<option value="${m.value}">${m.value}</option>
			  			</c:forEach>
			  		</select>
			  		<a class="font-blue" target="form" rel="dbBakForm">还原</a>
				</td>
			</tr>
		</tbody>
	</table>
</form>