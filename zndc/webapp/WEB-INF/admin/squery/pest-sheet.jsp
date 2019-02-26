<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div id="pest-sheet-div" class="pest-points" layoutH="40"></div>
<script type="text/javascript">
$(function(){
	pestpoints.init($("#pest-sheet-div"), {
		layers : '${layers}',
		tag : 'view',
		datas : $.parseJSON('${datas}'),
		results : $.parseJSON('${reqs}'),
		isAll : true
	});
})
</script>