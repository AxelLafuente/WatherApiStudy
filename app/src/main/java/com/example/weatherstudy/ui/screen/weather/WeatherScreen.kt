package com.example.weatherstudy.ui.screen.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherstudy.viewmodel.WeatherUiState
import com.example.weatherstudy.viewmodel.WeatherViewModel


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var city by rememberSaveable { mutableStateOf("São Paulo") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Clima Agora") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Digite uma cidade") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.fetchWeather(city) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Buscar")
                }

                when (uiState) {
                    is WeatherUiState.Idle -> {
                        Text("Insira uma cidade para buscar o clima.")
                    }
                    is WeatherUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is WeatherUiState.Success -> {
                        val weather = (uiState as WeatherUiState.Success).data
                        Text(
                            text = "${weather.location.name}, ${weather.location.country}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text("Temperatura: ${weather.current.temp_c}°C")
                        Text("Condição: ${weather.current.condition.text}")
                        Image(
                            painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    is WeatherUiState.Error -> {
                        Text(
                            text = (uiState as WeatherUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    )
}