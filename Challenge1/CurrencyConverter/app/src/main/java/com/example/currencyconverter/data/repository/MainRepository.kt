package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.models.ExchangeRatesResponse
import com.example.currencyconverter.util.Resource

interface MainRepository {
    suspend fun getExchangeRates(base: String): Resource<ExchangeRatesResponse>
}