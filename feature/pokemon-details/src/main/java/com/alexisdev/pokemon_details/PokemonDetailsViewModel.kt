package com.alexisdev.pokemon_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexisdev.common.Response
import com.alexisdev.domain.model.PokemonDetails
import com.alexisdev.domain.usecase.api.GetPokemonDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class PokemonDetailsViewModel(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
    private val saveStateHandle: SavedStateHandle
    ) : ViewModel() {

        private val _state = MutableStateFlow<PokemonDetailsState>(PokemonDetailsState.Loading)
        val state: StateFlow<PokemonDetailsState> get() = _state


        init {
            loadPokeDetails()
        }

    private fun loadPokeDetails() {
        saveStateHandle.get<String>(ARG_POKE_NAME)
            ?.also {
                getPokemonDetailsUseCase.execute(it)
                    .onEach { response ->
                        when (response) {
                            is Response.Error -> {
                                _state.value = PokemonDetailsState.Error(response.message)
                            }

                            is Response.Success -> {
                                _state.value = PokemonDetailsState.Content(pokemonDetails = response.data)
                            }
                        }
                    }
                    .launchIn(viewModelScope)
            }
    }

    fun onEvent(event: PokemonDetailsEvent) {
        when (event) {
            is PokemonDetailsEvent.OnRetry -> {
                _state.update { PokemonDetailsState.Loading }
                loadPokeDetails()
            }
        }
    }

    companion object {
        private const val ARG_POKE_NAME = "pokemonName"
    }
}

sealed interface PokemonDetailsState {
    data object Loading: PokemonDetailsState

    data class Content(val pokemonDetails: PokemonDetails) : PokemonDetailsState

    data class Error(val msg: String) : PokemonDetailsState
}

sealed interface PokemonDetailsEvent {
    data object OnRetry : PokemonDetailsEvent
}

