<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="str" uri="http://www.bjggs.com.cn/jstl/str"%>
<div style="margin:1px 0">
<form id="saveSysUserForm" action="${CTX_PATH}/admin/system/user/save" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="u.id" value="${u.id}"/>
	<input type="hidden" name="oldPass" value="${u.loginPass}"/>
	<input type="hidden" id="sysUserDeptId" name="u.deptId" value="${u.deptId}"/>
	<input type="hidden" id="sysUserRoleIds" name="roleIds" value="${str:joinEntries(u.roles, 'id', ',')}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="save"><span submitForm="saveSysUserForm" beforeExecFun="checkUserDeptRole">保存用户信息</span></a></li>
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="15%">登录名</th>
				<td width="35%"><input name="u.loginName" value="${u.loginName}" class="required login" <c:if test="${not empty u.id}">readonly="true"</c:if> style="width:95%" autocomplete="off"/></td>
				<th width="15%">手机号码</th>
				<td width="35%"><input name="u.phone" value="${u.phone}" class="tel" maxlength="20" style="width:95%" autocomplete="off"/></td>
			</tr>
			<tr>
				<th>真实姓名</th>
				<td><input name="u.name" value="${u.name}" class="required" maxlength="7" style="width:95%" autocomplete="off"/></td>
				<th>身份证号</th>
				<td><input name="u.idcard" value="${u.idcard}" maxlength="18" style="width:95%" autocomplete="off"/></td>
			</tr>
			<tr>
				<th>登录密码</th>
				<td><input type="password" id="loginPass" name="u.loginPass" value="${u.loginPass}" class="required passwd" minlength="6" maxlength="32" style="width:95%" autocomplete="off"/></td>
				<th>电子邮箱</th>
				<td><input name="u.email" value="${u.email}" class="mail" maxlength="25" style="width:95%" autocomplete="off"/></td>
			</tr>
			<tr>
				<th>确认密码</th>
				<td><input type="password" name="loginPassConfirm" value="${u.loginPass}" class="required passwd" minlength="6" maxlength="32" equalTo="#loginPass" style="width:95%" autocomplete="off"/></td>
				<th>是否有效</th>
				<td>
					<c:forEach var="e" items="${enums['TYPE_STATUS']}">
						<input type="radio" name="u.status" ${u.status == e.key ? "checked" : ""} value="${e.key}"/>${e.value}
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th>所属部门：</th><td id="userDeptRoleInfo">${u.deptName}</td>
				<th></th><td></td>
			</tr>
		</tbody>
	</table>
</form>
</div>
<div id="sysUserDeptDiv" class="treeContainer">
	<div id="sysUserDeptTreeDiv" class="zTreeDiv" data='${jsonString}'
		layoutH="0" divOffsetHeight="122" onClick="userDeptTreeClick" afterExecFun="initUserRolePage">
		<ul class="ztree"></ul>
	</div>
</div>
<form id="sysUserRoleForm" onsubmit="return divSearch(this, 'userRoleBox');" 
		action="${CTX_PATH}/admin/system/role/user/list/0" method="post"></form>
	<div id="userRoleBox" class="unitBox"></div>
<div class="clearBoth"></div>
<script type="text/javascript">
function initUserRolePage() {
	var selectedDeptId = "${u.deptId}"; 
	var treeId = "sysUserDeptTree";
	var zTreeObj = $.fn.zTree.getZTreeObj(treeId);
	var treeNode = selectedDeptId && selectedDeptId != "" && zTreeObj ? zTreeObj.getNodeByParam("id", selectedDeptId) : null;
	if (treeNode && treeNode != null) {
		zTreeObj.selectNode(treeNode);
		userDeptTreeClick(null, treeId, treeNode, true);
	} else {
		$("#sysUserRoleForm").submit();
	}
}
function userDeptTreeClick(event, treeId, treeNode, clickFlag){
	var id = treeNode.id;
	var text = treeNode.text;
	$("#sysUserDeptId").val(id);
	$("#userDeptRoleInfo").text(text);
	var url='${CTX_PATH}/admin/system/role/user/list/' + id + "?urids=${str:joinEntries(u.roles, 'id', ',')}";
	$("#sysUserRoleForm").attr("action",url).submit().attr("action","");
	return false;
}
function checkUserDeptRole() {
	var $chks = $(":checkbox[name='urids']:checked");
	var chksIds = [];
	$.each($chks, function(i, item) {
		chksIds.push(this.value);
	});
	$("#sysUserRoleIds").val(chksIds.length > 0 ? chksIds.join(",") : "");
	
	if($.trim($("#sysUserDeptId").val()).length == 0) {
		alertMsg.warn("请点击左下方树结点，选择用户所属部门和角色后再保存");
		return false;
	}
	
	return true;
}
</script>