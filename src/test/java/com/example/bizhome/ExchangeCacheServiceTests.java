package com.example.bizhome;

import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.service.ExchangeSource;
import com.example.bizhome.service.impl.ExchangeCacheService;
import com.example.bizhome.service.impl.exchange.ExchangeSourceAnswer;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class ExchangeCacheServiceTests {
    @MockBean
    ExchangeSource exchangeSource;

    @Qualifier("exchangeCacheService")
    @Autowired
    ExchangeService exchangeCacheService;

    @Test
    public void testCacheAnswers() {
        String currency = "RUB";
        LocalDate date = LocalDate.now();

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
        Float answer1Value = exchangeCacheService.getValue(currency, date).getAnswer().block();

        Mockito.when(exchangeSource.getValue(currency, date)).thenReturn(answer2);
        Float answer2Value = exchangeCacheService.getValue(currency, date).getAnswer().block();
        Assert.assertEquals(answer1Value, answer2Value, 0.0);
    }
}
