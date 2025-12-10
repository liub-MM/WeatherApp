package com.example.weatherapp.presentation.favourite

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.domain.usecase.GetFavouriteCitiesUseCase
import com.example.weatherapp.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetWeatherUseCase
) {


    fun create(): FavouriteStore =
        object : FavouriteStore,
            Store<FavouriteStore.Intent, FavouriteStore.State, FavouriteStore.Label> by storeFactory.create(
                name = "FavouriteStore",
                initialState = FavouriteStore.State(listOf()),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}


    private sealed interface Action {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val id: Int,
            val tempC: Float,
            val conditionUrl: String
        ) : Msg

        data class WeatherLoadingError(val cityId: Int) : Msg

        data class WeatherIsLoading(val cityId: Int) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
                FavouriteStore.Intent,
                Action,
                FavouriteStore.State,
                Msg,
                FavouriteStore.Label
                >() {

        override fun executeIntent(intent: FavouriteStore.Intent) {

            when(intent){
                is FavouriteStore.Intent.CityItemClicked -> {
                    publish(FavouriteStore.Label.CityItemClicked(intent.city))
                }
                FavouriteStore.Intent.ClickToFavourite -> {
                    publish(FavouriteStore.Label.ClickToFavourite)
                }
                FavouriteStore.Intent.SearchClick ->{
                    publish(FavouriteStore.Label.SearchClick)
                }
            }
        }

        override fun executeAction(action: Action) {
            when(action){
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(message = Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeather(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeather(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase.invoke(city.id)
                dispatch(Msg.WeatherLoaded(
                    city.id,
                    weather.tempC,
                    weather.conditionUrl))
            }catch (e: Exception){
                dispatch(Msg.WeatherLoadingError(city.id))
                Log.d("Exception" , e.toString())
            }

        }
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase.invoke().collect {
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }

        }
    }

    private object ReducerImpl : Reducer<FavouriteStore.State, Msg> {
        override fun FavouriteStore.State.reduce(msg: Msg): FavouriteStore.State {
            return when(msg){
                is Msg.FavouriteCitiesLoaded -> {
                    copy(
                        cityItems = msg.cities.map {
                            FavouriteStore.State.CityItem(
                                city = it,
                                weatherState = FavouriteStore.State.WeatherState.Initial
                            )
                        }
                    )
                }
                is Msg.WeatherIsLoading -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.cityId){
                                it.copy(weatherState = FavouriteStore.State.WeatherState.Loading)
                            }else {
                                it
                            }
                        }
                    )
                }
                is Msg.WeatherLoaded -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.id){
                                it.copy(weatherState = FavouriteStore.State.WeatherState.Loaded(
                                    tempC =msg.tempC,
                                    icon = msg.conditionUrl))
                            }else {
                                it
                            }
                        }
                    )
                }
                is Msg.WeatherLoadingError -> copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId){
                            it.copy(weatherState = FavouriteStore.State.WeatherState.Error)
                        }else {
                            it
                        }
                    }
                )
            }
        }
    }
}

