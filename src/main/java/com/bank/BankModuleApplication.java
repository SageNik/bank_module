package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class BankModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankModuleApplication.class, args);
	}

}
