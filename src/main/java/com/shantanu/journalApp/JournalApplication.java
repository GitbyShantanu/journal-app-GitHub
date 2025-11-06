package com.shantanu.journalApp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@Slf4j
public class JournalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JournalApplication.class, args);

        ConfigurableEnvironment environment = context.getEnvironment();
        log.info("environment profile: {}", environment.getActiveProfiles()[0]);
    }

    @Bean
    public PlatformTransactionManager MakeBeanX(MongoDatabaseFactory dbFactory) { // MongoTransactionManager implements PlatformTransactionManager thats why we create bean to use Implementation of MongoTransaction for our db operations
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public RestTemplate restTemplate() { // External API calls ke liye Spring ka HTTP client
        return new RestTemplate();
    }


}