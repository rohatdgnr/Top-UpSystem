package com.tyonex.top_up;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TopUpSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopUpSystemApplication.class, args);
	}

}
