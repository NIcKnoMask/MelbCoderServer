package com.unimelbCoder.melbcode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.unimelbCoder.melbcode.dao")
@SpringBootApplication
public class MelbcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MelbcodeApplication.class, args);
	}

}
