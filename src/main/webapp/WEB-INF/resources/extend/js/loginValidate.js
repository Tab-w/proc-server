var Login = function() {

	var handleLogin = function() {

		$('.login-form').validate({
			errorElement : 'span', // 装错误信息的标签元素
			errorClass : 'help-block', // 错误信息的类样式
			focusInvalid : true, // 验证不通过的第一项获取焦点
			// 规则
			rules : {
				username : {
					required : true
				},
				password : {
					required : true
				},
				remember : {
					required : false
				}
			},

			messages : {
				username : {
					required : "请输入账户."
				},
				password : {
					required : "请输入密码."
				}
			},

			invalidHandler : function(event, validator) {
				$('.login-form .alert').removeClass('alert-success');
				$('.login-form .alert').addClass('alert-danger');
				$('#error_info').text("请输入登录的账户和密码.");
				$('.alert-danger', $('.login-form')).show();
			},
			// 可以给未通过验证的元素加效果、闪烁等
			highlight : function(element) {
				$(element).closest('.form-group').addClass('has-error');
			},
			// 验证通过
			success : function(label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},
			// 指明错误放置的位置，默认情况是：error.appendTo(element.parent());即把错误信息放在验证的元素后面。
			errorPlacement : function(error, element) {
				error.insertAfter(element.closest('.input-icon'));
			},
			// 通过验证后运行的函数，里面要加上表单提交的函数，否则表单不会提交。
			submitHandler : function(form) {
				form.submit();
			}
		});
		// enter键
		$('.login-form input').keypress(function(e) {
			if (e.which == 13) {
				if ($('.login-form').validate().form()) {
					$('.login-form').submit();
				}
				return false;
			}
		});
	}

	var handleForgetPassword = function() {
		$('.forget-form').validate({
			errorElement : 'span', // default input error message container
			errorClass : 'help-block', // default input error message class
			focusInvalid : true, // do not focus the last invalid input
			ignore : "",
			rules : {
				email : {
					required : true,
					email : true
				}
			},

			messages : {
				email : {
					required : "请输入邮箱.",
					email : "邮箱格式不正确."
				}
			},

			invalidHandler : function(event, validator) {
			},

			highlight : function(element) {
				$(element).closest('.form-group').addClass('has-error');
			},

			success : function(label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},

			errorPlacement : function(error, element) {
				// error.insertAfter(element.closest('.input-icon'));
			},

			submitHandler : function(form) {
				form.submit();
			}
		});

		$('.forget-form input').keypress(function(e) {
			if (e.which == 13) {
				if ($('.forget-form').validate().form()) {
					$('.forget-form').submit();
				}
				return false;
			}
		});

		jQuery('#forget-password').click(function() {
			jQuery('.login-form').hide();
			jQuery('.forget-form').show();
		});

		jQuery('#back-btn').click(function() {
			jQuery('.login-form').show();
			jQuery('.forget-form').hide();
		});

	}

	var handleRegister = function() {
		$('.register-form').validate({
			errorElement : 'span', // default input error message container
			errorClass : 'help-block', // default input error message class
			focusInvalid : true, // do not focus the last invalid input
			ignore : "",
			rules : {
				username : {
					required : true,
					rangelength : [ 6, 15 ]
				},
				password : {
					required : true,
					rangelength : [6, 15 ]
				},
				rpassword : {
					required : true,
					equalTo : "#register_password"
				},
				email : {
					required : true,
					email : true,
					remote : {
						url : "/emailValidate",
						type : "post",
						dataType : "json"
					}
				}
			},

			messages : {
				username : {
					required : "账户不能为空.",
					rangelength : "请输入一个值6 - 15个字符."
				},
				password : {
					required : "密码不能为空.",
					rangelength : "请输入一个值6 - 15个字符."
				},
				rpassword : {
					required : "确认密码不能为空.",
					equalTo : "确认密码不一致."
				},
				email : {
					required : "邮箱不能为空.",
					email : "邮箱格式不正确",
					remote : "该邮箱已注册."
				}
			},

			invalidHandler : function(event, validator) {
				$('.alert-danger', $('.login-form')).show();
			},

			highlight : function(element) {
				$(element).closest('.form-group').addClass('has-error');
			},

			success : function(label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},

			errorPlacement : function(error, element) {
				error.insertAfter(element.closest('.form-control'));
			},

			submitHandler : function(form) {
				form.submit();
			}
		});

		$('.register-form input').keypress(function(e) {
			if (e.which == 13) {
				if ($('.register-form').validate().form()) {
					$('.register-form').submit();
				}
				return false;
			}
		});

		jQuery('#register-btn').click(function() {
			jQuery('.login-form').hide();
			jQuery('.register-form').show();
		});

		jQuery('#register-back-btn').click(function() {
			jQuery('.login-form').show();
			jQuery('.register-form').hide();
		});
	}

	return {
		init : function() {
			handleLogin();
			handleForgetPassword();
			handleRegister();
		}
	};
}();