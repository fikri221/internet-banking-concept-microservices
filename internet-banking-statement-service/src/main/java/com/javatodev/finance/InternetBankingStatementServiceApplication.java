package com.javatodev.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InternetBankingStatementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternetBankingStatementServiceApplication.class, args);
    }

}
