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


    fun create(): FavoutiteStore = object : FavoutiteStore,
        Store<FavoutiteStore.Intent, FavoutiteStore.State, FavoutiteStore.Label> by storeFactory.create(
            name = "AddContactStoreFactory",
            autoInit = true,
            initialState = FavoutiteStore.State(listOf()),
            executorFactory = { ExecutorImpl() },
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl,
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
                FavoutiteStore.Intent,
                Action,
                FavoutiteStore.State,
                Msg,
                FavoutiteStore.Label
                >() {

        override fun executeIntent(intent: FavoutiteStore.Intent) {

            when(intent){
                is FavoutiteStore.Intent.CityItemClicked -> {
                    publish(FavoutiteStore.Label.CityItemClicked(intent.city))
                }
                FavoutiteStore.Intent.ClickToFavourite -> {
                    publish(FavoutiteStore.Label.ClickToFavourite)
                }
                FavoutiteStore.Intent.SearchClick ->{
                    publish(FavoutiteStore.Label.SearchClick)
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

    private object ReducerImpl : Reducer<FavoutiteStore.State, Msg> {
        override fun FavoutiteStore.State.reduce(msg: Msg): FavoutiteStore.State {
            return when(msg){
                is Msg.FavouriteCitiesLoaded -> {
                    copy(
                        cityItems = msg.cities.map {
                            FavoutiteStore.State.CityItem(
                                city = it,
                                weatherState = FavoutiteStore.State.WeatherState.Initial
                            )
                        }
                    )
                }
                is Msg.WeatherIsLoading -> {
                    copy(
                        cityItems = cityItems.map {
                            if (it.city.id == msg.cityId){
                                it.copy(weatherState = FavoutiteStore.State.WeatherState.Loading)
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
                                it.copy(weatherState = FavoutiteStore.State.WeatherState.Loaded(
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
                            it.copy(weatherState = FavoutiteStore.State.WeatherState.Error)
                        }else {
                            it
                        }
                    }
                )
            }
        }
    }
}

