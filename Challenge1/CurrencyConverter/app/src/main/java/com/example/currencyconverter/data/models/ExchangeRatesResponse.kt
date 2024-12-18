package com.example.currencyconverter.data.models

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val privacy: String,
    val rates: Map<String, Double>,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
)