<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="str" uri="http://www.bjggs.com.cn/jstl/str"%>
<form id="savePersonForm" action="${CTX_PATH}/admin/person/save" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="u.id" value="${u.id}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="save"><span submitForm="savePersonForm" >保存用户信息</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="15%">真实姓名</th>
				<td width="35%"><input name="u.name" value="${u.name}" class="required" maxlength="7" style="width:95%" autocomplete="off"/></td>
				<th width="15%">手机号码</th>
				<td width="35%"><input name="u.phone" value="${u.phone}" class="tel" maxlength="20" style="width:95%" autocomplete="off"/></td>
			</tr>
				<th>身份证号</th>
				<td><input name="u.idcard" value="${u.idcard}" maxlength="18" style="width:95%" autocomplete="off"/></td>
				<th>电子邮箱</th>
				<td><input name="u.email" value="${u.email}" class="mail" maxlength="25" style="width:95%" autocomplete="off"/></td>
			</tr>
		</tbody>
	</table>
</form>