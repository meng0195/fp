<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="edit">
	<tbody>
		<tr>
			<th colspan="4" class="title1">摄像头信息</th>
		</tr>
		<tr>
			<th width="15%">设备名称</th>
			<td width="35%">${c.camName}</td>
			<th width="15%">设备IP</th>
			<td width="35%">${c.camIP}</td>
		</tr>
		<tr>
			<th>设备端口</th>
			<td>${c.camPort}</td>
			<th>通道号</th>
			<td>${c.channelsNum}</td>
		</tr>
		<tr>
			<th>登陆用户名</th>
			<td>${c.userName}</td>
			<th>登陆密码</th>
			<td>${c.password}</td>
		</tr>
		<tr>
			<th>设备类型</th>
			<td>${map:get(codes['TYPE_CAM'], c.type)}</td>
			<th>厂商</th>
			<td>${map:get(codes['TYPE_VENDOR'], c.vendor)}</td>
		</tr>
	</tbody>
</table>