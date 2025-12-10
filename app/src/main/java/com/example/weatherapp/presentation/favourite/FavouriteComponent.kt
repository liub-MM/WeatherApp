package com.example.weatherapp.presentation.favourite

import com.example.weatherapp.domain.entities.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val model : StateFlow<FavouriteStore.State>

    fun onCityClicked(city : City)

    fun onClickSearch()

    fun onClickAddToFavourite()
}