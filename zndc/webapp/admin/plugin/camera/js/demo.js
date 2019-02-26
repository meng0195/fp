// 初始化插件

// 全局保存当前选中窗口
var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口

// 显示操作信息
function showOPInfo(szInfo) {
	szInfo = "<div>" + szInfo + "</div>";
	$("#opinfo").html(szInfo + $("#opinfo").html());
}

// 显示回调信息
function showCBInfo(szInfo) {
	szInfo = "<div>" + dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss") + " " + szInfo + "</div>";
	$("#cbinfo").html(szInfo + $("#cbinfo").html());
}

// 获取窗口尺寸
function getWindowSize() {
	var nWidth = $(this).width() + $(this).scrollLeft(),
		nHeight = $(this).height() + $(this).scrollTop();

	return {width: nWidth, height: nHeight};
}

// 打开选择框 0：文件夹  1：文件
function clickOpenFileDlg(id, iType) {
	var szDirPath = WebVideoCtrl.I_OpenFileDlg(iType);
	
	if (szDirPath != -1 && szDirPath != "" && szDirPath != null) {
		$("#" + id).val(szDirPath);
	}
}

// 获取本地参数
function clickGetLocalCfg() {
	var xmlDoc = WebVideoCtrl.I_GetLocalCfg();

	$("#netsPreach").val($(xmlDoc).find("BuffNumberType").eq(0).text());
	$("#wndSize").val($(xmlDoc).find("PlayWndType").eq(0).text());
	$("#rulesInfo").val($(xmlDoc).find("IVSMode").eq(0).text());
	$("#captureFileFormat").val($(xmlDoc).find("CaptureFileFormat").eq(0).text());
	$("#packSize").val($(xmlDoc).find("PackgeSize").eq(0).text());
	$("#recordPath").val($(xmlDoc).find("RecordPath").eq(0).text());
	$("#downloadPath").val($(xmlDoc).find("DownloadPath").eq(0).text());
	$("#previewPicPath").val($(xmlDoc).find("CapturePath").eq(0).text());
	$("#playbackPicPath").val($(xmlDoc).find("PlaybackPicPath").eq(0).text());
	$("#playbackFilePath").val($(xmlDoc).find("PlaybackFilePath").eq(0).text());
    $("#protocolType").val($(xmlDoc).find("ProtocolType").eq(0).text());

	showOPInfo("本地配置获取成功！");
}

// 设置本地参数
function clickSetLocalCfg() {
	var arrXml = [],
		szInfo = "";
	
	arrXml.push("<LocalConfigInfo>");
	arrXml.push("<PackgeSize>" + $("#packSize").val() + "</PackgeSize>");
	arrXml.push("<PlayWndType>" + $("#wndSize").val() + "</PlayWndType>");
	arrXml.push("<BuffNumberType>" + $("#netsPreach").val() + "</BuffNumberType>");
	arrXml.push("<RecordPath>" + $("#recordPath").val() + "</RecordPath>");
	arrXml.push("<CapturePath>" + $("#previewPicPath").val() + "</CapturePath>");
	arrXml.push("<PlaybackFilePath>" + $("#playbackFilePath").val() + "</PlaybackFilePath>");
	arrXml.push("<PlaybackPicPath>" + $("#playbackPicPath").val() + "</PlaybackPicPath>");
	arrXml.push("<DownloadPath>" + $("#downloadPath").val() + "</DownloadPath>");
	arrXml.push("<IVSMode>" + $("#rulesInfo").val() + "</IVSMode>");
	arrXml.push("<CaptureFileFormat>" + $("#captureFileFormat").val() + "</CaptureFileFormat>");
    arrXml.push("<ProtocolType>" + $("#protocolType").val() + "</ProtocolType>");
	arrXml.push("</LocalConfigInfo>");

	var iRet = WebVideoCtrl.I_SetLocalCfg(arrXml.join(""));

	if (0 == iRet) {
		szInfo = "本地配置设置成功！";
	} else {
		szInfo = "本地配置设置失败！";
	}
	showOPInfo(szInfo);
}

// 窗口分割数
function changeWndNum(iType) {
//	iType = parseInt(iType, 10);
	
}

// 登录
function clickLogin() {
	WebVideoCtrl.I_ChangeWndNum(1);
	var szIP = $("#loginip").val(),
		szPort = $("#port").val(),
		szUsername = $("#username").val(),
		szPassword = $("#password").val();
	
	if ("" == szIP || "" == szPort) {
		return;
	}

	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {
			showOPInfo("登录成功！");
			//yucy自动加载点击预览方法
			clickStartRealPlay()
			$("#ip").prepend("<option value='" + szIP + "'>" + szIP + "</option>");
			setTimeout(function () {
				$("#ip").val(szIP);
				getChannelInfo();
			}, 10);
		},
		error: function () {
			showOPInfo("登录失败！");
		}
	});

	if (-1 == iRet) {
		//yucy自动加载点击预览方法
		clickStartRealPlay()
		$("#ip").prepend("<option value='" + szIP + "'>" + szIP + "</option>");
		setTimeout(function () {
			$("#ip").val(szIP);
			getChannelInfo();
		}, 10);
	}
}

// 退出
function clickLogout() {
	var szIP = $("#ip").val(),
		szInfo = "";

	if (szIP == "") {
		return;
	}

	var iRet = WebVideoCtrl.I_Logout(szIP);
	if (0 == iRet) {
		szInfo = "退出成功！";

		$("#ip option[value='" + szIP + "']").remove();
		getChannelInfo();
	} else {
		szInfo = "退出失败！";
	}
	showOPInfo(szInfo);
}

// 获取设备信息
function clickGetDeviceInfo() {
	var szIP = $("#ip").val();

	if ("" == szIP) {
		return;
	}

	WebVideoCtrl.I_GetDeviceInfo(szIP, {
		success: function (xmlDoc) {
			var arrStr = [];
			arrStr.push("设备名称：" + $(xmlDoc).find("deviceName").eq(0).text() + "\r\n");
			arrStr.push("设备ID：" + $(xmlDoc).find("deviceID").eq(0).text() + "\r\n");
			arrStr.push("型号：" + $(xmlDoc).find("model").eq(0).text() + "\r\n");
			arrStr.push("设备序列号：" + $(xmlDoc).find("serialNumber").eq(0).text() + "\r\n");
			arrStr.push("MAC地址：" + $(xmlDoc).find("macAddress").eq(0).text() + "\r\n");
			arrStr.push("主控版本：" + $(xmlDoc).find("firmwareVersion").eq(0).text() + " " + $(xmlDoc).find("firmwareReleasedDate").eq(0).text() + "\r\n");
			arrStr.push("编码版本：" + $(xmlDoc).find("encoderVersion").eq(0).text() + " " + $(xmlDoc).find("encoderReleasedDate").eq(0).text() + "\r\n");
			
			showOPInfo(szIP + " 获取设备信息成功！");
			alert(arrStr.join(""));
		},
		error: function () {
			showOPInfo(szIP + " 获取设备信息失败！");
		}
	});
}

// 获取通道
function getChannelInfo() {
	var szIP = $("#ip").val(),
		oSel = $("#channels").empty();

	if ("" == szIP) {
		return;
	}

	// 模拟通道
	WebVideoCtrl.I_GetAnalogChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("VideoInputChannel");

			$.each(oChannels, function (i) {
				var id = $(this).find("id").eq(0).text(),
					name = $(this).find("name").eq(0).text();
				if ("" == name) {
					name = "Camera " + (i < 9 ? "0" + (i + 1) : (i + 1));
				}
				oSel.append("<option value='" + id + "' bZero='false'>" + name + "</option>");
			});
			showOPInfo(szIP + " 获取模拟通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 获取模拟通道失败！");
		}
	});
	// 数字通道
	WebVideoCtrl.I_GetDigitalChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("InputProxyChannelStatus");

			$.each(oChannels, function (i) {
				var id = $(this).find("id").eq(0).text(),
					name = $(this).find("name").eq(0).text(),
					online = $(this).find("online").eq(0).text();
				if ("false" == online) {// 过滤禁用的数字通道
					//return true;
				}
				if ("" == name) {
					name = "IPCamera " + (i < 9 ? "0" + (i + 1) : (i + 1));
				}
				oSel.append("<option value='" + id + "' bZero='false'>" + name + "</option>");
			});
			showOPInfo(szIP + " 获取数字通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 获取数字通道失败！");
		}
	});
	// 零通道
	WebVideoCtrl.I_GetZeroChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("ZeroVideoChannel");
			
			$.each(oChannels, function (i) {
				var id = $(this).find("id").eq(0).text(),
					name = $(this).find("name").eq(0).text();
				if ("" == name) {
					name = "Zero Channel " + (i < 9 ? "0" + (i + 1) : (i + 1));
				}
				if ("true" == $(this).find("enabled").eq(0).text()) {// 过滤禁用的零通道
					oSel.append("<option value='" + id + "' bZero='true'>" + name + "</option>");
				}
			});
			showOPInfo(szIP + " 获取零通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 获取零通道失败！");
		}
	});
}

// 获取数字通道
function clickGetDigitalChannelInfo() {
	var szIP = $("#ip").val(),
		iAnalogChannelNum = 0;

	$("#digitalchannellist").empty();

	if ("" == szIP) {
		return;
	}

	// 模拟通道
	WebVideoCtrl.I_GetAnalogChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			iAnalogChannelNum = $(xmlDoc).find("VideoInputChannel").length;
		},
		error: function () {
			
		}
	});

	// 数字通道
	WebVideoCtrl.I_GetDigitalChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("InputProxyChannelStatus");
			
			$.each(oChannels, function () {
				var id = parseInt($(this).find("id").eq(0).text(), 10),
					ipAddress = $(this).find("ipAddress").eq(0).text(),
					srcInputPort = $(this).find("srcInputPort").eq(0).text(),
					managePortNo = $(this).find("managePortNo").eq(0).text(),
					online = $(this).find("online").eq(0).text(),
					proxyProtocol = $(this).find("proxyProtocol").eq(0).text();
							
				var objTr = $("#digitalchannellist").get(0).insertRow(-1);
				var objTd = objTr.insertCell(0);
				objTd.innerHTML = (id - iAnalogChannelNum) < 10 ? "D0" + (id - iAnalogChannelNum) : "D" + (id - iAnalogChannelNum);
				objTd = objTr.insertCell(1);
				objTd.width = "25%";
				objTd.innerHTML = ipAddress;
				objTd = objTr.insertCell(2);
				objTd.width = "15%";
				objTd.innerHTML = srcInputPort;
				objTd = objTr.insertCell(3);
				objTd.width = "20%";
				objTd.innerHTML = managePortNo;
				objTd = objTr.insertCell(4);
				objTd.width = "15%";
				objTd.innerHTML = "true" == online ? "在线" : "离线";
				objTd = objTr.insertCell(5);
				objTd.width = "25%";
				objTd.innerHTML = proxyProtocol;
			});
			showOPInfo(szIP + " 获取数字通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 没有数字通道！");
		}
	});
}

// 开始预览
function clickStartRealPlay() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		szIP = $("#loginip").val(),
		iStreamType = 1,//码流类型
		iChannelID = 1,//模拟通道号
		bZeroChannel = false,
		szInfo = "";

	if ("" == szIP) {
		return;
	}

	if (oWndInfo != null) {// 已经在播放了，先停止
		WebVideoCtrl.I_Stop();
	}

	var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
		iStreamType: iStreamType,
		iChannelID: iChannelID,
		bZeroChannel: bZeroChannel
	});

	if (0 == iRet) {
		szInfo = "开始预览成功！";
	} else {
		szInfo = "开始预览失败！";
	}

	showOPInfo(szInfo);
}

// 停止预览
function clickStopRealPlay() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		szInfo = "";

	if (oWndInfo != null) {
		var iRet = WebVideoCtrl.I_Stop();
		if (0 == iRet) {
			szInfo = "停止预览成功！";
		} else {
			szInfo = "停止预览失败！";
		}
		showOPInfo(szInfo);
	}
}

// 全屏
function clickFullScreen() {
	WebVideoCtrl.I_FullScreen(true);
}

// PTZ控制 9为自动，1,2,3,4,5,6,7,8为方向PTZ
var g_bPTZAuto = false;
function mouseDownPTZControl(iPTZIndex) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		bZeroChannel = false,
		iPTZSpeed = $("#ptzspeed").val();

	if (bZeroChannel) {// 零通道不支持云台
		return;
	}
	
	if (oWndInfo != null) {
		if (9 == iPTZIndex && g_bPTZAuto) {
			iPTZSpeed = 0;// 自动开启后，速度置为0可以关闭自动
		} else {
			g_bPTZAuto = false;// 点击其他方向，自动肯定会被关闭
		}

		WebVideoCtrl.I_PTZControl(iPTZIndex, false, {
			iPTZSpeed: iPTZSpeed,
			success: function (xmlDoc) {
				if (9 == iPTZIndex) {
					g_bPTZAuto = !g_bPTZAuto;
				}
				showOPInfo("开启云台成功！");
			},
			error: function () {
				showOPInfo("开启云台失败！");
			}
		});
	}
}

// 方向PTZ停止
function mouseUpPTZControl() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

	if (oWndInfo != null) {
		WebVideoCtrl.I_PTZControl(1, true, {
			success: function (xmlDoc) {
				showOPInfo("停止云台成功！");
			},
			error: function () {
				showOPInfo("停止云台失败！");
			}
		});
	}
}

// 设置预置点
function clickSetPreset() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iPresetID = parseInt($("#preset").val(), 10);

	if (oWndInfo != null) {
		WebVideoCtrl.I_SetPreset(iPresetID, {
			success: function (xmlDoc) {
				showOPInfo("设置预置点成功！");
			},
			error: function () {
				showOPInfo("设置预置点失败！");
			}
		});
	}
}

// 调用预置点
function clickGoPreset() {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iPresetID = parseInt($("#preset").val(), 10);

	if (oWndInfo != null) {
		WebVideoCtrl.I_GoPreset(iPresetID, {
			success: function (xmlDoc) {
				showOPInfo("调用预置点成功！");
			},
			error: function () {
				showOPInfo("调用预置点失败！");
			}
		});
	}
}



// 检查插件版本
function clickCheckPluginVersion() {
	var iRet = WebVideoCtrl.I_CheckPluginVersion();
	if (0 == iRet) {
		alert("您的插件版本已经是最新的！");
	} else {
		alert("检测到新的插件版本！");
	}
}
function PTZZoomIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(10, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("调焦+成功！");
            },
            error: function () {
                showOPInfo("调焦+失败！");
            }
        });
    }
}

function PTZZoomout() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(11, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("调焦-成功！");
            },
            error: function () {
                showOPInfo("调焦-失败！");
            }
        });
    }
}

function PTZZoomStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(11, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("调焦停止成功！");
            },
            error: function () {
                showOPInfo("调焦停止失败！");
            }
        });
    }
}

function PTZFocusIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(12, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("聚焦+成功！");
            },
            error: function () {
                showOPInfo("聚焦+失败！");
            }
        });
    }
}

function PTZFoucusOut() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(13, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("聚焦-成功！");
            },
            error: function () {
                showOPInfo("聚焦-失败！");
            }
        });
    }
}

function PTZFoucusStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(12, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("聚焦停止成功！");
            },
            error: function () {
                showOPInfo("聚焦停止失败！");
            }
        });
    }
}

function PTZIrisIn() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(14, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("光圈+成功！");
            },
            error: function () {
                showOPInfo("光圈+失败！");
            }
        });
    }
}

function PTZIrisOut() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(15, false, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("光圈-成功！");
            },
            error: function () {
                showOPInfo("光圈-失败！");
            }
        });
    }
}

function PTZIrisStop() {
    var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);

    if (oWndInfo != null) {
        WebVideoCtrl.I_PTZControl(14, true, {
            iWndIndex: g_iWndIndex,
            success: function (xmlDoc) {
                showOPInfo("光圈停止成功！");
            },
            error: function () {
                showOPInfo("光圈停止失败！");
            }
        });
    }
}

dateFormat = function (oDate, fmt) {
    var o = {
        "M+": oDate.getMonth() + 1, //月份
        "d+": oDate.getDate(), //日
        "h+": oDate.getHours(), //小时
        "m+": oDate.getMinutes(), //分
        "s+": oDate.getSeconds(), //秒
        "q+": Math.floor((oDate.getMonth() + 3) / 3), //季度
        "S": oDate.getMilliseconds()//毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (oDate.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if(new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};

// 切换模式
function changeIPMode(iType) {
	var arrPort = [0, 7071, 80];

	$("#serverport").val(arrPort[iType]);
}

// 获取设备IP
function clickGetDeviceIP() {
	var iDeviceMode = parseInt($("#devicemode").val(), 10),
		szAddress = $("#serveraddress").val(),
		iPort = parseInt($("#serverport").val(), 10) || 0,
		szDeviceID = $("#deviceid").val(),
		szDeviceInfo = "";

	szDeviceInfo = WebVideoCtrl.I_GetIPInfoByMode(iDeviceMode, szAddress, iPort, szDeviceID);

	if ("" == szDeviceInfo) {
		showOPInfo("设备IP和端口解析失败！");
	} else {
		showOPInfo("设备IP和端口解析成功！");

		var arrTemp = szDeviceInfo.split("-");
		$("#loginip").val(arrTemp[0]);
		$("#deviceport").val(arrTemp[1]);
	}
}