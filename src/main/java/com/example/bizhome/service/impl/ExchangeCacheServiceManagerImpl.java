package com.example.bizhome.service.impl;

import com.example.bizhome.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeCacheServiceManagerImpl implements com.example.bizhome.service.ExchangeServiceManager {
    private final List<ExchangeService> exchangeServices;
    private final Environment environment;
    private final Cache cache;

    @Autowired
    public ExchangeCacheServiceManagerImpl(List<ExchangeService> exchangeServices, Environment environment, CacheManager cacheManager) {
        this.exchangeServices = exchangeServices;
        this.environment = environment;
        this.cache = cacheManager.getCache("EXCHANGE-MANAGER");
    }

    private String getCacheKey(ExchangeService exchangeService, String currency, LocalDate date) {
        return exchangeService.getClass().getSimpleName() + ":" + currency + ":" + date.toString();
    }

    @Override
    synchronized public Mono<Float> getValue(String currency, LocalDate date) {
        ExchangeService exchangeService = exchangeServices.get(0);

        String cacheKey = getCacheKey(exchangeService, currency, date);

        if (cache.get(cacheKey) == null) {
            Mono<Float> mono = exchangeService.getValue(currency, date);
            cache.put(cacheKey, mono);
            return mono;
        }
        return cache.get(cacheKey, Mono.class);
    }

    @Scheduled(fixedDelay = 10000)
    private void recheckErrors() {
        if (environment.getProperty("exchange.cache.recheck-errors.enabled") != null &&
                environment.getProperty("exchange.cache.recheck-errors.enabled").equals("true")) {
            for (ExchangeService exchangeService : exchangeServices) {
                Map<String, Mono> nativeCache = (Map<String, Mono>) cache.getNativeCache();
                for (String key : nativeCache.keySet()) {
                    if (key.startsWith(exchangeService.getClass().getSimpleName())) {
                        try {
                            cache.get(key, Mono.class)
                                    .doOnError(error -> {
                                        String[] keyValues = key.split(":");
                                        getValue(keyValues[1], LocalDate.parse(keyValues[2]));
                                    })
                                    .block();
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }
        }
    }
}
