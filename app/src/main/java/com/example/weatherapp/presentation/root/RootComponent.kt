package com.example.weatherapp.presentation.root

import android.telecom.Call
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.weatherapp.presentation.auth.AuthComponent
import com.example.weatherapp.presentation.details.DetailsComponent
import com.example.weatherapp.presentation.favourite.FavouriteComponent
import com.example.weatherapp.presentation.search.SearchComponent

interface RootComponent {

    val stack : Value<ChildStack<*, Child>>

    sealed interface Child {
        class Auth(val component: AuthComponent) : Child

        data class Favourite(val component: FavouriteComponent) : Child

        data class Details(val component: DetailsComponent) : Child

        data class Search(val component: SearchComponent) : Child
    }
}