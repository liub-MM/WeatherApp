package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.entities.City

interface SearchRepository {

     suspend fun searchCity(query : String) : List<City>
}