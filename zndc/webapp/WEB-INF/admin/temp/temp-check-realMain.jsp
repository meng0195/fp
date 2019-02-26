<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%-- <a class="add"  style="color: #FFF;"  target="navTab" rel="tab-check-real-conf" href="${CTX_PATH}/check/temp/realConf?tid=main" title="实时数据配置"><span>检测配置</span></a> --%>
<input type="hidden" name="tid" value="${param.tid}"/>
<c:forEach var="house" items="${houses}">
	<a target="dialog" width="1000" height="650"  mask="true"  houseNo="${house.houseNo}"  rel="tab-temp-real-detail" 
		class="houseTest green" href="${CTX_PATH}/check/temp/showReal/${house.houseNo}?tid=${param.tid}">${house.houseName}</a>
</c:forEach>
