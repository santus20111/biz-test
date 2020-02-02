package com.example.bizhome.service.impl.cache;

import com.example.bizhome.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeCacheServiceManager implements com.example.bizhome.service.ExchangeServiceManager {
    private final ExchangeService exchangeService;

    @Cacheable(value = "EXCHANGE-VALUES", key = "{#currency, #date}")
    @Override
    synchronized public Mono<Float> getValue(String currency, LocalDate date) {
        return exchangeService.getValue(currency, date);
    }
}
