package com.lodekennes.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.lodekennes.carrental", "com.lodekennes.carrental.controllers", "com.lodekennes.carrental.models", "com.lodekennes.carrental.repositories", "com.lodekennes.carrental.services" })
public class MainApplicationClass {
    public static void main(String[] args) {
        SpringApplication.run(MainApplicationClass.class, args);
    }
}
