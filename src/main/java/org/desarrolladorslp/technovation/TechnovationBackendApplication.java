package org.desarrolladorslp.technovation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TechnovationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechnovationBackendApplication.class, args);
    }

}

