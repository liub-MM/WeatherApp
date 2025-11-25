package com.example.weatherapp.presentation.favourite

interface FavoutiteStore {

    data class State (
        val todo : Unit
    )

    sealed interface Label {

    }

    sealed interface Intent {

    }
}