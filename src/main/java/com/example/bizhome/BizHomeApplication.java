package com.example.bizhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BizHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizHomeApplication.class, args);
    }


    @Bean("RateExchangeApi")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.exchangeratesapi.io/")
                .build();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("EXCHANGE-VALUES");
    }
}
