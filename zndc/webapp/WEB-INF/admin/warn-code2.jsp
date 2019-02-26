<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="./head.jsp"%>
<title><fmt:bundle basename="static"><fmt:message key="app.title"/></fmt:bundle></title>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/login/css/login_la.css"/>
</head>
<body>
<div>
	<span>库识别码和库名称不匹配请联系良安科技技术支持获取识别码</span>
	<form id="md5Form" action="${CTX_PATH}/iface/md5" method="post">
	<input name="_name" />
	<input name="_code" />
	<a onclick="_submit()">提交</a>
	</form>
</div>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.min.js"></script>
<script type="text/javascript">
	function _submit(){
		$("#md5Form").submit();
	}
</script>
</body>
</html>