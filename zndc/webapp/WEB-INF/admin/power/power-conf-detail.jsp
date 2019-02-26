<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<table class="edit">
	<tbody>
		<tr>
			<th colspan="4" class="title1">电表信息</th>
		</tr>
		<tr>
			<th width="15%">仓房名称</th>
			<td width="35%">${map:get(codes['houses'],p.houseNo)}</td>
			<th width="15%">模式</th>
			<td width="35%">${map:get(enums['TYPE_BOARD'],p.boardType)}</td>
		</tr>
		<tr>
			<th>模块IP</th>
			<td>${p.powerIp}</td>
			<th>模块端口</th>
			<td>${p.powerPort}</td>
		</tr>
		<tr>
			<th>寄存器位</th>
			<td colspan=3>${p.regAdd}</td>
		</tr>
		
	</tbody>
</table>