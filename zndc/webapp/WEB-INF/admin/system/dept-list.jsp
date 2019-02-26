<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="margin: 1px;"></div>
<div id="sysDeptDiv" class="treeContainer">
	<div id="sysDeptTreeDiv" class="zTreeDiv" data='${jsonString}'
		layoutH="0" divOffsetHeight="122" onClick="deptTreeClick" afterExecFun="initDeptEditPage">
		<ul class="ztree"></ul>
	</div>
</div>
<div class="dragBar" minW="200" maxW="600" resizeFor="sysDeptDiv"></div>
<div class="pageContent">
	<form id="sysDeptEditForm" onsubmit="return divSearch(this, 'deptBox');" 
		action="${CTX_PATH}/admin/system/dept/add/0?tid=${param.tid}" method="post"></form>
	<div id="deptBox" class="unitBox"></div>
</div>
<div class="clearBoth"></div>
<script type="text/javascript">
function initDeptEditPage() {
	$("#sysDeptEditForm").submit();
}
function deptTreeClick(event, treeId, treeNode, clickFlag){
	var id = treeNode.id;
	var text = treeNode.text;
	var url='${CTX_PATH}/admin/system/dept/edit/' + id + '?tid=${param.tid}';
	$("#sysDeptEditForm").attr("action",url).submit().attr("action","");
	return false;
}
</script>