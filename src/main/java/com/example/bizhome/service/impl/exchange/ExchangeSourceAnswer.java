package com.example.bizhome.service.impl.exchange;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Getter
@Setter
public class ExchangeSourceAnswer {
    public enum STATUS {
        WAIT,
        SUCCESS,
        ERROR
    }

    private String currency;
    private LocalDate date;
    private STATUS status;
    private Mono<Float> answer;
}
