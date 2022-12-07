package com.kyu0.jungo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class JungoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JungoApplication.class, args);
	}

}
