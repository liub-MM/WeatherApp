package com.example.weatherapp.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.presentation.extensions.scope
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavouriteComponent @Inject constructor(
    private val favouriteStoreFactory: FavouriteStoreFactory,
    private val onCityItemClicked : (City) -> Unit,
    private val onAddToFavouriteClicked : () -> Unit,
    private val onSearchClicked : () -> Unit,
    componentContext: ComponentContext
) : FavouriteComponent, ComponentContext by componentContext {


    private val store: FavouriteStore = instanceKeeper.getStore {
        favouriteStoreFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouriteStore.State> = store.stateFlow

    init {
        scope().launch {
          store.labels.collect {
              when(it){
                  is FavouriteStore.Label.CityItemClicked -> {
                      onCityItemClicked(it.city)
                  }
                  FavouriteStore.Label.ClickToFavourite -> {
                      onAddToFavouriteClicked()
                  }
                  FavouriteStore.Label.SearchClick -> {
                      onSearchClicked()
                  }
              }
          }
        }

    }

    override fun onCityClicked(city: City) {
        store.accept(FavouriteStore.Intent.CityItemClicked(city))

    }

    override fun onClickSearch() {
        store.accept(FavouriteStore.Intent.SearchClick)
    }

    override fun onClickAddToFavourite() {
        store.accept(FavouriteStore.Intent.ClickToFavourite)
    }
}