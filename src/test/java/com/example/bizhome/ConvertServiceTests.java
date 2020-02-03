package com.example.bizhome;

import com.example.bizhome.service.ConvertService;
import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.service.impl.ConvertServiceImpl;
import com.example.bizhome.service.impl.exchange.ExchangeSourceAnswer;
import com.example.bizhome.web.request.ConvertRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RunWith(JUnit4.class)
public class ConvertServiceTests {
    @Test
    public void testConvertationMath() {
        ExchangeSourceAnswer answer1 = new ExchangeSourceAnswer();
        answer1.setStatus(ExchangeSourceAnswer.STATUS.SUCCESS);
        answer1.setDate(LocalDate.now());
        answer1.setCurrency("RUB");
        answer1.setAnswer(Mono.just(0.12f));

        ExchangeSourceAnswer answer2 = new ExchangeSourceAnswer();
        answer2.setStatus(ExchangeSourceAnswer.STATUS.SUCCESS);
        answer2.setDate(LocalDate.now());
        answer2.setCurrency("USD");
        answer2.setAnswer(Mono.just(1.2f));

        ExchangeService mock = Mockito.mock(ExchangeService.class);
        Mockito.when(mock.getValue("RUB", LocalDate.now())).thenReturn(answer1);
        Mockito.when(mock.getValue("USD", LocalDate.now())).thenReturn(answer2);


        ConvertService convertService = new ConvertServiceImpl(mock);

        ConvertRequest request = new ConvertRequest();
        request.setDate(LocalDate.now().toString());
        request.setSum(100f);
        request.setBaseCurrency("RUB");
        request.setConvertCurrency("USD");

        Assert.assertEquals(convertService.convert(request).block().getSum(), 100f / 0.12f * 1.2f, 0.0);
    }
}
