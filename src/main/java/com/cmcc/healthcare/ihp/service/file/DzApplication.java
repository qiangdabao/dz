package com.cmcc.healthcare.ihp.service.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class DzApplication{
	public static void main(String[] args) {
		SpringApplication.run(DzApplication.class, args);
	}
}
