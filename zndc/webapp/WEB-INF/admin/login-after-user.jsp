<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="headerNav">
	<a class="logo">标志</a>
	<ul class="nav">
		<li style="background:none"><a class="no-underline">您好，${sessionScope._SYS_USER_.loginName}（${sessionScope._SYS_USER_.name}）</a></li>
		<li><a class="no-underline" target="dialog" mask="true" width="500" height="160" href="${pageContext.request.contextPath}/admin/system/user/passwd/before">修改密码</a></li>
		<li><a class="no-underline" onclick="logout('${sessionScope._SYS_USER_.loginName}', '${pageContext.request.contextPath}/admin/logout');" style="cursor:pointer;">退出</a></li>
	</ul>
</div>