<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglib.jsp"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8">
<title>Index</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">
<jsp:include page="/WEB-INF/views/common/csslib.jsp"></jsp:include>
<link href="/resources/assets/admin/pages/css/tasks.css"
	rel="stylesheet" type="text/css" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
	<!-- BEGIN 页面顶部 -->
	<jsp:include page="/WEB-INF/views/common/head.jsp"></jsp:include>
	<!-- END 页面顶部 -->
	<!-- BEGIN PAGE容器 -->
	<div class="page-container">
		<!-- BEGIN PAGE内容  -->
		<div class="page-content">
			<!-- BEGIN 主体内容  -->
			<div class="container">
				<div class="portlet light">
					<div class="portlet-body form">
						<form role="form" action="/proc/convert">
							<div class="form-body">
								<div
									class="form-group form-md-line-input form-md-floating-label">
									<input type="text" class="form-control"> <label
										for="form_control_1">输入文件夹路径</label> <span class="help-block">PDF文件所在的文件夹</span>
								</div>
								<div
									class="form-group form-md-line-input form-md-floating-label has-info">
									<select class="form-control" id="form_control_1">
										<option value=""></option>
										<option value="1">Option 1</option>
										<option value="2">Option 2</option>
										<option value="3">Option 3</option>
										<option value="4">Option 4</option>
									</select> <label for="form_control_1">Dropdown sample</label>
								</div>
							</div>
							<div class="form-actions noborder">
								<button type="button" class="btn blue">提交</button>
								<!-- <button type="button" class="btn default">Cancel</button> -->
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- END 主体内容  -->
			<!-- BEGIN 右边信息栏 -->
			<a href="javascript:;" class="page-quick-sidebar-toggler"><i
				class="icon-login"></i></a>
			<div class="page-quick-sidebar-wrapper">
				<div class="page-quick-sidebar"></div>
			</div>
			<!-- END 右边信息栏 -->
		</div>
		<!-- END PAGE内容 -->
	</div>
	<!-- END PAGE容器 -->
	<!-- BEGIN 底部 -->
	<jsp:include page="/WEB-INF/views/common/foot.jsp"></jsp:include>
	<!-- END 底部 -->
	<jsp:include page="/WEB-INF/views/common/jslib.jsp"></jsp:include>
	<script>
		jQuery(document).ready(function() {
			Metronic.init(); // init metronic core componets
			Layout.init(); // init layout
			QuickSidebar.init(); // init quick sidebar
		});
	</script>
</body>
<!-- END BODY -->
</html>