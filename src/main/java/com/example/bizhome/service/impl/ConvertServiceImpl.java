package com.example.bizhome.service.impl;

import com.example.bizhome.service.ConvertService;
import com.example.bizhome.service.ExchangeService;
import com.example.bizhome.service.ExchangeServiceManager;
import com.example.bizhome.service.exception.CurrencyValueNotFoundException;
import com.example.bizhome.web.request.ConvertRequest;
import com.example.bizhome.web.response.ConvertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConvertServiceImpl implements ConvertService {
    private final ExchangeServiceManager exchangeServiceManager;

    @Override
    public Mono<ConvertResponse> convert(ConvertRequest request) {
        String baseCurrency = request.getBaseCurrency() == null ? "RUB" : request.getBaseCurrency();
        LocalDate date = request.getDate() == null ? LocalDate.now() : LocalDate.parse(request.getDate());

        Mono<Float> baseCurrencyMono = exchangeServiceManager.getValue(baseCurrency, date);

        Mono<Float> convertCurrencyMono = exchangeServiceManager.getValue(request.getConvertCurrency(), date);

        return baseCurrencyMono
                .zipWith(convertCurrencyMono, (baseValue, convertValue) -> {

                    Float resultSum = request.getSum() / baseValue * convertValue;

                    ConvertResponse response = new ConvertResponse();
                    response.setBaseCurrency(baseCurrency);
                    response.setBaseCurrencyValue(baseValue);
                    response.setConvertCurrency(request.getConvertCurrency());
                    response.setConvertCurrencyValue(convertValue);
                    response.setDate(date);
                    response.setSum(resultSum);

                    return response;
                });
    }
}
