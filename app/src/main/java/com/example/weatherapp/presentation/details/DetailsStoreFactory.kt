package com.example.weatherapp.presentation.details

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.entities.Forecast
import com.example.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.example.weatherapp.domain.usecase.GetForecastUseCase
import com.example.weatherapp.domain.usecase.ObserveIsFavouriteUseCase
import com.example.weatherapp.presentation.details.DetailsStore.State
import com.example.weatherapp.presentation.details.DetailsStore.State.ForecastState
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val observeIsFavouriteUseCase: ObserveIsFavouriteUseCase
) {


    fun create(city: City): DetailsStore = object : DetailsStore,
        Store<DetailsStore.Intent, State, DetailsStore.Label> by storeFactory.create(
            name = "DetailsStoreFactory",
            autoInit = true,
            initialState = State(
                city = city,
                isFavourite = false,
                forecastState = ForecastState.Initial
            ),
            executorFactory = { ExecutorImpl() },
            bootstrapper = BootstrapperImpl(city),
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action {
        data class FavouriteStatusChanged(val isFavourite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastStartLoading : Action

        data object ForecastLoadingError : Action
    }

    private sealed interface Msg {
        data class FavouriteStatusChanged(val isFavourite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastStartLoading : Msg

        data object ForecastLoadingError : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
                DetailsStore.Intent,
                Action,
                State,
                Msg,
                DetailsStore.Label
                >() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.FavouriteStatusChanged -> {
                    dispatch(Msg.FavouriteStatusChanged(action.isFavourite))
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }

                Action.ForecastStartLoading -> {
                    dispatch(Msg.ForecastStartLoading)
                }
            }
        }

        override fun executeIntent(intent: DetailsStore.Intent) {
            when (intent) {
                DetailsStore.Intent.ChangeFavouriteStatus -> {
                    val isFavourite = state().isFavourite
                    scope.launch {
                        if (isFavourite) {
                            changeFavouriteStateUseCase.removeFromFavourite(state().city.id)

                        } else {
                            changeFavouriteStateUseCase.addToFavourite(state().city)

                        }
                    }
                }

                DetailsStore.Intent.ClickBack -> {
                    publish(DetailsStore.Label.ClickBack)

                }
            }
        }
    }

    private inner class BootstrapperImpl(val city: City) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeIsFavouriteUseCase.invoke(city.id).collect {
                    dispatch(Action.FavouriteStatusChanged(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastStartLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))

                } catch (e: Exception) {
                    Log.e("DetailsStore", "Error loading forecast", e)
                    dispatch(Action.ForecastLoadingError)
                }

            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {
            return when (msg) {
                is Msg.FavouriteStatusChanged -> {
                    copy(isFavourite = msg.isFavourite)
                }

                is Msg.ForecastLoaded -> {
                    copy(forecastState = ForecastState.Loaded(msg.forecast))
                }

                Msg.ForecastLoadingError -> {
                    copy(forecastState = ForecastState.Error)

                }

                Msg.ForecastStartLoading -> {
                    copy(forecastState = ForecastState.Loading)

                }
            }
        }
    }
}

