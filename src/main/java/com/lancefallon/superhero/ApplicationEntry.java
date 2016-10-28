package com.lancefallon.superhero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationEntry {

	public static void main(String[] args) {
		SpringApplication.run(new Class[]{ApplicationEntry.class, SecurityConfiguration.class}, args);
	}
}
