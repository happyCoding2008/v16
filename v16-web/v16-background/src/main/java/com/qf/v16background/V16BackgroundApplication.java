package com.qf.v16background;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class V16BackgroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(V16BackgroundApplication.class, args);
	}

}
