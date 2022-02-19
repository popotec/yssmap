package yssmap.main.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCacheConfig implements WebMvcConfigurer {
	public static final List<String> PREFIX_STATIC_RESOURCES = Arrays.asList("/js", "/css", "/images", "/assets");

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		for(String prefix : PREFIX_STATIC_RESOURCES){
			registry.addResourceHandler(prefix+"/**")
				.addResourceLocations("classpath:/static"+prefix+"/")
				.setCachePeriod(60 * 60 * 24 * 365);
		}
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
		FilterRegistrationBean registration = new FilterRegistrationBean();
		Filter etagHeaderFilter = new ShallowEtagHeaderFilter();
		registration.setFilter(etagHeaderFilter);
		registration.addUrlPatterns(getUrlPatternsForEtag());
		return registration;
	}

	private String[] getUrlPatternsForEtag() {
		return PREFIX_STATIC_RESOURCES.stream()
			.map(prefix -> prefix+"/*")
			.toArray(String[]::new);
	}
}
