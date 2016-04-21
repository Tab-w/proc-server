<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="page-header-menu">
	<div class="container">
		<!-- BEGIN 主菜单 -->
		<!-- DOC: Apply "hor-menu-light" class after the "hor-menu" class below to have a horizontal menu with white background -->
		<!-- DOC: Remove data-hover="dropdown" and data-close-others="true" attributes below to disable the dropdown opening on mouse hover -->
		<div class="hor-menu ">
			<ul class="nav navbar-nav">
				<li class="menu-dropdown classic-menu-dropdown "><a
					data-hover="megamenu-dropdown" data-close-others="true"
					data-toggle="dropdown" href="javascript:;">户和权限管理<i
						class="fa fa-angle-down"></i>
				</a>
					<ul class="dropdown-menu pull-left">
						<li><a href="/admin/user"><i class="fa fa-users"></i>
								用户管理</a></li>
						<li><a href="/admin/role"><i class="fa fa-user"></i> 角色管理</a></li>
						<li><a href="/admin/permission"><i class="fa fa-key"></i>
								权限管理</a></li>
						<li><a href=":;"><i class="fa fa-navicon"></i> 主菜单管理</a></li>
					</ul></li>
			</ul>
		</div>
		<!-- END 主菜单 -->
	</div>
</div>
