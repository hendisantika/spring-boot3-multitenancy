package com.hendisantika.multitenancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

import java.util.Properties;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class SpringBoot3MultitenancyApplication {
    public static final Properties defaultProperties = new Properties();

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3MultitenancyApplication.class, args);
    }

}
