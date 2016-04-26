var DataTableAjax = function() {

	// 表格对象
	var table;
	// 需要更新的列编号从0开始
	var updateColumns;
	// 添加Url地址
	var addUrl;
	// 删除Url地址
	var deleteUrl;
	// 更新Url地址
	var saveUrl;

	// selector:table元素的id,
	// url:获取数据的地址,返回自定义的DatatablePage类型数据
	var buildTable = function(selector, scrollX, url, columns) {
		table = $(selector)
				.dataTable(
						{
							"scrollX" : scrollX,
							// 是否显示处理状态
							"processing" : true,
							// 是否开启服务器模式
							"serverSide" : true,
							// 是否允许Datatables开启本地搜索
							"searching" : false,
							// 是否允许Datatables开启排序
							"ordering" : true,
							// 保存状态 - 在页面重新加载的时候恢复状态（页码等内容）
							"stateSave" : true,
							// 分页按钮的显示方式
							"pagingType" : "full_numbers",
							// 改变初始的页面长度(每页显示的记录数)
							"pageLength" : 10,
							// 定义在每页显示记录数的select中显示的选项
							"lengthMenu" : [ [ 10, 20, 50, 100 ],
									[ 10, 20, 50, 100 ] ],
							// 默认排序
							"order" : [ [ 1, "asc" ] ],
							// 定义DataTables的组件元素的显示和显示顺序
							"dom" : "<'row' <'col-md-12'T>><'row'<'col-md-12 col-sm-6'r>><'table-scrollable't><'row'<'col-md-3 col-sm-12'i><'col-md-7 col-sm-12'p><'col-md-2 col-sm-12'l>>",
							// 所有DataTables的语言配置都在这里
							"language" : {
								"processing" : "努力加载中...",
								"lengthMenu" : "_MENU_ 条记录",
								"zeroRecords" : "没有找到记录",
								"info" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
								"infoEmpty" : "无记录",
								"infoFiltered" : "(从 _MAX_ 条记录过滤)",
								"paginate" : {
									"first" : "首页",
									"previous" : "上一页",
									"next" : "下一页",
									"last" : "尾页"
								}
							},
							// 从一个ajax数据源读取数据给表格内容
							"ajax" : url,
							// 设定列的所有初始属性
							"columns" : columns,
							// 设置定义列的初始属性
							"columnDefs" : [ {
								// 定义复选框
								"targets" : [ 0 ],
								"render" : function(data) {
									return generateCheckbox(data);
								}
							}, {
								// 隐藏id列
								"targets" : [ 1 ],
								"visible" : false,
								"searchable" : false
							} ]
						});

		// 生成checkbox,value为记录的id(必须有id列)
		var generateCheckbox = function(data) {
			return "<div class='checker'><span><input type='checkbox' class='checkboxes' value='"
					+ data.id + "'/></span></div>";
		}

		// 全选变色
		$("table").find('.group-checkable').change(function() {
			var set = $(this).attr("data-set");
			var checked = $(this).is(":checked");
			$(set).each(function() {
				if (checked) {
					$(this).parent("span").addClass("checked");
					$(this).parents('tr').addClass("success");
				} else {
					$(this).parent("span").removeClass("checked");
					$(this).parents('tr').removeClass("success");
				}
			});
		});

		// 点选变色
		table.on('change', 'tbody tr .checkboxes', function() {
			$(this).parent("span").toggleClass("checked");
			$(this).parents('tr').toggleClass("success");
		});

		// 页面改变事件
		table.on('page.dt', function() {
			$(".group-checkable").parent("span").removeClass("checked");
		});
	}

	// 刷新数据
	var refresh = function() {
		$(".group-checkable").parent("span").removeClass("checked");
		table.api().draw(false);
	}

	// 编辑选择项
	var editRow = function(row, columns) {
		var jqTds = $('>td', row);
		for ( var i in columns) {
			if (jqTds[columns[i].num].innerHTML
					.indexOf('<input type="text" class="form-control"') === -1) {
				jqTds[columns[i].num].innerHTML = '<input type="text" class="form-control" name="'
						+ columns[i].property
						+ '" value="'
						+ jqTds[columns[i].num].innerHTML + '">';
			}
		}
	}

	// 根据被选项生成传入保存方法的参数
	var generateParameter = function(ids) {
		var objects = new Array();
		for ( var i in ids) {
			var object = {};
			// 获取到所有可编辑输入框的值并存入map
			table.find("tbody .checker input[value='" + ids[i] + "']").parents(
					"td").siblings().each(function() {
				var name = $(this).find("input").attr("name");
				var value = $(this).find("input").attr("value");
				object[name] = value;
			});
			objects.push(object);
		}
		return objects;
	}
	// 获取选择的记录ID
	var getSelected = function() {
		var ids = new Array();
		table.find("tbody .checker span[class='checked'] input").each(
				function() {
					ids.push($(this).val());
				});
		return ids;
	}
	// 删除
	var del = function() {
		var ids = getSelected();
		if (ids.length === 0) {
			bootbox.dialog({
				title : "提示",
				message : "没有被选择的记录!",
				buttons : {
					success : {
						label : "确定!",
						className : "blue",
					}
				}
			});
		} else {
			bootbox.confirm({
				title : "提示",
				message : "确定删除选择的记录吗?",
				buttons : {
					confirm : {
						label : "确定",
						className : "blue",
						callback : function() {
							return true;
						}
					},
					cancel : {
						label : "取消",
						className : "btn-default",
						callback : function() {
							return false;
						}
					}
				},
				callback : function(result) {
					if (result) {
						$.ajax({
							"url" : DataTableAjax.deleteUrl,
							"type" : "post",
							"data" : {
								"ids" : ids.toString(),
								"info" : JSON
										.stringify(table.api().page.info())
							},
							"success" : function(data) {
								if (data) {
									DataTableAjax.refresh();
								} else {
									bootbox.dialog({
										title : "提示",
										message : "抱歉,没有删除成功!",
										buttons : {
											success : {
												label : "确定!",
												className : "blue",
											}
										}
									});
								}
							}
						});
					}
				}
			});
		}
	}

	// 更新
	var update = function() {
		var ids = getSelected();
		if (ids.length !== 1) {
			bootbox.dialog({
				title : "提示",
				message : "没有被选择的记录或选择了多条记录!",
				buttons : {
					success : {
						label : "确定",
						className : "blue",
					}
				}
			});
		} else {
			var $rows = table.find("tbody .checker span[class='checked']")
					.parents("tr");
			$rows.each(function() {
				DataTableAjax.editRow($(this), DataTableAjax.updateColumns);
			});
			$("#update").removeClass("blue").addClass("yellow");
			$("#update").text("保存")
		}
	}

	// 保存
	var save = function() {
		// 所有被选ID
		var ids = getSelected();
		// 获得所有修改后需要保存的对象集合
		var objects = generateParameter(ids);
		if (ids.length !== 1) {
			bootbox.dialog({
				title : "提示",
				message : "请选择要保存的记录!",
				buttons : {
					success : {
						label : "确定",
						className : "blue",
					}
				}
			});
		} else {
			bootbox.confirm({
				title : "提示",
				message : "确定保存选择的记录吗?",
				buttons : {
					confirm : {
						label : "确定",
						className : "blue",
						callback : function() {
							return true;
						}
					},
					cancel : {
						label : "取消",
						className : "btn-default",
						callback : function() {
							return false;
						}
					}
				},
				callback : function(result) {
					if (result) {
						// 支持批量更新
						for ( var i in objects) {
							objects[i]["id"] = ids[i];
							$.ajax({
								"url" : DataTableAjax.saveUrl,
								"type" : "post",
								"data" : objects[i],
								"success" : function(data) {
									if (data) {
										$("#update").removeClass("yellow")
												.addClass("blue");
										$("#update").text("编辑")
										DataTableAjax.refresh();
									} else {
										bootbox.dialog({
											title : "提示",
											message : "抱歉,没有删除成功!",
											buttons : {
												success : {
													label : "确定",
													className : "blue",
												}
											}
										});
									}
								}
							});
						}
					}
				}
			});
		}
	}
	return {
		init : function(selector, scrollX, url, columns) {
			buildTable(selector, scrollX, url, columns);
		},
		refresh : function() {
			refresh();
		},
		editRow : function(row, columns) {
			editRow(row, columns)
		},
		del : function() {
			del();
		},
		updateColumns : function(updateColumns) {
			this.updateColumns = updateColumns;
		},
		update : function() {
			update();
		},
		save : function() {
			save();
		},
		addUrl : function(addUrl) {
			this.addUrl = addUrl;
		},
		deleteUrl : function(deleteUrl) {
			this.deleteUrl = deleteUrl;
		},
		saveUrl : function(saveUrl) {
			this.saveUrl = saveUrl;
		}
	}
}();