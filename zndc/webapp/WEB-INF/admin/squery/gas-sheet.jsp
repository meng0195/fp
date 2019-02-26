<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<%@ taglib prefix="hex" uri="http://www.bjggs.com.cn/jstl/hex"%>
<div id="gasRankMap" layoutH="30">
	<c:forEach var="a" begin="0" end="${wayNumb - 1}" varStatus="st">
		<div class="gasWayRanking" style="left:${empty wayXaxis ? 0 : wayXaxis[st.index]}px;top:${empty wayYaxis ? 0 : wayYaxis[st.index]}px;" >
			<span class="fenglu">风路${st.index + 1}</span>
			<span class="v-co2">C:${hex:byte2Int(gset.gasSet[st.index * 6], gset.gasSet[st.index * 6 + 1])}</span>
			<span class="pic"></span>
			<span class="v-ph3">P:${hex:byte2Int(gset.gasSet[st.index * 6 + 4], gset.gasSet[st.index * 6 + 5])}</span>
			<span class="v-o2">O:${hex:byte2Double(gset.gasSet[st.index * 6 + 2], gset.gasSet[st.index * 6 + 3])}</span>
		</div>
	</c:forEach>
</div>
<script type="text/javascript">
</script>