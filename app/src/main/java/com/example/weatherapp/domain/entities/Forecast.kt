package com.example.weatherapp.domain.entities

data class Forecast(
    val currentWeather : Weather,
    val upcoming: List<Weather>
)
