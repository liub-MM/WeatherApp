package com.example.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.example.weatherapp.domain.usecase.SearchCityUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase
) {


    fun create(openReason: OpenReason): SearchStore = object : SearchStore,
        Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> by storeFactory.create(
            name = "AddContactStoreFactory",
            autoInit = true,
            initialState = SearchStore.State(
                searchQuery = "",
                searchState = SearchStore.State.SearchState.Initial
            ),
            executorFactory = { ExecutorImpl(openReason) },
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeSearchQuery(val query: String) : Msg

        data object Loading : Msg

        data object Error : Msg

        data class SearchResultLoaded(val cities: List<City>) : Msg
    }

    private var jobSearch : Job? = null

    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<
                SearchStore.Intent,
                Action,
                SearchStore.State,
                Msg,
                SearchStore.Label
                >() {
        override fun executeIntent(intent: SearchStore.Intent) {
            when (intent) {
                SearchStore.Intent.CLickSearch -> {
                    jobSearch?.cancel()
                    jobSearch = scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            val cities = searchCityUseCase(state().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))

                        } catch (e: Exception) {
                            dispatch(Msg.Error)
                        }
                    }
                }

                is SearchStore.Intent.ChangedSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                SearchStore.Intent.ClickBack -> {
                    publish(SearchStore.Label.ClickBack)
                }

                is SearchStore.Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.AddToFavourite -> {
                            scope.launch {
                                changeFavouriteStateUseCase.addToFavourite(intent.city)
                                publish(SearchStore.Label.SavedToFavourite)
                            }
                        }

                        OpenReason.Search -> {
                            publish(SearchStore.Label.OpenForecast(intent.city))
                        }
                    }

                }
            }
        }
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }


    private object ReducerImpl : Reducer<SearchStore.State, Msg> {
        override fun SearchStore.State.reduce(msg: Msg): SearchStore.State {
           return when(msg){
                is Msg.ChangeSearchQuery -> {
                    copy(searchQuery = msg.query)
                }
                Msg.Error -> {
                    copy(searchState = SearchStore.State.SearchState.Error)
                }
                Msg.Loading -> {
                    copy(searchState = SearchStore.State.SearchState.Loading)

                }
                is Msg.SearchResultLoaded -> {
                    val searchState = if (msg.cities.isEmpty()){
                        SearchStore.State.SearchState.EmptyResult
                    }else {
                        SearchStore.State.SearchState.SuccessLoaded(msg.cities)
                    }

                    copy(searchState = searchState)
                }
            }
        }
    }
}

