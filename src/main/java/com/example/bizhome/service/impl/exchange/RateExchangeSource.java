package com.example.bizhome.service.impl.exchange;

import com.example.bizhome.service.ExchangeSource;
import com.example.bizhome.service.exception.CurrencyValueNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Service("rateexchange")
public class RateExchangeSource implements ExchangeSource {
    private final WebClient webClient;

    public RateExchangeSource(@Qualifier("RateExchangeApi") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ExchangeSourceAnswer getValue(String currency, LocalDate date) {
        ExchangeSourceAnswer answer = new ExchangeSourceAnswer();
        answer.setStatus(ExchangeSourceAnswer.STATUS.WAIT);
        answer.setCurrency(currency);
        answer.setDate(date);

        Mono<Float> mono = webClient
                .get()
                .uri(String.format("/%s?symbols=%s", date.toString(), currency))
                .retrieve()
                .onRawStatus(httpStatus -> httpStatus != 200,
                        response -> {
                            answer.setStatus(ExchangeSourceAnswer.STATUS.ERROR);
                            return Mono.just(new CurrencyValueNotFoundException(currency, date));
                        })
                .toEntity(RateExchangeAnswer.class)
                .map(responseEntity -> {
                    answer.setStatus(ExchangeSourceAnswer.STATUS.SUCCESS);
                    return responseEntity.getBody().getRates().get(currency);
                });

        answer.setAnswer(mono);
        return answer;
    }


    @Getter
    @Setter
    public static class RateExchangeAnswer {
        Map<String, Float> rates;
    }

}
