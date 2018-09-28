package com.tom.sdgm.newspaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
public class NewspaperApplication{

	public static void main(String[] args) {
		SpringApplication.run(NewspaperApplication.class, args);
	}


}
