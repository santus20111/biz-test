package com.example.bizhome.service;

import com.example.bizhome.service.impl.exchange.ExchangeSourceAnswer;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ExchangeSource {
    ExchangeSourceAnswer getValue(String currency, LocalDate date);
}
