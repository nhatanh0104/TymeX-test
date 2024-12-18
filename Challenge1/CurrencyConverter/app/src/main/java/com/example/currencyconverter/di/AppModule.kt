package com.example.currencyconverter.di

import com.example.currencyconverter.data.api.CurrencyApi
import com.example.currencyconverter.data.models.ExchangeRatesJsonAdapter
import com.example.currencyconverter.data.repository.CurrencyRepository
import com.example.currencyconverter.data.repository.MainRepository
import com.example.currencyconverter.util.DispatcherProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.fxratesapi.com/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provide custom Moshi Adapter for the response JSON
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(ExchangeRatesJsonAdapter())
            .build()
    }

    // Get Exchanges Rate API
    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = CurrencyRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}