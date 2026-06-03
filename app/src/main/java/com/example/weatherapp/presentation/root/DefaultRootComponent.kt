package com.example.weatherapp.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.weatherapp.domain.entities.City
import com.example.weatherapp.presentation.auth.DefaultAuthComponent
import com.example.weatherapp.presentation.details.DefaultDetailsComponent
import com.example.weatherapp.presentation.favourite.DefaultFavouriteComponent
import com.example.weatherapp.presentation.search.DefaultSearchComponent
import com.example.weatherapp.presentation.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jakarta.inject.Inject
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import com.arkivanov.decompose.router.stack.replaceCurrent

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
    private val authComponentFactory: DefaultAuthComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Auth,
        childFactory = ::child,
        handleBackButton = true
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Auth ->{
                val component = authComponentFactory.create(
                    onAuthSuccess = {
                        navigation.replaceCurrent(Config.Favourite)
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Auth(component)
            }
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onClickBackFun = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }

            Config.Favourite -> {
                val component = favouriteComponentFactory.create(
                    onCityItemClicked = {
                        navigation.push(Config.Details(it))
                    },
                    onAddToFavouriteClicked = {
                        navigation.push(Config.Search(OpenReason.AddToFavourite))

                    },
                    onSearchClicked = {
                        navigation.push(Config.Search(OpenReason.Search))

                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Favourite(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    componentComponent = componentContext,
                    openReason = config.openReason,
                    onBackClick = {
                        navigation.pop()

                    },
                    onOpenForecast = {
                        navigation.push(Config.Details(it))
                    },
                    onSavedToFavourite = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.Search(component)
            }
        }

    }

    @Serializable
    sealed interface Config : Parcelable {

        @Parcelize data object Auth : Config
        @Parcelize
        data object Favourite : Config

        @Parcelize
        data class Search(val openReason: OpenReason) : Config

        @Parcelize
        data class Details(val city: City) : Config
    }
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ) : DefaultRootComponent
    }
}