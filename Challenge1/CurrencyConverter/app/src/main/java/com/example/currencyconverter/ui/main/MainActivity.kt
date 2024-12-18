package com.example.currencyconverter.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.util.currencyList
import com.example.currencyconverter.viewModel.CurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                CurrencyConverterApp(viewModel)
            }
        }
    }
}

@Composable
fun CurrencyConverterApp(viewModel: CurrencyViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .safeContentPadding()
        ) {
            CurrencyConverterScreen(
                uiState = uiState,
                onConvert = { amount, fromCurrency, toCurrency ->
                    viewModel.convert(amount, fromCurrency, toCurrency)
                }
            )
        }
    }
}

@Composable
fun CurrencyConverterScreen(
    uiState: CurrencyViewModel.CurrencyUIState,
    onConvert: (String, String, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var selectedIndexFrom by remember { mutableStateOf(currencyList.indexOf("USD")) }
    var selectedIndexTo by remember { mutableStateOf(currencyList.indexOf("EUR")) }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var result by remember { mutableStateOf("Enter amount to convert") }
    var error by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Currency Converter",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        CurrencySelection(
            label = "From",
            selectedIndex = selectedIndexFrom,
        ) { index, item ->
            selectedIndexFrom = index
            fromCurrency = item
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                fromCurrency = toCurrency.also { toCurrency = fromCurrency }
                selectedIndexFrom = selectedIndexTo.also { selectedIndexTo = selectedIndexFrom } },
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text(
                text = "Switch"
            )
        }

        CurrencySelection(
            label = "To",
            selectedIndex = selectedIndexTo,
        ) { index, item ->
            selectedIndexTo = index
            toCurrency = item
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Amount",
                style = MaterialTheme.typography.titleLarge,
            )
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                shape = RoundedCornerShape(30)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onConvert(amount, fromCurrency, toCurrency) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
        ) {
            Text(
                text = "Convert",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Result",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
                Text(
                    text = toCurrency,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
            OutlinedTextField(
                value = result,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30)
            )

            Text(
                text = error,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        result = when (uiState) {
            is CurrencyViewModel.CurrencyUIState.Loading -> {
                ""
            }

            is CurrencyViewModel.CurrencyUIState.Success -> {
                uiState.resultText
            }

            is CurrencyViewModel.CurrencyUIState.Failure -> {
                "An error occurred"
            }

            is CurrencyViewModel.CurrencyUIState.Empty -> {
                "Converted amount"
            }
        }

        error = when (uiState) {
            is CurrencyViewModel.CurrencyUIState.Loading -> {
                ""
            }

            is CurrencyViewModel.CurrencyUIState.Success -> {
                ""
            }

            is CurrencyViewModel.CurrencyUIState.Failure -> {
                uiState.errorText
            }

            is CurrencyViewModel.CurrencyUIState.Empty -> {
                ""
            }
        }
    }
}


@Composable
fun CurrencySelection(
    label: String,
    selectedIndex: Int,
    onItemSelected: (Int, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
        )
        LargeDropdownMenu(
            label = label,
            items = currencyList,
            selectedIndex = selectedIndex,
            onItemSelected = onItemSelected
        )
    }
}