package com.broadenway.ureasolution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/")
public class CommonController {

	@RequestMapping(value={"/robots.txt", "/robot.txt"})
	@ResponseBody
	public String getRobotsTxt() {
		return "User-agent: *\n" +
			"Disallow: /\n" +
			"User-agent: Googlebot\n" +
			"Allow: /\n" +
			"User-agent: Yetibot\n" +
			"Allow: /\n";
	}
}
