<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" id="loginip" value="${e.camIP}"/>
<input type="hidden" id="port" value="${e.camPort}"/>
<input type="hidden" id="username" value="${e.userName}"/>
<input type="hidden" id="password" value="${e.password}"/>
<div class="left">
	<div id="divPlugin" class="plugin"></div>
</div>
<div class="left">
	<fieldset class="ptz">
		<legend>云台控制</legend>
		<table class="tableCam">
			<tr>
				<td>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(5);" onmouseup="mouseUpPTZControl();">左上</a>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(1);" onmouseup="mouseUpPTZControl();">上</a>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(7);" onmouseup="mouseUpPTZControl();">右上</a>
				</td>
			</tr>
			<tr>
				<td>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(3);" onmouseup="mouseUpPTZControl();">左</a>
					<a class="cam-font-green" onclick="mouseDownPTZControl(9);">自动</a>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(4);" onmouseup="mouseUpPTZControl();">右</a>
				</td>
			</tr>
			<tr>
				<td>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(6);" onmouseup="mouseUpPTZControl();">左下</a>
					<a class="cam-font-green"  onmousedown="mouseDownPTZControl(2);" onmouseup="mouseUpPTZControl();">下</a>
					<a class="cam-font-green" onmousedown="mouseDownPTZControl(8);" onmouseup="mouseUpPTZControl();">右下</a>
				</td>
			</tr>
		</table>
		
        <table class="tableCam">
            <tr>
                <td><a class="cam-font-green1" onmousedown="PTZZoomIn()" onmouseup="PTZZoomStop()">变倍+</a></td>
                <td><a class="cam-font-green1" onmousedown="PTZZoomout()" onmouseup="PTZZoomStop()">变倍-</a></td>
            </tr>
            <tr>
                <td><a class="cam-font-green1" onmousedown="PTZFocusIn()" onmouseup="PTZFoucusStop()">变焦+</a></td>
                <td><a class="cam-font-green1" onmousedown="PTZFoucusOut()" onmouseup="PTZFoucusStop()">变焦-</a></td>
            </tr>
            <tr>
            	<td><a class="cam-font-green1" onmousedown="PTZIrisIn()" onmouseup="PTZIrisStop()">光圈+</a></td>
            	<td><a class="cam-font-green1" onmousedown="PTZIrisOut()" onmouseup="PTZIrisStop()">光圈-</a></td>
            </tr>
        </table>
        <table class="tableCam">
			<tr>
				<td class="cam-td">云台速度</td>
				<td class="cam-td">
					<select id="ptzspeed" class="sel">
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option selected>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
					</select>
				</td>
			</tr>
			<tr>
				<td style="width: 70px;height: 20px;margin: 3px;">预置点号</td>
				<td style="width: 70px;height: 20px;margin: 3px;"><input id="preset" type="text" style="width: 70px;height: 20px" value="1" /></td>
			</tr>
			<tr>
				<td colspan="2">
					<a class="cam-font-green1" onclick="clickSetPreset();">设置</a>
					<a class="cam-font-green1" onclick="clickGoPreset();">调用</a>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset class="operate">
		<legend>操作信息</legend>
		<div id="opinfo" class="opinfo"></div>
	</fieldset>
</div>

<script type="text/javascript" >
$(function () {
	// 检查插件是否已经安装过
    var iRet = WebVideoCtrl.I_CheckPluginInstall();
	if (-2 == iRet) {
		alert("您的Chrome浏览器版本过高，不支持NPAPI插件！");
		return;
	} else if (-1 == iRet) {
        alert("您还未安装过插件，双击开发包目录里的WebComponentsKit.exe安装！");
		return;
    }
	// 初始化插件参数及插入插件
	WebVideoCtrl.I_InitPlugin(980, 550, {
        bWndFull: true,//是否支持单窗口双击全屏，默认支持 true:支持 false:不支持
        iWndowType: 2,
		cbSelWnd: function (xmlDoc) {
			g_iWndIndex = $(xmlDoc).find("SelectWnd").eq(0).text();
			var szInfo = "当前选择的窗口编号：" + g_iWndIndex;
			showCBInfo(szInfo);
		}
	});
	WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");

	// 检查插件是否最新
	if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
		alert("检测到新的插件版本，双击开发包目录里的WebComponentsKit.exe升级！");
		return;
	}

	// 窗口事件绑定
	$(window).bind({
		resize: function () {
			var $Restart = $("#restartDiv");
			if ($Restart.length > 0) {
				var oSize = getWindowSize();
				$Restart.css({
					width: oSize.width + "px",
					height: oSize.height + "px"
				});
			}
		}
	});

    //初始化日期时间
    var szCurTime = dateFormat(new Date(), "yyyy-MM-dd");
    $("#starttime").val(szCurTime + " 00:00:00");
    $("#endtime").val(szCurTime + " 23:59:59");
    //初始化登陆
    clickLogin();
});
</script>
