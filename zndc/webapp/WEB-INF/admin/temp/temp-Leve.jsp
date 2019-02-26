<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="warn" uri="http://www.bjggs.com.cn/jstl/warn"%>
<div class="pageHeader" style="padding: 2px;">
	<div class="subBar">
		<a class="font-blue" id="leve-sel-btn" style="cursor: pointer;">确认</a>
	</div>
</div>
<table id="leve-sel-table" class="edit">
	<c:forEach var="l" items="${leves}" varStatus="st">
		<c:if test="${st.index % 4 == 0}"><tr></c:if>
		<td><label><input type="checkbox" name="s" ${l == 1 ? 'checked' : ''} value="${st.index}"/>${st.index + 1}层</label></td>
		<c:if test="${st.last && st.index % 4 == 0}"><td></td><td></td><td></td></tr></c:if>
		<c:if test="${st.last && st.index % 4 == 1}"><td></td><td></td></tr></c:if>
		<c:if test="${st.last && st.index % 4 == 2}"><td></td></tr></c:if>
		<c:if test="${st.index % 4 == 3}"></tr></c:if>
	</c:forEach>
</table>
<script type="text/javascript">
$("#leve-sel-btn").click(function(){
	var ss = "";
	var show = "";
	$("input[name='s']:checked", $("#leve-sel-table")).each(function(){
		ss += $(this).val() + ".";
		show += (parseInt($(this).val())+1) + "."; 
	})
	if(ss.length > 0) ss = ss.substring(0, ss.length - 1);
	if(show.length > 0) show = show.substring(0, show.length - 1);
	$("#" + '${id}', $("#conf-area-leve")).val(ss);
	$("#" + '${id}Name', $("#conf-area-leve")).val(show);
	$.pdialog.closeCurrent();
})
</script>