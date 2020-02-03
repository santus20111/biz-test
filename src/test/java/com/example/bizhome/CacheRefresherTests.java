package com.example.bizhome;

import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.service.ExchangeSource;
import com.example.bizhome.service.impl.cache.ScheduledExchangeCacheRefreshService;
import com.example.bizhome.service.impl.exchange.ExchangeSourceAnswer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheRefresherTests {
    @MockBean
    ExchangeSource exchangeSource;

    @Autowired
    ScheduledExchangeCacheRefreshService cacheRefreshService;

    @Qualifier("exchangeCacheService")
    @Autowired
    ExchangeService exchangeCacheService;

    @Test
    public void testCachePeriod() {
        String currency = "RUB";
        LocalDate date = LocalDate.now().minusDays(8);

        ExchangeSourceAnswer answer1 = new ExchangeSourceAnswer();
        answer1.setStatus(ExchangeSourceAnswer.STATUS.SUCCESS);
        answer1.setDate(date);
        answer1.setCurrency(currency);
        answer1.setAnswer(Mono.just(10f));

        ExchangeSourceAnswer answer2 = new ExchangeSourceAnswer();
        answer2.setStatus(ExchangeSourceAnswer.STATUS.SUCCESS);
        answer2.setDate(date);
        answer2.setCurrency(currency);
        answer2.setAnswer(Mono.just(20f));

        Mockito.when(exchangeSource.getValue(currency, date)).thenReturn(answer1);
        exchangeCacheService.getValue(currency, date).getAnswer().block();
        cacheRefreshService.refresh();

        Mockito.when(exchangeSource.getValue(currency, date)).thenReturn(answer2);
        Assert.assertEquals(20f, exchangeCacheService.getValue(currency, date).getAnswer().block(), 0.0);
    }

    @Test
    public void testCacheErrors() {
        String currency = "RUB";
        LocalDate date = LocalDate.now();

        ExchangeSourceAnswer answer1 = new ExchangeSourceAnswer();
        answer1.setStatus(ExchangeSourceAnswer.STATUS.ERROR);
        answer1.setDate(date);
        answer1.setCurrency(currency);
        answer1.setAnswer(Mono.just(10f));

        ExchangeSourceAnswer answer2 = new ExchangeSourceAnswer();
        answer2.setStatus(ExchangeSourceAnswer.STATUS.SUCCESS);
        answer2.setDate(date);
        answer2.setCurrency(currency);
        answer2.setAnswer(Mono.just(20f));

        Mockito.when(exchangeSource.getValue(currency, date)).thenReturn(answer1);
        exchangeCacheService.getValue(currency, date).getAnswer().block();
        cacheRefreshService.refresh();

        Mockito.when(exchangeSource.getValue(currency, date)).thenReturn(answer2);
        Assert.assertEquals(20f, exchangeCacheService.getValue(currency, date).getAnswer().block(), 0.0);
    }
}
