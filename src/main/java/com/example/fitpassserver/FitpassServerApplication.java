package com.example.fitpassserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FitpassServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitpassServerApplication.class, args);
    }

}
