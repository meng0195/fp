<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="margin: 1px;"></div>
<div id="sysMenuDiv" class="treeContainer">
	<div id="sysMenuTreeDiv" class="zTreeDiv" data='${jsonString}'
		layoutH="0" divOffsetHeight="122" onClick="menuTreeClick" afterExecFun="initMenuEditPage">
		<ul class="ztree"></ul>
	</div>
</div>
<div class="dragBar" minW="200" maxW="600" resizeFor="sysMenuDiv"></div>
<div class="pageContent">
	<form id="sysMenuEditForm" onsubmit="return divSearch(this, 'menuBox');" 
		action="${CTX_PATH}/admin/system/menu/add/0?tid=${param.tid}" method="post"></form>
	<div id="menuBox" class="unitBox"></div>
</div>
<div class="clearBoth"></div>
<script type="text/javascript">
function initMenuEditPage() {
	$("#sysMenuEditForm").submit();
}
function menuTreeClick(event, treeId, treeNode, clickFlag){
	var id = treeNode.id;
	var text = treeNode.text;
	var flag = treeNode.flag;
	var url='${CTX_PATH}/admin/system/menu/edit/' + id + '?tid=${param.tid}';
	$("#sysMenuEditForm").attr("action",url).submit().attr("action","");
	return false;
}
</script>