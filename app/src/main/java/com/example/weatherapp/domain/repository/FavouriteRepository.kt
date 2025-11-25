package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.entities.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    suspend fun addToFavourite(city : City)

    suspend fun removeFromFavourite(cityId : Int)

    val favouriteCities : Flow<City>

    fun observeIsFavourite(cityId: Int) : Flow<Boolean>
}