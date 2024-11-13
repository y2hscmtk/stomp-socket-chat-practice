package com.choi76.web_socket_jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Entity Listener available
public class WebSocketJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSocketJwtApplication.class, args);
	}

}
