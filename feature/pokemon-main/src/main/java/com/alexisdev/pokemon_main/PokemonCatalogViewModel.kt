package com.alexisdev.pokemon_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.usecase.api.GetPokemonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class PokemonCatalogViewModel(private val getPokemonsUseCase: GetPokemonsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow<PokemonCatalogState>(PokemonCatalogState.Loading)
    val state: MutableStateFlow<PokemonCatalogState> get() = _state

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        getPokemonsUseCase.execute()
            .onEach { pagingData ->
                _state.value = PokemonCatalogState.Content(pagingData)
            }
            .cachedIn(viewModelScope)
            .launchIn(viewModelScope)
    }

    fun onEvent(event: PokemonCatalogEvent) {
        when (event) {
            is PokemonCatalogEvent.OnRetry -> {
                loadPokemons()
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

}

