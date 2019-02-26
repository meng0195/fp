/*
var code; //在全局 定义验证码  
function createCode() {
	code = "";
	var codeLength = 4;//验证码的长度  
	var checkCode = document.getElementById("checkCode");
	var selectChar = new Array(2, 3, 4, 5, 6, 7, 8, 9, "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");//所有候选组成验证码的字符，当然也可以用中文的  
	for (var i = 0; i < codeLength; i++) {
		var charIndex = Math.floor(Math.random() * 31);
		code += selectChar[charIndex];
	}
	if (checkCode) {
		checkCode.className = "checkCode";
		checkCode.value = code;
	}
	checkCode.focus();
}*/

function validate() {
	var msgsContentObj = document.getElementById("errorMessage");
	var username = document.getElementById("loginName");
	if (!username || username.value.length <= 0) {
		msgsContentObj.innerHTML = "请输入您的登录名！";
		username.focus();
		return false;
	}
	var password = document.getElementById("loginPass");
	if (!password || password.value.length <= 0) {
		msgsContentObj.innerHTML = "请输入您的密码！";
		password.focus();
		return false;
	}
	/*
	var verifycode = document.getElementById("verifyCheckCode");
	var vCodeVal = verifycode.value;
	if (!vCodeVal || vCodeVal.length <= 0) {
		msgsContentObj.innerHTML = "请输入验证码！";
		verifycode.focus();
		return false;
	} else {
		if (vCodeVal.toLowerCase() != code.toLowerCase()) {
			msgsContentObj.innerHTML = "验证码输入错误，请重新输入！";
			createCode();
			verifycode.focus();
			return false;
		}
	}*/
	$("#loginForm").submit();
	return true;
}

function addFavroite(url, title) {
	try {
		window.external.addFavorite(url, title);
	} catch(e) {
		try {
			window.sidebar.addPanel(title, url);
		} catch(e) {
			alert("抱歉，加入收藏失败，请使用Ctrl + D进行添加。");
		}
	}
}

/*
function setHome(obj, url) {
	try {
		obj.style.behavior='url(#default#homepage)';
		obj.setHomePage(url);
	} catch(e) {
		if(window.netscape) {
			try {
				window.netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
			} catch(e) {
				alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入‘about:config’并回车\n然后将[signed.applets.codebase_principal_support]的值设置为‘true’，双击即可。");
			}
			var prefs = Components.classes['@mozilla.org/preferences-service;'].getService(Components.interfaces.nslPrefBranch);
			prefs.setCharPref('browser.startup.homepage',url);
		} else {
			alert("抱歉，您的浏览器不支持此项操作。");
		}
	}
}*/