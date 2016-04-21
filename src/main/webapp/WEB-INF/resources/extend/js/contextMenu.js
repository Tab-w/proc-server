var ContextMenu = function() {
	var options;
	var buildMenu = function(selector, opts) {
		options = $.extend({}, options, opts);
		// 右键菜单
		$(document)
				.on(
						"contextmenu",
						selector + ",#contextMenu",
						function(e) {
							e.preventDefault();
							e.stopPropagation();
							var $contextMenu = $("<div id='contextMenu' class='dropdown open'></div>");
							$contextMenu.css("position", "fixed");
							$contextMenu.css("left", e.pageX
									- document.body.scrollLeft);
							$contextMenu.css("top", e.pageY
									- document.body.scrollTop);
							$contextMenu.css("z-index", 11);
							$contextMenu
									.append("<ul class='dropdown-menu'></ul>");
							for ( var i in options) {
								if (options[i].divider) {
									$contextMenu.find("ul").append(
											"<li class='divider'></li>");
								} else {
									// 是否有切换
									if (!options[i].switchable) {
										var $item = $("<li><a href='"
												+ options[i].url + "'>"
												+ options[i].text + "</a></li>");
										$item.on("click", options[i].action);
										$contextMenu.find("ul").append($item);
									} else {
										if ($("#update").text() === "编辑") {
											var $item = $("<li><a href='"
													+ options[i].url + "'>"
													+ options[i].text[0]
													+ "</a></li>");
											$item.on("click",
													options[i].action[0]);
											$contextMenu.find("ul").append(
													$item);
										} else {
											var $item = $("<li><a href='"
													+ options[i].url + "'>"
													+ options[i].text[1]
													+ "</a></li>");
											$item.on("click",
													options[i].action[1]);
											$contextMenu.find("ul").append(
													$item);
										}

									}
								}
							}
							$("#contextMenu").remove();
							$(selector).parent("div").before($contextMenu);
						});
		$(document).on("click", "html", function(e) {
			$("#contextMenu").remove();
		});
	}
	return {
		init : function(selector, opts) {
			buildMenu(selector, opts);
		}
	}
}();