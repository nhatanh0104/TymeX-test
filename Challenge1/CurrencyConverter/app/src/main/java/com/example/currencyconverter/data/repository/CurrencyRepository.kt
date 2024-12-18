package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.api.CurrencyApi
import com.example.currencyconverter.data.models.ExchangeRatesResponse
import com.example.currencyconverter.util.Resource
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: CurrencyApi
) : MainRepository {
    override suspend fun getExchangeRates(base: String): Resource<ExchangeRatesResponse> {
        return try {
            val response = api.getExchangeRates(base)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                // Fetch unsuccessful
                Resource.Error("Unable to fetch exchange rates")
            }
        } catch (e: Exception) {
            Resource.Error("Network error, please check your network connection")
        }
    }
}