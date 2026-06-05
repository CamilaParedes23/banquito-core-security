package com.banquito.platform.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IdentityAccessApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentityAccessApplication.class, args);
    }
}
