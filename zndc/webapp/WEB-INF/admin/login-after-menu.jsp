<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="toggleCollapse"><h2><fmt:bundle basename="static"><fmt:message key="sys.menu.name"/></fmt:bundle></h2><div>收缩</div></div>
<div class="accordion" fillSpace="sidebar">${sessionScope._SYS_USER_MENUS_}</div>