package com.example.weatherapp.presentation.details

import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.entities.Forecast

interface DetailsStore {

    data class State (
        val city: City,
        val isFavourite : Boolean,
        val forecastState: ForecastState
    ){
        sealed interface ForecastState{

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack : Label

        data object ChangeFavouriteStatus : Label

    }

    sealed interface Intent {
        data object ClickBack : Intent

        data object ChangeFavouriteStatus : Intent
    }
}