package com.bigpicture.moonrabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoonrabbitApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoonrabbitApplication.class, args);
	}

}
