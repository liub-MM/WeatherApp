package com.example.weatherapp.presentation.auth

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultAuthComponent @AssistedInject constructor(
    @Assisted("onAuthSuccess") private val onAuthSuccess: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : AuthComponent, ComponentContext by componentContext {

    override fun onLoginSuccess() {
        onAuthSuccess()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onAuthSuccess") onAuthSuccess: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAuthComponent
    }
}