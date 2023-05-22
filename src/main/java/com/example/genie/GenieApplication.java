package com.example.genie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GenieApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenieApplication.class, args);
	}

}
