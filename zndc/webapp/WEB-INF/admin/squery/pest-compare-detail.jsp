<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<div width="100%" layoutH="30" id="squery-pest-compare"></div>
<script type="text/javascript">
$(function(){
	var json = $.parseJSON('${json}');
	if(json){
		json.tag = "diff";
		pestpoints.init($("#squery-pest-compare"), json);
	}
})
</script>