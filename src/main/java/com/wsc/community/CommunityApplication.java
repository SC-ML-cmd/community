package com.wsc.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityApplication {
	//Spring boot 中内嵌了tomcat
	//运行后，创建Spring容器
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
