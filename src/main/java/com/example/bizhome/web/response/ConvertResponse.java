package com.example.bizhome.web.response;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class ConvertResponse {
    private String baseCurrency;
    private String convertCurrency;
    private Float baseCurrencyValue;
    private Float convertCurrencyValue;
    private LocalDate date;
    private Float sum;
}
