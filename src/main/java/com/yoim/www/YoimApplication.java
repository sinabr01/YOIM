package com.yoim.www;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yoim.www.mapper")
public class YoimApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoimApplication.class, args);
	}

}
