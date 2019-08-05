package com.qf.v16productservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qf.v16.mapper")
public class V16ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(V16ProductServiceApplication.class, args);
	}

}
