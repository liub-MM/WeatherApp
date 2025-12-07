package com.example.weatherapp.data.repository

import com.example.weatherapp.data.mapper.toEntity
import com.example.weatherapp.data.network.api.ApiService
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.repository.SearchRepository
import jakarta.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun searchCity(query: String): List<City> {
        return apiService.searchCity(query)
            .map { it.toEntity() }
    }
}