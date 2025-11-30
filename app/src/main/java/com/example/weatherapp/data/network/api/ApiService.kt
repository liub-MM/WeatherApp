package com.example.weatherapp.data.network.api


import com.example.weatherapp.data.network.dto.CityDto
import com.example.weatherapp.data.network.dto.WeatherCurrentDto
import com.example.weatherapp.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json?key=1c376ceb0056455e9b0165142253011")
    suspend fun loadCurrentWeather(
        @Query("q") query: String,
    ): WeatherCurrentDto

    @GET("forecast.json?key=1c376ceb0056455e9b0165142253011")
    suspend fun loadForecastWeather(
        @Query("q") query: String,
        @Query("days") days: Int = 4,
    ): WeatherForecastDto

    @GET("search.json?key=1c376ceb0056455e9b0165142253011")
    suspend fun searchCity(
        @Query("q") query: String,
    ): List<CityDto>
}