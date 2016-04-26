var Validate = function() {
	var handle = function(selector,rules, messages) {
		$(selector).validate({
			errorElement : 'span', // 装错误信息的标签元素
			errorClass : 'help-block', // 错误信息的类样式
			focusInvalid : true, // 验证不通过的第一项获取焦点
			// 规则
			rules : rules,
			messages : messages,
			invalidHandler : function(event, validator) {
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
				error.appendTo(element.parent());
			},
			// 通过验证后运行的函数，里面要加上表单提交的函数，否则表单不会提交。
			submitHandler : function(form) {
				form.submit();
			}
		});
		// enter键
		$(selector+' input').keypress(function(e) {
			if (e.which == 13) {
				if ($(selector).validate().form()) {
					$(selector).submit();
				}
				return false;
			}
		});
	}
	return {
		init : function(selector,rules, messages) {
			handle(selector,rules, messages);
		}
	};
}();