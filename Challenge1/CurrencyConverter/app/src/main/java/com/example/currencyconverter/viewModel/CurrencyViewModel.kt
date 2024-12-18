package com.example.currencyconverter.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.repository.MainRepository
import com.example.currencyconverter.util.DispatcherProvider
import com.example.currencyconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

    sealed class CurrencyUIState {
        class Success(val resultText: String) : CurrencyUIState()
        class Failure(val errorText: String) : CurrencyUIState()
        object Loading : CurrencyUIState()
        object Empty : CurrencyUIState()
    }

    private val _uiState = MutableStateFlow<CurrencyUIState>(CurrencyUIState.Empty)
    val uiState: StateFlow<CurrencyUIState> = _uiState

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String,
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _uiState.value = CurrencyUIState.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatcher.io) {
            _uiState.value = CurrencyUIState.Loading
            when(val exchangeRatesResponse = repository.getExchangeRates(fromCurrency)) {
                is Resource.Error -> _uiState.value = CurrencyUIState.Failure(exchangeRatesResponse.message!!)
                is Resource.Success -> {
                    val rates = exchangeRatesResponse.data!!.rates
                    val rate = rates[toCurrency]
                    if (rate == null) {
                        _uiState.value = CurrencyUIState.Failure("Unavailable exchange rates")
                    } else {
                        val convertedAmount = round(fromAmount * rate * 100) / 100
                        _uiState.value = CurrencyUIState.Success(
                            convertedAmount.toString()
                        )
                    }
                }
            }
        }
    }
}