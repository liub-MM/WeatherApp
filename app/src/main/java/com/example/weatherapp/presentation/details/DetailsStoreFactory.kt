package com.example.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.weatherapp.presentation.search.SearchStoreFactory
import javax.inject.Inject

class DetailsStoreFactory @Inject constructor(private val storeFactory: StoreFactory ) {

//    private val storeFactory: StoreFactory = DefaultStoreFactory()


    fun create(): DetailsStore = object : DetailsStore,
        Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> by storeFactory.create(
            name = "AddContactStoreFactory",
            autoInit = true,
            initialState = DetailsStore.State(Unit),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action

    private sealed interface Msg {
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
                DetailsStore.Intent,
                Action, DetailsStore.State,
                Msg,
                DetailsStore.Label
                >() {
        override fun executeIntent(intent: DetailsStore.Intent) {
            when (intent) {

                else -> {}
            }
        }
    }


    private object ReducerImpl : Reducer<DetailsStore.State, DetailsStoreFactory.Msg> {
        override fun DetailsStore.State.reduce(msg: Msg): DetailsStore.State {
            TODO("Not yet implemented")
        }
    }
}

