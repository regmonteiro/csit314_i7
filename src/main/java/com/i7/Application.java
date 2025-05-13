package com.i7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.i7.test.LoginTest;
import com.i7.test.ViewListingTest;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		LoginTest.run();
		ViewListingTest.run();
	}

}
