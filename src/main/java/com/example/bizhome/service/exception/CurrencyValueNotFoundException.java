package com.example.bizhome.service.exception;

import java.time.LocalDate;

public class CurrencyValueNotFoundException extends Exception {
    public CurrencyValueNotFoundException(String currency, LocalDate date) {
        super("Отсутсвует курс по валюте '" + currency + "' на дату " + date);
    }
}
