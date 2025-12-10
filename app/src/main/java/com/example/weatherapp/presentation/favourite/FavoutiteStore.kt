package com.example.weatherapp.presentation.favourite

import com.example.weatherapp.domain.entities.City

interface FavoutiteStore {

    data class State(
        val cityItems : List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )
        sealed interface WeatherState {

            data object Error : WeatherState
            data object Initial : WeatherState
            data object Loading : WeatherState
            data class Loaded(
                val tempC: Float,
                val icon: String
            ) : WeatherState
        }
    }

    sealed interface Label {
        data object SearchClick : Label

        data object ClickToFavourite : Label

        data class CityItemClicked(val city : City) : Label
    }

    sealed interface Intent {

        data object SearchClick : Intent

        data object ClickToFavourite : Intent

        data class CityItemClicked(val city : City) : Intent

    }
}