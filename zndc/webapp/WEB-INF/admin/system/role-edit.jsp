<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="str" uri="http://www.bjggs.com.cn/jstl/str"%>
<form id="saveSysRoleForm" action="${CTX_PATH}/admin/system/role/save" method="post" 
	onsubmit="return validateCallback(this, afterSysRoleAjaxDone);" class="pageForm required-validate">
	<c:set var="sysRoleMenuIds" value="${str:joinEntries(r.menus, 'id', ',')}"/> 
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="r.id" value="${r.id}"/>
	<input type="hidden" name="r.deptId" value="${d.id}"/>
	<input type="hidden" id="roleMenuIds" name="menuIds" value="${sysRoleMenuIds}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="save"><span submitForm="saveSysRoleForm" beforeExecFun="checkRoleMenu">保存角色信息</span></a></li>
		</ul>
	</div>
	<div id="sysRoleMenuDiv" class="treeContainer" style="border-left:1px solid #BFD4F0;">
		<div id="sysRoleMenuTreeDiv" class="zTreeDiv" data='${jsonString}' multiMode="true"
			layoutH="22" divOffsetHeight="122" afterExecFun="initRoleMenuEditPage">
			<ul class="ztree"></ul>
		</div>
	</div>
	<table class="edit" layoutH="0">
		<tbody>
			<tr>
				<th width="20%" nowrap="nowrap">部门名称</th>
				<td width="80%">${d.name}</td>
			</tr>
			<tr>
				<th>部门编码</th>
				<td>${d.code}</td>
			</tr>
			<tr>
				<th>角色名称</th>
				<td>
					<input name="r.name" value="${r.name}" class="required" maxlength="15" style="width:95%" autocomplete="off" onblur="$('#role_desc').val(this.value);"/>
				</td>
			</tr>
			<tr>
				<th>角色编码</th>
				<td><input name="r.code" value="${r.code}" class="required" maxlength="10" style="width:95%" autocomplete="off"/></td>
			</tr>
			<tr>
				<th>角色描述</th>
				<td>
					<input id="role_desc" name="r.desc" value="${r.desc}" maxlength="50" style="width:95%" autocomplete="off"/>
				</td>
			</tr>
			<tr>
				<th>是否有效</th>
				<td>
					<c:forEach var="e" items="${enums['TYPE_STATUS']}">
						<input type="radio" name="r.status" ${r.status == e.key ? "checked" : ""} value="${e.key}"/>${e.value}
					</c:forEach>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
function initRoleMenuEditPage() {
	var zTreeObj = $.fn.zTree.getZTreeObj("sysRoleMenuTree");
	if(zTreeObj == null) return null;
	var roleMenuIds = '${sysRoleMenuIds}';
	var checkedNodeIds = roleMenuIds && roleMenuIds.indexOf(",") != -1 ? roleMenuIds.split(",") : [roleMenuIds];
	$.each(checkedNodeIds, function(i, id){
		if(id && typeof(id) != "undefined"){
			var treeNode = zTreeObj.getNodeByParam("id", id);
			if(treeNode && !treeNode.isParent) {
				zTreeObj.checkNode(treeNode, true, true, false);
			}
		}
	});
	return true;
}
function getRoleMenuChecked(treeId) {
	var zTreeObj = $.fn.zTree.getZTreeObj(treeId);
	var nodes = zTreeObj.getCheckedNodes(true);
	var nodeIds = [];
	$.each(nodes, function(i, node){
		var status = node.getCheckStatus();
		if(node.id){ //保留半勾选状态
			//&& (status.checked && !status.half)){//剔除半勾选状态的节点
			nodeIds.push(node.id);
		}
	});
	return nodeIds.join(",")
};
function checkRoleMenu() {
	$("#roleMenuIds").val(getRoleMenuChecked("sysRoleMenuTree"));
	if($.trim($("#roleMenuIds").val()).length == 0) {
		alertMsg.warn("请勾选左侧菜单信息后再保存！");
		return false;
	}
	return true;
}
</script>