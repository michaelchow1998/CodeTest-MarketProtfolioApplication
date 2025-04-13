package com.michael.marketprotfoliosystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarketProtfolioSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketProtfolioSystemApplication.class, args);
    }

}
