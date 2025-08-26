package com.skillup30.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void loadEnv() {
        try {
            System.out.println("Loading .env file...");
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .filename(".env")
                    .load();

            System.out.println("Found " + dotenv.entries().size() + " environment variables");

            // Set system properties from .env file
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
                System.out.println("Set property: " + entry.getKey() + " = " + entry.getValue());
            });

            // Also set environment variables
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            dotenv.entries().forEach(entry -> {
                writableEnv.put(entry.getKey(), entry.getValue());
                System.out.println("Set env var: " + entry.getKey() + " = " + entry.getValue());
            });

            System.out.println("Environment variables loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}