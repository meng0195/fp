<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="helpDocForm" method="post">
	<span>帮助文档</span>
	<a target="form" rel="helpDocForm" title="确定要下载文档么？" href="${CTX_PATH}/admin/person/help/0">智能粮仓管理系统实施手册</a>
	<a target="form" rel="helpDocForm" title="确定要下载文档么？" href="${CTX_PATH}/admin/person/help/2">智能粮仓管理系统配置说明手册</a>
	<a target="form" rel="helpDocForm" title="确定要下载文档么？" href="${CTX_PATH}/admin/person/help/1">智能粮仓管理系统用户操作使用说明书</a>
	<span>插件下载</span>
	<a target="form" rel="helpDocForm" title="确定要下载插件么？" href="${CTX_PATH}/admin/person/help/3">兼容版浏览器下载</a>
	<a target="form" rel="helpDocForm" title="确定要下载插件么？" href="${CTX_PATH}/admin/person/help/4">摄像头插件下载</a>
	<a target="form" rel="helpDocForm" title="确定要下载插件么？" href="${CTX_PATH}/admin/person/help/5">3D漫游插件下载</a>
	
</form>