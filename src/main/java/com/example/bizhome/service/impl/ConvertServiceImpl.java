package com.example.bizhome.service.impl;

import com.example.bizhome.service.ConvertService;
import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.web.request.ConvertRequest;
import com.example.bizhome.web.response.ConvertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConvertServiceImpl implements ConvertService {
    private final ExchangeService exchangeService;
    @Value("${exchange.default-base-currency}")
    private String defaultBaseCurrency;

    @Override
    public Mono<ConvertResponse> convert(ConvertRequest request) {
        String baseCurrency = request.getBaseCurrency() == null ? defaultBaseCurrency : request.getBaseCurrency();
        LocalDate date = request.getDate() == null ? LocalDate.now() : LocalDate.parse(request.getDate());

        Mono<Float> baseCurrencyMono = exchangeService.getValue(baseCurrency, date).getAnswer();

        Mono<Float> convertCurrencyMono = exchangeService.getValue(request.getConvertCurrency(), date).getAnswer();

        return baseCurrencyMono
                .zipWith(convertCurrencyMono, (baseValue, convertValue) -> {
                    Float resultSum = request.getSum() / baseValue * convertValue;
                    return ConvertResponse.from(baseCurrency,
                            request.getConvertCurrency(),
                            baseValue,
                            convertValue,
                            date,
                            resultSum);
                });
    }
}
