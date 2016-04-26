package com.hlsq.elib.proc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/proc/index")
	public String procIndex() {
		return "index";
	}
}
