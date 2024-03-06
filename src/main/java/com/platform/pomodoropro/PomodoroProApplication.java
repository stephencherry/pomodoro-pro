package com.platform.pomodoropro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PomodoroProApplication {

	public static void main(String[] args) {
		System.out.println("Hello Pomodoro Pro Application");
		SpringApplication.run(PomodoroProApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());
		return passwordEncoder;
	}
}
