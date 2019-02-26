<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="./head.jsp"%>
<title>欢迎使用<fmt:bundle basename="static"><fmt:message key="app.title"/></fmt:bundle></title>
<link id="skinLink" rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/themes/default/style.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/css/core.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/css/print.css" media="print"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/css/common.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/css/sheet.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/ztree/css/zTreeStyle/zTreeStyle.min.css" />
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/css/ieHack.css" media="screen"/>
<![endif]-->
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/person/css/person.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/rank/css/rank.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/pest/css/pest.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/gas/css/gas.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/equip/css/equip.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/temp/css/temp.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/core/css/report.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="${CTX_PATH}/admin/plugin/camera/css/camera.css" media="screen"/>

<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.validate.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.bgiframe.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.livequery.min.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/plugin/combox/js/jquery.comboxtree.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/ztree/js/jquery.ztree.all-3.4.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/ztree/js/jquery.treextend.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/plugin/sort/jquery.event.drag-2.2-min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/sort/jquery.dragsort-0.5.1.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/sort/jquery.tablednd.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.combox.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.scrollCenter.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.core.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.util.date.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.util.number.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.validate.method.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.barDrag.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.drag.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.tree.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.accordion.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.theme.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.switchEnv.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.contextmenu.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.navTab.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.tab.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.resize.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.dialog.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.dialogDrag.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.sortDrag.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.cssTable.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.stable.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.taskBar.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.ajax.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.pagination.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.database.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.datepicker.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.effects.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.panel.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.checkbox.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.history.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.file.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.print.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.ui.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.alertMsg.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/core/js/dwz.regional.zh.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/core/js/common.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/plugin/echarts/js/echarts.min.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/check/js/check.js"></script>

<script type="text/javascript" src="${CTX_PATH}/dwr/engine.js"></script>
<script type="text/javascript" src="${CTX_PATH}/dwr/util.js"></script>

<script type="text/javascript" src="${CTX_PATH}/admin/plugin/pest/js/pestpoints.js"></script>
<script type="text/javascript" src="${CTX_PATH}/dwr/interface/JDate.js"></script>

<script type="text/javascript" src="${CTX_PATH}/dwr/util.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/temp/js/UnityObject2.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/equip/js/equip.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/camera/js/webVideoCtrl.js"></script>
<script type="text/javascript" src="${CTX_PATH}/admin/plugin/camera/js/demo.js"></script>
</head>
<body scroll="no">
<c:if test="${empty sessionScope._SYS_USER_}">
	<c:redirect url="/admin/logout"/>
</c:if>
<form id="loginAjaxUserForm" silent="true" onsubmit="return divSearch(this, 'header');" action="${CTX_PATH}/admin/login/after/user" method="post"></form>
<form id="loginAjaxMenuForm" silent="true" onsubmit="return divSearch(this, 'sidebar');" action="${CTX_PATH}/admin/login/after/menu" method="post"></form>
<div id="layout">
	<div id="header">
		<div class="headerNav">
			<a class="logo">标志</a>
			<ul class="nav">
				<li style="background:none"><a class="no-underline">您好，${sessionScope._SYS_USER_.loginName}（${sessionScope._SYS_USER_.name}）</a></li>
				<li><a class="no-underline" target="navTab" rel="tab-person-main" href="${CTX_PATH}/admin/person/to/main" title="个人中心">个人中心</a></li>
				<li><a class="no-underline" target="dialog" mask="true" width="500" height="170" href="${CTX_PATH}/admin/system/user/passwd/before">修改密码</a></li>
				<li><a class="no-underline" target="dialog" mask="true" width="580" height="350" href="${CTX_PATH}/admin/person/to/help">文件下载</a></li>
				<li><a class="no-underline" onclick="logout('${sessionScope._SYS_USER_.loginName}', '${CTX_PATH}/admin/logout');" style="cursor:pointer;">安全退出</a></li>
				<li><a class="no-underline" target="dialog" mask="true" width="440" height="170" href="${CTX_PATH}/admin/person/to/us">关于我们</a></li>
			</ul>
			<ul class="skin">
				<li class="green sel" code="default"></li>
				<li class="blue" code="blue"></li>
				<li class="gray" code="gray"></li>
				<li class="pink" code="pink"></li>
				<li class="golden" code="golden"></li>
				<li class="purple" code="purple"></li>
			</ul>
		</div>
	</div>
	<div id="leftside">
		<div id="sidebar_s">
			<div class="collapse">
				<div class="toggleCollapse"><div></div></div>
			</div>
		</div>
		<div id="sidebar">
			<div class="toggleCollapse"><h2><fmt:bundle basename="static"><fmt:message key="sys.menu.name"/></fmt:bundle></h2><div>收缩</div></div>
			<div class="accordion" fillSpace="sidebar">${sessionScope._SYS_USER_MENUS_}</div>
		</div>
	</div>
	<div id="container">
		<div id="navTab" class="tabsPage">
			<div class="tabsPageHeader">
				<div class="tabsPageHeaderContent">
					<ul class="navTab-tab">
						<li tabid="page_main" class="main" title="首页"><a href="javascript:;"><span><span class="home_icon">首页</span></span></a></li>
					</ul>
				</div>
				<div class="tabsLeft">left</div>
				<div class="tabsRight">right</div>
				<div class="tabsMore">more</div>
			</div>
			<ul class="tabsMoreList">
				<li><a href="javascript:;">首页</a></li>
			</ul>
			<div class="navTab-panel tabsPageContent layoutBox">
				<div id="mainContent" class="page unitBox">
					<div class="main-weather"></div>
					<div class="main-weather-body">
						<table class="edit">
							<tbody>
								<tr>
									<th width="25%">仓外温度</td>
									<td width="25%">${wea.temp }°C</td>
									<th width="25%">仓外湿度</td>
									<td width="25%">${wea.humidity }%</td>
								</tr>
								<tr>
									<th>风向</th>
									<td>${wea.direcStr }</td>
									<th>风速</th>
									<td>${wea.windSpeed }m/s</td>
								</tr>
								<tr>
									<th>气压</th>
									<td>${wea.kpa }kpa</td>
									<th>雨雪</th>
									<td>${wea.sleet == 0 ? "无雨雪" : "有雨雪" }</td>
								</tr>		
							</tbody>
						</table>
					</div>
					<div class="main-msg ${msg.waits == 0 ? 'main-msg-pic' : 'main-msg-pic1'}">${msg.waits}<a target="navTab" rel="page_004014" href="${CTX_PATH}/admin/msg/list" title="消息提醒"></a><span>${msg.waits}</span></div>
					<div class="main-msg-body">
						<ul>
							<c:forEach var="m" items="${msgs.result}" varStatus="st">
								<li>${map:get(codes['houses'], m.houseNo)}：${m.msg}</li>
							</c:forEach>
						</ul>
					</div>
					<div layoutH="2" class="houseMap">
						<c:forEach var="h" items="${list}">
							<a class="houseTest ${h.tag == 1 ? 'red' : 'green'}" houseNo="${h.houseNo}" target="dialog" rel="dialog-house-detail" width="1200" height="600" mask="true" href="${CTX_PATH}/squery/show/${h.houseNo}/0/0">${h.houseName}</a>
							<table class="mainHouseDetail">
								<tr>
									<th width="16%">仓房名称</th>
									<td width="16%">${h.houseName}</td>
									<th width="16%">仓房类型</th>
									<td width="16%">${h.typeName}</td>
									<th width="16%">设计仓容(t)</th>
									<td width="16%"><fmt:formatNumber value="${h.storeCount}" pattern="#0.0"/></td>
								</tr>
								<tr>
									<th>粮食品种</th>
									<td>${h.grainName}</td>
									<th>储粮性质</th>
									<td>${h.natureName}</td>
									<th>建筑年份</th>
									<td>${h.builtYear}</td>
								</tr>
								<tr>
									<th>现存数量(t)</th>
									<td>${h.grainCount}</td>
									<th>储粮等级</th>
									<td>${h.gradeName}</td>
									<th>生产年限</th>
									<td>${h.gainYear}</td>
								</tr>
								<tr>
									<th>入仓时间</th>
									<td><fmt:formatDate value="${h.dateOfIn}" pattern="yyyy-MM"/></td>
									<th>三防设备</th>
									<td>${h.proof3}</td>
									<th>仓容状态</th>
									<td></td>
								</tr>
								<tr>
									<th>保管员</th>
									<td>${h.keeperName}</td>
									<th>粮食产地</th>
									<td colspan="3">${h.origin}</td>
									
								</tr>
								<tr>
									<th style="text-align: center;" colspan="2">项</th>
									<th style="text-align: center;" colspan="2">检测时间</th>
									<th style="text-align: center;" colspan="2">是否报警</th>
								</tr>
								<tr style="text-align: center;" class="${h.tempTag == 1 ? 'orange' : ''}">
									<td colspan="2">温度</td>
									<td colspan="2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${h.tempTime}" /></td>
									<td colspan="2">${map:get(enums['TYPE_FLAG'], h.tempTag)}</td>
								</tr>
								<tr style="text-align: center;" class="${h.pestTag == 1 ? 'orange' : ''}">
									<td colspan="2">虫害</td>
									<td colspan="2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${h.pestTime}" /></td>
									<td colspan="2">${map:get(enums['TYPE_FLAG'], h.pestTag)}</td>
								</tr>
								<tr style="text-align: center;" class="${h.gasTag == 1 ? 'orange' : ''}">
									<td colspan="2">气体</td>
									<td colspan="2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${h.gasTime}" /></td>
									<td colspan="2">${map:get(enums['TYPE_FLAG'], h.gasTag)}</td>
								</tr>
								<tr style="text-align: center;" class="${h.equipTag != 0 ? 'orange' : ''}">
									<td colspan="2">设备</td>
									<td colspan="2">${h.equipTag}</td>
									<td colspan="2">${map:get(enums['TYPE_FLAG'], (h.equipTag == 0 ? 0 : 1))}</td>
								</tr>
							</table>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="footer">
	<span class="copyright"><fmt:bundle basename="static"><fmt:message key="app.copyright"/></fmt:bundle></span>
	<span class="version">版本：<fmt:bundle basename="static"><fmt:message key="app.version"/></fmt:bundle></span>
</div>
</body>
<script type="text/javascript">
dwr.engine.setActiveReverseAjax(true);
dwr.engine.setNotifyServerOnPageUnload(true);
var mapL = $(".houseMap").offset().left;
var mapT = $(".houseMap").offset().top;
var x1 = $(window).width();
var y1 = $(window).height();
$("#mainContent a.houseTest").hover(function(e){
	var $a = $(e.target);
	$a.next().show();
	//获取鼠标偏移量
	var x = e.pageX;
	var y = e.pageY;
	if(x1 - x > 500){
		$a.next().css({left:(e.pageX + 5 - mapL) + "px"});
	} else {
		$a.next().css({left:(e.pageX + 5 - mapL - 500) + "px"});
	}
	if(y1 - y > 300){
		$a.next().css({top:(e.pageY + 5 - mapT) + "px"});
	} else {
		$a.next().css({top:(e.pageY + 5 - mapT - 250) + "px"});
	}
	$a.mouseout(function(){
		$a.next().hide();
	})
})
$("div.main-msg").hover(function(){
	var $div = $("div.main-msg");
	$div.next().show();
	$div.mouseout(function(){
		$div.next().hide();
	})
})

$("div.main-weather").hover(function(){
	var $div = $("div.main-weather");
	$div.next().show();
	$div.mouseout(function(){
		$div.next().hide();
	})
})
$("ul.skin").click(function(e){
	if(e.target.tagName.toUpperCase() == "LI"){
		var $skin = $(e.target);
		var c = $skin.attr("code");
		$skin.siblings().removeClass("sel");
		$skin.addClass("sel");
		skinSetCookie("zndcSkinCookie", c);
		$("#skinLink").attr("href", "/zndc/admin/core/themes/" + c + "/style.css");
	}
})
</script>
</html>
