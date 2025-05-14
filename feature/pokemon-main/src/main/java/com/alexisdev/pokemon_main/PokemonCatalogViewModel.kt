package com.alexisdev.pokemon_main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alexisdev.common.navigation.NavDirection
import com.alexisdev.common.navigation.NavEffect
import com.alexisdev.common.navigation.NavigationManager
import com.alexisdev.domain.model.Pokemon
import com.alexisdev.domain.model.StatType
import com.alexisdev.domain.usecase.api.CheckStatFilterUseCase
import com.alexisdev.domain.usecase.api.FindPokemonByFiltersUseCase
import com.alexisdev.domain.usecase.api.GetCheckedStatFiltersUseCase
import com.alexisdev.domain.usecase.api.GetPokemonsUseCase
import com.alexisdev.domain.usecase.api.SaveTopPokemonUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class PokemonCatalogViewModel(
    private val getPokemonsUseCase: GetPokemonsUseCase,
    private val checkStatFilterUseCase: CheckStatFilterUseCase,
    private val findPokemonByFiltersUseCase: FindPokemonByFiltersUseCase,
    private val getCheckedStatFiltersUseCase: GetCheckedStatFiltersUseCase,
    private val saveTopPokemonUseCase: SaveTopPokemonUseCase,
    private val navManager: NavigationManager
) :
    ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Boolean>(1)
    private val _state = MutableStateFlow<PokemonCatalogState>(PokemonCatalogState.Loading)
    val state: MutableStateFlow<PokemonCatalogState> get() = _state

    private val _topPokemonUpdateState = MutableSharedFlow<Unit>()
    val topPokemonUpdateState: SharedFlow<Unit> = _topPokemonUpdateState.asSharedFlow()

    init {
        setupPokemonFlow()
        getCheckedFilters()
        loadFilteredPokemon()
        refresh(isRandomStartPositionMode = false)
    }

    private fun setupPokemonFlow() {
        refreshTrigger.flatMapLatest { isRandomStartPositionMode ->
            getPokemonsUseCase.execute(isRandomStartPositionMode)
                .cachedIn(viewModelScope)
                .onEach { pagingData ->
                    _state.update { pokemonCatalogState ->
                        when (pokemonCatalogState) {
                            is PokemonCatalogState.Content -> {
                                pokemonCatalogState.copy(pagingData = pagingData)
                            }

                            else -> {
                                PokemonCatalogState.Content(pagingData)
                            }
                        }
                    }
                }
        }.launchIn(viewModelScope)
    }

    private fun getCheckedFilters() {
        getCheckedStatFiltersUseCase.execute()
            .onEach { filters ->
                _state.update { pokemonCatalogState ->
                    when (pokemonCatalogState) {
                        is PokemonCatalogState.Content -> {
                            Log.d("PokeTest-Filters", filters.toString())
                            pokemonCatalogState.copy(filters = filters)
                        }

                        else -> pokemonCatalogState
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadFilteredPokemon() {
        findPokemonByFiltersUseCase.execute()
            .onEach { pokemon ->
                saveTopPokemonUseCase.execute(pokemon)
                _topPokemonUpdateState.emit(Unit)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: PokemonCatalogEvent) {
        when (event) {
            is PokemonCatalogEvent.OnRetry -> {
                refresh(false)
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
                refresh(true)
            }

            is PokemonCatalogEvent.OnCheckStatFilter -> {
                handleOnCheckStatFilters(event.statType, event.isChecked)
            }
        }
    }

    private fun handleOnCheckStatFilters(statType: StatType, isChecked: Boolean) =
        viewModelScope.launch {
            checkStatFilterUseCase.execute(statType)
        }

    private fun refresh(isRandomStartPositionMode: Boolean) = viewModelScope.launch {
        refreshTrigger.emit(isRandomStartPositionMode)
    }

}

sealed interface PokemonCatalogState {

    data object Loading : PokemonCatalogState

    data class Content(
        val pagingData: PagingData<Pokemon>,
        val filters: Set<StatType> = emptySet(),
        val filteredPokemon: Pokemon? = null
    ) : PokemonCatalogState
}

sealed interface PokemonCatalogEvent {

    data object OnRetry : PokemonCatalogEvent

    data class OnNavigateToDetails(val pokeName: String) : PokemonCatalogEvent

    data object OnReinitialize : PokemonCatalogEvent

    data class OnCheckStatFilter(val statType: StatType, val isChecked: Boolean) :
        PokemonCatalogEvent
}

