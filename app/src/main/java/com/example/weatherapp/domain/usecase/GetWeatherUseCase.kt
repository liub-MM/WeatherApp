package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.repository.FavouriteRepository
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(cityId : Int) = repository.getWeather(cityId)

}