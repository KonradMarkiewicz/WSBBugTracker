package com.wsb.WSBBugTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WsbBugTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsbBugTrackerApplication.class, args);
	}

}
