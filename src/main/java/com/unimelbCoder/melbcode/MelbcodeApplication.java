package com.unimelbCoder.melbcode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.unimelbCoder.melbcode.models.dao")
@SpringBootApplication
@EnableCaching
public class MelbcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MelbcodeApplication.class, args);
	}

}
