package com.example.bizhome.service;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ExchangeServiceManager {
    Mono<Float> getValue(String currency, LocalDate date);
}
