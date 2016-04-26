<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="page-header">
	<!-- BEGIN 顶部上半部分 -->
	<div class="page-header-top">
		<div class="container">
			<!-- BEGIN LOGO -->
			<div class="page-logo">
				<a href="index.html"><img
					src="/resources/assets/admin/layout3/img/logo-default.png"
					alt="logo" class="logo-default"></a>
			</div>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="menu-toggler"></a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN 上半部分导航菜单 -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<!-- BEGIN 用户登录选项 -->
					<li class="dropdown dropdown-user dropdown-dark"><a
						href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"
						data-hover="dropdown" data-close-others="true"><span
							class="username username-hide-mobile">Tab</span> </a>
						<ul class="dropdown-menu dropdown-menu-default">
							<li><a href="javascript:;"> <i class="icon-key"></i> 注销
							</a></li>
						</ul></li>
					<!-- END 用户登录选项 -->
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown dropdown-extended quick-sidebar-toggler"><span
						class="sr-only">Toggle Quick Sidebar</span> <i class="icon-logout"></i></li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>
			<!-- END 上半部分导航菜单 -->
		</div>
	</div>
	<!-- END 顶部上半部分 -->
	<!-- BEGIN 页面顶部主菜单 -->
	<jsp:include page="/WEB-INF/views/common/mainMenu.jsp"></jsp:include>
	<!-- END 页面顶部主菜单 -->
</div>