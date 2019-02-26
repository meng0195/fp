<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="dlgPasswdForm" action="${CTX_PATH}/admin/system/user/passwd/update" method="post"
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
	<div class="panelBar">
		<ul class="toolBar">
			<li style="float:right"><a class="save"><span id="dlgPasswdFormBtn" submitForm="dlgPasswdForm">保存</span></a></li>
			<li style="float:right"><a class="icon"><span clearForm="dlgPasswdForm">重置</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="35%" style="text-align:center">原密码</th>
				<td width="65%"><input class="required passwd" type="password" name="oldPass" autcomplete="off" style="width:95%"/></td>
			</tr>
			<tr>
				<th style="text-align:center">新密码</th>
				<td><input class="required passwd" type="password" id="loginPass" name="loginPass" autcomplete="off" style="width:95%"/></td>
			</tr>
			<tr>
				<th style="text-align:center">确认密码</th>
				<td><input class="required passwd" type="password" name="confirmPass" equalTo="#loginPass" autcomplete="off" style="width:95%"/></td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
$(function() {
	$(document).keyup(function(e){
		if(e.keyCode == 13) {
			$("#dlgPasswdFormBtn").submit();
		}
	});
});
</script>