package com.example.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Store
import com.example.weatherapp.domain.entities.City

interface SearchStore : Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> {

    data class State (
        val searchQuery : String,
        val searchState: SearchState
    ){
        sealed interface SearchState {
            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class SuccessLoaded(val cities : List<City>) : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label

        data object SavedToFavourite : Label

        data class OpenForecast(val city: City) : Label

    }

    sealed interface Intent {
        data class ChangedSearchQuery(val query : String) : Intent

        data object ClickBack : Intent

        data object CLickSearch : Intent

        data class ClickCity(val city: City) : Intent
    }
}