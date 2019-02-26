<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="map" uri="http://www.bjggs.com.cn/jstl/map"%>
<form id="saveConfWarnsForm" action="${CTX_PATH}/conf/warn/save" method="post"
	onsubmit="return validateCallback(this, navTabAjaxDone);" class="pageForm required-validate">
	<input type="hidden" name="tid" value="${param.tid}"/>
	<input type="hidden" name="a.houseNo" value="${houseNo}"/>
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"><span submitForm="saveConfWarnsForm">保存</span></a></li>
		</ul>
	</div>
	<table class="edit" layoutH="50">
		<tbody>
			<tr>
				<th width="25%">归属模块</th>
				<th width="25%">预警名称</th>
				<th width="25%">是否开启</th>
				<th width="25%">预警阀值</th>
			</tr>
			<tr>
				<td><input name="a.type" value="0" type="hidden"/>测温</td>
				<td><input name="a.type1" value="1" type="hidden"/>温度高限</td>
				<td>
					<input type="radio" name="a.alarmTag0" value="1" ${list[0].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag0" value="0" ${list[0].alarmTag == 0 || empty list[0].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[0].threshold}" min="-20" max="50"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="0" type="hidden"/>测温</td>
				<td><input name="a.type1" value="2" type="hidden"/>温度升高率</td>
				<td>
					<input type="radio" name="a.alarmTag1" value="1" ${list[1].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag1" value="0" ${list[1].alarmTag == 0 || empty list[1].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[1].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="0" type="hidden"/>测温</td>
				<td><input name="a.type1" value="3" type="hidden"/>温度异常点</td>
				<td>
					<input type="radio" name="a.alarmTag2" value="1" ${list[2].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag2" value="0" ${list[2].alarmTag == 0 || empty list[2].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[2].threshold}" class="digits"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="0" type="hidden"/>测温</td>
				<td><input name="a.type1" value="4" type="hidden"/>层均温</td>
				<td>
					<input type="radio" name="a.alarmTag3" value="1" ${list[3].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag3" value="0" ${list[3].alarmTag == 0 || empty list[3].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[3].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="0" type="hidden"/>测温</td>
				<td><input name="a.type1" value="5" type="hidden"/>缺点率</td>
				<td>
					<input type="radio" name="a.alarmTag4" value="1" ${list[4].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag4" value="0" ${list[4].alarmTag == 0 || empty list[4].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[4].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="0" type="hidden"/>测温</td>
				<td><input name="a.type1" value="6" type="hidden"/>冷芯报警</td>
				<td>
					<input type="radio" name="a.alarmTag5" value="1" ${list[5].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag5" value="0" ${list[5].alarmTag == 0 || empty list[5].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[5].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="1" type="hidden"/>储粮</td>
				<td><input name="a.type1" value="1" type="hidden"/>满仓提醒</td>
				<td>
					<input type="radio" name="a.alarmTag6" value="1" ${list[6].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag6" value="0" ${list[6].alarmTag == 0 || empty list[6].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[6].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="1" type="hidden"/>储粮</td>
				<td><input name="a.type1" value="2" type="hidden"/>半仓提醒</td>
				<td>
					<input type="radio" name="a.alarmTag7" value="1" ${list[7].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag7" value="0" ${list[7].alarmTag == 0 || empty list[7].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[7].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="2" type="hidden"/>测虫</td>
				<td><input name="a.type1" value="1" type="hidden"/>预警</td>
				<td>
					<input type="radio" name="a.alarmTag8" value="1" ${list[8].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag8" value="0" ${list[8].alarmTag == 0 || empty list[8].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[8].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="2" type="hidden"/>测虫</td>
				<td><input name="a.type1" value="2" type="hidden"/>报警</td>
				<td>
					<input type="radio" name="a.alarmTag9" value="1" ${list[9].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag9" value="0" ${list[9].alarmTag == 0 || empty list[9].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[9].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="3" type="hidden"/>测气</td>
				<td><input name="a.type1" value="1" type="hidden"/>磷化氢</td>
				<td>
					<input type="radio" name="a.alarmTag10" value="1" ${list[10].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag10" value="0" ${list[10].alarmTag == 0 || empty list[10].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[10].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="3" type="hidden"/>测气</td>
				<td><input name="a.type1" value="2" type="hidden"/>二氧化碳</td>
				<td>
					<input type="radio" name="a.alarmTag11" value="1" ${list[11].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag11" value="0" ${list[11].alarmTag == 0 || empty list[11].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[11].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="3" type="hidden"/>测气</td>
				<td><input name="a.type1" value="3" type="hidden"/>氧气（下限）</td>
				<td>
					<input type="radio" name="a.alarmTag12" value="1" ${list[12].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag12" value="0" ${list[12].alarmTag == 0 || empty list[12].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[12].threshold}" class="double4"/></td>
			</tr>
			<tr>
				<td><input name="a.type" value="4" type="hidden"/>控制</td>
				<td><input name="a.type1" value="1" type="hidden"/>设备异常</td>
				<td>
					<input type="radio" name="a.alarmTag13" value="1" ${list[13].alarmTag == 1 ? 'checked' : ''}/>是
					<input type="radio" name="a.alarmTag13" value="0" ${list[13].alarmTag == 0 || empty list[13].alarmTag ? 'checked' : ''}/>否
				</td>
				<td><input name="a.threshold" value="${list[13].threshold}" class="double4"/></td>
			</tr>
		</tbody>
	</table>
</form>