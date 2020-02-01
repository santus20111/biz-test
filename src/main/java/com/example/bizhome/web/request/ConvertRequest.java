package com.example.bizhome.web.request;

import com.example.bizhome.web.validation.ConvertDateConstraint;
import com.example.bizhome.web.validation.CurrencyISOCodeConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class ConvertRequest {
    @CurrencyISOCodeConstraint
    private String baseCurrency;

    @CurrencyISOCodeConstraint
    @NotNull(message = "Необходимо указать валюту конвертации")
    private String convertCurrency;

    @NotNull(message = "Необходимо указать сумму")
    @Min(value = 0, message = "Сумма не может быть меньше 0")
    private Float sum;

    @ConvertDateConstraint
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Формат даты: 'YYYY-MM-DD'")
    private String date;
}
