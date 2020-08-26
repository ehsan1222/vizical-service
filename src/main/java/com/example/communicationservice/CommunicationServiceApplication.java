package com.example.communicationservice;

import com.example.communicationservice.models.Roles;
import com.example.communicationservice.services.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

@SpringBootApplication
public class CommunicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunicationServiceApplication.class, args);
	}

	@Autowired
	private UserService userService;

	@Bean
	public InitializingBean initializingBean() {
		return () -> {
			userService.addUser("user", "password", Roles.USER);
			userService.addUser("admin", "admin", Roles.ADMIN);

		};
	}
}
