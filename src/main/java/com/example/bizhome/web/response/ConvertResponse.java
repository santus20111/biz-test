package com.example.bizhome.web.response;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
public class ConvertResponse {
    private String baseCurrency;
    private String convertCurrency;
    private Float baseCurrencyValue;
    private Float convertCurrencyValue;
    private LocalDate date;
    private Float sum;


    private ConvertResponse(String baseCurrency, String convertCurrency, Float baseCurrencyValue, Float convertCurrencyValue, LocalDate date, Float sum) {
        this.baseCurrency = baseCurrency;
        this.convertCurrency = convertCurrency;
        this.baseCurrencyValue = baseCurrencyValue;
        this.convertCurrencyValue = convertCurrencyValue;
        this.date = date;
        this.sum = sum;
    }

    public static ConvertResponse from(String baseCurrency,
                                       String convertCurrency,
                                       Float baseCurrencyValue,
                                       Float convertCurrencyValue,
                                       LocalDate date,
                                       Float sum) {
        return new ConvertResponse(baseCurrency,
                convertCurrency,
                baseCurrencyValue,
                convertCurrencyValue,
                date,
                sum);
    }
}
