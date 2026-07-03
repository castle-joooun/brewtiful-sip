package com.brewtifulsip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BrewtifulSipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrewtifulSipApplication.class, args);
    }
}
