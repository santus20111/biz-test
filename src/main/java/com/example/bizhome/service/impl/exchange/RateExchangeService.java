package com.example.bizhome.service.impl.exchange;

import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.service.exception.CurrencyValueNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;

@Service
public class RateExchangeService implements ExchangeService {
    private final WebClient webClient;

    public RateExchangeService(@Qualifier("RateExchangeApi") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Float> getValue(String currency, LocalDate date) {
            return webClient
                    .get()
                    .uri(String.format("/%s?symbols=%s", date.toString(), currency))
                    .retrieve()
                    .onRawStatus(httpStatus -> httpStatus == 400,
                            response -> Mono.just(new CurrencyValueNotFoundException(currency, date)))
                    .toEntity(RateExchangeAnswer.class)
                    .map(responseEntity -> responseEntity.getBody().getRates().get(currency));
    }


    @Getter
    @Setter
    public static class RateExchangeAnswer {
        Map<String, Float> rates;
    }

}
