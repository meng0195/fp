<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveGlobalForm" action="${CTX_PATH}/global/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="g.id" value="${g.id}"/>
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveGlobalForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="4" class="title1">全局信息</th>
			</tr>
			<tr>
				<th width="100px">识别码</th>
				<td><input name="g.identCode" value="${base_code}" style="width: 60%;"/></td>
			</tr>
			<tr>
				<th width="100px">库名称</th>
				<td><input name="g.identName" value="${base_name}" style="width: 60%;"/></td>
			</tr>
			<tr>
				<th width="100px">天气情况</th>
				<td>
					<c:forEach var="e" items="${codes['TYPE_TQ']}">
						<label><input type="radio" name="g.weaTag" value="${e.key}" ${base_weaTag == e.key ? "checked" : ""}/>${e.value}</label>
					</c:forEach>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
</script>