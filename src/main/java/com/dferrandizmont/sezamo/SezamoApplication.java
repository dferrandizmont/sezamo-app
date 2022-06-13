package com.dferrandizmont.sezamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SezamoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SezamoApplication.class, args);
    }

}
