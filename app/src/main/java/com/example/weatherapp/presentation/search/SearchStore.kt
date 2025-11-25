package com.example.weatherapp.presentation.search

interface SearchStore {

    data class State (
        val todo : Unit
    )

    sealed interface Label {

    }

    sealed interface Intent {

    }
}