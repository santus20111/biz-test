package com.example.bizhome.service.impl.cache;

import com.example.bizhome.service.ExchangeCacheRefreshService;
import com.example.bizhome.service.ExchangeService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ConditionalOnProperty(value = "exchange.cache.recheck-errors.enabled", havingValue = "true")
@Service
public class ScheduledExchangeCacheRefreshService implements ExchangeCacheRefreshService {

    private final Cache cache;

    public ScheduledExchangeCacheRefreshService(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("EXCHANGE-VALUES");
    }

    @Scheduled(fixedDelay = 10000)
    private void recheckErrors() {
        refresh();
    }

    /**
     * Удаляет из кэша запросы, которые упали с ошибкой
     */
    @Override
    public void refresh() {
        Map<List<Object>, Mono> nativeCache = (Map<List<Object>, Mono>) cache.getNativeCache();
        for(List<Object> key: nativeCache.keySet()) {
            try {
                ((Mono)cache.get(key).get()).block();
            } catch (Throwable throwable) {
                cache.evict(key);
            }
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
