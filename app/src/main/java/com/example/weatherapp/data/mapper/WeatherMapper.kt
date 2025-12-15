package com.example.weatherapp.data.mapper

import android.icu.util.Calendar
import android.util.Log
import com.example.weatherapp.data.network.dto.WeatherCurrentDto
import com.example.weatherapp.data.network.dto.WeatherDto
import com.example.weatherapp.data.network.dto.WeatherForecastDto
import com.example.weatherapp.domain.entities.Forecast
import com.example.weatherapp.domain.entities.Weather
import java.util.Date

fun WeatherDto.toEntity(): Weather = Weather(
    tempC,
    condition.text,
    condition.icon.correctImageUrl(),
    date.toCalendar()
)

fun WeatherCurrentDto.toEntity(): Weather = this@toEntity.current.toEntity()
private fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}


fun WeatherForecastDto.toEntity(): Forecast {
    Log.d("API_TEST", "Days received: ${forecastDto.forecastDay.size}")
    return Forecast(
        currentWeather = current.toEntity(),
        upcoming = forecastDto.forecastDay.drop(1).map {
            val dayWeatherDto = it.dayWeatherDto
            Weather(
                tempC = dayWeatherDto.temp,
                conditionText = dayWeatherDto.conditionDto.text,
                conditionUrl = dayWeatherDto.conditionDto.icon.correctImageUrl(),
                date = it.date.toCalendar(),
            )
        }
    )
}

private fun String.correctImageUrl() =
    "https:${this}".replace(oldValue = "64x64", newValue = "128x128")
