package com.example.weatherapp.presentation.search

import com.example.weatherapp.domain.entities.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model : StateFlow<SearchStore.State>

    fun onClickBack()
    fun onSearchClick()
    fun onQueryChanged(query : String)
    fun onClickCity(city: City)


}