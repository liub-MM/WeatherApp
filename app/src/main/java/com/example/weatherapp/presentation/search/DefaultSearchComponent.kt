package com.example.weatherapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.presentation.details.DetailsComponent
import com.example.weatherapp.presentation.extensions.scope
import dagger.assisted.AssistedInject
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSearchComponent @Inject constructor(
    componentComponent: ComponentContext,
    private val openReason: OpenReason,
    private val onBackClick : () -> Unit,
    private val onOpenForecast : (City) -> Unit,
    private val onSavedToFavourite : () -> Unit,
    private val searchStoreFactory: SearchStoreFactory
) : SearchComponent, ComponentContext by componentComponent {

    private val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }

    init {
        scope().launch {
            store.labels.collect {
                when(it){
                    SearchStore.Label.ClickBack -> {onBackClick()}
                    is SearchStore.Label.OpenForecast -> {onOpenForecast(it.city)}
                    SearchStore.Label.SavedToFavourite -> {onSavedToFavourite()}
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onSearchClick() {
        store.accept(SearchStore.Intent.CLickSearch)
    }

    override fun onQueryChanged(query: String) {
        store.accept(SearchStore.Intent.ChangedSearchQuery(query))
    }

    override fun onClickCity(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }
}