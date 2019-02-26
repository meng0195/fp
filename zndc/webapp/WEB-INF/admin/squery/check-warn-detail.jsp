<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div layoutH="125">
<table class="checkDetail" style="width:100%;">
	<tr>
		<td width="93">数量统计</td>
		<td><c:if test="${not empty r.add and r.add != 0}">${r.add}/</c:if>${r.all}</td>
	</tr>
</table>
<table class="checkDetail" style="width:100%;">
	${r.html}
</table>
</div>

<script type="text/javascript">
	$(function(){
		$(".checkDetail td a").on("click",function(){
			$(".checkDetail td a").removeClass("font-orange");
			$(this).addClass("font-orange");
		});	
	});
</script>