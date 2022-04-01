package yssmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import yssmap.main.handleradapter.ExtendedRequestMappingHandlerAdapter;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public WebMvcRegistrations mvcRegistrations() {
		return new WebMvcRegistrations() {
			@Override
			public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
				return new ExtendedRequestMappingHandlerAdapter();
			}
		};
	}
}
