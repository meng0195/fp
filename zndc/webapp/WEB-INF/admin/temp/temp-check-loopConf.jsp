<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveBasisChecksLoopForm" action="${CTX_PATH}/check/temp/loopSave" method="post" 
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveBasisChecksLoopForm">保存</span></a></li>
		</ul>
	</div>
	<div layoutH="30">
	<table class="edit">
		<tbody>
			<tr>
				<th colspan="10"><a class="btn-norel" onclick="checkAll('#saveBasisChecksLoopForm', 'houseNos')">全选</a><a class="btn-norel" onclick="unCheckAll('#saveBasisChecksLoopForm', 'houseNos')">全不选</a><a class="btn-norel" onclick="opCheck('#saveBasisChecksLoopForm', 'houseNos')">反选</a></th>
			</tr>
			${c.clsStr}
		</tbody>
	</table>
	</div>
</form>