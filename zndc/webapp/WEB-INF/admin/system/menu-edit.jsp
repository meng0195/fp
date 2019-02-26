<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<form id="saveMenuForm" name="saveMenuForm" action="${CTX_PATH}/admin/system/menu/save" method="post" 
	onsubmit="return validateCallback(this, treeAfterSaveCallback);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>	
	<input type="hidden" name="m.id" value="${m.id}"/>
	<input type="hidden" name="m.pid" value="${m.pid}"/>
	<input type="hidden" name="m.level" value="${m.level}"/>
	<input type="hidden" name="m.code" value="${m.code}" />
	<div class="panelBar">
		<ul class="toolBar">
			<c:choose>
				<c:when test="${m.flag == 1}">
					<c:if test="${not empty m.id}">
						<li><a class="sort" target="dialog" width="430" height="480" mask="true" href="${CTX_PATH}/admin/system/sort/before/SysMenu/pid/${m.id}?tid=${param.tid}"><span>同级排序</span></a></li>
						<li><a class="delete" target="ajaxTodo" callback="afterDeleteMenuCallback" title="该项连同其子项一起被删除，请确认?" href="${CTX_PATH}/admin/system/menu/del/${m.id}?tid=${param.tid}"><span>删除菜单</span></a></li>
						<li><a class="add"><span submitForm="sysMenuEditForm" href="${CTX_PATH}/admin/system/menu/save/next/before/${m.id}/${m.level}?tid=${param.tid}">添加下级菜单</span></a></li>
					</c:if>
					<li><a class="save"><span submitForm="saveMenuForm">保存菜单</span></a></li>
				</c:when>
				<c:otherwise>
						<li><a class="add"><span submitForm="sysMenuEditForm" href="${CTX_PATH}/admin/system/menu/save/next/before/${m.id}/${m.level}?tid=${param.tid}">添加下级菜单</span></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<table class="edit" layoutH="110">
		<tbody>
			<tr>
				<th width="15%">菜单名称</th>
				<td width="35%"><input name="m.name" value="${m.name}" class="required normal" maxlength="10" style="width:95%" onblur="$('#menu_desc').val(this.value);" autocomplete="off"/></td>
				<th width="15%">菜单编码</th>
				<td width="35%">${m.code}</td>
			</tr>
			<tr>
				<th>描述</th>
				<td colspan="3"><input id="menu_desc" name="m.desc" value="${m.desc}" maxlength="50" style="width:98%" autocomplete="off"/></td>			
			</tr>
			<tr>
				<th>URL</th>
				<td><input name="m.link" value="${m.link}" maxlength="50" style="width:95%" autocomplete="off"/></td>	
				<th>渲染方式</th>
				<td>
					<c:forEach var="e" items="${enums['TYPE_PAGE_OPEN']}">
						<input type="radio" name="m.render" ${m.render == e.key ? "checked" : ""} value="${e.key}"/>${e.value}
					</c:forEach>
				</td>		
			</tr>
			<tr>
				<th>排序号</th>
				<td><input name="m.seqno" value="${m.seqno}" class="required digits" maxlength="3" style="width:95%" autocomplete="off"/></td>
				<th>是否有效</th>
				<td>
					<c:forEach var="e" items="${enums['TYPE_STATUS']}">
						<input type="radio" name="m.status" ${m.status == e.key ? "checked" : ""} value="${e.key}"/>${e.value}
					</c:forEach>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<script type="text/javascript">
function afterDeleteMenuCallback(json){
	if(json == null) return false;
	if (json.statusCode == DWZ.statusCode.ok) {
		alertMsg.correct(json.message);
		deleteTreeNode(json, "sysMenuTree");
	}else{
		alertMsg.error(json.message);
	}
}
</script>