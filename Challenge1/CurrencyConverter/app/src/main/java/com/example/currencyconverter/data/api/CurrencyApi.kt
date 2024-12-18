package com.example.currencyconverter.data.api

import com.example.currencyconverter.data.models.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    // Get latest rates with respect to a base currency
    @GET("/latest")
    suspend fun getExchangeRates(
        @Query("base") base: String
    ): Response<ExchangeRatesResponse>
}