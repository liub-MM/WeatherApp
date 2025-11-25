package com.example.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

class SearchStoreFactory() {

    private val storeFactory: StoreFactory = DefaultStoreFactory()


    fun create(): SearchStore = object : SearchStore,
        Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> by storeFactory.create(
            name = "AddContactStoreFactory",
            autoInit = true,
            initialState = SearchStore.State(Unit),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action {}

    private sealed interface Msg {
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
                SearchStore.Intent,
                Action,
                SearchStore.State,
                Msg,
                SearchStore.Label
                >() {
        override fun executeIntent(intent: SearchStore.Intent) {
            when (intent) {

                else -> {}
            }
        }
    }


    private object ReducerImpl : Reducer<SearchStore.State, Msg> {
        override fun SearchStore.State.reduce(msg: Msg): SearchStore.State {
            TODO("Not yet implemented")
        }
    }
}

