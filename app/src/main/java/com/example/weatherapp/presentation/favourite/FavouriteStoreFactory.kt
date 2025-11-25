package com.example.weatherapp.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

class FavouriteStoreFactory() {

    private val storeFactory: StoreFactory = DefaultStoreFactory()


    fun create(): FavoutiteStore = object : FavoutiteStore,
        Store<FavoutiteStore.Intent, FavoutiteStore.State, FavoutiteStore.Label> by storeFactory.create(
            name = "AddContactStoreFactory",
            autoInit = true,
            initialState = FavoutiteStore.State(Unit),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action {}

    private sealed interface Msg {
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
            when (intent) {

                else -> {}
            }
        }
    }


    private object ReducerImpl : Reducer<FavoutiteStore.State, Msg> {
        override fun FavoutiteStore.State.reduce(msg: Msg): FavoutiteStore.State {
            TODO("Not yet implemented")
        }
    }
}

