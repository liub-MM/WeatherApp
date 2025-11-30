package com.example.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current") val currentDto: WeatherCurrentDto,
    @SerializedName("forecast") val forecastDto: ForecastDto
)
