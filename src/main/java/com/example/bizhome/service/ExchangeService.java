package com.example.bizhome.service;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ExchangeService {
    Mono<Float> getValue(String currency, LocalDate date);
}
