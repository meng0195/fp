<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="dlgLoginForm" action="${pageContext.request.contextPath}/admin/login/ajax" method="post"
	onsubmit="return validateCallback(this, loginDialogAjaxDone);" class="pageForm required-validate">
	<div class="panelBar">
		<ul class="toolBar">
			<li style="float:right"><a class="add"><span id="dlgLoginFormBtn" submitForm="dlgLoginForm">登录</span></a></li>
			<li style="float:right"><a class="icon"><span clearForm="dlgLoginForm">重置</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="35%" style="text-align:center">登录名</th>
				<td width="65%"><input class="required" name="loginName" autcomplete="off" style="width:95%"/></td>
			</tr>
			<tr>
				<th style="text-align:center">密&nbsp;&nbsp;&nbsp;码</th>
				<td><input class="required" type="password" name="loginPass" autcomplete="off" style="width:95%"/></td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
$(function() {
	$(document).keyup(function(e){
		if(e.keyCode == 13) {
			$("#dlgLoginFormBtn").submit();
		}
	});
});
</script>
