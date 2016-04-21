package com.hlsq.elib.proc.web;

import org.restlet.engine.adapter.HttpRequest;
import org.restlet.engine.adapter.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProcController {
	@RequestMapping("/proc/index")
	public String procIndex() {
		return "index";
	}

	@RequestMapping(value = "/proc/convert", method = RequestMethod.POST)
	public String procConvert(HttpRequest request, HttpResponse response, Model model) {
		System.out.println("aaa");
		return "index";
	}
}
