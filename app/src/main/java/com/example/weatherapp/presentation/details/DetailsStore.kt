package com.example.weatherapp.presentation.details

interface DetailsStore {

    data class State (
        val todo : Unit
    )

    sealed interface Label {

    }

    sealed interface Intent {

    }
}