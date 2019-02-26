<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div><%-- style="max-width:825px;" --%>
<form id="saveDeptForm" name="saveDeptForm" action="${CTX_PATH}/admin/system/dept/save" method="post" 
	onsubmit="return validateCallback(this, treeAfterSaveCallback);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>	
	<input type="hidden" name="d.id" value="${d.id}"/>
	<input type="hidden" name="d.pid" value="${d.pid}"/>
	<input type="hidden" name="d.level" value="${d.level}"/>
	<input type="hidden" name="d.code" value="${d.code}" />
	<div class="panelBar">
		<ul class="toolBar">
			<c:choose>
				<c:when test="${d.flag == 1}">
					<c:if test="${not empty d.id}">
						<li><a class="sort" target="dialog" width="430" height="480" mask="true" href="${CTX_PATH}/admin/system/sort/before/SysDept/pid/${d.id}?tid=${param.tid}"><span>同级排序</span></a></li>
						<li><a class="delete" target="ajaxTodo" callback="afterDeleteDeptCallback" title="该项连同其子项和角色信息一起被删除，请确认?" href="${CTX_PATH}/admin/system/dept/del/${d.id}?tid=${param.tid}"><span>删除部门</span></a></li>
						<li><a class="add" target="form" rel="sysRoleListForm" href="${CTX_PATH}/admin/system/role/list/${d.id}?tid=${param.tid}"><span>加载角色列表</span></a></li>
						<li><a class="add"><span submitForm="sysDeptEditForm" href="${CTX_PATH}/admin/system/dept/save/next/before/${d.id}/${d.level}?tid=${param.tid}">添加下级部门</span></a></li>
					</c:if>
					<li><a class="save"><span submitForm="saveDeptForm">保存部门</span></a></li>
				</c:when>
				<c:otherwise>
					<li><a class="add"><span submitForm="sysDeptEditForm" href="${CTX_PATH}/admin/system/dept/save/next/before/${d.id}/${d.level}?tid=${param.tid}">添加下级部门</span></a></li>	
				</c:otherwise>
			</c:choose>			
		</ul>
	</div>
	<table class="edit">
		<tbody>
			<tr>
				<th width="15%">部门名称</th>
				<td width="35%"><input name="d.name" value="${d.name}" class="required normal" maxlength="15" style="width:95%" onblur="$('#dept_desc').val(this.value);" autocomplete="off"/></td>
				<th width="15%">部门编码</th>
				<td width="35%">${d.code}</td>
			</tr>
			<tr>
				<th>描述</th>
				<td colspan="3"><input id="dept_desc" name="d.desc" value="${d.desc}" maxlength="50" style="width:98%" autocomplete="off"/></td>			
			</tr>
			<tr>
				<th>排序号</th>
				<td><input name="d.seqno" value="${d.seqno}" class="required digits" maxlength="2" style="width:95%" autocomplete="off"/></td>
				<th></th><td></td>
				<%-- 
				<th>是否有效</th>
				<td>
					<c:forEach var="e" items="${enums['STATUS']}">
						<input type="radio" name="d.status" ${d.status == e.key ? "checked" : ""} value="${e.key}"/>${e.value}
					</c:forEach>
				</td>
				 --%>
			</tr>
		</tbody>
	</table>
</form>
<form id="sysRoleListForm" onsubmit="return divSearch(this, 'roleBox');" action="" method="post"></form>
<div id="roleBox" class="unitBox"><div>
</div>		
<script type="text/javascript">
function afterDeleteDeptCallback(json){
	if(json == null) return false;
	if (json.statusCode == DWZ.statusCode.ok) {
		alertMsg.correct(json.message);
		deleteTreeNode(json, "sysDeptTree");
	}else{
		alertMsg.error(json.message);
	}
}
</script>