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
<div class="page-box login-page-box">
	<div class="page-main">
		<div class="header-bg">
		    <div class="header">
		        <div class="header-left">
		        	<div class="logo"></div>
		            <div class="header-line"></div>
		            <div class="header-t1"></div>
		        </div>
		    </div>
		</div>
		<div class="login-body">
			<div class="login-main">
		    	<div class="login-img"></div>
		    	<div class="login-content">
		        	<h2 class="login-tit">登录</h2>
		            <div class="login-form-box">
		            	<div id="errorMessage" style="height: 30px;position: relative;top: 0;left: 0;font-size:21px;margin-top: -20px;color:rgb(243,107,116)">
							${errorMessage}
						</div>
		            	<form id="loginForm" action="${CTX_PATH}/admin/login/validate" method="post">
		            		<div class="login-user-box"><i class="yonghm"></i><input type="text" id="loginName" placeholder="请输入用户名" name="loginName" /></div>
			                <div class="login-user-box"><i class="mim"></i><input type="password" id="loginPass" placeholder="密码" name="loginPass" /><span id="loginPwd" class="login-pwd-open"></span></div>
							<input type="button" class="login-btn" onclick="javascript:validate();" />
						</form>
						<a id="myAddress" href="index/index.html" target="_self" style="display:none;"></a>
		   				<div id="login_label" style="font-size: 12px;color: red;width: 350px;margin-top:10px;"></div>     
		            </div>
		        </div>
		    </div>
		</div>
	</div>
	<div class="footer">
		<p><fmt:bundle basename="static"><fmt:message key="app.copyright"/></fmt:bundle></p>
	</div>
</div>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/login/js/login.js"></script>
<script type="text/javascript">
	//createCode();
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
	
	$("#loginPwd").on("click", function(){
		if($("#loginPwd").attr("class") == "login-pwd-open"){
			$("#loginPwd").attr("class","login-pwd-up");
			//$("#loginPass").attr("type","text");
			//猜测是该版本jQuery不允许修改控件type属性，使用原生js语句可以解决这个问题。
			$("#loginPass")[0].type = "text";
		} else {
			$("#loginPwd").attr("class","login-pwd-open");
			$("#loginPass")[0].type = "password";
		}
	});
	
</script>
</body>
</html>