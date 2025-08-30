package dev.kuku.vfl.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VflHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(VflHubApplication.class, args);
    }

}
