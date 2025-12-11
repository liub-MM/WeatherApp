package com.example.weatherapp.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.presentation.extensions.scope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavouriteComponent @AssistedInject constructor(
    private val favouriteStoreFactory: FavouriteStoreFactory,
    @Assisted("onCityItemClicked") private val onCityItemClicked : (City) -> Unit,
    @Assisted("onAddToFavouriteClicked") private val onAddToFavouriteClicked : () -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked : () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
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
    @AssistedFactory
    interface Factory {
        fun create (
            @Assisted("onCityItemClicked")  onCityItemClicked : (City) -> Unit,
            @Assisted("onAddToFavouriteClicked")  onAddToFavouriteClicked : () -> Unit,
            @Assisted("onSearchClicked")  onSearchClicked : () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFavouriteComponent
    }
}