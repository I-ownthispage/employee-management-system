package com.crudapi.mysqlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MysqlapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysqlapiApplication.class, args);
	}

}
