<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="./head.jsp"%>
<title><fmt:bundle basename="static"><fmt:message key="app.title"/></fmt:bundle></title>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/login/css/login.css"/>
</head>
<body>
<div id="login">
	<div id="login_header">
		<h1 class="login_logo">
			<a><img src="${CTX_PATH}/admin/login/pic/logo.png" /></a>
		</h1>
		<div class="login_headerContent">
			<div class="navList">
				<ul>
					<li><a style="cursor:pointer" onclick="setHome(this, window.location)">设为首页</a></li>
					<li><a style="cursor:pointer" onclick="addFavroite(window.location, document.title)">加入收藏</a></li>
				</ul>
			</div>
			<h2 class="login_title"><img src="${CTX_PATH}/admin/login/pic/logo_r.png" /></h2>
		</div>
	</div>
	<div id="login_content">
		<div class="loginForm">
			<div id="errorMessage" style="height: 30px;position: relative;top: 0;left: 55px;margin-top: -20px;color:rgb(243,107,116)">
				${errorMessage}
			</div>
			<form id="loginForm" action="${CTX_PATH}/admin/login/validate" method="post">
			<table>
				<tr>
					<td>登录名：</td><td><input id="loginName" name="loginName" size="20" autocomplete="off" style="width:150px;"/></td>
				</tr>
				<tr>
					<td>密&nbsp;&nbsp;&nbsp;码：</td><td><input type="password" id="loginPass" name="loginPass" size="20" autocomplete="off" style="width:150px;"/></td>
				</tr>
				<tr>
					<td>验证码：</td><td><input id="verifyCheckCode" type="text" style="width:50px"/>
					<input id="checkCode" class="checkCode" onclick="createCode()" readonly="true" style="cursor:pointer;border:0;width:50px;height:19px;"/>
					<a onclick="createCode();" style="cursor:pointer;" title="看不清，点击此链接换下一组验证码">看不清?</a>
				</tr>
			</table>
				<div class="login_bar">
					<input type="button" class="sub" onclick="javascript:validate();"/>
				</div>
			</form>
		</div>
		<div class="login_banner"><img src="${CTX_PATH}/admin/login/pic/banner.jpg" /></div>
	</div>
	<div id="login_footer"><br/><br/><br/><fmt:bundle basename="static"><fmt:message key="app.copyright"/></fmt:bundle></div>
</div>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/login/js/login.js"></script>
<script type="text/javascript">
	createCode();
	$('#loginName').focus();
	$(document).keypress(function(event){
		if(event.keyCode == 13) {
			return validate();
		}
	});
	$(document).on("contextmenu", function(e) {
		return false;
	});
	$(document).on("keydown", function(e) {
		var code = e.keyCode;
		//禁用退格键、Alt + <- 和 Alt + ->组合键
		if(code == 37 || code == 39) {
			return false;
		}
	});
</script>
</body>
</html>