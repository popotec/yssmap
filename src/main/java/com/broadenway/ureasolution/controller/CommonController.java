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
			"Sitemap: https://yssmap.com/sitemap.xml\n" +
			"User-agent: Daum\n" +
			"Allow: /\n" +
			"Sitemap: https://yssmap.com/sitemap.xml\n" +
			"User-agent: Yetibot\n" +
			"Allow: /\n" +
			"Sitemap: https://yssmap.com/sitemap.xml\n" +
			"DaumWebMasterTool:95d3266fc401ba3bacb41a69b11f55a404664144e7609a814f68c0890b147c46:AfWVm7J7RFpH4KiqxuBceA==";
	}
}
