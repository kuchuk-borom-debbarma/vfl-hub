package dev.kuku.vfl.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VflHubApplication {
    //TODO use hypertables for timeseries as it will make it easier for fetching data based on time in future
    //TODO cloud service!
    //TODO block entered, exited, returned
    public static void main(String[] args) {
        SpringApplication.run(VflHubApplication.class, args);
    }
}
