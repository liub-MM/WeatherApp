package com.example.weatherapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.presentation.extensions.scope
import com.example.weatherapp.presentation.favourite.FavouriteStoreFactory
import dagger.assisted.AssistedInject
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultDetailsComponent @Inject constructor(
    private val city: City,
    private val detailsStoreFactory: DetailsStoreFactory,
    private val onClickBackFun: () -> Unit,
    componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city) }

    init {
        scope().launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> {
                        onClickBackFun()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onClickChangeFavouriteStatus() {
        store.accept(DetailsStore.Intent.ChangeFavouriteStatus)
    }
}