package com.hustle.Traineeship.Management.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping(path = "print")
public class TraineeshipManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraineeshipManagementApplication.class, args);

	}
	@GetMapping
	public String print(){
		System.out.println();
		return "Hello World";
	}

}
