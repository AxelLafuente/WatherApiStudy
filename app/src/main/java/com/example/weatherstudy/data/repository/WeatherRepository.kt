package com.example.weatherstudy.data.repository

import com.example.weatherstudy.data.model.WeatherResponse
import com.example.weatherstudy.data.remote.RetrofitInstance
import com.example.weatherstudy.utils.Constants

class WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse? {
        val response = RetrofitInstance.api.getCurrentWeather(
            apiKey = Constants.API_KEY,
            city = city
        )
        return if (response.isSuccessful) response.body() else null
    }
}