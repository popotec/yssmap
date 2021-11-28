package com.broadenway.ureasolution.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/")
public class CommonController {

	@Value("${daum-webmaster-tool}")
	private String daumWebMasterKey;

	@RequestMapping(value={"/robots.txt", "/robot.txt"})
	@ResponseBody
	public String getRobotsTxt() {
		return  makeRobotsTxt();
	}

	private String makeRobotsTxt(){
		StringBuilder sb = new StringBuilder();
		sb.append("User-agent: *\n");
		sb.append("Disallow: /\n");
		sb.append("User-agent: Googlebot\n");
		sb.append("Allow: /\n");
		sb.append("Sitemap: https://yssmap.com/sitemap.xml\n");
		sb.append("User-agent: Daum\n");
		sb.append("Allow: /\n");
		sb.append("Sitemap: https://yssmap.com/sitemap.xml\n");
		sb.append("User-agent: Yetibot\n");
		sb.append("Allow: /\n");
		sb.append("Sitemap: https://yssmap.com/sitemap.xml\n");
		sb.append("DaumWebMasterTool:");
		sb.append(daumWebMasterKey);
		return sb.toString();
	}
}
