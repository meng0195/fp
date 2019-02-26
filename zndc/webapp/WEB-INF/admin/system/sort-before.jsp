<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="list" uri="http://www.bjggs.com.cn/jstl/list"%>
<script type="text/javascript">
$(document).ready(function() {
	$("#commonSortTree").tableDnD({
	    onDragClass: "tableDnDDragClass",
	    onDropClass: "tableDnDDropClass",
	    onDrop: function(table, row) {
            var rows = table.tBodies[0].rows;
            var debugStr = [];
            for (var i=0; i<rows.length; i++) {
                debugStr.push(rows[i].id);
            }
	        $("#sortedIds").val(debugStr.join(","));
	    },
		onDragStart: function(table, row) {
			$("#sortedIds").val(row.id);
		}
	});
});
function beforeSortTreeData(){
	if($("#sortedIds").val() == "") {
		alertMsg.warn("您还未做排序操作，用鼠标上下拖拽排序项后保存即可！");
		return false;
	}
}
</script>
<form id="commonSortTreeForm" name="commonSortTreeForm" action="${CTX_PATH}/admin/system/sort/update/${tag}" method="post" 
	onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate">
<div class="panelBar">
	<ul class="toolBar">
		<li><a class="save"><span submitForm="commonSortTreeForm" before="beforeSortTreeData">保存</span></a></li>
	</ul>
</div>
<input type="hidden" id="sortedIds" name="sortedIds"/>
<input type="hidden" name="tid" value="${param.tid}"/>
<table id="commonSortTree" width="100%" class="list" cellspacing="0" cellpadding="2">
<thead><th width="35px">序号</th><th>排序项</th></thead>
<tbody>
<c:set var="seqnos" value="${list:init()}"/>
<c:forEach var="obj" items="${objs}" varStatus="st">
	<c:set var="seqnos" value="${list:add(seqnos, obj.seqno)}"/>
	<tr id="${obj.id}">
		<td>${st.count}</td>
		<td>${obj.name}</td>
	</tr>
</c:forEach>
</tbody>
</table>
</form>