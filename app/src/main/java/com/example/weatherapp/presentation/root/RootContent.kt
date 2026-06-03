package com.example.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.weatherapp.presentation.auth.AuthScreen
import com.example.weatherapp.presentation.details.DetailsContent
import com.example.weatherapp.presentation.favourite.FavouriteContent
import com.example.weatherapp.presentation.search.SearchContent

@Composable
fun RootContent(component: RootComponent) {

    Children(stack = component.stack) {
        when (val instance = it.instance) {


            is RootComponent.Child.Details -> {
                DetailsContent(instance.component)
            }

            is RootComponent.Child.Favourite -> {
                FavouriteContent(instance.component)

            }

            is RootComponent.Child.Search -> {
                SearchContent(instance.component)

            }

            is RootComponent.Child.Auth -> {
                AuthScreen(component = instance.component)
            }
        }
    }

}