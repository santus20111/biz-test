package com.example.bizhome.service.impl.cache;

import com.example.bizhome.service.impl.exchange.ExchangeSourceAnswer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ScheduledExchangeCacheRefreshService {

    @Value("${exchange.cache.recheck-errors}")
    private Boolean recheckErrors;

    @Value("${exchange.cache.period}")
    private Integer cachePeriod;

    private final Cache cache;

    public ScheduledExchangeCacheRefreshService(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("EXCHANGE-VALUES");
    }

    @Scheduled(fixedDelay = 10000)
    private void recheckErrors() {
        refresh();
    }

    /**
     * Удаляет из кэша ответы, которые не входят в указанный период кэширования, или которые упали с ошибкой
     */
    public void refresh() {
        Map<Object, ExchangeSourceAnswer> nativeCache = (Map<Object, ExchangeSourceAnswer>) cache.getNativeCache();
        for(Object key: nativeCache.keySet()) {
            ExchangeSourceAnswer answer = nativeCache.get(key);
            if(LocalDate.now().minusDays(cachePeriod).isAfter(answer.getDate())) {
                cache.evict(key);
            } else if(recheckErrors && answer.getStatus().equals(ExchangeSourceAnswer.STATUS.ERROR)) {
                cache.evict(key);
            }
        }
    }
}
