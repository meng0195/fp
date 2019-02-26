<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
table{display:block;width:700px;height:250px;margin:15px auto;background:#e8edf2}
table th{width:95px;height:50px;line-height:50px;}
table input{width:550px;height:33px;line-height:33px;border-radius:5px;padding-left:10px;color:red;font-size:18px;font-weight:bold;}
#button{padding:3px 10px;border:3px solid #7F9DB9;width:60px;height:25px;line-height:25px;margin:0 auto;border-radius:5px;background:#7F9DB9;cursor:pointer;}
</style>
</head>
<body>
<form id="md5Form" action="${CTX_PATH}/iface/md5" method="post">
<table>
	<tr>
		<th colspan="2">库识别码和库名称不匹配请联系良安科技技术支持获取识别码</th>
	</tr>
	<tr>
		<th>库名称</th>
		<td><input name="_name" /></td>
	</tr>
	<tr>
		<th>库识别码</th>
		<td><input name="_code" /></td>
	</tr>
	<tr>
		<th colspan="2"><div id="button">提交</div></th>
	</tr>
</table>
</form>
</body>
<script type="text/javascript" src="${CTX_PATH}/admin/core/js/jquery.min.js"></script>
<script type="text/javascript">
$("#button").click(function(){
	$("#md5Form").submit();
})
</script>
</html>
