package com.nethaji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NethajiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NethajiApplication.class, args);
	}

}
