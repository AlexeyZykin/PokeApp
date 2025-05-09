package com.alexisdev.pokemon_main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexisdev.common.Response
import com.alexisdev.common.navigation.NavDirection
import com.alexisdev.common.navigation.NavEffect
import com.alexisdev.common.navigation.NavigationManager
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.usecase.api.GetPokemonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class PokemonCatalogViewModel(
    private val getPokemonsUseCase: GetPokemonsUseCase,
    private val navManager: NavigationManager
) :
    ViewModel() {

    private val _state = MutableStateFlow<PokemonCatalogState>(PokemonCatalogState.Loading)
    val state: MutableStateFlow<PokemonCatalogState> get() = _state

    init {
        loadPokemons()
    }

    private fun loadPokemons(isRandomStartPositionMode: Boolean = false) {
        getPokemonsUseCase.execute(isRandomStartPositionMode)
            .cachedIn(viewModelScope)
            .onEach { pagingData ->
                pagingData.map { Log.d("PokeTest", it.id.toString()) }
                _state.update {
                    PokemonCatalogState.Content(pagingData)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: PokemonCatalogEvent) {
        when (event) {
            is PokemonCatalogEvent.OnRetry -> {
                loadPokemons()
            }

            is PokemonCatalogEvent.OnNavigateToDetails -> {
                navManager.navigate(
                    NavEffect.NavigateTo(
                        NavDirection.PokemonCatalogToPokemonDetails(
                            event.pokeName
                        )
                    )
                )
            }

            is PokemonCatalogEvent.OnReinitialize -> {
                loadPokemons(isRandomStartPositionMode = true)
            }
        }
    }
}

sealed interface PokemonCatalogState {

    data object Loading : PokemonCatalogState

    data class Content(val pagingData: PagingData<Pokemon>) : PokemonCatalogState
}

sealed interface PokemonCatalogEvent {

    data object OnRetry : PokemonCatalogEvent

    data class OnNavigateToDetails(val pokeName: String) : PokemonCatalogEvent

    data object OnReinitialize : PokemonCatalogEvent
}

